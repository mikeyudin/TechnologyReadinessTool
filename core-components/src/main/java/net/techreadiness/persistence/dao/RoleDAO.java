package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Role;

public interface RoleDAO extends BaseDAO<RoleDO> {
	Set<Long> getAssociatedPermissionIds(Long roleId);

	boolean isUniqueRoleCategoryNameCodeByScopes(Role role, Collection<ScopeDO> pathScopes);

	List<RoleDO> findRolesFromScope(Long scopeId, Long userId, boolean ignoreConfer);

	List<RoleDO> findById(Collection<Long> roleIds);

	List<RoleDO> getRolesBySearchTerm(Long scopeId, String term, Long userId, boolean ignoreConfer);

	RoleDO getRoleByCode(Long scopeId, String code);

	List<RoleDO> findRolesByCode(Long scopeId, List<String> codes);

	RoleDO getRoleByName(Long scopeId, String name);

	Map<String, Boolean> getRoleConferAsMap(ServiceContext context);

	Boolean isDelegated(Long roleId, Long delegRoleId);

	/**
	 * Finds the roles that the user is able to give other users for a given scope.
	 * 
	 * @param userId
	 *            The userId of the user doing the conferring.
	 * @param scopeId
	 *            The scope the user is currently operating in.
	 * @return A list of roles the user can give other users.
	 */
	List<RoleDO> findDelegatableRoles(Long userId, Long scopeId);
}
