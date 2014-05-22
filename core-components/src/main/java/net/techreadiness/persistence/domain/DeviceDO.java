package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntityWithExt;
import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.persistence.ServiceObjectMapped;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.Device;

@Entity
@Table(name = "device")
public class DeviceDO extends AuditedBaseEntityWithExt implements Serializable, ServiceObjectMapped {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "device_id")
	private Long deviceId;

	// bi-directional many-to-one association to ContactType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	private OrgDO org;

	@Column(name = "operating_system")
	private String operatingSystem;

	private String name;
	private String location;
	private Integer count;
	private Integer processor;
	private Integer memory;
	private Integer storage;
	@Column(name = "flash_version")
	private Integer flashVersion;

	@Column(name = "java_version")
	private Integer javaVersion;

	private Integer browser;

	@Column(name = "screen_resolution")
	private Integer screenResolution;

	@Column(name = "display_size")
	private Integer monitorDisplaySize;

	@Column(name = "environment")
	private String environment;

	@Column(name = "owner")
	private String owner;

	@Column(name = "device_type")
	private String deviceType;

	@Column(name = "wireless")
	private String wireless;

	// bi-directional many-to-one association to DeviceExtDO
	@OneToMany(mappedBy = "device", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	private List<DeviceExtDO> deviceExts;

	@Override
	protected void populateExtAttributes() {
		Map<String, String> map = new HashMap<>();

		if (deviceExts != null && deviceExts.size() > 0) {
			for (DeviceExtDO deviceExtDO : deviceExts) {
				map.put(deviceExtDO.getEntityField().getCode(), deviceExtDO.getValue());
			}
		}

		this.setExtAttributes(map);
	}

	public DeviceDO() {

	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public OrgDO getOrg() {
		return org;
	}

	public void setOrg(OrgDO org) {
		this.org = org;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getProcessor() {
		return processor;
	}

	public void setProcessor(Integer processor) {
		this.processor = processor;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}

	public Integer getFlashVersion() {
		return flashVersion;
	}

	public void setFlashVersion(Integer flashVersion) {
		this.flashVersion = flashVersion;
	}

	public Integer getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(Integer javaVersion) {
		this.javaVersion = javaVersion;
	}

	public Integer getBrowser() {
		return browser;
	}

	public void setBrowser(Integer browser) {
		this.browser = browser;
	}

	public Integer getScreenResolution() {
		return screenResolution;
	}

	public void setScreenResolution(Integer screenResolution) {
		this.screenResolution = screenResolution;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getWireless() {
		return wireless;
	}

	public void setWireless(String wireless) {
		this.wireless = wireless;
	}

	public List<DeviceExtDO> getDeviceExts() {
		return deviceExts;
	}

	public void setDeviceExts(List<DeviceExtDO> deviceExts) {
		this.deviceExts = deviceExts;
	}

	public void setMonitorDisplaySize(Integer monitorDisplaySize) {
		this.monitorDisplaySize = monitorDisplaySize;
	}

	public Integer getMonitorDisplaySize() {
		return monitorDisplaySize;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (deviceId == null ? 0 : deviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DeviceDO other = (DeviceDO) obj;
		if (deviceId == null) {
			if (other.deviceId != null) {
				return false;
			}
		} else if (!deviceId.equals(other.deviceId)) {
			return false;
		}
		return true;
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return Device.class;
	}
}
