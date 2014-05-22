package net.techreadiness.customer.action.task.user.roleassign;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ROLE_UPDATE;

import java.util.Collection;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.User;
import net.techreadiness.service.object.UserRole;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", location = "/task/user/roleassign.jsp"),
		@Result(name = "nouser", location = "/task/user/nouser.jsp") })
public class EditAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(Long.class)
	private Multimap<Long, Long> assignments;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_ROLE_UPDATE })
	public String execute() {
		if (getTaskFlowData().getUsers() == null || getTaskFlowData().getUsers().isEmpty()) {
			return "nouser";
		}

		if (!getTaskFlowData().getTaskFlowState().isInitialized()) {
			getTaskFlowData().getRoles().addAll(
					userService.findRolesForUsers(getServiceContext(), getServiceContext().getScopeId(), getTaskFlowData()
							.getUserIds()));
			getTaskFlowData().getTaskFlowState().initialized();
		}

		for (User user : getUsers()) {
			for (UserRole userRole : user.getUserRoles()) {
				assignments.put(user.getUserId(), userRole.getRoleId());
			}
		}

		conversation.put("roleFilterSelectionHandler", "roleFilterSelectionHandlerForUserTaskFlow");

		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		assignments = HashMultimap.create();
	}

	public Collection<User> getUsers() {
		return getTaskFlowData().getUsers();
	}

	public Collection<Role> getRoles() {
		return getTaskFlowData().getRoles();
	}

	public Multimap<Long, Long> getAssignments() {
		return assignments;
	}

}
