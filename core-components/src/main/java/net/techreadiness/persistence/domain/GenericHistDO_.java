package net.techreadiness.persistence.domain;

import java.sql.Timestamp;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(GenericHistDO.class)
public abstract class GenericHistDO_ {

	public static volatile SingularAttribute<GenericHistDO, String> changeType;
	public static volatile SingularAttribute<GenericHistDO, String> newValue;
	public static volatile SingularAttribute<GenericHistDO, Timestamp> changeDate;
	public static volatile SingularAttribute<GenericHistDO, String> tableName;
	public static volatile SingularAttribute<GenericHistDO, Integer> primaryKey;
	public static volatile SingularAttribute<GenericHistDO, String> columnName;
	public static volatile SingularAttribute<GenericHistDO, String> changeUser;
	public static volatile SingularAttribute<GenericHistDO, String> oldValue;
	public static volatile SingularAttribute<GenericHistDO, String> genericHistId;
	public static volatile SingularAttribute<GenericHistDO, Short> changeVersion;

}
