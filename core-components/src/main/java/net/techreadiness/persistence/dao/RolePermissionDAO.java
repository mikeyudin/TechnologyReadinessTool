package net.techreadiness.persistence.dao;

import java.util.Collection;

import net.techreadiness.persistence.domain.RolePermissionDO;

public interface RolePermissionDAO extends BaseDAO<RolePermissionDO> {

	Collection<RolePermissionDO> getRolePermissionsByPermission(Long permissionId);

	Collection<RolePermissionDO> findPermissionsForRole(Long roleId);

	RolePermissionDO getByRoleIdPermissionId(Long roleId, Long permissionId);
}
