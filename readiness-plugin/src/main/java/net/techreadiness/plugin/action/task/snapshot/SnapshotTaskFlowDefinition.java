package net.techreadiness.plugin.action.task.snapshot;

import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowDefinition;

import org.springframework.stereotype.Component;

@Component
public class SnapshotTaskFlowDefinition extends TaskFlowDefinition {

	public SnapshotTaskFlowDefinition() {
		addTask(new Task("/task/snapshot", "add", "ready.task.snapshot.add.title",
				CorePermissionCodes.READY_CUSTOMER_SNAPSHOT_CREATE));
		addTask(new Task("/task/snapshot/edit", "edit", "ready.task.snapshot.edit.title",
				CorePermissionCodes.READY_CUSTOMER_SNAPSHOT_EDIT));
		addTask(new Task("/task/snapshot/remove", "remove", "ready.task.snapshot.delete.title",
				CorePermissionCodes.READY_CUSTOMER_SNAPSHOT_DELETE));
	}

	@Override
	public String getNamespace() {
		return "/task/snapshot";
	}

	@Override
	public String getStartAction() {
		return "snapshotTaskFlowBegin";
	}

	@Override
	public String getReturnUrl() {
		return "/snapshot/list";
	}

	@Override
	public String getEndTaskFlowUrl() {
		return "/task/snapshot/taskFlowEnd";
	}

}
