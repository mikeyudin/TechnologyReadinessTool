package net.techreadiness.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;

/**
 * The persistent class for the user_ext database table.
 * 
 */
@Entity
@Table(name = "user_ext")
public class UserExtDO extends AbstractAuditedBaseEntityWithExt<UserDO> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_ext_id", unique = true, nullable = false)
	private Long userExtId;

	@Column(nullable = false, length = 500)
	private String value;

	// bi-directional many-to-one association to EntityFieldDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_field_id", nullable = false)
	private EntityFieldDO entityField;

	// bi-directional many-to-one association to UserDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserDO user;

	public UserExtDO() {
	}

	public Long getUserExtId() {
		return userExtId;
	}

	public void setUserExtId(Long userExtId) {
		this.userExtId = userExtId;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public EntityFieldDO getEntityField() {
		return entityField;
	}

	@Override
	public void setEntityField(EntityFieldDO entityField) {
		this.entityField = entityField;
	}

	public UserDO getUser() {
		return user;
	}

	public void setUser(UserDO user) {
		this.user = user;
	}

	@Override
	public UserDO getParent() {
		return user;
	}

	@Override
	public void setParent(UserDO baseEntityWithExt) {
		user = baseEntityWithExt;

	}

}