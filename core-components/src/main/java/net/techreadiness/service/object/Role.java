package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.RoleDO;

import com.google.common.base.Objects;

public class Role extends BaseObject<RoleDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	Long roleId;
	@CoreField
	String code;
	@CoreField
	String description;
	@CoreField
	Integer displayOrder;
	@CoreField
	String name;
	@CoreField
	String category;
	@CoreField
	String shortName;

	Scope scope;

	public Role() { // required by JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("roleId", roleId).add("name", name).toString();
	}

	@Override
	public Class<RoleDO> getBaseEntityType() {
		return RoleDO.class;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Long getId() {
		return roleId;
	}
}
