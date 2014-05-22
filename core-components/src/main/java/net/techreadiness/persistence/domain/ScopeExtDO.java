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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.base.Objects;

/**
 * The persistent class for the scope_ext database table.
 * 
 */
@Entity
@Table(name = "scope_ext")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ScopeExtDO extends AbstractAuditedBaseEntityWithExt<ScopeDO> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "scope_ext_id", unique = true, nullable = false)
	private Long scopeExtId;

	@Column(nullable = false, length = 30000)
	private String value;

	// bi-directional many-to-one association to EntityFieldDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_field_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private EntityFieldDO entityField;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO scope;

	public ScopeExtDO() {
	}

	@Override
	public ScopeDO getParent() {
		return scope;
	}

	@Override
	public void setParent(ScopeDO baseEntityWithExt) {
		scope = baseEntityWithExt;

	}

	public Long getScopeExtId() {
		return scopeExtId;
	}

	public void setScopeExtId(Long scopeExtId) {
		this.scopeExtId = scopeExtId;
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

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("fieldName", entityField.getCode()).add("value", value).toString();
	}

}