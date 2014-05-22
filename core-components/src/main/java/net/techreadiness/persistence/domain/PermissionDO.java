package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.List;

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

import net.techreadiness.persistence.AuditedBaseEntity;
import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.persistence.ServiceObjectMapped;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.Permission;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the permission database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "permission")
public class PermissionDO extends AuditedBaseEntity implements Serializable, ServiceObjectMapped {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "permission_id", unique = true, nullable = false)
	private Long permissionId;

	@Column(nullable = false, length = 50)
	private String code;

	@Column(length = 1000)
	private String description;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(nullable = false, length = 100)
	private String name;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO scope;

	// bi-directional many-to-one association to RolePermissionDO
	@OneToMany(mappedBy = "permission")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<RolePermissionDO> rolePermissions;

	public PermissionDO() {
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

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public List<RolePermissionDO> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(List<RolePermissionDO> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return Permission.class;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getPermissionId() == null ? 0 : getPermissionId().hashCode());
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
		if (!(obj instanceof PermissionDO)) {
			return false;
		}
		PermissionDO other = (PermissionDO) obj;
		if (getPermissionId() == null) {
			if (other.getPermissionId() != null) {
				return false;
			}
		} else if (!getPermissionId().equals(other.getPermissionId())) {
			return false;
		}
		return true;
	}
}