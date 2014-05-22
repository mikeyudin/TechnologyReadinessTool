package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrgTypeDO.class)
public abstract class OrgTypeDO_ {

	public static volatile ListAttribute<OrgTypeDO, OrgDO> orgs;
	public static volatile SingularAttribute<OrgTypeDO, OrgTypeDO> parentOrgType;
	public static volatile SingularAttribute<OrgTypeDO, Boolean> allowStudent;
	public static volatile SingularAttribute<OrgTypeDO, ScopeDO> scope;
	public static volatile SingularAttribute<OrgTypeDO, Long> orgTypeId;
	public static volatile ListAttribute<OrgTypeDO, OrgTypeDO> orgTypes;
	public static volatile SingularAttribute<OrgTypeDO, String> name;
	public static volatile SingularAttribute<OrgTypeDO, String> code;
	public static volatile SingularAttribute<OrgTypeDO, Boolean> allowGroup;
	public static volatile SingularAttribute<OrgTypeDO, Boolean> allowDevice;

}
