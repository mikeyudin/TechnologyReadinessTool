package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the view_def database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "view_def")
public class ViewDefDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "view_def_id", unique = true, nullable = false)
	private Long viewDefId;

	@Column(nullable = true, length = 200)
	private String name;

	@Column(name = "collapsible")
	private boolean collapsible;

	@Column(name = "collapsed_by_default")
	private boolean collapsedByDefault;

	@Column(name = "column1_width", nullable = true)
	private String column1Width;

	@Column(name = "column1_label_width", nullable = true)
	private String column1LabelWidth;

	@Column(name = "column2_width", nullable = true)
	private String column2Width;

	@Column(name = "column2_label_width", nullable = true)
	private String column2LabelWidth;

	@Column(name = "column3_width", nullable = true)
	private String column3Width;

	@Column(name = "column3_label_width", nullable = true)
	private String column3LabelWidth;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO scope;

	// bi-directional many-to-one association to ViewDefType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "view_def_type_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ViewDefTypeDO viewDefType;

	// bi-directional many-to-one association to ViewDefFieldDO
	@OneToMany(mappedBy = "viewDef", fetch = FetchType.LAZY)
	@OrderBy("displayOrder, overrideName")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ViewDefFieldDO> viewDefFields;

	@OneToMany(mappedBy = "viewDef")
	@OrderBy("displayOrder")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ViewDefTextDO> viewDefTexts;

	public ViewDefDO() {
	}

	public Long getViewDefId() {
		return viewDefId;
	}

	public void setViewDefId(Long viewDefId) {
		this.viewDefId = viewDefId;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public ViewDefTypeDO getViewDefType() {
		return viewDefType;
	}

	public void setViewDefType(ViewDefTypeDO viewDefType) {
		this.viewDefType = viewDefType;
	}

	public List<ViewDefFieldDO> getViewDefFields() {
		return viewDefFields;
	}

	public void setViewDefFields(List<ViewDefFieldDO> viewDefFields) {
		this.viewDefFields = viewDefFields;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumn1Width() {
		return column1Width;
	}

	public void setColumn1Width(String column1Width) {
		this.column1Width = column1Width;
	}

	public String getColumn2Width() {
		return column2Width;
	}

	public void setColumn2Width(String column2Width) {
		this.column2Width = column2Width;
	}

	public String getColumn3Width() {
		return column3Width;
	}

	public void setColumn3Width(String column3Width) {
		this.column3Width = column3Width;
	}

	public boolean isCollapsible() {
		return collapsible;
	}

	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}

	public boolean isCollapsedByDefault() {
		return collapsedByDefault;
	}

	public void setCollapsedByDefault(boolean collapsedByDefault) {
		this.collapsedByDefault = collapsedByDefault;
	}

	public String getColumn1LabelWidth() {
		return column1LabelWidth;
	}

	public void setColumn1LabelWidth(String column1LabelWidth) {
		this.column1LabelWidth = column1LabelWidth;
	}

	public String getColumn2LabelWidth() {
		return column2LabelWidth;
	}

	public void setColumn2LabelWidth(String column2LabelWidth) {
		this.column2LabelWidth = column2LabelWidth;
	}

	public String getColumn3LabelWidth() {
		return column3LabelWidth;
	}

	public void setColumn3LabelWidth(String column3LabelWidth) {
		this.column3LabelWidth = column3LabelWidth;
	}

	public List<ViewDefTextDO> getViewDefTexts() {
		return viewDefTexts;
	}

	public void setViewDefTexts(List<ViewDefTextDO> viewDefTexts) {
		this.viewDefTexts = viewDefTexts;
	}

}