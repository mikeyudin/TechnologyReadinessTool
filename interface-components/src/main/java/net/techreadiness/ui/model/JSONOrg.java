package net.techreadiness.ui.model;

public class JSONOrg {
	private Long orgId;
	private String name;
	private String code;
	private String description;

	public JSONOrg(Long orgId, String name, String code, String city, String state, String type) {
		this.orgId = orgId;
		this.name = name;
		this.code = code;
		description = type + " in " + city + ", " + state;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getId() {
		return orgId;
	}

	public void setId(Long id) {
		orgId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
