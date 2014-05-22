package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ViewDefDO.class)
public abstract class ViewDefDO_ {

	public static volatile SingularAttribute<ViewDefDO, Long> viewDefId;
	public static volatile SingularAttribute<ViewDefDO, ScopeDO> scope;
	public static volatile SingularAttribute<ViewDefDO, ViewDefTypeDO> viewDefType;
	public static volatile SingularAttribute<ViewDefDO, String> name;
	public static volatile ListAttribute<ViewDefDO, ViewDefFieldDO> viewDefFields;
	public static volatile SingularAttribute<ViewDefDO, String> code;
	public static volatile SingularAttribute<ViewDefDO, String> column1LabelWidth;
	public static volatile SingularAttribute<ViewDefDO, String> column2LabelWidth;
	public static volatile SingularAttribute<ViewDefDO, String> column3LabelWidth;

	public static volatile SingularAttribute<ViewDefDO, String> column1Width;
	public static volatile SingularAttribute<ViewDefDO, String> column2Width;
	public static volatile SingularAttribute<ViewDefDO, String> column3Width;

	public static volatile ListAttribute<ViewDefDO, ViewDefTextDO> viewDefTexts;
	public static volatile SingularAttribute<ViewDefDO, Boolean> collapsedByDefault;
	public static volatile SingularAttribute<ViewDefDO, Boolean> collapsible;

}
