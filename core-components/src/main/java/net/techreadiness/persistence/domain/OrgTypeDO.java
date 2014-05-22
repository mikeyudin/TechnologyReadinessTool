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
import net.techreadiness.service.object.OrgType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the org_type database table.
 * 
 */
@Entity
@Table(name = "org_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrgTypeDO extends AuditedBaseEntity implements Serializable, ServiceObjectMapped {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "org_type_id", unique = true, nullable = false)
	private Long orgTypeId;

	@Column(name = "allow_device", nullable = false)
	private boolean allowDevice;

	@Column(nullable = false, length = 50)
	private String code;

	@Column(nullable = false, length = 100)
	private String name;

	// bi-directional many-to-one association to OrgDO
	@OneToMany(mappedBy = "orgType")
	private List<OrgDO> orgs;

	// bi-directional many-to-one association to OrgTypeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_org_type_id")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private OrgTypeDO parentOrgType;

	// bi-directional many-to-one association to OrgTypeDO
	@OneToMany(mappedBy = "parentOrgType")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<OrgTypeDO> orgTypes;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO scope;

	public OrgTypeDO() {
	}

	public Long getOrgTypeId() {
		return orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId) {
		this.orgTypeId = orgTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OrgDO> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<OrgDO> orgs) {
		this.orgs = orgs;
	}

	public OrgTypeDO getParentOrgType() {
		return parentOrgType;
	}

	public void setParentOrgType(OrgTypeDO orgType) {
		parentOrgType = orgType;
	}

	public List<OrgTypeDO> getOrgTypes() {
		return orgTypes;
	}

	public void setOrgTypes(List<OrgTypeDO> orgTypes) {
		this.orgTypes = orgTypes;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public boolean isAllowDevice() {
		return allowDevice;
	}

	public void setAllowDevice(boolean allowDevice) {
		this.allowDevice = allowDevice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (orgTypeId == null ? 0 : orgTypeId.hashCode());
		result = prime * result + (parentOrgType == null ? 0 : parentOrgType.hashCode());
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
		if (!(obj instanceof OrgTypeDO)) {
			return false;
		}
		OrgTypeDO other = (OrgTypeDO) obj;
		if (orgTypeId == null) {
			if (other.getOrgTypeId() != null) {
				return false;
			}
		} else if (!orgTypeId.equals(other.getOrgTypeId())) {
			return false;
		}
		if (parentOrgType == null) {
			if (other.getParentOrgType() != null) {
				return false;
			}
		} else if (!parentOrgType.equals(other.getParentOrgType())) {
			return false;
		}
		return true;
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return OrgType.class;
	}
}