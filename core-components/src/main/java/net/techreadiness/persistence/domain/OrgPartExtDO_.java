package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrgPartExtDO.class)
public abstract class OrgPartExtDO_ {

	public static volatile SingularAttribute<OrgPartExtDO, OrgPartDO> orgPart;
	public static volatile SingularAttribute<OrgPartExtDO, String> value;
	public static volatile SingularAttribute<OrgPartExtDO, Long> orgPartExtId;
	public static volatile SingularAttribute<OrgPartExtDO, EntityFieldDO> entityField;

}
