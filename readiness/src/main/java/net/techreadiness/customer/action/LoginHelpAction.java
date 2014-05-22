package net.techreadiness.customer.action;

import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class LoginHelpAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Override
	@Action(results = { @Result(name = "success", location = "/loginHelp.jsp") })
	public String execute() {
		return SUCCESS;
	}
}
