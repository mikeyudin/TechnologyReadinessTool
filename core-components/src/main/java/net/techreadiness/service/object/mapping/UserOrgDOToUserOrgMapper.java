package net.techreadiness.service.object.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.UserOrgDO;
import net.techreadiness.service.object.UserOrg;

public class UserOrgDOToUserOrgMapper extends CustomMapper<UserOrgDO, UserOrg> {

	@Override
	public void mapAtoB(UserOrgDO a, UserOrg b, MappingContext context) {
		b.setOrgId(a.getOrg().getOrgId());
		b.setUserId(a.getUser().getUserId());
	}
}
