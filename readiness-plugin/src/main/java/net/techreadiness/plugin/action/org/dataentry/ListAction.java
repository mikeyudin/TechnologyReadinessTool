package net.techreadiness.plugin.action.org.dataentry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.navigation.taskflow.org.dataentry.DataEntryTaskFlowDefinition;
import net.techreadiness.plugin.action.org.ReadyOrgItemProvider;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.security.PermissionCodeSet;
import net.techreadiness.security.PermissionCodeSetImpl;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.action.filters.DataGridAware;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskControlAction;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", location = "/net/techreadiness/plugin/action/org/dataentry/list.jsp") })
public class ListAction extends BaseTaskControlAction<DataEntryTaskFlowDefinition> implements ConversationAware,
		DataGridAware {
	public static final String SCOPE_ID = "organization.scopeId";

	private static final long serialVersionUID = 1L;
	private ViewDef dataGridView;
	private boolean showDataEntryCompleteCheckboxDisplayed;

	@Inject
	private ReadyOrgItemProvider readyOrgItemProvider;
	@Inject
	private ConfigService configService;

	@ConversationScoped(value = "dataEntrySearchGrid")
	private DataGridState<?> dataEntrySearchGrid;
	private Conversation conversation;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DATAENTRY_ACCESS })
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

		showDataEntryCompleteCheckboxDisplayed = hasPermission(CorePermissionCodes.CORE_SEARCH_ALL_ORGS)
				&& !scopeTypeAllowOrg;
		readyOrgItemProvider.setScope(getServiceContext().getScopeId());

		dataGridView = configService.getViewDefinition(serviceContext, ViewDef.ViewDefTypeCode.ORG_DATAGRID);
		conversation.put("scopeFilterSelectionHandler", "scopeSelectionFilterHandlerForOrgList");
		conversation.put("orgFilterSelectionHandler", "parentOrgSelectionFilterForOrgList");
		return SUCCESS;
	}

	public DataGridItemProvider<?> getOrgItemProvider() {
		return readyOrgItemProvider;
	}

	public ViewDef getDataGridView() {
		return dataGridView;
	}

	public void setDataEntrySearchGrid(DataGridState<?> dataEntrySearchGrid) {
		this.dataEntrySearchGrid = dataEntrySearchGrid;
	}

	public DataGridState<?> getDataEntrySearchGrid() {
		return dataEntrySearchGrid;
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public PermissionCodeSet getCoreOrgUpdate() {
		return new PermissionCodeSetImpl(CorePermissionCodes.CORE_CUSTOMER_ORG_UPDATE);
	}

	public PermissionCodeSet getCoreOrgDelete() {
		return new PermissionCodeSetImpl(CorePermissionCodes.CORE_CUSTOMER_ORG_DELETE);
	}

	public PermissionCodeSet getCoreOrgCreate() {
		return new PermissionCodeSetImpl(CorePermissionCodes.CORE_CUSTOMER_ORG_CREATE);
	}

	public boolean isShowAllOrgsCheckboxDisplayed() {
		return showDataEntryCompleteCheckboxDisplayed;
	}

	public void setShowAllOrgsCheckboxDisplayed(boolean showAllOrgsCheckboxDisplayed) {
		showDataEntryCompleteCheckboxDisplayed = showAllOrgsCheckboxDisplayed;
	}

	@Override
	public void setDataGrid(DataGrid<?> grid) {
		dataEntrySearchGrid = (DataGridState<?>) grid;

	}
}
