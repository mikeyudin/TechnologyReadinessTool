package net.techreadiness.customer.action.task.user.roleassign;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ROLE_UPDATE;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.UserRoleService;
import net.techreadiness.service.exception.ValidationServiceException;
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

@Results({
		@Result(name = "success", type = "redirectAction", params = { "namespace", "/task/user/roleassign", "actionName",
				"edit" }), @Result(name = "invalid", type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(Long.class)
	private Multimap<Long, Long> assignments;

	@Inject
	private UserRoleService userRoleService;

	@Override
	public void prepare() throws Exception {
		assignments = HashMultimap.create();
	}

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_ROLE_UPDATE })
	public String execute() {
		for (User user : getTaskFlowData().getUsers()) {
			for (Role role : getTaskFlowData().getRoles()) {
				try {
					if (assignments.get(user.getUserId()).contains(role.getRoleId())) {
						UserRole userRole = userRoleService.persist(getServiceContext(), user.getUserId(), role.getRoleId());
						user.getUserRoles().add(userRole);
					}
				} catch (ValidationServiceException e) {
					addFieldError("assignments[" + user.getUserId() + "]", e.getLocalizedMessage());
				}
			}
		}

		for (User user : getTaskFlowData().getUsers()) {
			for (Role role : getTaskFlowData().getRoles()) {
				try {
					if (!assignments.get(user.getUserId()).contains(role.getRoleId())) {
						userRoleService.delete(getServiceContext(), user.getUserId(), role.getRoleId());
						UserRole userRole = new UserRole();
						userRole.setRoleId(role.getRoleId());
						userRole.setUserId(user.getUserId());
						user.getUserRoles().remove(userRole);
					}
				} catch (ValidationServiceException e) {
					addFieldError("assignments[" + user.getUserId() + "]", e.getLocalizedMessage());
				}
			}
		}

		if (hasErrors()) {
			return "invalid";
		}
		return SUCCESS;
	}

	public Multimap<Long, Long> getAssignments() {
		return assignments;
	}
}