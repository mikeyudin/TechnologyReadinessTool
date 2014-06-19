package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ScopeDO.class)
public abstract class ScopeDO_ {

	public static volatile SingularAttribute<ScopeDO, String> path;
	public static volatile ListAttribute<ScopeDO, OrgDO> orgs;
	public static volatile ListAttribute<ScopeDO, ScopeTreeDO> ancestorScopeTrees;
	public static volatile SingularAttribute<ScopeDO, ScopeDO> parentScope;
	public static volatile SetAttribute<ScopeDO, RoleDO> roles;
	public static volatile ListAttribute<ScopeDO, ScopeExtDO> scopeExts;
	public static volatile ListAttribute<ScopeDO, OrgTypeDO> orgTypes;
	public static volatile ListAttribute<ScopeDO, OptionListDO> optionLists;
	public static volatile SingularAttribute<ScopeDO, String> code;
	public static volatile ListAttribute<ScopeDO, EntityDO> entities;
	public static volatile ListAttribute<ScopeDO, ScopeTreeDO> scopeTrees;
	public static volatile ListAttribute<ScopeDO, ScopeDO> scopes;
	public static volatile ListAttribute<ScopeDO, OrgPartDO> orgParts;
	public static volatile SingularAttribute<ScopeDO, ScopeTypeDO> scopeType;
	public static volatile SingularAttribute<ScopeDO, String> description;
	public static volatile SingularAttribute<ScopeDO, String> name;
	public static volatile ListAttribute<ScopeDO, QuerySqlDO> querySqls;
	public static volatile SingularAttribute<ScopeDO, Long> scopeId;
	public static volatile ListAttribute<ScopeDO, ViewDefDO> viewDefs;
	public static volatile SetAttribute<ScopeDO, ContactTypeDO> contactTypes;
	public static volatile SetAttribute<ScopeDO, CustomTextDO> customTexts;

}
