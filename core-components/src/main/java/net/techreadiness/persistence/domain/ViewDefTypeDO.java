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
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the view_def_type database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "view_def_type")
public class ViewDefTypeDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "view_def_type_id", unique = true, nullable = false)
	private Long viewDefTypeId;

	@Column(nullable = false, length = 1)
	private String category;

	@Column(nullable = false, length = 100)
	private String code;

	@Column(nullable = false)
	private boolean defaultView;

	@Column(nullable = false, length = 200)
	private String name;

	// bi-directional many-to-one association to ViewDefDO
	@OneToMany(mappedBy = "viewDefType")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ViewDefDO> viewDefs;

	// bi-directional many-to-one association to EntityTypeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_type_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private EntityTypeDO entityType;

	public ViewDefTypeDO() {
	}

	public Long getViewDefTypeId() {
		return viewDefTypeId;
	}

	public void setViewDefTypeId(Long viewDefTypeId) {
		this.viewDefTypeId = viewDefTypeId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(boolean defaultView) {
		this.defaultView = defaultView;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ViewDefDO> getViewDefs() {
		return viewDefs;
	}

	public void setViewDefs(List<ViewDefDO> viewDefs) {
		this.viewDefs = viewDefs;
	}

	public EntityTypeDO getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityTypeDO entityType) {
		this.entityType = entityType;
	}

}