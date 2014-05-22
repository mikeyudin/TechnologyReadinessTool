package net.techreadiness.customer.action;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.service.OrgPartService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.UserService;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.OrgPart;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ChangeScopeAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private List<Scope> scopes;
	private Long scopeId;
	private String returnUrl;
	private String scopeTypeName;

	@Inject
	private ScopeService scopeService;
	@Inject
	private OrganizationService organizationService;
	@Inject
	private OrgPartService orgPartService;
	@Inject
	private UserService userService;

	@Override
	@Action(results = { @Result(name = "success", location = "/changeScope.jsp") })
	public String execute() {
		List<OrgPart> orgParts = orgPartService.findOrgPartsForOrg(getServiceContext(), getServiceContext().getOrgId());
		scopes = Lists.transform(orgParts, new Function<OrgPart, Scope>() {

			@Override
			public Scope apply(OrgPart input) {
				return input.getScope();
			}
		});

		scopeId = getServiceContext().getScopeId();

		scopeTypeName = Iterables.getFirst(scopeService.findSelectableScopes(getServiceContext()), null).getScopeTypeName();

		return SUCCESS;
	}

	@Action(value = "update-scope", results = { @Result(name = "success", type = "redirect", location = "${returnUrl}") })
	public String updateScope() {
		Scope scope = scopeService.getById(getServiceContext(), scopeId);
		if (scope == null) {
			return SUCCESS;
		}
		Scope oldOrgScope = scopeService.getScopeWithOrgs(getServiceContext());

		getServiceContext().setScope(scope);

		Scope newOrgScope = scopeService.getScopeWithOrgs(getServiceContext());

		if (oldOrgScope != null && newOrgScope != null) {
			if (!oldOrgScope.getPath().equals(newOrgScope.getPath())) {
				Org org = organizationService.getOrgForUser(getServiceContext());

				getServiceContext().setOrg(org);
			}
		}

		// update user.selected_scope_id
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

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setScopeTypeName(String scopeTypeName) {
		this.scopeTypeName = scopeTypeName;
	}

	public String getScopeTypeName() {
		return scopeTypeName;
	}

}
