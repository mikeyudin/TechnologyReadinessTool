package net.techreadiness.persistence.dao;

import java.util.List;

import javax.inject.Named;

import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserExtDO;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository
@Named("userExtDAOImpl")
public class UserExtDAOImpl extends BaseDAOImpl<UserExtDO> implements UserExtDAO, ExtDAO<UserDO, UserExtDO> {

	@Override
	public List<UserExtDO> getExtDOs(UserDO baseEntityWithExt) {
		if (baseEntityWithExt == null || baseEntityWithExt.getUserExts() == null) {
			return null;
		}
		return Lists.newArrayList(baseEntityWithExt.getUserExts());
	}

	@Override
	public UserExtDO getNew() {
		return new UserExtDO();
	}

}
