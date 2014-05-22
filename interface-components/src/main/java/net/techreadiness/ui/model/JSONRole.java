package net.techreadiness.ui.model;

public class JSONRole {

	private Long roleId;
	private String name;
	private String code;
	private String description;

	public JSONRole(Long roleId, String name, String code) {
		this.roleId = roleId;
		this.setId(roleId);
		this.name = name;
		this.code = code;
		description = name;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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

	public Long getId() {
		return roleId;
	}

	public void setId(Long id) {
		roleId = id;
	}

}
