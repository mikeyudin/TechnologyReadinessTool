package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
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

import net.techreadiness.persistence.AuditedBaseEntityWithExt;
import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.persistence.ServiceObjectMapped;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.OrgPart;

/**
 * The persistent class for the org_part database table.
 * 
 */
@Entity
@Table(name = "org_part")
public class OrgPartDO extends AuditedBaseEntityWithExt implements Serializable, ServiceObjectMapped {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "org_part_id", unique = true, nullable = false)
	private Long orgPartId;

	// bi-directional many-to-one association to OrgDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = false)
	private OrgDO org;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	private ScopeDO scope;

	// bi-directional many-to-one association to OrgPartExtDO
	@OneToMany(mappedBy = "orgPart", cascade = { CascadeType.REMOVE })
	private List<OrgPartExtDO> orgPartExts;

	public OrgPartDO() {
	}

	@Override
	protected void populateExtAttributes() {
		Map<String, String> map = new HashMap<>();

		if (orgPartExts != null && orgPartExts.size() > 0) {
			for (OrgPartExtDO orgPartExtDO : orgPartExts) {
				map.put(orgPartExtDO.getEntityField().getCode(), orgPartExtDO.getValue());
			}
		}

		this.setExtAttributes(map);
	}

	public Long getOrgPartId() {
		return orgPartId;
	}

	public void setOrgPartId(Long orgPartId) {
		this.orgPartId = orgPartId;
	}

	public OrgDO getOrg() {
		return org;
	}

	public void setOrg(OrgDO org) {
		this.org = org;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public List<OrgPartExtDO> getOrgPartExts() {
		return orgPartExts;
	}

	public void setOrgPartExts(List<OrgPartExtDO> orgPartExts) {
		this.orgPartExts = orgPartExts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (org == null ? 0 : org.hashCode());
		result = prime * result + (scope == null ? 0 : scope.hashCode());
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
		if (!(obj instanceof OrgPartDO)) {
			return false;
		}
		OrgPartDO other = (OrgPartDO) obj;
		if (org == null) {
			if (other.org != null) {
				return false;
			}
		} else if (!org.equals(other.org)) {
			return false;
		}
		if (scope == null) {
			if (other.scope != null) {
				return false;
			}
		} else if (!scope.equals(other.scope)) {
			return false;
		}
		return true;
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return OrgPart.class;
	}
}