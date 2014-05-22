package net.techreadiness.customer.action.task.user.update;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_RESET_PASSWORD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "reset-password" }),
		@Result(name = "invalid", type = "lastAction", params = { "actionName", "reset-password" }) })
public class SaveResetPasswordAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(Boolean.class)
	private Map<Long, Boolean> users;

	@ConversationScoped
	private DataGridState<User> resetUsersDataGrid;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_RESET_PASSWORD })
	public String execute() {

		for (Entry<Long, Boolean> user : users.entrySet()) {
			if (user.getValue().booleanValue()) {
				Long userId = user.getKey();
				try {
					userService.resetPassword(getServiceContext(), userId);
				} catch (ValidationServiceException vse) {
					List<ValidationError> errors = vse.getFaultInfo().getAttributeErrors();
					for (ValidationError validationError : errors) {
						addActionError(validationError.getOnlineMessage());
					}
				} catch (ServiceException se) {
					addActionError(se.getMessage());
				}
			}
		}
		if (hasErrors()) {
			return "invalid";
		}
		return SUCCESS;
	}

	public Map<Long, Boolean> getUsers() {
		return users;
	}

	public DataGridState<User> getResetUsersDataGrid() {
		return resetUsersDataGrid;
	}

	public void setResetUsersDataGrid(DataGridState<User> resetUsersDataGrid) {
		this.resetUsersDataGrid = resetUsersDataGrid;
	}

	@Override
	public void prepare() throws Exception {
		users = new HashMap<>();
	}
}