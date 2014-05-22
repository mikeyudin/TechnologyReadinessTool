package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(QuerySqlDO.class)
public abstract class QuerySqlDO_ {

	public static volatile SingularAttribute<QuerySqlDO, ScopeDO> scope;
	public static volatile SingularAttribute<QuerySqlDO, String> keywords;
	public static volatile SingularAttribute<QuerySqlDO, Long> querySqlId;
	public static volatile SingularAttribute<QuerySqlDO, String> description;
	public static volatile SingularAttribute<QuerySqlDO, String> name;
	public static volatile SingularAttribute<QuerySqlDO, String> sqlText;
	public static volatile SingularAttribute<QuerySqlDO, String> code;
	public static volatile SingularAttribute<QuerySqlDO, EntityTypeDO> entityType;

}
