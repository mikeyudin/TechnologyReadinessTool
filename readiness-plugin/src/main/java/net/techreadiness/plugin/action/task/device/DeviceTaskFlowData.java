package net.techreadiness.plugin.action.task.device;

import java.util.Collection;

import net.techreadiness.service.object.Device;
import net.techreadiness.ui.task.TaskFlowData;

import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Scope("session")
public class DeviceTaskFlowData extends TaskFlowData {
	private static final long serialVersionUID = 1L;
	private Collection<Device> devices;

	public Collection<Device> getDevices() {
		return devices;
	}

	public void setDevices(Collection<Device> devices) {
		this.devices = devices;
	}
}
