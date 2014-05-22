package net.techreadiness.customer.action.organization;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORGANIZATION_ACCESS;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.navigation.taskflow.org.OrgTaskFlowData;
import net.techreadiness.navigation.taskflow.org.OrgTaskFlowDefinition;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Contact;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.OrgPart;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Lists;

public class DetailsAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private Long orgId;
	private Map<String, String> org;

	private boolean showTaskLinks;
	private boolean reset;

	private ViewDef viewDef;
	private ViewDef contactsViewDef;
	private ViewDef orgPartViewDef;

	@Inject
	private ConfigService configService;
	@Inject
	private OrganizationService orgService;

	@Inject
	private ScopeService scopeService;

	@Inject
	ContactByOrgItemProvider contactByOrgItemProvider;
	@Inject
	OrgPartItemProvider orgPartItemProvider;

	@ConversationScoped
	private DataGridState<Contact> contactDataGridState;

	@ConversationScoped
	private DataGridState<OrgPart> orgPartDataGridState;

	private Scope orgPartScope;

	@Inject
	private OrgTaskFlowDefinition orgTaskFlow;
	@Inject
	private OrgTaskFlowData orgTaskFlowData;

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORGANIZATION_ACCESS })
	@Action(value = "details", results = { @Result(name = "success", location = "/org/details.jsp") })
	public String execute() throws ServiceException {
		if (org == null) {
			Org o = orgService.getById(getServiceContext(), getOrgId());

			org = o.getAsMap();

			org.put("parentOrgName", o.getParentOrgName());
		}
		if (reset) {
			contactDataGridState.getFilters().clear();
			orgPartDataGridState.getFilters().clear();
		}

		orgPartScope = scopeService.getScopeWithOrgParts(getServiceContext());

		contactByOrgItemProvider.setOrgId(getOrgId());

		orgPartItemProvider.setOrgId(getOrgId());
		orgPartItemProvider.setScopeId(getServiceContext().getScopeId());

		viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.ORG);
		contactsViewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.CONTACT_DATAGRID);
		orgPartViewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.ORG_PART_DATAGRID);

		return SUCCESS;
	}

	@Action(value = "detailsEditOrg", results = { @Result(name = "success", type = "redirectAction", params = { "namespace",
			"/task/org", "actionName", "altOrgTaskFlowBegin" }) })
	public String detailsEditOrg() throws ServiceException {
		return routeToTask(OrgTaskFlowDefinition.TASK_UPDATE_ORG);
	}

	@Action(value = "detailsEditContact", results = { @Result(name = "success", type = "redirectAction", params = {
			"namespace", "/task/org", "actionName", "altOrgTaskFlowBegin" }) })
	public String detailsEditContacts() throws ServiceException {
		return routeToTask(OrgTaskFlowDefinition.TASK_EDIT_CONTACTS);
	}

	@Action(value = "detailsAddContact", results = { @Result(name = "success", type = "redirectAction", params = {
			"namespace", "/task/org", "actionName", "altOrgTaskFlowBegin" }) })
	public String detailsAddContacts() throws ServiceException {
		return routeToTask(OrgTaskFlowDefinition.TASK_EDIT_CONTACTS);
	}

	@Action(value = "detailsEditParticipation", results = { @Result(name = "success", type = "redirectAction", params = {
			"namespace", "/task/org", "actionName", "altOrgTaskFlowBegin" }) })
	public String detailsEditParticipations() throws ServiceException {
		return routeToTask(OrgTaskFlowDefinition.TASK_PARICIPATIONS);
	}

	@Action(value = "detailsAddParticipation", results = { @Result(name = "success", type = "redirectAction", params = {
			"namespace", "/task/org", "actionName", "altOrgTaskFlowBegin" }) })
	public String detailsAddParticipations() throws ServiceException {
		return routeToTask(OrgTaskFlowDefinition.TASK_PARICIPATIONS);
	}

	private String routeToTask(String taskName) {
		Set<Org> orgs = new HashSet<>();
		orgs.add(orgService.getById(getServiceContext(), getOrgId()));
		Task task = orgTaskFlow.getTask(taskName);
		TaskFlowState state = new TaskFlowState();
		state.setTasks(Lists.newArrayList(task));
		orgTaskFlowData.setTaskFlowState(state);
		orgTaskFlowData.setOrgs(orgs);

		return SUCCESS;
	}

	public DataGridItemProvider<Contact> getContactItemProvider() {
		return contactByOrgItemProvider;
	}

	public DataGridItemProvider<Map<String, String>> getOrgPartItemProvider() {
		return orgPartItemProvider;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public boolean isShowTaskLinks() {
		return showTaskLinks;
	}

	public void setShowTaskLinks(boolean showTaskLinks) {
		this.showTaskLinks = showTaskLinks;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public ViewDef getContactsViewDef() {
		return contactsViewDef;
	}

	public ViewDef getOrgPartViewDef() {
		return orgPartViewDef;
	}

	public void setOrgPartViewDef(ViewDef orgPartViewDef) {
		this.orgPartViewDef = orgPartViewDef;
	}

	public void setContactsViewDef(ViewDef contactsViewDef) {
		this.contactsViewDef = contactsViewDef;
	}

	public DataGridState<Contact> getContactDataGridState() {
		return contactDataGridState;
	}

	public void setContactDataGridState(DataGridState<Contact> contactDataGridState) {
		this.contactDataGridState = contactDataGridState;
	}

	public Map<String, String> getOrg() {
		return org;
	}

	public void setOrg(Map<String, String> org) {
		this.org = org;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public DataGridState<OrgPart> getOrgPartDataGridState() {
		return orgPartDataGridState;
	}

	public void setOrgPartDataGridState(DataGridState<OrgPart> orgPartDataGridState) {
		this.orgPartDataGridState = orgPartDataGridState;
	}

	public Scope getOrgPartScope() {
		return orgPartScope;
	}

}
