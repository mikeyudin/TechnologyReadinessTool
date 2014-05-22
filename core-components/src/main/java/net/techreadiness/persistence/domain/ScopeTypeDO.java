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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.base.Objects;

/**
 * The persistent class for the scope_type database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "scope_type")
public class ScopeTypeDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "scope_type_id", unique = true, nullable = false)
	private Long scopeTypeId;

	@Column(name = "allow_org", nullable = false)
	private boolean allowOrg;

	@Column(name = "allow_org_part", nullable = false)
	private boolean allowOrgPart;

	@Column(name = "allow_user", nullable = false)
	private boolean allowUser;

	@Column(nullable = false, length = 50)
	private String code;

	@Column(length = 1000)
	private String description;

	@Column(nullable = false, length = 100)
	private String name;

	// bi-directional many-to-one association to ScopeDO
	@OneToMany(mappedBy = "scopeType")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ScopeDO> scopes;

	// bi-directional many-to-one association to ScopeTypeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_scope_type_id")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeTypeDO parentScopeType;

	// bi-directional many-to-one association to ScopeTypeDO
	@OneToMany(mappedBy = "parentScopeType")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ScopeTypeDO> scopeTypes;

	public ScopeTypeDO() {
	}

	public Long getScopeTypeId() {
		return scopeTypeId;
	}

	public void setScopeTypeId(Long scopeTypeId) {
		this.scopeTypeId = scopeTypeId;
	}

	public boolean isAllowOrg() {
		return allowOrg;
	}

	public void setAllowOrg(boolean allowOrg) {
		this.allowOrg = allowOrg;
	}

	public boolean isAllowOrgPart() {
		return allowOrgPart;
	}

	public void setAllowOrgPart(boolean allowOrgPart) {
		this.allowOrgPart = allowOrgPart;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ScopeDO> getScopes() {
		return scopes;
	}

	public void setScopes(List<ScopeDO> scopes) {
		this.scopes = scopes;
	}

	public ScopeTypeDO getParentScopeType() {
		return parentScopeType;
	}

	public void setParentScopeType(ScopeTypeDO parentScopeType) {
		this.parentScopeType = parentScopeType;
	}

	public List<ScopeTypeDO> getScopeTypes() {
		return scopeTypes;
	}

	public void setScopeTypes(List<ScopeTypeDO> scopeTypes) {
		this.scopeTypes = scopeTypes;
	}

	public void setAllowUser(boolean allowUser) {
		this.allowUser = allowUser;
	}

	public boolean isAllowUser() {
		return allowUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (scopeTypeId == null ? 0 : scopeTypeId.hashCode());
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
		if (!(obj instanceof ScopeTypeDO)) {
			return false;
		}
		ScopeTypeDO other = (ScopeTypeDO) obj;
		if (scopeTypeId == null) {
			if (other.scopeTypeId != null) {
				return false;
			}
		} else if (!scopeTypeId.equals(other.scopeTypeId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("scopeTypeId", scopeTypeId).add("name", name).toString();
	}
}