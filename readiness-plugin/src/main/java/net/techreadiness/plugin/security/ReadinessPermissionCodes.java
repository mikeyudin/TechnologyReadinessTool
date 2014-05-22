package net.techreadiness.plugin.security;

import java.util.HashMap;
import java.util.Map;

import net.techreadiness.security.PermissionCode;

public enum ReadinessPermissionCodes implements PermissionCode {

	READY_CUSTOMER_DEVICE_FILE_EXPORT, READY_CUSTOMER_DEVICE_FILE_IMPORT, READY_CUSTOMER_DEVICE_ANALYZE, READY_CUSTOMER_DEVICE_CREATE, READY_CUSTOMER_DEVICE_DELETE, READY_CUSTOMER_DEVICE_RPT_ACCESS, READY_CUSTOMER_DEVICE_TESTER, READY_CUSTOMER_NETWORK_INFRASTRUCTURE, READY_CUSTOMER_READINESS_ACCESS;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

	private static final Map<String, PermissionCode> stringToEnum = new HashMap<>();

	static {
		for (PermissionCode code : values()) {
			stringToEnum.put(code.toString(), code);
		}
	}

	public static PermissionCode fromString(String code) {
		return stringToEnum.get(code);
	}

	@Override
	public String key() {
		return String.valueOf(this.ordinal());
	}
}
