package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ViewDefTypeDO.class)
public abstract class ViewDefTypeDO_ {

	public static volatile SingularAttribute<ViewDefTypeDO, String> category;
	public static volatile SingularAttribute<ViewDefTypeDO, String> name;
	public static volatile SingularAttribute<ViewDefTypeDO, String> code;
	public static volatile SingularAttribute<ViewDefTypeDO, EntityTypeDO> entityType;
	public static volatile ListAttribute<ViewDefTypeDO, ViewDefDO> viewDefs;
	public static volatile SingularAttribute<ViewDefTypeDO, Boolean> defaultView;
	public static volatile SingularAttribute<ViewDefTypeDO, Long> viewDefTypeId;

}
