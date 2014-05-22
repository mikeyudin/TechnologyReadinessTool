package net.techreadiness.service.exception;

import java.util.List;

import net.techreadiness.service.common.ValidationError;

import com.google.common.collect.Lists;

public class FaultInfo {
	private String message;

	private Long correlationId = System.currentTimeMillis();

	private String stackTrace;

	private List<ValidationError> attributeErrors = Lists.<ValidationError> newArrayList();

	private boolean logged = false;

	private String method = "(unknown, see stacktrace)";

	public FaultInfo() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public List<ValidationError> getAttributeErrors() {
		return attributeErrors;
	}

	public void setAttributeErrors(List<ValidationError> attributeErrors) {
		this.attributeErrors = attributeErrors;
	}

	public Long getCorrelationId() {
		return correlationId;
	}

	@Override
	public String toString() {
		return stackTrace;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}
}
