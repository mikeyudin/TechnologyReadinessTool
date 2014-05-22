package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.techreadiness.persistence.AuditedBaseEntityWithExt;
import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.persistence.ServiceObjectMapped;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.User;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "user")
public class UserDO extends AuditedBaseEntityWithExt implements Serializable, ServiceObjectMapped {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", unique = true, nullable = false)
	private Long userId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "active_begin_date")
	private Date activeBeginDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "active_end_date")
	private Date activeEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "delete_date")
	private Date deleteDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "disable_date")
	private Date disableDate;

	@Column(name = "disable_reason", length = 1000)
	private String disableReason;

	@Column(length = 100)
	private String email;

	@Column(name = "first_name", length = 50)
	private String firstName;

	@Column(name = "last_name", length = 50)
	private String lastName;

	@Column(nullable = false, length = 100)
	private String username;

	@Column(name = "reset_token_1", length = 100)
	private String resetToken1;

	@Column(name = "reset_token_2", length = 100)
	private String resetToken2;

	@Column(name = "reset_token_3", length = 100)
	private String resetToken3;

	@Column(name = "reset_token_4", length = 100)
	private String resetToken4;

	@Column(name = "reset_token_5", length = 100)
	private String resetToken5;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	private ScopeDO scope;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "selected_scope_id")
	private ScopeDO selectedScope;

	// bi-directional many-to-one association to UserExtDO
	@OneToMany(mappedBy = "user")
	private Set<UserExtDO> userExts;

	// bi-directional many-to-one association to UserOrgDO
	@OneToMany(mappedBy = "user")
	private Set<UserOrgDO> userOrgs;

	// bi-directional many-to-one association to UserRoleDO
	@OneToMany(mappedBy = "user")
	private Set<UserRoleDO> userRoles;

	public UserDO() {
	}

	@Override
	protected void populateExtAttributes() {
		Map<String, String> map = new HashMap<>();

		if (userExts != null && userExts.size() > 0) {
			for (UserExtDO userExtDO : userExts) {
				map.put(userExtDO.getEntityField().getCode(), userExtDO.getValue());
			}
		}

		this.setExtAttributes(map);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getActiveBeginDateString() {
		return userDateFormatter(activeBeginDate);
	}

	public void setActiveBeginDateString(String date) {
		activeBeginDate = userDateParser(date);
	}

	public Date getActiveBeginDate() {
		return activeBeginDate;
	}

	public void setActiveBeginDate(Date activeBeginDate) {
		this.activeBeginDate = activeBeginDate;
	}

	public String getActiveEndDateString() {
		return userDateFormatter(activeEndDate);
	}

	public void setActiveEndDateString(String date) {
		activeEndDate = userDateParser(date);
	}

	public Date getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
	}

	public String getDeleteDateString() {
		return userDateFormatter(deleteDate);
	}

	public void setDeleteDateString(String date) {
		deleteDate = userDateParser(date);
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String getDisableDateString() {
		return userDateFormatter(disableDate);
	}

	public void setDisableDateString(String date) {
		disableDate = userDateParser(date);
	}

	public Date getDisableDate() {
		return disableDate;
	}

	public void setDisableDate(Date disableDate) {
		this.disableDate = disableDate;
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

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public Set<UserExtDO> getUserExts() {
		return userExts;
	}

	public void setUserExts(Set<UserExtDO> userExts) {
		this.userExts = userExts;
	}

	public Set<UserOrgDO> getUserOrgs() {
		return userOrgs;
	}

	public void setUserOrgs(Set<UserOrgDO> userOrgs) {
		this.userOrgs = userOrgs;
	}

	public Set<UserRoleDO> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRoleDO> userRoles) {
		this.userRoles = userRoles;
	}

	public void setResetToken5(String resetToken5) {
		this.resetToken5 = resetToken5;
	}

	public String getResetToken5() {
		return resetToken5;
	}

	public void setResetToken4(String resetToken4) {
		this.resetToken4 = resetToken4;
	}

	public String getResetToken4() {
		return resetToken4;
	}

	public void setResetToken3(String resetToken3) {
		this.resetToken3 = resetToken3;
	}

	public String getResetToken3() {
		return resetToken3;
	}

	public void setResetToken2(String resetToken2) {
		this.resetToken2 = resetToken2;
	}

	public String getResetToken2() {
		return resetToken2;
	}

	public void setResetToken1(String resetToken1) {
		this.resetToken1 = resetToken1;
	}

	public String getResetToken1() {
		return resetToken1;
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

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("userId", userId).add("username", username).toString();
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return User.class;
	}

	public ScopeDO getSelectedScope() {
		return selectedScope;
	}

	public void setSelectedScope(ScopeDO selectedScope) {
		this.selectedScope = selectedScope;
	}
}