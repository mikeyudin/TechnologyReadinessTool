package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(DeviceDO.class)
public class DeviceDO_ {

	public static volatile SingularAttribute<DeviceDO, Long> deviceId;
	public static volatile SingularAttribute<DeviceDO, OrgDO> org;
	public static volatile SingularAttribute<DeviceDO, Long> snapshotSummaryId;
	public static volatile SingularAttribute<DeviceDO, String> location;
	public static volatile SingularAttribute<DeviceDO, String> name;
	public static volatile SingularAttribute<DeviceDO, Integer> count;
	public static volatile SingularAttribute<DeviceDO, String> operatingSystem;
	public static volatile SingularAttribute<DeviceDO, String> owner;
	public static volatile SingularAttribute<DeviceDO, String> environment;
	public static volatile SingularAttribute<DeviceDO, Integer> processor;
	public static volatile SingularAttribute<DeviceDO, Integer> memory;
	public static volatile SingularAttribute<DeviceDO, Integer> storage;
	public static volatile SingularAttribute<DeviceDO, Integer> flashVersion;
	public static volatile SingularAttribute<DeviceDO, Integer> javaVersion;
	public static volatile SingularAttribute<DeviceDO, String> deviceType;
	public static volatile SingularAttribute<DeviceDO, String> wireless;
	public static volatile SingularAttribute<DeviceDO, Integer> browser;
	public static volatile SingularAttribute<DeviceDO, Integer> screenResolution;
	public static volatile SingularAttribute<DeviceDO, Integer> monitorDisplaySize;
	public static volatile ListAttribute<DeviceDO, DeviceExtDO> deviceExts;
}
