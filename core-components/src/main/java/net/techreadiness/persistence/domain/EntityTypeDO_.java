package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(EntityTypeDO.class)
public abstract class EntityTypeDO_ {

	public static volatile ListAttribute<EntityTypeDO, ViewDefTypeDO> viewDefTypes;
	public static volatile SingularAttribute<EntityTypeDO, Long> entityTypeId;
	public static volatile SingularAttribute<EntityTypeDO, String> name;
	public static volatile SingularAttribute<EntityTypeDO, String> javaClass;
	public static volatile ListAttribute<EntityTypeDO, QuerySqlDO> querySqls;
	public static volatile SingularAttribute<EntityTypeDO, String> code;
	public static volatile ListAttribute<EntityTypeDO, EntityDO> entities;

}
