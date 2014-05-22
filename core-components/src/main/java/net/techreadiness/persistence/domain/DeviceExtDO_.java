package net.techreadiness.persistence.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.techreadiness.persistence.BaseEntity_;

@Generated(value = "Dali", date = "2011-11-21T14:26:54.529-0600")
@StaticMetamodel(DeviceExtDO.class)
public class DeviceExtDO_ extends BaseEntity_ {
	public static volatile SingularAttribute<DeviceExtDO, Long> deviceExtId;
	public static volatile SingularAttribute<DeviceExtDO, String> value;
	public static volatile SingularAttribute<DeviceExtDO, EntityFieldDO> entityField;
	public static volatile SingularAttribute<DeviceExtDO, DeviceDO> device;
}
