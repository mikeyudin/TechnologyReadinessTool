package net.techreadiness.customer.action.task.user.update;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_RESET_PASSWORD;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.datagrid.UserByUserItemProvider;
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

@Results({ @Result(name = Action.SUCCESS, location = "/task/user/resetPassword.jsp"),
		@Result(name = "nouser", location = "/task/user/nouser.jsp") })
public class ResetPasswordAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;
	@Inject
	private UserByUserItemProvider itemProvider;
	@Key(Long.class)
	@Element(Boolean.class)
	private Map<Long, Boolean> users;

	@ConversationScoped(value = "resetUsersDataGrid")
	private DataGridState<User> resetUsersDataGrid;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_RESET_PASSWORD })
	public String execute() {
		if (getTaskFlowData().getUsers() == null || getTaskFlowData().getUsers().isEmpty()) {
			return "nouser";
		}

		itemProvider.setUserIds(getTaskFlowData().getUserIds());
		return SUCCESS;
	}

	@Override
	public void prepare() {
		users = new HashMap<>();
	}

	public void setItemProvider(UserByUserItemProvider itemProvider) {
		this.itemProvider = itemProvider;
	}

	public UserByUserItemProvider getItemProvider() {
		return itemProvider;
	}

	public DataGridState<User> getResetUsersDataGrid() {
		return resetUsersDataGrid;
	}

	public void setResetUsersDataGrid(DataGridState<User> resetUsersDataGrid) {
		this.resetUsersDataGrid = resetUsersDataGrid;
	}

}