package net.techreadiness.customer.action.task.org.delete;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_DELETE;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.customer.datagrid.OrgsByIdItemProvider;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", location = "/task/org/delete.jsp"),
		@Result(name = "noorg", location = "/task/org/noorg.jsp") })
public class DeleteAction extends OrgTaskFlowAction {
	private static final long serialVersionUID = 1L;

	@Inject
	private OrgsByIdItemProvider orgsByIdItemProvider;

	@ConversationScoped
	private DataGridState<Map<String, String>> deleteOrgsDataGrid;

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_DELETE })
	public String execute() {
		if (getTaskFlowData().getOrgs() == null || getTaskFlowData().getOrgs().isEmpty()) {
			return "noorg";
		}
		orgsByIdItemProvider.setOrgs(getTaskFlowData().getOrgs());

		return SUCCESS;
	}

	public OrgsByIdItemProvider getOrgsByIdItemProvider() {
		return orgsByIdItemProvider;
	}

	public void setOrgsByIdItemProvider(OrgsByIdItemProvider orgsByIdItemProvider) {
		this.orgsByIdItemProvider = orgsByIdItemProvider;
	}

	public DataGridState<Map<String, String>> getDeleteOrgsDataGrid() {
		return deleteOrgsDataGrid;
	}

	public void setDeleteOrgsDataGrid(DataGridState<Map<String, String>> deleteOrgsDataGrid) {
		this.deleteOrgsDataGrid = deleteOrgsDataGrid;
	}

}
