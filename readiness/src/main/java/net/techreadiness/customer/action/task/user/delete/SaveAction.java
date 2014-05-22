package net.techreadiness.customer.action.task.user.delete;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_DELETE;

import java.util.HashMap;
import java.util.Map;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({
		@Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "delete", "namespace",
				"/task/user/delete" }), @Result(name = "invalid", type = "lastAction", params = { "actionName", "delete" }) })
public class SaveAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@ConversationScoped
	private DataGridState<User> deleteUsersDataGrid;

	@ConversationScoped
	private DataGridState<User> userGrid;

	@Key(Long.class)
	@Element(Boolean.class)
	private Map<Long, Boolean> userStates;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_DELETE })
	public String execute() {
		for (User user : getTaskFlowData().getUsers()) {
			if (userStates.containsKey(user.getUserId())) {
				userService.delete(getServiceContext(), user);
			} else {
				userService.unDelete(getServiceContext(), user);
			}
		}

		if (hasErrors()) {
			return "invalid";
		}

		return SUCCESS;
	}

	public DataGridState<User> getDeleteUsersDataGrid() {
		return deleteUsersDataGrid;
	}

	public void setDeleteUsersDataGrid(DataGridState<User> deleteUsersDataGrid) {
		this.deleteUsersDataGrid = deleteUsersDataGrid;
	}

	public Map<Long, Boolean> getUserStates() {
		return userStates;
	}

	public void setUserStates(Map<Long, Boolean> userStates) {
		this.userStates = userStates;
	}

	@Override
	public void prepare() throws Exception {
		userStates = new HashMap<>();
	}
}