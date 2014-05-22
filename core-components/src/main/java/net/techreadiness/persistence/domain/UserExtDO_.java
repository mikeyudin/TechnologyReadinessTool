package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserExtDO.class)
public abstract class UserExtDO_ {

	public static volatile SingularAttribute<UserExtDO, Long> userExtId;
	public static volatile SingularAttribute<UserExtDO, String> value;
	public static volatile SingularAttribute<UserExtDO, UserDO> user;
	public static volatile SingularAttribute<UserExtDO, EntityFieldDO> entityField;

}
