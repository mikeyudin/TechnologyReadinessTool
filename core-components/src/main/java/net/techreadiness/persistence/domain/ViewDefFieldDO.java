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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the view_def_field database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "view_def_field")
public class ViewDefFieldDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "view_def_field_id", unique = true, nullable = false)
	private Long viewDefFieldId;

	@Column(name = "column_number")
	private Integer columnNumber;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(name = "input_type", length = 1)
	private String inputType;

	@Column(name = "override_name", length = 200)
	private String overrideName;

	@Column(name = "label_position", length = 200)
	private String labelPosition;

	@Column(name = "label_style", length = 300)
	private String labelStyle;

	@Column(name = "input_style", length = 300)
	private String inputStyle;

	@Column(name = "display_width", length = 200)
	private String displayWidth;

	@Column(name = "read_only", nullable = false)
	private boolean readOnly;

	// bi-directional many-to-one association to EntityFieldDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_field_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private EntityFieldDO entityField;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "display_entity_rule_id", nullable = true)
	private EntityRuleDO displayRule;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "edit_entity_rule_id", nullable = true)
	private EntityRuleDO editRule;

	// bi-directional many-to-one association to ViewDefDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "view_def_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ViewDefDO viewDef;

	public ViewDefFieldDO() {
	}

	public Long getViewDefFieldId() {
		return viewDefFieldId;
	}

	public void setViewDefFieldId(Long viewDefFieldId) {
		this.viewDefFieldId = viewDefFieldId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getOverrideName() {
		return overrideName;
	}

	public void setOverrideName(String overrideName) {
		this.overrideName = overrideName;
	}

	public boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public EntityFieldDO getEntityField() {
		return entityField;
	}

	public void setEntityField(EntityFieldDO entityField) {
		this.entityField = entityField;
	}

	public ViewDefDO getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDefDO viewDef) {
		this.viewDef = viewDef;
	}

	public String getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	public Integer getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String getDisplayWidth() {
		return displayWidth;
	}

	public void setDisplayWidth(String displayWidth) {
		this.displayWidth = displayWidth;
	}

	public EntityRuleDO getDisplayRule() {
		return displayRule;
	}

	public void setDisplayRule(EntityRuleDO displayRule) {
		this.displayRule = displayRule;
	}

	public EntityRuleDO getEditRule() {
		return editRule;
	}

	public void setEditRule(EntityRuleDO editRule) {
		this.editRule = editRule;
	}

	public String getLabelStyle() {
		return labelStyle;
	}

	public void setLabelStyle(String labelStyle) {
		this.labelStyle = labelStyle;
	}

	public String getInputStyle() {
		return inputStyle;
	}

	public void setInputStyle(String inputStyle) {
		this.inputStyle = inputStyle;
	}

}