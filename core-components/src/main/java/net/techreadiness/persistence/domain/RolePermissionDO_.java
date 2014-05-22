package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RolePermissionDO.class)
public abstract class RolePermissionDO_ {

	public static volatile SingularAttribute<RolePermissionDO, Long> rolePermissionId;
	public static volatile SingularAttribute<RolePermissionDO, RoleDO> role;
	public static volatile SingularAttribute<RolePermissionDO, PermissionDO> permission;

}
