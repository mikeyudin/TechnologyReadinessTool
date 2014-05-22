package net.techreadiness.ui.action.task.org.dataentry;

import java.util.HashSet;
import java.util.List;

import net.techreadiness.navigation.taskflow.org.OrgTaskFlowData;
import net.techreadiness.navigation.taskflow.org.dataentry.DataEntryTaskFlowDefinition;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class DataEntryTaskFlowAction extends BaseTaskFlowAction<OrgTaskFlowData, DataEntryTaskFlowDefinition> {

	private static final long serialVersionUID = 1L;

	private static String DATA_ENTRY_SEARCH_GRID = "dataEntrySearchGrid";

	@Action(value = "dataEntryTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String beginTaskFlow() throws ServiceException {
		startNewTaskFlow();

		DataGridState<Org> dataGridState = conversation.get(DataGridState.class, DATA_ENTRY_SEARCH_GRID);

		getTaskFlowData().setOrgs(new HashSet<>(dataGridState.getSelectedItems()));

		return SUCCESS;
	}

	@Override
	public String endTaskFlow() throws ServiceException {

		if (getTaskFlowData().isStandardInvocation()) {
			DataGridState<Org> dataGridState = conversation.get(DataGridState.class, DATA_ENTRY_SEARCH_GRID);

			List<Org> selectedOrgs = dataGridState.getSelectedItems();

			for (Org org : selectedOrgs) {

				boolean found = false;
				for (Org orgMap : getTaskFlowData().getOrgs()) {

					if (orgMap.getOrgId().equals(org.getOrgId())) {
						found = true;
						break;
					}
				}

				if (!found) {
					dataGridState.deSelectItem(org.getOrgId().toString());
				}
			}

			for (Org orgMap : getTaskFlowData().getOrgs()) {
				if (orgMap.getOrgId() != null) {
					dataGridState.selectItem(orgMap.getOrgId().toString(), orgMap);
				}
			}
		}
		return SUCCESS;
	}

	@Action(value = "altDataEntryTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String altBeginTaskFlow() throws ServiceException {
		getTaskFlowData().setStandardInvocation(false);
		return SUCCESS;
	}

}
