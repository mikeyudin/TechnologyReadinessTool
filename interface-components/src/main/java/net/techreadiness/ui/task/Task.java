package net.techreadiness.ui.task;

import java.io.Serializable;
import java.util.Arrays;

import net.techreadiness.security.PermissionCode;

public class Task implements Serializable {
	private static final long serialVersionUID = 1L;
	private String namespace;
	private String action;
	private String taskName;
	private PermissionCode[] permissionCodes;

	public Task(String namespace, String action, String taskName) {
		setNamespace(namespace);
		setAction(action);
		this.taskName = taskName;
	}

	public Task(String namespace, String action, String taskName, PermissionCode... permissionCodes) {
		this(namespace, action, taskName);
		setPermissionCodes(permissionCodes);
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setPermissionCodes(PermissionCode... permissionCodes) {
		this.permissionCodes = Arrays.copyOf(permissionCodes, permissionCodes.length);
	}

	public String getTaskUrl() {
		return new StringBuilder().append(namespace).append("/").append(action).append(".action").toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (action == null ? 0 : action.hashCode());
		result = prime * result + (namespace == null ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Task)) {
			return false;
		}
		Task other = (Task) obj;
		if (action == null) {
			if (other.action != null) {
				return false;
			}
		} else if (!action.equals(other.action)) {
			return false;
		}
		if (namespace == null) {
			if (other.namespace != null) {
				return false;
			}
		} else if (!namespace.equals(other.namespace)) {
			return false;
		}
		return true;
	}

	public PermissionCode[] getPermissionCodes() {
		if (permissionCodes == null) {
			return null;
		}
		return Arrays.copyOf(permissionCodes, permissionCodes.length);
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
