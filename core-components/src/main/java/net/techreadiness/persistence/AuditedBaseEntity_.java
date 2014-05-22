package net.techreadiness.persistence;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "Dali", date = "2012-01-24T14:51:54.991-0600")
@StaticMetamodel(AuditedBaseEntity.class)
public class AuditedBaseEntity_ extends BaseEntity_ {
	public static volatile SingularAttribute<AuditedBaseEntity, String> changeUser;
	public static volatile SingularAttribute<AuditedBaseEntity, Date> changeDate;
}
