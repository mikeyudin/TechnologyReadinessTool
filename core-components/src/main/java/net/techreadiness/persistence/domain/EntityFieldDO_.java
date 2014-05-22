package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(EntityFieldDO.class)
public abstract class EntityFieldDO_ {

	public static volatile SingularAttribute<EntityFieldDO, String> regex;
	public static volatile ListAttribute<EntityFieldDO, ScopeExtDO> scopeExts;
	public static volatile SingularAttribute<EntityFieldDO, EntityDO> entity;
	public static volatile SingularAttribute<EntityFieldDO, String> regexDisplay;
	public static volatile SingularAttribute<EntityFieldDO, String> code;
	public static volatile SingularAttribute<EntityFieldDO, EntityDataTypeDO> entityDataType;
	public static volatile ListAttribute<EntityFieldDO, OrgPartExtDO> orgPartExts;
	public static volatile SingularAttribute<EntityFieldDO, Integer> maxLength;
	public static volatile SingularAttribute<EntityFieldDO, String> description;
	public static volatile SingularAttribute<EntityFieldDO, Integer> minLength;
	public static volatile SingularAttribute<EntityFieldDO, Long> entityFieldId;
	public static volatile SingularAttribute<EntityFieldDO, String> name;
	public static volatile ListAttribute<EntityFieldDO, ViewDefFieldDO> viewDefFields;
	public static volatile SingularAttribute<EntityFieldDO, Integer> displayOrder;
	public static volatile ListAttribute<EntityFieldDO, OrgExtDO> orgExts;
	public static volatile SingularAttribute<EntityFieldDO, Boolean> required;
	public static volatile SingularAttribute<EntityFieldDO, OptionListDO> optionList;
	public static volatile SingularAttribute<EntityFieldDO, Boolean> disabled;

}
