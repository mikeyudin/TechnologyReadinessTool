package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.PermissionDO;

import com.google.common.base.Objects;

public class Permission extends BaseObject<PermissionDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	Long permissionId;
	@CoreField
	String code;
	@CoreField
	String description;
	@CoreField
	Integer displayOrder;
	@CoreField
	String name;

	Scope scope;

	public Permission() { // required by JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("permissionId", permissionId).add("name", name).toString();
	}

	@Override
	public Class<PermissionDO> getBaseEntityType() {
		return PermissionDO.class;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
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

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Long getId() {
		return permissionId;
	}
}
