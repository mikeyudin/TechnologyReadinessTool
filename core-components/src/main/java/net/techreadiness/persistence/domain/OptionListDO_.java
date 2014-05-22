package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OptionListDO.class)
public abstract class OptionListDO_ {

	public static volatile SingularAttribute<OptionListDO, Boolean> shared;
	public static volatile SingularAttribute<OptionListDO, ScopeDO> scope;
	public static volatile SingularAttribute<OptionListDO, Long> optionListId;
	public static volatile SingularAttribute<OptionListDO, String> name;
	public static volatile SingularAttribute<OptionListDO, String> sqlText;
	public static volatile SingularAttribute<OptionListDO, String> code;
	public static volatile ListAttribute<OptionListDO, EntityFieldDO> entityFields;
	public static volatile ListAttribute<OptionListDO, OptionListValueDO> optionListValues;

}
