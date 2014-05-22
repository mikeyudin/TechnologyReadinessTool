package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(EntityDO.class)
public abstract class EntityDO_ {

	public static volatile SingularAttribute<EntityDO, ScopeDO> scope;
	public static volatile SingularAttribute<EntityDO, Long> entityId;
	public static volatile ListAttribute<EntityDO, EntityRuleDO> entityRules;
	public static volatile SingularAttribute<EntityDO, EntityTypeDO> entityType;
	public static volatile ListAttribute<EntityDO, EntityFieldDO> entityFields;

}
