package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.ScopeExtDO;

public interface ScopeExtDAO extends BaseDAO<ScopeExtDO> {
	ScopeExtDO getLowestExistingConfigurationItem(Long scopeId, String entityCode);

	ScopeExtDO getMostRecentlyUpdated(Long scopeId, Long viewDefId);
}
