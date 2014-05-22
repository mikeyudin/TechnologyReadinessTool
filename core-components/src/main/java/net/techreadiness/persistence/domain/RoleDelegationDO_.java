package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RoleDelegationDO.class)
public abstract class RoleDelegationDO_ {

	public static volatile SingularAttribute<RoleDelegationDO, Long> roleDelegationId;
	public static volatile SingularAttribute<RoleDelegationDO, RoleDO> role;
	public static volatile SingularAttribute<RoleDelegationDO, RoleDO> delegRole;

}