package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.ScopeTypeDO;

public interface ScopeTypeDAO extends BaseDAO<ScopeTypeDO> {

	/**
	 * Retrieves the list of {@link ScopeTypeDO}s that can be a child of the specified scope.
	 * 
	 * @param scopeId
	 *            The parent scope Id
	 * @return The list of possible child {@link ScopeTypeDO}s.
	 */
	List<ScopeTypeDO> findChildTypesByScopeId(Long scopeId);

}
