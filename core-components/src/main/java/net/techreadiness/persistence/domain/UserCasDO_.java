package net.techreadiness.persistence.domain;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserCasDO.class)
public abstract class UserCasDO_ {

	public static volatile SingularAttribute<UserCasDO, Long> userId;
	public static volatile SingularAttribute<UserCasDO, String> username;
	public static volatile SingularAttribute<UserCasDO, String> password;
	public static volatile SingularAttribute<UserCasDO, Integer> failedAttempts;
	public static volatile SingularAttribute<UserCasDO, Date> lastLoginDate;
}