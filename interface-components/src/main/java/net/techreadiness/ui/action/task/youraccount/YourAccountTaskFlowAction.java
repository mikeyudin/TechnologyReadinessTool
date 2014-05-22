package net.techreadiness.ui.action.task.youraccount;

import java.util.ArrayList;
import java.util.List;

import net.techreadiness.navigation.taskflow.youraccount.YourAccountTaskFlowData;
import net.techreadiness.navigation.taskflow.youraccount.YourAccountTaskFlowDefinition;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class YourAccountTaskFlowAction extends BaseTaskFlowAction<YourAccountTaskFlowData, YourAccountTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	private String dataGridId;

	@Action(value = "yourAccountTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String beginTaskFlow() throws ServiceException {
		startNewTaskFlow();

		DataGridState<User> userGrid = conversation.get(DataGridState.class, dataGridId);
		List<User> list = userGrid.getSelectedItems();
		getTaskFlowData().setUsers(new ArrayList<>(list));

		return SUCCESS;
	}

	public void setDataGridId(String dataGridId) {
		this.dataGridId = dataGridId;
	}

	public String getDataGridId() {
		return dataGridId;
	}
}
