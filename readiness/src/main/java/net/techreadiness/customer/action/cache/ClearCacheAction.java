package net.techreadiness.customer.action.cache;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@Results({ @Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "index" }) })
public class ClearCacheAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	@PersistenceUnit
	private EntityManagerFactory emFactory;

	@Override
	public String execute() throws Exception {
		emFactory.getCache().evictAll();
		return SUCCESS;
	}
}
