package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(PermissionDO.class)
public abstract class PermissionDO_ {

	public static volatile ListAttribute<PermissionDO, RolePermissionDO> rolePermissions;
	public static volatile SingularAttribute<PermissionDO, ScopeDO> scope;
	public static volatile SingularAttribute<PermissionDO, String> description;
	public static volatile SingularAttribute<PermissionDO, String> name;
	public static volatile SingularAttribute<PermissionDO, Integer> displayOrder;
	public static volatile SingularAttribute<PermissionDO, String> code;
	public static volatile SingularAttribute<PermissionDO, Long> permissionId;

}
