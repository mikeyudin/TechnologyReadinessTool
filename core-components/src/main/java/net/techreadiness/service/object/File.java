package net.techreadiness.service.object;

import java.util.Date;
import java.util.List;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.FileDO;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class File extends BaseObject<FileDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	private Long fileId;
	@CoreField
	private Long batchJobExecutionId;
	@CoreField
	private String description;
	@CoreField
	private String displayFilename;
	@CoreField
	private String filename;
	@CoreField
	private Integer kilobytes;
	@CoreField
	private String path;
	@CoreField
	private Date requestDate;
	@CoreField
	private String status;
	@CoreField
	private Integer totalRecordCount;
	@CoreField
	private String statusMessage;
	@CoreField
	private String mode;
	@CoreField
	private String errorDataFilename;
	@CoreField
	private String errorMessageFilename;

	private String username;
	private Long userId;
	private Org org;

	private Long fileTypeId;
	private String fileTypeCode;
	private String fileTypeName;

	// Okira maps this automatically ...
	private List<FileError> fileErrors = Lists.newArrayList();

	public static class FileError {
		private Integer recordNumber;
		private String errorCode;
		private String message;

		public Integer getRecordNumber() {
			return recordNumber;
		}

		public void setRecordNumber(Integer recordNumber) {
			this.recordNumber = recordNumber;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	public File() { // required by JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("fileId", fileId).add("displayFilename", displayFilename)
				.add("filename", filename).toString();
	}

	@Override
	public Class<FileDO> getBaseEntityType() {
		return FileDO.class;
	}

	@Override
	public Long getId() {
		return fileId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getBatchJobExecutionId() {
		return batchJobExecutionId;
	}

	public void setBatchJobExecutionId(Long batchJobExecutionId) {
		this.batchJobExecutionId = batchJobExecutionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayFilename() {
		return displayFilename;
	}

	public void setDisplayFilename(String displayFilename) {
		this.displayFilename = displayFilename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getKilobytes() {
		return kilobytes;
	}

	public void setKilobytes(Integer kilobytes) {
		this.kilobytes = kilobytes;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(Integer totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Long getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(Long fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	public String getFileTypeCode() {
		return fileTypeCode;
	}

	public void setFileTypeCode(String fileTypeCode) {
		this.fileTypeCode = fileTypeCode;
	}

	public String getFileTypeName() {
		return fileTypeName;
	}

	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getErrorMessageFilename() {
		return errorMessageFilename;
	}

	public void setErrorMessageFilename(String errorMessageFilename) {
		this.errorMessageFilename = errorMessageFilename;
	}

	public String getErrorDataFilename() {
		return errorDataFilename;
	}

	public void setErrorDataFilename(String errorDataFilename) {
		this.errorDataFilename = errorDataFilename;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof FileDO) {
			return fileId.equals(((FileDO) obj).getFileId());
		}
		if (!(obj instanceof File)) {
			return false;
		}
		File other = (File) obj;
		if (fileId == null) {
			if (other.fileId != null) {
				return false;
			}
		} else if (!fileId.equals(other.fileId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fileId == null ? 0 : fileId.hashCode());
		return result;
	}

	public List<FileError> getFileErrors() {
		return fileErrors;
	}

	public void setFileErrors(List<FileError> fileErrors) {
		this.fileErrors = fileErrors;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
