package net.techreadiness.service.object.mapping;

import java.util.Map;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserExtDO;
import net.techreadiness.service.object.User;

import com.google.common.collect.Maps;

public class UserDOandUserMapper extends CustomMapper<UserDO, User> {

	@Override
	public void mapAtoB(UserDO userDO, User user, MappingContext context) {
		Map<String, String> map = Maps.newHashMap();
		if (userDO.getUserExts() != null) {
			for (UserExtDO userExtDO : userDO.getUserExts()) {
				map.put(userExtDO.getEntityField().getCode(), userExtDO.getValue());
			}
		}
		user.setExtendedAttributes(map);
	}

	@Override
	public void mapBtoA(User user, UserDO userDO, MappingContext context) {
		userDO.setExtAttributes(user.getExtendedAttributes());
	}
}
