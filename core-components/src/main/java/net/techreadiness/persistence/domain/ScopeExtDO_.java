package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ScopeExtDO.class)
public abstract class ScopeExtDO_ {

	public static volatile SingularAttribute<ScopeExtDO, ScopeDO> scope;
	public static volatile SingularAttribute<ScopeExtDO, Long> scopeExtId;
	public static volatile SingularAttribute<ScopeExtDO, String> value;
	public static volatile SingularAttribute<ScopeExtDO, EntityFieldDO> entityField;

}
