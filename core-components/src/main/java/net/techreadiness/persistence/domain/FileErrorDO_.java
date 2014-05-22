package net.techreadiness.persistence.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "Dali", date = "2012-01-25T08:54:22.829-0600")
@StaticMetamodel(FileErrorDO.class)
public class FileErrorDO_ {
	public static volatile SingularAttribute<FileErrorDO, Long> fileErrorId;
	public static volatile SingularAttribute<FileErrorDO, String> errorCode;
	public static volatile SingularAttribute<FileErrorDO, String> message;
	public static volatile SingularAttribute<FileErrorDO, Integer> recordNumber;
	public static volatile SingularAttribute<FileErrorDO, FileDO> file;
}
