package net.techreadiness.service.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.UserDO;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

public class User extends BaseObjectWithExts<UserDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	private Long userId;
	@CoreField
	private Date activeBeginDate;
	@CoreField
	private Date activeEndDate;
	@CoreField
	private Date deleteDate;
	@CoreField
	private Date disableDate;
	@CoreField
	private String disableReason;
	@CoreField
	private String email;
	@CoreField
	private String firstName;
	@CoreField
	private String lastName;
	@CoreField
	private String username;
	@CoreField
	private String resetToken1;
	@CoreField
	private String resetToken2;
	@CoreField
	private String resetToken3;
	@CoreField
	private String resetToken4;
	@CoreField
	private String resetToken5;

	private Scope scope;

	// Okira maps this automatically ...
	private Set<UserOrg> userOrgs = Sets.newHashSet();
	private Set<UserRole> userRoles = Sets.newHashSet();

	public User() { // required by JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("userId", userId).add("username", username).toString();
	}

	@Override
	public Class<UserDO> getBaseEntityType() {
		return UserDO.class;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getActiveBeginDate() {
		return activeBeginDate;
	}

	public void setActiveBeginDate(Date activeBeginDate) {
		this.activeBeginDate = activeBeginDate;
	}

	public String getActiveBeginDateString() {
		return userDateFormatter(activeBeginDate);
	}

	public void setActiveBeginDateString(String date) {
		activeBeginDate = userDateParser(date);
	}

	public Date getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
	}

	public String getActiveEndDateString() {
		return userDateFormatter(activeEndDate);
	}

	public void setActiveEndDateString(String date) {
		activeEndDate = userDateParser(date);
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String getDeleteDateString() {
		return userDateFormatter(deleteDate);
	}

	public void setDeleteDateString(String date) {
		deleteDate = userDateParser(date);
	}

	public Date getDisableDate() {
		return disableDate;
	}

	public void setDisableDate(Date disableDate) {
		this.disableDate = disableDate;
	}

	public String getDisableDateString() {
		return userDateFormatter(disableDate);
	}

	public void setDisableDateString(String date) {
		disableDate = userDateParser(date);
	}

	public String getDisableReason() {
		return disableReason;
	}

	public void setDisableReason(String disableReason) {
		this.disableReason = disableReason;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getResetToken1() {
		return resetToken1;
	}

	public void setResetToken1(String resetToken1) {
		this.resetToken1 = resetToken1;
	}

	public String getResetToken2() {
		return resetToken2;
	}

	public void setResetToken2(String resetToken2) {
		this.resetToken2 = resetToken2;
	}

	public String getResetToken3() {
		return resetToken3;
	}

	public void setResetToken3(String resetToken3) {
		this.resetToken3 = resetToken3;
	}

	public String getResetToken4() {
		return resetToken4;
	}

	public void setResetToken4(String resetToken4) {
		this.resetToken4 = resetToken4;
	}

	public String getResetToken5() {
		return resetToken5;
	}

	public void setResetToken5(String resetToken5) {
		this.resetToken5 = resetToken5;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Long getId() {
		return userId;
	}

	private static String userDateFormatter(Date field) {
		if (field != null) {
			try {
				return new SimpleDateFormat("MM/dd/yyyy").format(field);
			} catch (IllegalArgumentException iae) {
				return "";
			}
		}
		return "";
	}

	private static Date userDateParser(String field) {
		if (StringUtils.isNotEmpty(field)) {
			try {
				return new SimpleDateFormat("MM/dd/yyyy").parse(field);
			} catch (ParseException pe) {
				return null;
			}
		}
		return null;
	}

	public Set<UserOrg> getUserOrgs() {
		return userOrgs;
	}

	public void setUserOrgs(Set<UserOrg> userOrgs) {
		this.userOrgs = userOrgs;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (email == null ? 0 : email.hashCode());
		result = prime * result + (firstName == null ? 0 : firstName.hashCode());
		result = prime * result + (lastName == null ? 0 : lastName.hashCode());
		result = prime * result + (scope == null ? 0 : scope.hashCode());
		result = prime * result + (username == null ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		if (scope == null) {
			if (other.scope != null) {
				return false;
			}
		} else if (!scope.equals(other.scope)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}
}
