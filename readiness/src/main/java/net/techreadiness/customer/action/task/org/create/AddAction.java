package net.techreadiness.customer.action.task.org.create;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_CREATE;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.Org;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class AddAction extends BaseOrgAction {
	private static final long serialVersionUID = 1L;

	@Inject
	private ConfigService configService;

	private ViewDef viewDef;
	private List<SimpleEntry<Long, String>> orgTypes;
	private List<Org> orgs;

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_CREATE })
	@Action(results = { @Result(name = "success", location = "/task/org/add.jsp"),@Result(name = "input", location = "/task/org/add.jsp") })
	public String execute() {
		conversation.put("orgFilterSelectionHandler", "orgFilterSelectionHandlerForOrgCreate");
		populateForm(false);

		return SUCCESS;
	}

	@Action(value = "updateForm", results = { @Result(name = "success", location = "/task/org/add.jsp", params = {
			"parentOrganizationId", "%{parentOrganizationId}", "ajax", "true" }) })
	public String updateForm() {
		populateForm(true);
		return SUCCESS;
	}

	private void populateForm(boolean reload) {
		Long parentOrgTypeId = null;
		Org parentOrg = null;

		List<Org> orgSelection = getOrgSelectionHandler().getSelection();
		if (orgSelection.size() > 0) {
			parentOrg = orgSelection.get(0);
			if (parentOrg != null) {
				parentOrgTypeId = parentOrg.getOrgTypeId();
				parentOrganizationId = parentOrg.getOrgId();
			}
		}

		orgTypes = organizationService.findChildOrgTypesByParentOrgType(getServiceContext(), parentOrgTypeId);

		viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.ORG);
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public List<SimpleEntry<Long, String>> getOrgTypes() {
		return orgTypes;
	}

	public void setOrgTypes(List<SimpleEntry<Long, String>> orgTypes) {
		this.orgTypes = orgTypes;
	}

	public void setOrgs(List<Org> orgs) {
		this.orgs = orgs;
	}

	public List<Org> getOrgs() {
		return orgs;
	}

}
