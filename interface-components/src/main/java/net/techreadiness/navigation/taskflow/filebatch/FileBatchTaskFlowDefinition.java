package net.techreadiness.navigation.taskflow.filebatch;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_DEVICE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_LOADING_ACCESS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_ORG;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_USER;
import static net.techreadiness.security.CorePermissionCodes.READY_CUSTOMER_FILE_ORG_INFO;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowDefinition;

import org.springframework.stereotype.Component;

@Component
public class FileBatchTaskFlowDefinition extends TaskFlowDefinition {

	public static final String TASK_FILEBATCH_DETAILS = "task.filebatch.details";
	public static final String TASK_FILEBATCH_ORGANIZATIONS = "task.filebatch.organizations";
	public static final String TASK_FILEBATCH_USERS = "task.filebatch.users";
	public static final String TASK_FILEBATCH_DEVICES = "task.filebatch.devices";
	public static final String TASK_FILEBATCH_ORG_INFO = "task.filebatch.orgInfo";

	public FileBatchTaskFlowDefinition() {
		addTask(new Task("/task/batch/device", "devices", TASK_FILEBATCH_DEVICES, CORE_CUSTOMER_FILE_DEVICE));
		addTask(new Task("/task/batch/user", "users", TASK_FILEBATCH_USERS, CORE_CUSTOMER_FILE_USER));
		addTask(new Task("/task/batch/org", "org-batch-task", TASK_FILEBATCH_ORGANIZATIONS, CORE_CUSTOMER_FILE_ORG));
		addTask(new Task("/task/batch/details", "details", TASK_FILEBATCH_DETAILS, CORE_CUSTOMER_FILE_LOADING_ACCESS));
		addTask(new Task("/task/batch/org/info", "org-info-batch-task", TASK_FILEBATCH_ORG_INFO,
				READY_CUSTOMER_FILE_ORG_INFO));
	}

	@Override
	public String getNamespace() {
		return "/task/batch";
	}

	@Override
	public String getStartAction() {
		return "fileBatchTaskFlowBegin";
	}

	@Override
	public String getReturnUrl() {
		return "/filebatch/list";
	}

	@Override
	public String getEndTaskFlowUrl() {
		return null;
	}
}
