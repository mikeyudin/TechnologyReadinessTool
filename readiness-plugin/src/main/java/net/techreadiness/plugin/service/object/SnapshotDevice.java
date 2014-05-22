package net.techreadiness.plugin.service.object;

public class SnapshotDevice {
	private String orgName;
	private String orgCode;
	private String localOrgCode;
	private String parentOrgName;
	private String parentOrgCode;
	private String parentLocalOrgCode;
	private String name;
	private Long count;
	private String operatingSystem;
	private String memory;
	private String monitorDisplaySize;
	private String screenResolution;
	private String environment;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getLocalOrgCode() {
		return localOrgCode;
	}

	public void setLocalOrgCode(String localOrgCode) {
		this.localOrgCode = localOrgCode;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getParentOrgCode() {
		return parentOrgCode;
	}

	public void setParentOrgCode(String parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}

	public String getParentLocalOrgCode() {
		return parentLocalOrgCode;
	}

	public void setParentLocalOrgCode(String parentLocalOrgCode) {
		this.parentLocalOrgCode = parentLocalOrgCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getMonitorDisplaySize() {
		return monitorDisplaySize;
	}

	public void setMonitorDisplaySize(String monitorDisplaySize) {
		this.monitorDisplaySize = monitorDisplaySize;
	}

	public String getScreenResolution() {
		return screenResolution;
	}

	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
}
