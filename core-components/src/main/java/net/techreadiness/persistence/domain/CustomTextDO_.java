package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CustomTextDO.class)
public abstract class CustomTextDO_ {

	public static volatile SingularAttribute<CustomTextDO, String> text;
	public static volatile SingularAttribute<CustomTextDO, ScopeDO> scope;
	public static volatile SingularAttribute<CustomTextDO, Integer> customTextId;
	public static volatile SingularAttribute<CustomTextDO, String> code;

}
