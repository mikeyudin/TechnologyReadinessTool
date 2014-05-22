package net.techreadiness.security;

import java.util.Set;

public interface PermissionCodeSet {

	boolean add(PermissionCode permissionCode);

	int size();

	PermissionCode[] toArray();

	Set<PermissionCode> getPermissionCodes();

	void setPermissionCodes(Set<PermissionCode> permissionCodes);
}
