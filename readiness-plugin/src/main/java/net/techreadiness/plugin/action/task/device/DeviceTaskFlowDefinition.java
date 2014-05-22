package net.techreadiness.plugin.action.task.device;

import static net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition.TASK_FILEBATCH_DEVICES;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowDefinition;

import org.springframework.stereotype.Component;

@Component
public class DeviceTaskFlowDefinition extends TaskFlowDefinition {

	public DeviceTaskFlowDefinition() {
		// The text for the keys used below can be found in "customer.properties"
		addTask(new Task("/task/device", "add", "ready.device.task.add", CorePermissionCodes.READY_CUSTOMER_DEVICE_CREATE));
		addTask(new Task("/task/device/edit", "edit", "ready.device.task.edit",
				CorePermissionCodes.READY_CUSTOMER_DEVICE_UPDATE));
		addTask(new Task("/task/device/remove", "remove", "ready.device.task.remove",
				CorePermissionCodes.READY_CUSTOMER_DEVICE_DELETE));
		addExternalTask(new Task("/task/batch/device", "devices", TASK_FILEBATCH_DEVICES,
				CorePermissionCodes.CORE_CUSTOMER_FILE_DEVICE));
	}

	@Override
	public String getNamespace() {
		return "/task/device";
	}

	@Override
	public String getStartAction() {
		return "deviceTaskFlowBegin";
	}

	@Override
	public String getReturnUrl() {
		return "/device/list";
	}

	@Override
	public String getEndTaskFlowUrl() {
		return "/device/task/taskFlowEnd";
	}
}
