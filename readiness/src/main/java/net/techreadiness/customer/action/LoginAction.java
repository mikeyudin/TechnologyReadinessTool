package net.techreadiness.customer.action;

import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, location = "/login-custom.jsp") })
public class LoginAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() {
		return SUCCESS;
	}
}
