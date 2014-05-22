package net.techreadiness.navigation.taskflow.youraccount;

import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowDefinition;

import org.springframework.stereotype.Component;

@Component
public class YourAccountTaskFlowDefinition extends TaskFlowDefinition {
	public static String TASK_EDIT_USERS = "Edit Users";
	public static String TASK_CHANGE_PASSWORD = "Change Password";

	public YourAccountTaskFlowDefinition() {
		addTask(new Task("/task/youraccount/update", "edit", TASK_EDIT_USERS));
		addTask(new Task("/task/youraccount/update", "password", TASK_CHANGE_PASSWORD));
	}

	@Override
	public String getNamespace() {
		return "/task/youraccount";
	}

	@Override
	public String getStartAction() {
		return "yourAccountTaskFlowBegin";
	}

	@Override
	public String getReturnUrl() {
		return "/info";
	}

	@Override
	public String getEndTaskFlowUrl() {
		return "/task/youraccount/yourAccountTaskFlowEnd";
	}

}
