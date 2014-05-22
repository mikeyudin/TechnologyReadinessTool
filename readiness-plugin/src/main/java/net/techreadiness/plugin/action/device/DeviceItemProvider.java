package net.techreadiness.plugin.action.device;

import java.util.Collection;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Device;

public interface DeviceItemProvider extends DataGridItemProvider<Device> {
	void setDevices(Collection<Device> devices);
}
