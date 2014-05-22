package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import net.techreadiness.service.object.File;
import net.techreadiness.service.object.FileType;

public interface FileService extends BaseService {

	public enum FileStatus {
		PENDING, PROCESSING, COMPLETE, ERRORS, STOPPED;
		public String getName() {
			return name();
		}

		public int getOrdinal() {
			return ordinal();
		}
	}

	File getById(ServiceContext context, Long fileId);

	File addOrUpdate(ServiceContext context, File file);

	File addErrors(ServiceContext context, Long fileId, Integer lineNumber, Map<String, List<String>> errors);

	File setFileStatus(ServiceContext context, Long fileId, FileStatus fileStatus);

	File setFileStatusMessage(ServiceContext context, Long fileId, String statusMessage);

	String getUploadDir(ServiceContext context);

	String getTempExportDir(ServiceContext context);

	List<FileType> findFileTypes(ServiceContext context);
}
