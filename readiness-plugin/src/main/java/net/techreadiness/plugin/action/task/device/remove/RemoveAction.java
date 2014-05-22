package net.techreadiness.plugin.action.task.device.remove;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.device.BaseDeviceTaskAction;
import net.techreadiness.plugin.action.device.DeviceItemProvider;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", location = "/net/techreadiness/plugin/action/device/remove.jsp"),
		@Result(name = "nodevice", location = "/net/techreadiness/plugin/action/device/nodevice.jsp"),
		@Result(name = "invalidorg", location = "/net/techreadiness/plugin/action/device/invalidorg.jsp") })
public class RemoveAction extends BaseDeviceTaskAction {
	private static final long serialVersionUID = 1L;

	@ConversationScoped(value = "deviceRemoveGrid")
	private DataGridState<Map<String, String>> deviceRemoveGrid;

	@Inject
	private DeviceItemProvider deviceItemProvider;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DEVICE_DELETE })
	@Override
	public String execute() {
		if (getTaskFlowData().getDevices() == null || getTaskFlowData().getDevices().isEmpty()) {
			return "nodevice";
		}
		deviceItemProvider.setDevices(getTaskFlowData().getDevices());
		return SUCCESS;
	}

	public DataGridState<Map<String, String>> getDeviceRemoveGrid() {
		return deviceRemoveGrid;
	}

	public void setDeviceRemoveGrid(DataGridState<Map<String, String>> deviceRemoveGrid) {
		this.deviceRemoveGrid = deviceRemoveGrid;
	}

	public DeviceItemProvider getDeviceItemProvider() {
		return deviceItemProvider;
	}

}
