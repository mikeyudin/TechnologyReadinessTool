package net.techreadiness.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.techreadiness.persistence.BaseEntity;

/**
 * The persistent class for the file_error database table.
 * 
 */
@Entity
@Table(name = "file_error")
public class FileErrorDO extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "file_error_id")
	private Long fileErrorId;

	@Column(name = "error_code")
	private String errorCode;

	private String message;

	@Column(name = "record_number")
	private Integer recordNumber;

	// bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name = "file_id")
	private FileDO file;

	public FileErrorDO() {
	}

	public Long getFileErrorId() {
		return fileErrorId;
	}

	public void setFileErrorId(Long fileErrorId) {
		this.fileErrorId = fileErrorId;
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

	public Integer getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}

	public FileDO getFile() {
		return file;
	}

	public void setFile(FileDO file) {
		this.file = file;
	}
}