package net.techreadiness.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the role_delegation database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "role_delegation")
public class RoleDelegationDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_delegation_id", unique = true, nullable = false)
	private Long roleDelegationId;

	// bi-directional many-to-one association to RoleDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delegated_role_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private RoleDO delegRole;

	// bi-directional many-to-one association to RoleDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private RoleDO role;

	public RoleDelegationDO() {
	}

	public Long getRoleDelegationId() {
		return roleDelegationId;
	}

	public void setRoleDelegationId(Long roleDelegationId) {
		this.roleDelegationId = roleDelegationId;
	}

	public RoleDO getDelegRole() {
		return delegRole;
	}

	public void setDelegRole(RoleDO delegRole) {
		this.delegRole = delegRole;
	}

	public RoleDO getRole() {
		return role;
	}

	public void setRole(RoleDO role) {
		this.role = role;
	}

}