package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import net.techreadiness.service.object.Scope;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import com.google.common.base.Objects;

/**
 * The persistent class for the scope database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "scope")
public class ScopeDO extends AuditedBaseEntityWithExt implements Serializable, ServiceObjectMapped {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "scope_id", unique = true, nullable = false)
	private Long scopeId;

	@Formula("(select st.path from scope_tree st where st.scope_id=scope_id and st.distance=0)")
	private String path;

	@Column(nullable = false, length = 50)
	private String code;

	@Column(length = 1000)
	private String description;

	@Column(nullable = false, length = 100)
	private String name;

	// bi-directional many-to-one association to EntityDO
	@OneToMany(mappedBy = "scope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<EntityDO> entities;

	// bi-directional many-to-one association to OptionListDO
	@OneToMany(mappedBy = "scope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<OptionListDO> optionLists;

	// bi-directional many-to-one association to OrgDO
	@OneToMany(mappedBy = "scope")
	private List<OrgDO> orgs;

	// bi-directional many-to-one association to OrgPartDO
	@OneToMany(mappedBy = "scope")
	private List<OrgPartDO> orgParts;

	// bi-directional many-to-one association to OrgTypeDO
	@OneToMany(mappedBy = "scope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<OrgTypeDO> orgTypes;

	// bi-directional many-to-one association to QuerySqlDO
	@OneToMany(mappedBy = "scope")
	private List<QuerySqlDO> querySqls;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_scope_id")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO parentScope;

	// bi-directional many-to-one association to ScopeDO
	@OneToMany(mappedBy = "parentScope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ScopeDO> scopes;

	// bi-directional many-to-one association to ScopeTypeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_type_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeTypeDO scopeType;

	// bi-directional many-to-one association to ScopeExtDO
	@OneToMany(mappedBy = "scope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ScopeExtDO> scopeExts;

	// bi-directional many-to-one association to ScopeTreeDO
	@OneToMany(mappedBy = "scope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ScopeTreeDO> scopeTrees;

	// bi-directional many-to-one association to ScopeTreeDO
	@OneToMany(mappedBy = "ancestorScope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ScopeTreeDO> ancestorScopeTrees;

	// bi-directional many-to-one association to ViewDefDO
	@OneToMany(mappedBy = "scope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ViewDefDO> viewDefs;

	@OneToMany(mappedBy = "scope")
	private Set<ContactTypeDO> contactTypes;

	@OneToMany(mappedBy = "scope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CustomTextDO> customTexts;

	@OneToMany(mappedBy = "scope")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<RoleDO> roles;

	public ScopeDO() {
	}

	@Override
	protected void populateExtAttributes() {
		Map<String, String> map = new HashMap<>();

		if (scopeExts != null && scopeExts.size() > 0) {
			for (ScopeExtDO scopeExtDO : scopeExts) {
				map.put(scopeExtDO.getEntityField().getCode(), scopeExtDO.getValue());
			}
		}

		this.setExtAttributes(map);
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
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

	public List<EntityDO> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityDO> entities) {
		this.entities = entities;
	}

	public List<OptionListDO> getOptionLists() {
		return optionLists;
	}

	public void setOptionLists(List<OptionListDO> optionLists) {
		this.optionLists = optionLists;
	}

	public List<OrgDO> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<OrgDO> orgs) {
		this.orgs = orgs;
	}

	public List<OrgPartDO> getOrgParts() {
		return orgParts;
	}

	public void setOrgParts(List<OrgPartDO> orgParts) {
		this.orgParts = orgParts;
	}

	public List<OrgTypeDO> getOrgTypes() {
		return orgTypes;
	}

	public void setOrgTypes(List<OrgTypeDO> orgTypes) {
		this.orgTypes = orgTypes;
	}

	public List<QuerySqlDO> getQuerySqls() {
		return querySqls;
	}

	public void setQuerySqls(List<QuerySqlDO> querySqls) {
		this.querySqls = querySqls;
	}

	public ScopeDO getParentScope() {
		return parentScope;
	}

	public void setParentScope(ScopeDO parentScope) {
		this.parentScope = parentScope;
	}

	public List<ScopeDO> getScopes() {
		return scopes;
	}

	public void setScopes(List<ScopeDO> scopes) {
		this.scopes = scopes;
	}

	public ScopeTypeDO getScopeType() {
		return scopeType;
	}

	public void setScopeType(ScopeTypeDO scopeType) {
		this.scopeType = scopeType;
	}

	public List<ScopeExtDO> getScopeExts() {
		return scopeExts;
	}

	public void setScopeExts(List<ScopeExtDO> scopeExts) {
		this.scopeExts = scopeExts;
	}

	public List<ScopeTreeDO> getScopeTrees() {
		return scopeTrees;
	}

	public void setScopeTrees(List<ScopeTreeDO> scopeTrees) {
		this.scopeTrees = scopeTrees;
	}

	public List<ScopeTreeDO> getAncestorScopeTrees() {
		return ancestorScopeTrees;
	}

	public void setAncestorScopeTrees(List<ScopeTreeDO> ancestorScopeTrees) {
		this.ancestorScopeTrees = ancestorScopeTrees;
	}

	public List<ViewDefDO> getViewDefs() {
		return viewDefs;
	}

	public void setViewDefs(List<ViewDefDO> viewDefs) {
		this.viewDefs = viewDefs;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Set<ContactTypeDO> getContactTypes() {
		return contactTypes;
	}

	public void setContactTypes(Set<ContactTypeDO> contactTypes) {
		this.contactTypes = contactTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (scopeId == null ? 0 : scopeId.hashCode());
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
		if (!(obj instanceof ScopeDO)) {
			return false;
		}
		ScopeDO other = (ScopeDO) obj;
		if (scopeId == null) {
			if (other.scopeId != null) {
				return false;
			}
		} else if (!scopeId.equals(other.getScopeId())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("scopeId", scopeId).add("path", path).add("name", name).toString();
	}

	public Set<CustomTextDO> getCustomTexts() {
		return customTexts;
	}

	public void setCustomTexts(Set<CustomTextDO> customTexts) {
		this.customTexts = customTexts;
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return Scope.class;
	}

	public Set<RoleDO> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDO> roles) {
		this.roles = roles;
	}
}
