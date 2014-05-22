package net.techreadiness.ui.util;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ResponseHeaderInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("Last-Modified", new Date().toString());
		response.setHeader("Expires", "-1");
		response.setHeader("Cache-Control", "private, must-revalidate");
		return invocation.invoke();
	}
}
