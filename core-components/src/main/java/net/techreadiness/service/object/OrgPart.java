package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.OrgPartDO;

import com.google.common.base.Objects;

public class OrgPart extends BaseObject<OrgPartDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	Long orgPartId;

	// extra mapped information
	Scope scope;
	Org org;

	public OrgPart() { // required by JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("orgPartId", orgPartId).add("org", org).toString();
	}

	@Override
	public Class<OrgPartDO> getBaseEntityType() {
		return OrgPartDO.class;
	}

	public Long getOrgPartId() {
		return orgPartId;
	}

	public void setOrgPartId(Long orgPartId) {
		this.orgPartId = orgPartId;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	@Override
	public Long getId() {
		return orgPartId;
	}
}
