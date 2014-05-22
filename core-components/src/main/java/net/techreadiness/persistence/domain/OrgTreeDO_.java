package net.techreadiness.persistence.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrgTreeDO.class)
public abstract class OrgTreeDO_ {

	public static volatile SingularAttribute<OrgTreeDO, Long> orgTreeId;
	public static volatile SingularAttribute<OrgTreeDO, Short> distance;
	public static volatile SingularAttribute<OrgTreeDO, OrgDO> ancestorOrg;
	public static volatile SingularAttribute<OrgTreeDO, String> path;
	public static volatile SingularAttribute<OrgTreeDO, String> ancestorPath;
	public static volatile SingularAttribute<OrgTreeDO, OrgDO> org;
	public static volatile SingularAttribute<OrgTreeDO, Short> depth;

}
