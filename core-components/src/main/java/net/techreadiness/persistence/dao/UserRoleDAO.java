package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.persistence.domain.UserRoleDO;

public interface UserRoleDAO extends BaseDAO<UserRoleDO> {
	UserRoleDO getUserRoleByUserIdAndRoleId(Long userId, Long roleId);

	List<UserRoleDO> findUserRolesByUser(Long userId);

	List<RoleDO> findRolesForUsers(Long scopeId, Collection<Long> userIds);
}
