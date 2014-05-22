package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserOrgDO.class)
public abstract class UserOrgDO_ {

	public static volatile SingularAttribute<UserOrgDO, Long> userOrgId;
	public static volatile SingularAttribute<UserOrgDO, OrgDO> org;
	public static volatile SingularAttribute<UserOrgDO, UserDO> user;

}
