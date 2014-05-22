package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ScopeTypeDO.class)
public abstract class ScopeTypeDO_ {
	public static volatile SingularAttribute<ScopeTypeDO, Long> scopeTypeId;
	public static volatile SingularAttribute<ScopeTypeDO, String> code;
	public static volatile ListAttribute<ScopeTypeDO, ScopeDO> scopes;
	public static volatile SingularAttribute<ScopeTypeDO, ScopeTypeDO> parentScopeType;
	public static volatile SingularAttribute<ScopeTypeDO, Boolean> allowOrgPart;
	public static volatile SingularAttribute<ScopeTypeDO, String> description;
	public static volatile SingularAttribute<ScopeTypeDO, String> name;
	public static volatile ListAttribute<ScopeTypeDO, ScopeTypeDO> scopeTypes;
	public static volatile SingularAttribute<ScopeTypeDO, Boolean> allowOrg;
	public static volatile SingularAttribute<ScopeTypeDO, Boolean> allowUser;

}
