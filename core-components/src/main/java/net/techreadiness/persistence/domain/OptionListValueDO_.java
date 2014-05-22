package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OptionListValueDO.class)
public abstract class OptionListValueDO_ {

	public static volatile SingularAttribute<OptionListValueDO, Long> optionListValueId;
	public static volatile SingularAttribute<OptionListValueDO, String> name;
	public static volatile SingularAttribute<OptionListValueDO, String> value;
	public static volatile SingularAttribute<OptionListValueDO, Integer> displayOrder;
	public static volatile SingularAttribute<OptionListValueDO, OptionListDO> optionList;

}
