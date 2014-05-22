package net.techreadiness.customer.action;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.security.core.context.SecurityContextHolder;

public class SetScopeAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private Long scopeId;

	@Inject
	private ScopeService scopeService;
	@Inject
	private OrganizationService organizationService;

	@Override
	@Action(results = { @Result(name = "success", type = "redirect", location = "/info") })
	public String execute() {
		Scope scope = scopeService.getById(getServiceContext(), scopeId);

		User user = userService.getByUsername(getServiceContext(), SecurityContextHolder.getContext().getAuthentication()
				.getName());

		ServiceContext serviceContext = getServiceContext();

		serviceContext.setScope(scope);
		serviceContext.setUser(user);

		Org org = organizationService.getOrgForUser(serviceContext);

		serviceContext.setOrg(org);

		return SUCCESS;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public Long getScopeId() {
		return scopeId;
	}
}