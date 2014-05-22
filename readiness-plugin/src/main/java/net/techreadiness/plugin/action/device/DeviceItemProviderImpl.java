package net.techreadiness.plugin.action.device;

import java.util.Collection;

import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Device;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DeviceItemProviderImpl implements DeviceItemProvider {

	private Collection<Device> selectedDevices;

	@Override
	public void setDevices(Collection<Device> devices) {
		selectedDevices = devices;
	}

	@Override
	public Collection<Device> getPage(DataGrid<Device> grid) {
		return selectedDevices;
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Device> grid) {
		if (selectedDevices != null) {
			return selectedDevices.size();
		}
		return 0;
	}

}
