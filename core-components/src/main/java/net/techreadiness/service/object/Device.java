package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.DeviceDO;

public class Device extends BaseObjectWithExts<DeviceDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	private Long deviceId;
	@CoreField
	private String operatingSystem;
	@CoreField
	private String location;
	@CoreField
	private String name;
	@CoreField
	private Integer count;
	@CoreField
	private Integer processor;
	@CoreField
	private Integer memory;
	@CoreField
	private Integer storage;
	@CoreField
	private Integer flashVersion;
	@CoreField
	private Integer javaVersion;
	@CoreField
	private Integer browser;
	@CoreField
	private Integer screenResolution;
	@CoreField
	private Integer monitorDisplaySize;
	@CoreField
	private String environment;
	@CoreField
	private String owner;
	@CoreField
	private String deviceType;
	@CoreField
	private String wireless;

	private Org org;

	@Override
	public Long getId() {
		return deviceId;
	}

	@Override
	public Class<DeviceDO> getBaseEntityType() {
		return DeviceDO.class;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
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

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Integer getMonitorDisplaySize() {
		return monitorDisplaySize;
	}

	public void setMonitorDisplaySize(Integer monitorDisplaySize) {
		this.monitorDisplaySize = monitorDisplaySize;
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

}
