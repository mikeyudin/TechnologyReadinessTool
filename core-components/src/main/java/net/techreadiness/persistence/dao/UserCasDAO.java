package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.UserCasDO;
import net.techreadiness.persistence.domain.UserDO;

public interface UserCasDAO extends BaseDAO<UserCasDO> {
	UserCasDO getByUsername(String username);

	void create(final UserDO userDO, String initialPassword);
}
