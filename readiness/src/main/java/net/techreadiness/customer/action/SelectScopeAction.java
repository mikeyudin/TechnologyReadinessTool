package net.techreadiness.customer.action;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.service.OrgPartService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.OrgPart;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class SelectScopeAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private List<Scope> scopes;
	private Long scopeId;
	private String scopeTypeName;
	@Inject
	private ScopeService scopeService;
	@Inject
	private OrganizationService organizationService;
	@Inject
	private OrgPartService orgPartService;

	@Override
	@Action(results = { @Result(name = "success", type = "redirect", location = "/info") })
	public String execute() {

		ServiceContext serviceContext = getServiceContext();

		if (serviceContext == null) {
			serviceContext = new ServiceContext();
		}

		User user = userService.getByUsername(serviceContext, SecurityContextHolder.getContext().getAuthentication()
				.getName());
		serviceContext.setUser(user);

		Scope scope = scopeService.getSelectedScopeForUser(serviceContext);
		if (scope != null) {
			serviceContext.setScope(scope);
		}

		Org org = organizationService.getOrgForUser(serviceContext);
		serviceContext.setOrg(org);

		getSession().put(SERVICE_CONTEXT, serviceContext);

		if (org == null) {
			throw new AuthorizationException(getText("not.authorized.for.any.organization"));
		}
		List<OrgPart> orgParts = orgPartService.findOrgPartsForOrg(getServiceContext(), org.getOrgId());
		if (!orgParts.isEmpty()) {
			List<Scope> scopes = Lists.transform(orgParts, new Function<OrgPart, Scope>() {

				@Override
				public Scope apply(OrgPart input) {
					return input.getScope();
				}
			});
			if (!scopes.contains(getServiceContext().getScope()) && !scopes.isEmpty()) {
				getServiceContext().setScope(Iterables.getFirst(scopes, null));
			}
		}

		if (!userActive()) {
			throwNotAuthorized(this);
		}

		userService.updateUserSelectedScopeId(getServiceContext());
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

	public void setScopeTypeName(String scopeTypeName) {
		this.scopeTypeName = scopeTypeName;
	}

	public String getScopeTypeName() {
		return scopeTypeName;
	}
}
