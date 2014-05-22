package net.techreadiness.service.object.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.OrgTypeDO;
import net.techreadiness.service.object.OrgType;

public class OrgTypeDOandOrgTypeMapper extends CustomMapper<OrgTypeDO, OrgType> {

	@Override
	public void mapAtoB(OrgTypeDO orgTypeDO, OrgType orgType, MappingContext context) {
		orgType.setParentOrgTypeId(orgTypeDO.getParentOrgType().getOrgTypeId());
		orgType.setScopeId(orgTypeDO.getScope().getScopeId());
	}
}
