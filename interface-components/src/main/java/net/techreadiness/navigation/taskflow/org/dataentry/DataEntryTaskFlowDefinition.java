package net.techreadiness.navigation.taskflow.org.dataentry;

import static net.techreadiness.security.CorePermissionCodes.READY_CUSTOMER_NETWORK_INFRASTRUCTURE;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowDefinition;

import org.springframework.stereotype.Component;

@Component
public class DataEntryTaskFlowDefinition extends TaskFlowDefinition {

	public DataEntryTaskFlowDefinition() {
		addTask(new Task("/task/dataentry", "data-entry", "ready.org.task.dataEntry", READY_CUSTOMER_NETWORK_INFRASTRUCTURE));

	}

	@Override
	public String getNamespace() {
		return "/task/dataentry";
	}

	@Override
	public String getStartAction() {
		return "dataEntryTaskFlowBegin";
	}

	@Override
	public String getReturnUrl() {
		return "/org/dataentry/list";
	}

	@Override
	public String getEndTaskFlowUrl() {
		return "task/dataentry/end";
	}

}
