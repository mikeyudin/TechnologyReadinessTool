package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrgPartDO.class)
public abstract class OrgPartDO_ {

	public static volatile SingularAttribute<OrgPartDO, ScopeDO> scope;
	public static volatile ListAttribute<OrgPartDO, OrgPartExtDO> orgPartExts;
	public static volatile SingularAttribute<OrgPartDO, Long> orgPartId;
	public static volatile SingularAttribute<OrgPartDO, OrgDO> org;

}
