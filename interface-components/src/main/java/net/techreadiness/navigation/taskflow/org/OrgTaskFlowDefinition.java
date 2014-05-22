package net.techreadiness.navigation.taskflow.org;

import static net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition.TASK_FILEBATCH_ORGANIZATIONS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_ORG;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_CONTACT_UPDATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_CREATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_DELETE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_PART_UPDATE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_UPDATE;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowDefinition;

import org.springframework.stereotype.Component;

@Component
public class OrgTaskFlowDefinition extends TaskFlowDefinition {
	public static String TASK_CREATE_ORG = "task.title.organization.new";
	public static String TASK_UPDATE_ORG = "task.title.organization.edit";
	public static String TASK_PARICIPATIONS = "task.title.organization.participations";
	public static String TASK_EDIT_CONTACTS = "task.title.organization.contacts";
	public static String TASK_DELETE = "task.title.organization.delete";

	public OrgTaskFlowDefinition() {
		addTask(new Task("/task/org/create", "add", TASK_CREATE_ORG, CORE_CUSTOMER_ORG_CREATE));
		addTask(new Task("/task/org/edit", "edit", TASK_UPDATE_ORG, CORE_CUSTOMER_ORG_UPDATE));
		addTask(new Task("/task/org/part", "edit", TASK_PARICIPATIONS, CORE_CUSTOMER_ORG_PART_UPDATE));
		addTask(new Task("/task/org/contact", "edit", TASK_EDIT_CONTACTS, CORE_CUSTOMER_ORG_CONTACT_UPDATE));
		addTask(new Task("/task/org/delete", "delete", TASK_DELETE, CORE_CUSTOMER_ORG_DELETE));
		addExternalTask(new Task("/task/batch/org", "org-batch-task", TASK_FILEBATCH_ORGANIZATIONS, CORE_CUSTOMER_FILE_ORG));
	}

	@Override
	public String getNamespace() {
		return "/task/org";
	}

	@Override
	public String getStartAction() {
		return "orgTaskFlowBegin";
	}

	@Override
	public String getReturnUrl() {
		return "/organization/list";
	}

	@Override
	public String getEndTaskFlowUrl() {
		return "/task/org/orgTaskFlowEnd";
	}

}
