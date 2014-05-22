package net.techreadiness.navigation.taskflow.user;

import static net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition.TASK_FILEBATCH_USERS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_USER;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_CREATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_DELETE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ENABLE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ORG_UPDATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_RESET_PASSWORD;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ROLE_UPDATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_UPDATE;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowDefinition;

import org.springframework.stereotype.Component;

@Component
public class UserTaskFlowDefinition extends TaskFlowDefinition {
	public static String TASK_NEW_USERS = "task.title.users.new";
	public static String TASK_ENABLE_USERS = "task.title.users.enable";
	public static String TASK_ORG_ASSIGN = "task.title.users.organizationAssignment";
	public static String TASK_EDIT_USERS = "task.title.users.edit";
	public static String TASK_ROLE_ASSIGN = "task.title.users.role";
	public static String TASK_CHANGE_PASSWORD = "task.title.users.changePassword";
	public static String TASK_RESET_PASSWORD = "task.title.users.resetPassword";
	public static String TASK_DELETE_USERS = "task.title.users.delete";

	public UserTaskFlowDefinition() {
		addTask(new Task("/task/user/create", "add", TASK_NEW_USERS, CORE_CUSTOMER_USER_CREATE));
		addTask(new Task("/task/user/update", "edit", TASK_EDIT_USERS, CORE_CUSTOMER_USER_UPDATE));
		addTask(new Task("/task/user/enable", "edit", TASK_ENABLE_USERS, CORE_CUSTOMER_USER_ENABLE));
		addTask(new Task("/task/user/orgassign", "edit", TASK_ORG_ASSIGN, CORE_CUSTOMER_USER_ORG_UPDATE));
		addTask(new Task("/task/user/roleassign", "edit", TASK_ROLE_ASSIGN, CORE_CUSTOMER_USER_ROLE_UPDATE));
		addTask(new Task("/task/user/update", "reset-password", TASK_RESET_PASSWORD, CORE_CUSTOMER_USER_RESET_PASSWORD));
		addTask(new Task("/task/user/delete", "delete", TASK_DELETE_USERS, CORE_CUSTOMER_USER_DELETE));
		addExternalTask(new Task("/task/batch/user", "users", TASK_FILEBATCH_USERS, CORE_CUSTOMER_FILE_USER));
	}

	@Override
	public String getNamespace() {
		return "/task/user";
	}

	@Override
	public String getStartAction() {
		return "userTaskFlowBegin";
	}

	@Override
	public String getReturnUrl() {
		return "/user/list";
	}

	@Override
	public String getEndTaskFlowUrl() {
		return "/task/user/userTaskFlowEnd";
	}

}
