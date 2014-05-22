package net.techreadiness.customer.action.organization;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORGANIZATION_ACCESS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_CREATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_DELETE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_UPDATE;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.navigation.taskflow.org.OrgTaskFlowDefinition;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.security.PermissionCodeSet;
import net.techreadiness.security.PermissionCodeSetImpl;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskControlAction;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", location = "/org/list.jsp") })
public class ListAction extends BaseTaskControlAction<OrgTaskFlowDefinition> implements ConversationAware {
	public static final String SCOPE_ID = "organization.scopeId";

	private static final long serialVersionUID = 1L;
	private ViewDef dataGridView;
	private boolean showAllOrgsCheckboxDisplayed;

	@Inject
	private OrganizationItemProvider orgItemProvider;
	@Inject
	private ConfigService configService;

	@ConversationScoped(value = "orgSearchGrid")
	private DataGridState<?> orgSearchGrid;
	private Conversation conversation;

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORGANIZATION_ACCESS })
	public String execute() {
		ServiceContext serviceContext = getServiceContext();
		if (serviceContext == null) {
			throw new ServiceException("ServiceContext was null");
		}

		Scope scope = serviceContext.getScope();
		if (scope == null) {
			throw new ServiceException("Scope was null : scopePath=" + serviceContext.getScopePath() + " scopeId="
					+ serviceContext.getScopeId());
		}

		Boolean scopeTypeAllowOrg = scope.getScopeTypeAllowOrg();
		if (scopeTypeAllowOrg == null) {
			scopeTypeAllowOrg = false;
		}

		showAllOrgsCheckboxDisplayed = hasPermission(CorePermissionCodes.CORE_SEARCH_ALL_ORGS) && !scopeTypeAllowOrg;
		orgItemProvider.setScope(getServiceContext().getScopeId());
		dataGridView = configService.getViewDefinition(serviceContext, ViewDef.ViewDefTypeCode.ORG_DATAGRID);
		conversation.put("scopeFilterSelectionHandler", "scopeSelectionFilterHandlerForOrgList");
		conversation.put("orgFilterSelectionHandler", "parentOrgSelectionFilterForOrgList");
		return SUCCESS;
	}

	public DataGridItemProvider<?> getOrgItemProvider() {
		return orgItemProvider;
	}

	public ViewDef getDataGridView() {
		return dataGridView;
	}

	public void setOrgSearchGrid(DataGridState<?> orgSearchGrid) {
		this.orgSearchGrid = orgSearchGrid;
	}

	public DataGridState<?> getOrgSearchGrid() {
		return orgSearchGrid;
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public PermissionCodeSet getCoreOrgUpdate() {
		return new PermissionCodeSetImpl(CORE_CUSTOMER_ORG_UPDATE);
	}

	public PermissionCodeSet getCoreOrgDelete() {
		return new PermissionCodeSetImpl(CORE_CUSTOMER_ORG_DELETE);
	}

	public PermissionCodeSet getCoreOrgCreate() {
		return new PermissionCodeSetImpl(CORE_CUSTOMER_ORG_CREATE);
	}

	public boolean isShowAllOrgsCheckboxDisplayed() {
		return showAllOrgsCheckboxDisplayed;
	}

	public void setShowAllOrgsCheckboxDisplayed(boolean showAllOrgsCheckboxDisplayed) {
		this.showAllOrgsCheckboxDisplayed = showAllOrgsCheckboxDisplayed;
	}
}
