package net.techreadiness.plugin.action.task.device;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.object.Device;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

@Namespace("/task/device")
public class DeviceTaskFlowAction extends BaseTaskFlowAction<DeviceTaskFlowData, DeviceTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_READINESS_ACCESS })
	@Action(value = "deviceTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String beginTaskFlow() {
		startNewTaskFlow();

		DataGridState<Device> dataGridState = conversation.get(DataGridState.class, "deviceSearchGrid");

		getTaskFlowData().setDevices(dataGridState.getSelectedItems());

		return SUCCESS;
	}
}
