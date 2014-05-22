package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.UserOrgDO;

public interface UserOrgDAO extends BaseDAO<UserOrgDO> {
	UserOrgDO getUserOrgByUserIdAndOrgId(Long userId, Long orgId);

	List<UserOrgDO> getUserOrgByUserId(Long scopeId, Long userId);

	boolean isOrgModifiable(Long userId, Long orgId, Long scopeId);

	List<OrgDO> findOrgsForUsers(Long scopeId, Long orgId, Collection<Long> userIds);
}
