package net.techreadiness.service.object.mapping;

import java.util.Map;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.DeviceDO;
import net.techreadiness.persistence.domain.DeviceExtDO;
import net.techreadiness.service.object.Device;

import com.google.common.collect.Maps;

public class DeviceDOandDeviceMapper extends CustomMapper<DeviceDO, Device> {

	@Override
	public void mapAtoB(DeviceDO deviceDO, Device device, MappingContext context) {
		Map<String, String> map = Maps.newHashMap();
		if (deviceDO.getDeviceExts() != null) {
			for (DeviceExtDO deviceExtDO : deviceDO.getDeviceExts()) {
				map.put(deviceExtDO.getEntityField().getCode(), deviceExtDO.getValue());
			}
		}
		device.setExtendedAttributes(map);
	}

	@Override
	public void mapBtoA(Device device, DeviceDO deviceDO, MappingContext context) {
		deviceDO.setExtAttributes(device.getExtendedAttributes());
	}
}
