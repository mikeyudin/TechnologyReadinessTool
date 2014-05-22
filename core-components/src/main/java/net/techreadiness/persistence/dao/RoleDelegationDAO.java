package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.RoleDelegationDO;

public interface RoleDelegationDAO extends BaseDAO<RoleDelegationDO> {

	RoleDelegationDO getByRoleIdDelegRoleId(Long roleId, Long delegRoleId);
}
