package net.techreadiness.persistence.domain;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import net.techreadiness.persistence.AuditedBaseEntity_;

@Generated(value = "Dali", date = "2012-01-27T11:11:19.802-0600")
@StaticMetamodel(FileDO.class)
public class FileDO_ extends AuditedBaseEntity_ {
	public static volatile SingularAttribute<FileDO, Long> fileId;
	public static volatile SingularAttribute<FileDO, Long> batchJobExecutionId;
	public static volatile SingularAttribute<FileDO, String> description;
	public static volatile SingularAttribute<FileDO, String> displayFilename;
	public static volatile SingularAttribute<FileDO, String> filename;
	public static volatile SingularAttribute<FileDO, String> statusMessage;
	public static volatile SingularAttribute<FileDO, Integer> kilobytes;
	public static volatile SingularAttribute<FileDO, OrgDO> org;
	public static volatile SingularAttribute<FileDO, String> path;
	public static volatile SingularAttribute<FileDO, Date> requestDate;
	public static volatile SingularAttribute<FileDO, String> status;
	public static volatile SingularAttribute<FileDO, String> mode;
	public static volatile SingularAttribute<FileDO, Integer> totalRecordCount;
	public static volatile SingularAttribute<FileDO, UserDO> user;
	public static volatile SingularAttribute<FileDO, FileTypeDO> fileType;
	public static volatile ListAttribute<FileDO, FileErrorDO> fileErrors;
	public static volatile SingularAttribute<FileDO, String> errorDataFilename;
	public static volatile SingularAttribute<FileDO, String> errorMessageFilename;
}
