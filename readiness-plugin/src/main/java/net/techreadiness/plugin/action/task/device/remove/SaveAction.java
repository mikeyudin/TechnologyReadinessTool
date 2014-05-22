package net.techreadiness.plugin.action.task.device.remove;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.device.BaseDeviceTaskAction;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.DeviceService;
import net.techreadiness.service.object.Device;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "remove" }),
		@Result(name = "invalid", type = "lastAction", params = { "actionName", "remove" }) })
public class SaveAction extends BaseDeviceTaskAction {
	private static final long serialVersionUID = 1L;

	@ConversationScoped(value = "deviceSearchGrid")
	private DataGridState<Device> deviceSearchGrid;
	@Key(Long.class)
	@Element(Boolean.class)
	private final Map<Long, Boolean> devices = new HashMap<>();

	@Inject
	private DeviceService deviceService;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DEVICE_DELETE })
	@Override
	public String execute() {
		boolean deleted = false;

		for (Entry<Long, Boolean> device : devices.entrySet()) {
			if (device.getValue().booleanValue()) {
				deviceService.delete(getServiceContext(), device.getKey());
				deviceSearchGrid.deSelectItem(device.getKey().toString());
				Iterator<Device> iterator = getTaskFlowData().getDevices().iterator();
				while (iterator.hasNext()) {
					Device next = iterator.next();
					if (next.getDeviceId().equals(device.getKey())) {
						iterator.remove();
						break;
					}
				}
				deleted = true;
			}
		}

		if (!deleted) {
			addActionError(getText("ready.noDeviceSelected"));
			return "invalid";
		}

		return SUCCESS;
	}

	public Map<Long, Boolean> getDevices() {
		return devices;
	}
}
