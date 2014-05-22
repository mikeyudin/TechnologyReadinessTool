package net.techreadiness.security;

import net.techreadiness.service.exception.AuthorizationException;

public interface UserAware {

	boolean userActive();

	boolean hasPermission(String permissionCode) throws Exception;

	boolean hasPermission(PermissionCode[] permissionCodes);

	boolean hasPermission(PermissionCode permissionCode);

	void throwNotAuthorized(Object o) throws AuthorizationException;

}
