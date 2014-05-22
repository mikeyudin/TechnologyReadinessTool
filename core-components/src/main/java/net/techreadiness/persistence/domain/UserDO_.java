package net.techreadiness.persistence.domain;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserDO.class)
public abstract class UserDO_ {

	public static volatile SetAttribute<UserDO, UserExtDO> userExts;
	public static volatile SingularAttribute<UserDO, String> lastName;
	public static volatile SingularAttribute<UserDO, ScopeDO> scope;
	public static volatile SingularAttribute<UserDO, ScopeDO> selectedScope;
	public static volatile SingularAttribute<UserDO, Date> deleteDate;
	public static volatile SingularAttribute<UserDO, Date> activeBeginDate;
	public static volatile SingularAttribute<UserDO, String> disableReason;
	public static volatile SetAttribute<UserDO, UserOrgDO> userOrgs;
	public static volatile SingularAttribute<UserDO, String> username;
	public static volatile SingularAttribute<UserDO, Date> activeEndDate;
	public static volatile SingularAttribute<UserDO, Date> disableDate;
	public static volatile SingularAttribute<UserDO, String> email;
	public static volatile SetAttribute<UserDO, UserRoleDO> userRoles;
	public static volatile SingularAttribute<UserDO, Long> userId;
	public static volatile SingularAttribute<UserDO, String> firstName;
	public static volatile SingularAttribute<UserDO, String> resetToken1;
	public static volatile SingularAttribute<UserDO, String> resetToken2;
	public static volatile SingularAttribute<UserDO, String> resetToken3;
	public static volatile SingularAttribute<UserDO, String> resetToken4;
	public static volatile SingularAttribute<UserDO, String> resetToken5;

}
