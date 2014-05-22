package net.techreadiness.batch.user;

import javax.inject.Inject;

import net.techreadiness.batch.BaseItemProcessor;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserOrgDO;
import net.techreadiness.persistence.domain.UserRoleDO;
import net.techreadiness.service.object.User;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.batch.item.ItemProcessor;

public class UserDOToDataProcessor extends BaseItemProcessor implements ItemProcessor<UserDO, UserData> {
	@Inject
	private MappingService mappingService;

	@Override
	public UserData process(UserDO item) throws Exception {
		UserData userData = new UserData();
		User user = mappingService.map(item);
		userData.setUser(user);
		if (user.getDeleteDate() == null) {
			userData.setAction("u");
		} else {
			userData.setAction("d");
		}
		for (UserOrgDO userOrg : item.getUserOrgs()) {
			userData.getOrgCodes().add(userOrg.getOrg().getLocalCode());
			userData.setStateCode(userOrg.getOrg().getState());// User should not be authorized for orgs in more than one
																// state, they should all be equal
		}

		for (UserRoleDO userRole : item.getUserRoles()) {
			userData.getRoleCodes().add(userRole.getRole().getCode());
		}

		return userData;
	}

}
