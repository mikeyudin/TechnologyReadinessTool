package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(EntityDataTypeDO.class)
public abstract class EntityDataTypeDO_ {

	public static volatile SingularAttribute<EntityDataTypeDO, String> name;
	public static volatile SingularAttribute<EntityDataTypeDO, Long> entityDataTypeId;
	public static volatile SingularAttribute<EntityDataTypeDO, String> code;
	public static volatile ListAttribute<EntityDataTypeDO, EntityFieldDO> entityFields;

}
