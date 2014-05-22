package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ViewDefTextDO.class)
public abstract class ViewDefTextDO_ {

	public static volatile SingularAttribute<ViewDefTextDO, String> text;
	public static volatile SingularAttribute<ViewDefTextDO, ViewDefDO> viewDef;
	public static volatile SingularAttribute<ViewDefTextDO, Integer> viewDefTextId;
	public static volatile SingularAttribute<ViewDefTextDO, Integer> displayOrder;
	public static volatile SingularAttribute<ViewDefTextDO, Integer> columnNumber;

}
