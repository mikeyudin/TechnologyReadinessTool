package net.techreadiness.plugin.action.task.device;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.model.JSONOrg;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ActionContext;

public class OrganizationDeviceControl extends BaseAction {
	private static final long serialVersionUID = 1L;

	private List<JSONOrg> jsonOrgs;
	private Long orgId;
	private List<Org> orgs = Lists.newArrayList();
	@Inject
	private OrganizationService orgService;

	public static final String DEVICE_ADD_ORGID = "device.add.orgId";

	@Action(value = "ajaxOrgLoad", results = { @Result(type = "json", params = { "root", "jsonOrgs" }) })
	public String ajaxOrgLoad() {
		ActionContext context = ActionContext.getContext();
		String[] term = (String[]) context.getParameters().get("term");
		List<Org> orgs = orgService.findChildOrgsThatAllowDevices(getServiceContext(), term[0], 50);
		jsonOrgs = Lists.newArrayList();

		// convert objects to JSON
		for (Org org : orgs) {
			jsonOrgs.add(new JSONOrg(org.getOrgId(), org.getName(), org.getCode(), org.getCity(), org.getState(), org
					.getOrgTypeName()));
		}

		return SUCCESS;
	}

	@Action(value = "addOrganization", results = { @Result(name = "success", location = "show", type = "redirect", params = {
			"ajax", "true", "orgId", "%{orgId}" }) })
	public String add() {
		getSession().put(DEVICE_ADD_ORGID, orgId);
		return SUCCESS;
	}

	@Action(value = "show", results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/device/displayOrgSelection.jsp") }, params = {
			"ajax", "true" })
	public String show() {
		orgs = Lists.newArrayList();
		orgId = (Long) getSession().get(DEVICE_ADD_ORGID);
		Org org = orgService.getById(getServiceContext(), orgId);
		if (org != null) {
			orgs.add(org);
		}
		return SUCCESS;
	}

	@Action(value = "remove", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true" }) })
	public String remove() {
		getSession().put(DEVICE_ADD_ORGID, null);
		return SUCCESS;
	}

	@Action(value = "showOptions", results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/device/changeOrgSelection.jsp") })
	public String showOptions() {
		return SUCCESS;
	}

	public List<JSONOrg> getJsonOrgs() {
		return jsonOrgs;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public List<Org> getOrgs() {
		return orgs;
	}
}
