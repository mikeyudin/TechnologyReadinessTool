package net.techreadiness.persistence.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "Dali", date = "2012-01-25T08:54:39.635-0600")
@StaticMetamodel(FileTypeDO.class)
public class FileTypeDO_ {
	public static volatile SingularAttribute<FileTypeDO, Long> fileTypeId;
	public static volatile SingularAttribute<FileTypeDO, String> code;
	public static volatile SingularAttribute<FileTypeDO, String> name;
	public static volatile ListAttribute<FileTypeDO, FileDO> files;
}
