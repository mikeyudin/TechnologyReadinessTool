package net.techreadiness.service.common;

import org.apache.commons.lang3.StringEscapeUtils;

public class ValidationError {
	private String fieldName;
	private String onlineMessage;
	private String batchCode;
	private String batchMessage;

	public ValidationError() {
	}

	public ValidationError(String fieldName, String displayName, String onlineMessage) {
		setFieldName(fieldName);
		setOnlineMessage(onlineMessage);
		setBatchMessage(onlineMessage);
	}

	public ValidationError(String fieldName, String displayName, String onlineMessage, String batchCode, String batchMessage) {
		setFieldName(fieldName);
		setOnlineMessage(onlineMessage);
		setBatchCode(batchCode);
		setBatchMessage(batchMessage);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = StringEscapeUtils.escapeHtml4(fieldName);
	}

	public String getOnlineMessage() {
		return onlineMessage;
	}

	public void setOnlineMessage(String message) {
		onlineMessage = StringEscapeUtils.escapeHtml4(message);
	}

	public void setBatchMessage(String batchMessage) {
		this.batchMessage = batchMessage;
	}

	public String getBatchMessage() {
		return batchMessage;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
}
