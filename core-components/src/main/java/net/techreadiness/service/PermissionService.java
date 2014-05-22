package net.techreadiness.service;

import java.util.List;

import net.techreadiness.service.object.Permission;

public interface PermissionService extends BaseService {

	List<Permission> findAllAncestorPermissions(ServiceContext context);
}
