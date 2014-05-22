package net.techreadiness.customer.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@Results({ @Result(name = Action.SUCCESS, location = "/forgot-password.jsp") })
public class ForgotPasswordAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String username;

	@Override
	public String execute() {
		return SUCCESS;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}