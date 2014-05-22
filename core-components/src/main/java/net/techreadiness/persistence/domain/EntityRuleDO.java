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

import net.techreadiness.persistence.AuditedBaseEntity;

/**
 * The persistent class for the entity_rule database table.
 * 
 */
@Entity
@Table(name = "entity_rule")
public class EntityRuleDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "entity_rule_id", unique = true, nullable = false)
	private Long entityRuleId;

	@Column(name = "batch_error_message", length = 1000)
	private String batchErrorMessage;

	@Column(length = 1000)
	private String description;

	@Column(nullable = false)
	private boolean disabled;

	@Column(name = "error_message", length = 1000)
	private String errorMessage;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(nullable = false, length = 30000)
	private String rule;

	// bi-directional many-to-one association to EntityDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_id", nullable = false)
	private EntityDO entity;

	// bi-directional many-to-one association to EntityFieldDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_field_id")
	private EntityFieldDO entityField;

	@Column
	private String type;

	public EntityRuleDO() {
	}

	public Long getEntityRuleId() {
		return entityRuleId;
	}

	public void setEntityRuleId(Long entityRuleId) {
		this.entityRuleId = entityRuleId;
	}

	public String getBatchErrorMessage() {
		return batchErrorMessage;
	}

	public void setBatchErrorMessage(String batchErrorMessage) {
		this.batchErrorMessage = batchErrorMessage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public EntityDO getEntity() {
		return entity;
	}

	public void setEntity(EntityDO entity) {
		this.entity = entity;
	}

	public EntityFieldDO getEntityField() {
		return entityField;
	}

	public void setEntityField(EntityFieldDO entityField) {
		this.entityField = entityField;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}