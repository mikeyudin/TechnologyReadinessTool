package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(EntityRuleDO.class)
public abstract class EntityRuleDO_ {

	public static volatile SingularAttribute<EntityRuleDO, String> errorMessage;
	public static volatile SingularAttribute<EntityRuleDO, String> batchErrorMessage;
	public static volatile SingularAttribute<EntityRuleDO, String> description;
	public static volatile SingularAttribute<EntityRuleDO, String> rule;
	public static volatile SingularAttribute<EntityRuleDO, String> name;
	public static volatile SingularAttribute<EntityRuleDO, EntityDO> entity;
	public static volatile SingularAttribute<EntityRuleDO, Long> entityRuleId;
	public static volatile SingularAttribute<EntityRuleDO, Boolean> disabled;
	public static volatile SingularAttribute<EntityRuleDO, EntityFieldDO> entityField;
	public static volatile SingularAttribute<EntityRuleDO, String> type;
}
