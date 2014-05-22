package net.techreadiness.ui.action.task.user;

import java.util.HashSet;
import java.util.List;

import net.techreadiness.navigation.taskflow.user.UserTaskFlowData;
import net.techreadiness.navigation.taskflow.user.UserTaskFlowDefinition;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class UserTaskFlowAction extends BaseTaskFlowAction<UserTaskFlowData, UserTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	private String dataGridId;

	private static String USER_SEARCH_GRID = "userGrid";

	@Action(value = "userTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String beginTaskFlow() throws ServiceException {
		startNewTaskFlow();

		DataGridState<User> userGrid = conversation.get(DataGridState.class, USER_SEARCH_GRID);
		List<User> list = userGrid.getSelectedItems();
		getTaskFlowData().setUsers(new HashSet<>(list));

		return SUCCESS;
	}

	@Action(value = "altUserTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String altBeginTaskFlow() throws ServiceException {
		getTaskFlowData().setStandardInvocation(false);

		return SUCCESS;
	}

	@Override
	public String endTaskFlow() throws ServiceException {
		if (getTaskFlowData().isStandardInvocation()) {
			DataGridState<User> dataGridState = conversation.get(DataGridState.class, USER_SEARCH_GRID);

			List<User> selectedUsers = dataGridState.getSelectedItems();

			for (User user : selectedUsers) {
				String userId = String.valueOf(user.getUserId());

				boolean found = false;
				for (User userElement : getTaskFlowData().getUsers()) {
					String id = String.valueOf(userElement.getUserId());

					if (id.equals(userId)) {
						found = true;
						break;
					}
				}

				if (!found) {
					dataGridState.deSelectItem(userId);
				}
			}

			for (User userElement : getTaskFlowData().getUsers()) {
				String userId = String.valueOf(userElement.getUserId());

				if (StringUtils.isNotBlank(userId)) {
					dataGridState.selectItem(userId, userElement);
				}
			}
		}

		return SUCCESS;
	}

	public void setDataGridId(String dataGridId) {
		this.dataGridId = dataGridId;
	}

	public String getDataGridId() {
		return dataGridId;
	}
}
