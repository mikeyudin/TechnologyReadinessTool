package net.techreadiness.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Role;

public interface RoleService extends BaseService {
	Role getById(ServiceContext context, Long roleId);

	Collection<Role> findRolesFromScope(ServiceContext context);

	Set<Long> findAssociatedPermissionIds(ServiceContext context, Long roleId);

	List<Role> findByIds(ServiceContext context, Collection<Long> roleIds);

	void updateRolePermissions(ServiceContext context, Long roleId, Set<Long> permissions);

	void unassignPermissions(ServiceContext context, Set<Long> permissionIdSet);

	boolean isUniqueRoleCategoryNameCodeByScope(ServiceContext context, Role role);

	List<Role> findRolesBySearchTerm(ServiceContext context, String term);

	void validateRole(ServiceContext context, Map<String, String> map, Role role);

	Role create(ServiceContext context, Role role) throws ValidationServiceException, ServiceException;

	Role update(ServiceContext context, Long scopeId, Role role);

	Map<String, Boolean> getRoleConferAsMap(ServiceContext context);

	Boolean isDelegated(Long roleId, Long delegRoleId);

	void updateRoleDelegations(ServiceContext context, Long roleId, Set<Long> addDelegationSet, Set<Long> delDelegationSet);

	Role getRoleByCode(ServiceContext context, String code);
}
