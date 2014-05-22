package net.techreadiness.service.object.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.UserRoleDO;
import net.techreadiness.service.object.UserRole;

public class UserRoleDOtoUserRoleMapper extends CustomMapper<UserRoleDO, UserRole> {

	@Override
	public void mapAtoB(UserRoleDO a, UserRole b, MappingContext context) {
		super.mapAtoB(a, b, context);
		b.setRoleId(a.getRole().getRoleId());
		b.setUserId(a.getUser().getUserId());
	}
}
