package net.techreadiness.customer.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@Results({ @Result(name = Action.SUCCESS, type = "httpheader", params = { "status", "200" }),
		@Result(name = Action.ERROR, type = "httpheader", params = { "error", "500", "errorMessage", "${message}" }) })
public class HealthCheckAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
}
