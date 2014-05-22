package net.techreadiness.customer.action;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.UserService;
import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", type = "redirect", location = "${returnUrl}") })
public class UpdateOrgAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private Long selectedOrgId;
	private String returnUrl;

	@Inject
	private OrganizationService orgService;
	@Inject
	private UserService userService;

	@Override
	public String execute() throws Exception {
		if (selectedOrgId != null) {
			boolean hasAccess = userService.hasAccessToOrg(getServiceContext(), getServiceContext().getUserId(),
					selectedOrgId);
			if (hasAccess) {
				Org org = orgService.getById(getServiceContext(), selectedOrgId);
				getServiceContext().setOrg(org);
			} else {
				throw new AuthorizationException();
			}
		}

		return SUCCESS;
	}

	public void setSelectedOrgId(Long selectedOrgId) {
		this.selectedOrgId = selectedOrgId;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
}
