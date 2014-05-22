package net.techreadiness.customer.action;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.service.ScopeService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, location = "/change-password.jsp") })
public class ChangePasswordAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private List<Scope> scopes;
	private Long scopeId;
	private String username;
	@Inject
	private ScopeService scopeService;

	@Override
	public String execute() {
		ServiceContext serviceContext = getServiceContext();

		if (serviceContext == null) {
			serviceContext = new ServiceContext();
			getSession().put(SERVICE_CONTEXT, serviceContext);
		}

		setScopes(scopeService.findAppRootScopes(serviceContext));
		scopeId = serviceContext.getScopeId();
		username = serviceContext.getUserName();
		return SUCCESS;
	}

	public void setScopes(List<Scope> scopes) {
		this.scopes = scopes;
	}

	public List<Scope> getScopes() {
		return scopes;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}