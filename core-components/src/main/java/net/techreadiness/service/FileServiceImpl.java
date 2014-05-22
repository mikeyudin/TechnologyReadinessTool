package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.persistence.dao.FileDAO;
import net.techreadiness.persistence.dao.FileErrorDAO;
import net.techreadiness.persistence.dao.FileTypeDAO;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.domain.FileDO;
import net.techreadiness.persistence.domain.FileErrorDO;
import net.techreadiness.persistence.domain.FileTypeDO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.File;
import net.techreadiness.service.object.FileType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@WebService
@Service
@Transactional
public class FileServiceImpl extends BaseServiceImpl implements FileService {

	@Inject
	private FileDAO fileDAO;
	@Inject
	private FileErrorDAO fileErrorDAO;
	@Inject
	private FileTypeDAO fileTypeDAO;
	@Inject
	private UserDAO userDAO;
	@Inject
	private OrgDAO orgDAO;

	@Value("${file.upload.dir}")
	private String fileUploadDir;

	@Value("${file.temp.export.dir}")
	private String fileTempExportDir;

	@Override
	public File getById(ServiceContext context, Long fileId) {
		return getMappingService().map(fileDAO.getById(fileId));
	}

	@Override
	public File addOrUpdate(ServiceContext context, File file) {

		FileDO fileDO;
		if (file.getFileId() == null) {
			fileDO = getMappingService().map(file);
		} else {
			fileDO = fileDAO.getById(file.getFileId());

			// don't allow these to get mapped
			file.setFileErrors(null);

			getMappingService().getMapper().map(file, fileDO);
		}

		UserDO userDO = userDAO.getById(context.getUserId());
		OrgDO orgDO = orgDAO.getById(context.getOrgId());
		FileTypeDO fileTypeDO = fileTypeDAO.getByCode(file.getFileTypeCode());

		if (userDO != null) {
			fileDO.setUser(userDO);
		}

		if (orgDO != null) {
			fileDO.setOrg(orgDO);
		}

		if (StringUtils.isEmpty(file.getFileTypeCode())) {
			FaultInfo faultInfo = new FaultInfo();
			ValidationError error = new ValidationError("fileTypeCode", "No fileTypeCode was provided.",
					"The file type code was not specified.");
			faultInfo.getAttributeErrors().add(error);
			throw new ValidationServiceException(faultInfo);
		}

		if (fileTypeDO == null) {
			FaultInfo faultInfo = new FaultInfo();
			ValidationError error = new ValidationError("fileTypeCode", "An invalid fileTypeCode was provided.",
					"The file type code can not be found.");
			faultInfo.getAttributeErrors().add(error);
			throw new ValidationServiceException(faultInfo);
		}

		fileDO.setFileType(fileTypeDO);

		if (file.getFileId() == null) {
			return getMappingService().map(fileDAO.create(fileDO));
		}

		return getMappingService().map(fileDAO.update(fileDO));
	}

	@Override
	public File setFileStatus(ServiceContext context, Long fileId, FileStatus fileStatus) {
		FileDO fileDO = fileDAO.getById(fileId);
		if (fileDO == null) {
			throw new ServiceException("Invalid fileId.");
		}

		fileDO.setStatus(fileStatus.toString().toLowerCase());

		return getMappingService().map(fileDAO.update(fileDO));
	}

	@Override
	public File addErrors(ServiceContext context, Long fileId, Integer lineNumber, Map<String, List<String>> errors) {
		FileDO fileDO = fileDAO.getById(fileId);
		if (fileDO == null) {
			throw new ServiceException("Invalid fileId.");
		}

		if (errors == null || errors.size() == 0) {
			// nothing to do, maybe should be an error?
			return getMappingService().map(fileDO);
		}

		for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
			for (String s : entry.getValue()) {
				FileErrorDO fileErrorDO = new FileErrorDO();

				fileErrorDO.setFile(fileDO);

				String errorCode = "unknown";
				if (StringUtils.isNotBlank(entry.getKey())) {
					errorCode = entry.getKey();
				}

				fileErrorDO.setErrorCode(errorCode);
				fileErrorDO.setRecordNumber(lineNumber);
				fileErrorDO.setMessage(s);

				fileErrorDAO.create(fileErrorDO);
			}
		}

		return getMappingService().map(fileDAO.update(fileDO));
	}

	@Override
	public File setFileStatusMessage(ServiceContext context, Long fileId, String statusMessage) {
		FileDO fileDO = fileDAO.getById(fileId);
		if (fileDO == null) {
			throw new ServiceException("Invalid fileId.");
		}

		fileDO.setStatusMessage(statusMessage);

		return getMappingService().map(fileDAO.update(fileDO));
	}

	@Override
	public String getUploadDir(ServiceContext context) {
		if (StringUtils.isBlank(fileUploadDir)) {
			String tempDir = System.getProperty("java.io.tmpdir") + java.io.File.separator + "uploads";
			java.io.File f = new java.io.File(tempDir);
			f.mkdirs();
			fileUploadDir = tempDir;
		}

		return fileUploadDir;
	}

	@Override
	public String getTempExportDir(ServiceContext context) {
		if (StringUtils.isBlank(fileTempExportDir)) {
			String tempDir = System.getProperty("java.io.tmpdir") + java.io.File.separator + "uploads";
			java.io.File f = new java.io.File(tempDir);
			f.mkdirs();
			fileTempExportDir = tempDir;
		}

		return fileTempExportDir;
	}

	public String getFileUploadDir() {
		return fileUploadDir;
	}

	public void setFileUploadDir(String fileUploadDir) {
		this.fileUploadDir = fileUploadDir;
	}

	public String getFileTempExportDir() {
		return fileTempExportDir;
	}

	public void setFileTempExportDir(String fileTempExportDir) {
		this.fileTempExportDir = fileTempExportDir;
	}

	@Override
	public List<FileType> findFileTypes(ServiceContext context) {
		return getMappingService().mapFromDOList(fileTypeDAO.findAll());
	}
}
