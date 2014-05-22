package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import net.techreadiness.persistence.domain.ScopeDO;

public interface ScopeDAO extends BaseDAO<ScopeDO> {

	/**
	 * Retrieves the list of {@link ScopeDO}s that are children of the specified parent path..
	 * 
	 * @param scopePath
	 *            The parent scope path
	 * @return The list of child {@link ScopeDO}s.
	 */
	List<ScopeDO> findByParentPath(String scopePath);

	/**
	 * Retrieves the {@link ScopeDO} at the given path.
	 * 
	 * @param scopePath
	 *            The scope path
	 * @return The {@link ScopeDO} at the given given path (if existing).
	 */
	ScopeDO getByScopePath(String scopePath);

	/**
	 * Gets the scope that allows users from the given path. Walks up the scope path from the given path.
	 * 
	 * This IGNORES the root scope
	 * 
	 * @param scopeId
	 *            the path to use to look for users allowed scope
	 * @return the scope that allows users given the input scope path, or null if not found.
	 */
	ScopeDO getScopeForUsers(Long scopeId);

	ScopeDO getScopeForOrgs(Long scopeId);

	ScopeDO getScopeForOrgParts(Long scopeId);

	List<ScopeDO> getScopesWithNoParent();

	List<ScopeDO> getAppRootScopes();

	ScopeDO getTopLevelParent(Long scopeId);

	/**
	 * This method will return a list of {@link ScopeDO} objects that are children of the passed in scopeId. It is also
	 * self-inclusive as it uses the scope tree to retrieve the list.
	 * 
	 * @param scopeId
	 *            The scope id
	 * @return The list of {@link ScopeDO} child objects for the passed in scope id
	 */
	List<ScopeDO> getDescendantScopes(Long scopeId);

	/**
	 * Gets all ancestors and their children of the provided scopeId. The scope defined by the scopeId is considered an
	 * ancestor.
	 * 
	 * @param scopeId
	 *            The scope id
	 * @return The collection of scopes
	 */
	Collection<ScopeDO> getAncestorsAndChildren(Long scopeId);

	/**
	 * Given a scope point in the tree as identified by id, get:
	 * <ul>
	 * <li>the scope node itself</li>
	 * <li>any ancestor of that node</li>
	 * <li>any descendant of that node</li>
	 * </ul>
	 * 
	 * @param scopeId
	 *            The scope id
	 * @return The collection of scopes
	 */
	Collection<ScopeDO> getAncestorsAndDescendants(Long scopeId);

	/**
	 * Gets the parents of the scope identified by the scopeId. The results will include the scope of the provided scopeId.
	 * 
	 * @param scopeId
	 *            The scopeId of the scope to look for parents.
	 * @return The parents of the scope.
	 */
	Collection<ScopeDO> findParentsOfScope(Long scopeId);

	/**
	 * 
	 */
	List<ScopeDO> getChildScopesOfRoot();

	List<ScopeDO> findDescendantScopes(Long scopeId, boolean allDescendants);
}
