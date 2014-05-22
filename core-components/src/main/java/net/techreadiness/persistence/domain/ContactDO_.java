package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ContactDO.class)
public abstract class ContactDO_ {

	public static volatile SingularAttribute<ContactDO, Long> contactId;
	public static volatile SingularAttribute<ContactDO, ContactTypeDO> contactType;
	public static volatile SingularAttribute<ContactDO, OrgDO> org;
	public static volatile SingularAttribute<ContactDO, String> name;
	public static volatile SingularAttribute<ContactDO, String> title;
	public static volatile SingularAttribute<ContactDO, String> email;
	public static volatile SingularAttribute<ContactDO, String> addressLine1;
	public static volatile SingularAttribute<ContactDO, String> addressLine2;
	public static volatile SingularAttribute<ContactDO, String> city;
	public static volatile SingularAttribute<ContactDO, String> state;
	public static volatile SingularAttribute<ContactDO, String> country;
	public static volatile SingularAttribute<ContactDO, String> zip;
	public static volatile SingularAttribute<ContactDO, String> phone;
	public static volatile SingularAttribute<ContactDO, String> phoneExtension;
	public static volatile SingularAttribute<ContactDO, String> fax;

}
