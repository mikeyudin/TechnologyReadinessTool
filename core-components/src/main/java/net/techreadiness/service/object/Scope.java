package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.ScopeDO;

import com.google.common.base.Objects;

public class Scope extends BaseObjectWithExts<ScopeDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	Long scopeId;
	@CoreField
	String path;
	@CoreField
	String code;
	@CoreField
	String description;
	@CoreField
	String name;

	Long parentScopeId;
	String parentScopeCode;
	String parentScopeName;
	String parentScopePath;

	Long scopeTypeId;
	String scopeTypeCode;
	String scopeTypeName;
	Boolean scopeTypeAllowOrg;
	Boolean scopeTypeAllowOrgPart;
	Boolean scopeTypeAllowStaff;
	Boolean scopeTypeAllowUser;

	public Scope() { // needed for JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("scopeId", scopeId).add("path", path).add("name", name).toString();
	}

	@Override
	public Class<ScopeDO> getBaseEntityType() {
		return ScopeDO.class;
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public Long getParentScopeId() {
		return parentScopeId;
	}

	public void setParentScopeId(Long parentScopeId) {
		this.parentScopeId = parentScopeId;
	}

	public String getParentScopeCode() {
		return parentScopeCode;
	}

	public void setParentScopeCode(String parentScopeCode) {
		this.parentScopeCode = parentScopeCode;
	}

	public String getParentScopeName() {
		return parentScopeName;
	}

	public void setParentScopeName(String parentScopeName) {
		this.parentScopeName = parentScopeName;
	}

	public Long getScopeTypeId() {
		return scopeTypeId;
	}

	public void setScopeTypeId(Long scopeTypeId) {
		this.scopeTypeId = scopeTypeId;
	}

	public String getScopeTypeCode() {
		return scopeTypeCode;
	}

	public void setScopeTypeCode(String scopeTypeCode) {
		this.scopeTypeCode = scopeTypeCode;
	}

	public String getScopeTypeName() {
		return scopeTypeName;
	}

	public void setScopeTypeName(String scopeTypeName) {
		this.scopeTypeName = scopeTypeName;
	}

	public void setParentScopePath(String parentScopePath) {
		this.parentScopePath = parentScopePath;
	}

	public String getParentScopePath() {
		return parentScopePath;
	}

	public Boolean getScopeTypeAllowOrg() {
		return scopeTypeAllowOrg;
	}

	public void setScopeTypeAllowOrg(Boolean scopeTypeAllowOrg) {
		this.scopeTypeAllowOrg = scopeTypeAllowOrg;
	}

	public Boolean getScopeTypeAllowOrgPart() {
		return scopeTypeAllowOrgPart;
	}

	public void setScopeTypeAllowOrgPart(Boolean scopeTypeAllowOrgPart) {
		this.scopeTypeAllowOrgPart = scopeTypeAllowOrgPart;
	}

	public Boolean getScopeTypeAllowStaff() {
		return scopeTypeAllowStaff;
	}

	public void setScopeTypeAllowStaff(Boolean scopeTypeAllowStaff) {
		this.scopeTypeAllowStaff = scopeTypeAllowStaff;
	}

	public Boolean getScopeTypeAllowUser() {
		return scopeTypeAllowUser;
	}

	public void setScopeTypeAllowUser(Boolean scopeTypeAllowUser) {
		this.scopeTypeAllowUser = scopeTypeAllowUser;
	}

	@Override
	public Long getId() {
		return scopeId;
	}
}
