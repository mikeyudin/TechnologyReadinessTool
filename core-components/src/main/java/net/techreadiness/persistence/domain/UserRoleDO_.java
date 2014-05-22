package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserRoleDO.class)
public abstract class UserRoleDO_ {

	public static volatile SingularAttribute<UserRoleDO, Long> userRoleId;
	public static volatile SingularAttribute<UserRoleDO, RoleDO> role;
	public static volatile SingularAttribute<UserRoleDO, UserDO> user;

}
