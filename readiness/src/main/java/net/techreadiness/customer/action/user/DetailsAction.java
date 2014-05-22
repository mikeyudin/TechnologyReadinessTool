package net.techreadiness.customer.action.user;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ORG_UPDATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_PASSWORD_TOKEN_ACCESS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ROLE_UPDATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_UPDATE;
import net.techreadiness.navigation.taskflow.user.UserTaskFlowDefinition;
import net.techreadiness.security.PermissionCodeSet;
import net.techreadiness.security.PermissionCodeSetImpl;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.exception.ServiceException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class DetailsAction extends AbstractUserAction {

	private static final long serialVersionUID = 1L;
	private boolean reset;

	@Action(value = "details", results = { @Result(name = "success", location = "/user/details.jsp") })
	public String details() throws ServiceException {
		if (getUser().getUserId() == null) {
			setUser(userService.getById(getServiceContext(), getUserId()));
		}
		viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.USER);
		if (reset) {
			orgDataGridState.getFilters().clear();
			roleDataGridState.getFilters().clear();
		}

		return SUCCESS;
	}

	@Action(value = "detailsUserEdit", results = { @Result(name = "success", type = "redirect", location = "/task/user/altUserTaskFlowBegin") })
	public String detailsUserEdit() throws ServiceException {
		return routeToTask(UserTaskFlowDefinition.TASK_EDIT_USERS);
	}

	@Action(value = "detailsRoleAssignments", results = { @Result(name = "success", type = "redirect", location = "/task/user/altUserTaskFlowBegin") })
	public String detailsRoleAssignments() throws ServiceException {
		setRole(roleService.getById(getServiceContext(), roleId));
		return routeToTask(UserTaskFlowDefinition.TASK_ROLE_ASSIGN);
	}

	@Action(value = "detailsOrgAssignments", results = { @Result(name = "success", type = "redirect", location = "/task/user/altUserTaskFlowBegin") })
	public String detailsOrgAssignments() throws ServiceException {
		setOrg(orgService.getById(getServiceContext(), orgId));
		return routeToTask(UserTaskFlowDefinition.TASK_ORG_ASSIGN);
	}

	@Action(value = "detailsAddOrgAssignments", results = { @Result(name = "success", type = "redirect", location = "/task/user/altUserTaskFlowBegin") })
	public String detailsAddOrgAssignments() throws ServiceException {
		return routeToTask(UserTaskFlowDefinition.TASK_ORG_ASSIGN);
	}

	@Action(value = "detailsAddRoleAssignments", results = { @Result(name = "success", type = "redirect", location = "/task/user/altUserTaskFlowBegin") })
	public String detailsAddRoleAssignments() throws ServiceException {
		return routeToTask(UserTaskFlowDefinition.TASK_ROLE_ASSIGN);
	}

	public boolean isShowUserEditLink() {
		if (hasPermission(CORE_CUSTOMER_USER_UPDATE)) {
			return showTaskLinks;
		}
		return false;
	}

	public boolean isShowOrgTaskLinks() {
		if (hasPermission(CORE_CUSTOMER_USER_ORG_UPDATE)) {
			return showTaskLinks;
		}
		return false;
	}

	public boolean isShowRoleTaskLinks() {
		if (hasPermission(CORE_CUSTOMER_USER_ROLE_UPDATE)) {
			return showTaskLinks;
		}
		return false;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public String getEmailInfo() {
		if (getUser().getUserId() == null) {
			return "";
		}
		return userService.getCurrentResetEmailText(getServiceContext(), getUser().getUserId()).replaceAll("&", "%26")
				.replaceAll("\n", "%0A");
	}

	public String getEmailInfoHTML() {
		if (getUser().getUserId() == null) {
			return "";
		}
		return userService.getCurrentResetEmailText(getServiceContext(), getUser().getUserId()).replaceAll(
				"(\r\n|\n\r|\r|\n)", "<br />");
	}

	public PermissionCodeSet getCoreCustomerUserPasswordTokenAccess() {
		return new PermissionCodeSetImpl(CORE_CUSTOMER_USER_PASSWORD_TOKEN_ACCESS);
	}
}
