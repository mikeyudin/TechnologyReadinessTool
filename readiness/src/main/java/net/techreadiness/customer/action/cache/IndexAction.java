package net.techreadiness.customer.action.cache;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@Results({ @Result(name = Action.SUCCESS, location = "/WEB-INF/cache/index.jsp") })
public class IndexAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
}
