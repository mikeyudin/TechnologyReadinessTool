package net.techreadiness.customer.action;

import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class LogoutAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@Override
	@Action(results = { @Result(name = "success", type = "redirect", location = "/logout") })
	public String execute() {
		getRequest().getSession().invalidate();
		return SUCCESS;
	}
}
