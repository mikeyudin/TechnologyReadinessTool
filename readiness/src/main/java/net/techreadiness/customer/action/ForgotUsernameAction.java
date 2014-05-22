package net.techreadiness.customer.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@Results({ @Result(name = Action.SUCCESS, location = "/forgot-username.jsp") })
public class ForgotUsernameAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Override
	public String execute() {
		return SUCCESS;
	}

}