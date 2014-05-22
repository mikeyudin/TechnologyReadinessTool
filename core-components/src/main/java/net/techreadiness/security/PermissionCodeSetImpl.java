package net.techreadiness.security;

import java.util.HashSet;
import java.util.Set;

public class PermissionCodeSetImpl implements PermissionCodeSet {

	Set<PermissionCode> permissionCodes = new HashSet<>();

	public PermissionCodeSetImpl() {
	}

	public PermissionCodeSetImpl(PermissionCode permissionCode) {
		permissionCodes.add(permissionCode);
	}

	@Override
	public boolean add(PermissionCode permissionCode) {
		return permissionCodes.add(permissionCode);
	}

	@Override
	public int size() {
		return permissionCodes.size();
	}

	@Override
	public PermissionCode[] toArray() {
		return permissionCodes.toArray(new PermissionCode[0]);
	}

	@Override
	public Set<PermissionCode> getPermissionCodes() {
		return permissionCodes;
	}

	@Override
	public void setPermissionCodes(Set<PermissionCode> permissionCodes) {
		this.permissionCodes = permissionCodes;

	}

}
