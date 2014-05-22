package net.techreadiness.customer.action.task.org.create;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_CREATE;

import java.util.List;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({
		@Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "add", "namespace",
				"/task/org/create" }),
		@Result(name = Action.INPUT, type = "lastAction", params = { "fieldName", "organization", "actionName", "add" }) })
public class SaveAction extends BaseOrgAction {
	private static final long serialVersionUID = 1L;

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_CREATE })
	public String execute() {
		try {
			List<Org> orgSelection = getOrgSelectionHandler().getSelection();
			if (orgSelection.size() > 0) {
				parentOrganizationId = orgSelection.get(0).getOrgId();
				organization.setParentOrgId(parentOrganizationId);
			}

			organization = organizationService.create(getServiceContext(), organization);
		} catch (ValidationServiceException e) {
			getSession().put("parentOrganizationId", parentOrganizationId);
			for (ValidationError error : e.getFaultInfo().getAttributeErrors()) {
				addFieldError("organization." + error.getFieldName(), error.getOnlineMessage());
			}
		}
		if (hasErrors()) {
			return INPUT;
		}
		// if successful add to task flow data
		getTaskFlowData().getOrgs().add(organization);

		return SUCCESS;
	}

}
