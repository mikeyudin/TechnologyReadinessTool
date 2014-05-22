package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrgExtDO.class)
public abstract class OrgExtDO_ {

	public static volatile SingularAttribute<OrgExtDO, Long> orgExtId;
	public static volatile SingularAttribute<OrgExtDO, String> value;
	public static volatile SingularAttribute<OrgExtDO, OrgDO> org;
	public static volatile SingularAttribute<OrgExtDO, EntityFieldDO> entityField;

}
