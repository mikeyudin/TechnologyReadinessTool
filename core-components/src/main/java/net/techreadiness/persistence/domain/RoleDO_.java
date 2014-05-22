package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RoleDO.class)
public abstract class RoleDO_ {

	public static volatile ListAttribute<RoleDO, RolePermissionDO> rolePermissions;
	public static volatile SingularAttribute<RoleDO, ScopeDO> scope;
	public static volatile ListAttribute<RoleDO, UserRoleDO> userRoles;
	public static volatile SingularAttribute<RoleDO, String> description;
	public static volatile SingularAttribute<RoleDO, String> name;
	public static volatile SingularAttribute<RoleDO, String> category;
	public static volatile SingularAttribute<RoleDO, String> shortName;
	public static volatile SingularAttribute<RoleDO, Integer> displayOrder;
	public static volatile SingularAttribute<RoleDO, String> code;
	public static volatile SingularAttribute<RoleDO, Long> roleId;

}
