package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.OrgTypeDO;

public class OrgType extends BaseObject<OrgTypeDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	Long orgTypeId;
	@CoreField
	String code;
	@CoreField
	String name;
	@CoreField
	Boolean allowStudent;
	@CoreField
	Boolean allowGroup;
	@CoreField
	Boolean allowDevice;

	Long parentOrgTypeId;
	Long scopeId;

	public OrgType() {

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

	public Boolean getAllowStudent() {
		return allowStudent;
	}

	public void setAllowStudent(Boolean allowStudent) {
		this.allowStudent = allowStudent;
	}

	public Boolean getAllowGroup() {
		return allowGroup;
	}

	public void setAllowGroup(Boolean allowGroup) {
		this.allowGroup = allowGroup;
	}

	public Long getParentOrgTypeId() {
		return parentOrgTypeId;
	}

	public void setParentOrgTypeId(Long parentOrgTypeId) {
		this.parentOrgTypeId = parentOrgTypeId;
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public Boolean getAllowDevice() {
		return allowDevice;
	}

	public void setAllowDevice(Boolean allowDevice) {
		this.allowDevice = allowDevice;
	}

	@Override
	public Class<OrgTypeDO> getBaseEntityType() {
		return OrgTypeDO.class;
	}

	@Override
	public Long getId() {
		return orgTypeId;
	}
}
