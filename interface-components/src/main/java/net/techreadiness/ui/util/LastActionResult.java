package net.techreadiness.ui.util;

import org.apache.struts2.dispatcher.ServletActionRedirectResult;

import com.opensymphony.xwork2.ActionInvocation;

public class LastActionResult extends ServletActionRedirectResult {
	private static final long serialVersionUID = 1L;

	String fieldName;

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		requestParameters.put("populatePrevious", Boolean.TRUE);
		super.execute(invocation);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
