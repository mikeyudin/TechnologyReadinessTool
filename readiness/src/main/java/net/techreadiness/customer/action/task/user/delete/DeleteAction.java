package net.techreadiness.customer.action.task.user.delete;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_DELETE;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.customer.datagrid.UsersByIdItemProvider;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", location = "/task/user/delete.jsp"),
		@Result(name = "nouserDelete", location = "/task/user/nouserDelete.jsp") })
public class DeleteAction extends UserTaskFlowAction {
	private static final long serialVersionUID = 1L;

	@Inject
	private UsersByIdItemProvider usersByIdItemProvider;

	@ConversationScoped
	private DataGridState<Map<String, String>> deleteUsersDataGrid;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_DELETE })
	public String execute() {

		if (getTaskFlowData().getUsers() == null || getTaskFlowData().getUsers().isEmpty()) {
			return "nouserDelete";
		}

		usersByIdItemProvider.setUsers(getTaskFlowData().getUsers());

		for (User user : getTaskFlowData().getUsers()) {
			if (user.getDeleteDate() != null) {
				deleteUsersDataGrid.selectItem(String.valueOf(user.getUserId()), user.getAsMap());
			}
		}

		return SUCCESS;
	}

	public Collection<User> getUsers() {
		return getTaskFlowData().getUsers();
	}
}
