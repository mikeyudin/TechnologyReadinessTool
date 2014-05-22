package net.techreadiness.service.object;

import net.techreadiness.persistence.domain.UserRoleDO;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserRole extends BaseObject<UserRoleDO> implements Comparable<UserRole> {
	private static final long serialVersionUID = 1L;

	private Long userRoleId;
	private Long roleId;
	private Long userId;

	@Override
	public int compareTo(UserRole o) {
		CompareToBuilder builder = new CompareToBuilder();
		builder.append(getRoleId(), o.getRoleId());
		builder.append(getUserId(), o.getUserId());
		return builder.toComparison();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("roleId", getRoleId());
		builder.append("userId", getUserId());
		return builder.build();
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	@Override
	public Class<UserRoleDO> getBaseEntityType() {
		return UserRoleDO.class;
	}

	@Override
	public Long getId() {
		return getUserRoleId();
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (roleId == null ? 0 : roleId.hashCode());
		result = prime * result + (userId == null ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof UserRole)) {
			return false;
		}
		UserRole other = (UserRole) obj;
		if (getRoleId() == null) {
			if (other.getRoleId() != null) {
				return false;
			}
		} else if (!getRoleId().equals(other.getRoleId())) {
			return false;
		}
		if (getUserId() == null) {
			if (other.getUserId() != null) {
				return false;
			}
		} else if (!getUserId().equals(other.getUserId())) {
			return false;
		}
		return true;
	}
}