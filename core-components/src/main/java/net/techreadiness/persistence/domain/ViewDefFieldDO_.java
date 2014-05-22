package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ViewDefFieldDO.class)
public abstract class ViewDefFieldDO_ {

	public static volatile SingularAttribute<ViewDefFieldDO, Long> viewDefFieldId;
	public static volatile SingularAttribute<ViewDefFieldDO, Boolean> readOnly;
	public static volatile SingularAttribute<ViewDefFieldDO, ViewDefDO> viewDef;
	public static volatile SingularAttribute<ViewDefFieldDO, String> inputType;
	public static volatile SingularAttribute<ViewDefFieldDO, String> inputStyle;
	public static volatile SingularAttribute<ViewDefFieldDO, String> labelStyle;
	public static volatile SingularAttribute<ViewDefFieldDO, Integer> displayOrder;
	public static volatile SingularAttribute<ViewDefFieldDO, String> overrideName;
	public static volatile SingularAttribute<ViewDefFieldDO, EntityFieldDO> entityField;
	public static volatile SingularAttribute<ViewDefFieldDO, String> labelPosition;
	public static volatile SingularAttribute<ViewDefFieldDO, EntityRuleDO> editRule;
	public static volatile SingularAttribute<ViewDefFieldDO, EntityRuleDO> displayRule;
	public static volatile SingularAttribute<ViewDefFieldDO, String> displayWidth;
	public static volatile SingularAttribute<ViewDefFieldDO, Integer> columnNumber;

}
