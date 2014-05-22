package net.techreadiness.service.object;

import net.techreadiness.persistence.domain.UserOrgDO;

public class UserOrg extends BaseObject<UserOrgDO> implements Comparable<UserOrg> {
	private static final long serialVersionUID = 1L;

	private Long userOrgId;
	private Long orgId;
	private Long userId;

	@Override
	public int compareTo(UserOrg o) {
		return (int) (getUserOrgId() - o.getUserOrgId());
	}

	public Long getUserOrgId() {
		return userOrgId;
	}

	public void setUserOrgId(Long userOrgId) {
		this.userOrgId = userOrgId;
	}

	@Override
	public Class<UserOrgDO> getBaseEntityType() {
		return UserOrgDO.class;
	}

	@Override
	public Long getId() {
		return getUserOrgId();
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
		result = prime * result + (orgId == null ? 0 : orgId.hashCode());
		result = prime * result + (userId == null ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserOrg)) {
			return false;
		}
		UserOrg other = (UserOrg) obj;
		if (orgId == null) {
			if (other.orgId != null) {
				return false;
			}
		} else if (!orgId.equals(other.orgId)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}

		return true;
	}
}