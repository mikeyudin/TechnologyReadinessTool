package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ContactTypeDO.class)
public abstract class ContactTypeDO_ {
	public static volatile SingularAttribute<ContactTypeDO, String> name;
	public static volatile SingularAttribute<ContactTypeDO, Long> contactTypeId;
	public static volatile SingularAttribute<ContactTypeDO, String> code;
	public static volatile SetAttribute<ContactTypeDO, ContactDO> contacts;
	public static volatile SingularAttribute<ContactTypeDO, ScopeDO> scope;
	public static volatile SingularAttribute<ContactTypeDO, Integer> displayOrder;
}
