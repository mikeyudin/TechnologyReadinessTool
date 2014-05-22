package net.techreadiness.persistence.dao;

import java.util.List;

import javax.inject.Named;

import net.techreadiness.persistence.domain.DeviceDO;
import net.techreadiness.persistence.domain.DeviceExtDO;

import org.springframework.stereotype.Repository;

@Repository
@Named("deviceExtDAOImpl")
public class DeviceExtDAOImpl extends BaseDAOImpl<DeviceExtDO> implements DeviceExtDAO, ExtDAO<DeviceDO, DeviceExtDO> {

	@Override
	public List<DeviceExtDO> getExtDOs(DeviceDO baseEntityWithExt) {
		return baseEntityWithExt.getDeviceExts();
	}

	@Override
	public DeviceExtDO getNew() {
		return new DeviceExtDO();
	}

}
