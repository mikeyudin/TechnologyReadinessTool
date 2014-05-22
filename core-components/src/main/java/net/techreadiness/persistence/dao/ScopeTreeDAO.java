package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeTreeDO;

public interface ScopeTreeDAO extends BaseDAO<ScopeTreeDO> {

	/**
	 * Retrieves the list of {@link ScopeDO}s that are children of the specified parent path..
	 * 
	 * @param scopePath
	 *            The parent scope path
	 * @return The list of child {@link ScopeDO}s.
	 */
	List<ScopeTreeDO> findByAncestorPath(String scopePath);
}
