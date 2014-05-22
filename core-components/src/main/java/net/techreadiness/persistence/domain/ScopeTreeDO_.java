package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ScopeTreeDO.class)
public abstract class ScopeTreeDO_ {

	public static volatile SingularAttribute<ScopeTreeDO, Short> distance;
	public static volatile SingularAttribute<ScopeTreeDO, ScopeDO> scope;
	public static volatile SingularAttribute<ScopeTreeDO, ScopeDO> ancestorScope;
	public static volatile SingularAttribute<ScopeTreeDO, String> path;
	public static volatile SingularAttribute<ScopeTreeDO, String> ancestorPath;
	public static volatile SingularAttribute<ScopeTreeDO, Long> scopeTreeId;
	public static volatile SingularAttribute<ScopeTreeDO, Short> depth;
	public static volatile SingularAttribute<ScopeTreeDO, Short> ancestorDepth;

}
