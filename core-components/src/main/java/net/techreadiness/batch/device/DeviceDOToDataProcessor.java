package net.techreadiness.batch.device;

import javax.inject.Inject;

import net.techreadiness.persistence.domain.DeviceDO;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.batch.item.ItemProcessor;

public class DeviceDOToDataProcessor implements ItemProcessor<DeviceDO, DeviceData> {

	@Inject
	private MappingService mappingService;

	@Override
	public DeviceData process(DeviceDO item) throws Exception {
		DeviceData deviceData = new DeviceData();
		Device device = mappingService.map(item);
		deviceData.setDevice(device);
		deviceData.setStateCode(device.getOrg().getState());
		return deviceData;
	}

}
