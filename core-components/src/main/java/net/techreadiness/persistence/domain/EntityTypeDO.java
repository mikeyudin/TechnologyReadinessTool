package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the entity_type database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "entity_type")
public class EntityTypeDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "entity_type_id", unique = true, nullable = false)
	private Long entityTypeId;

	@Column(nullable = false, length = 100)
	private String code;

	@Column(name = "java_class", nullable = false, length = 200)
	private String javaClass;

	@Column(nullable = false, length = 200)
	private String name;

	// bi-directional many-to-one association to EntityDO
	@OneToMany(mappedBy = "entityType")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<EntityDO> entities;

	// bi-directional many-to-one association to QuerySqlDO
	@OneToMany(mappedBy = "entityType")
	private List<QuerySqlDO> querySqls;

	// bi-directional many-to-one association to ViewDefType
	@OneToMany(mappedBy = "entityType")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ViewDefTypeDO> viewDefTypes;

	public EntityTypeDO() {
	}

	public Long getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(Long entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EntityDO> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityDO> entities) {
		this.entities = entities;
	}

	public List<QuerySqlDO> getQuerySqls() {
		return querySqls;
	}

	public void setQuerySqls(List<QuerySqlDO> querySqls) {
		this.querySqls = querySqls;
	}

	public List<ViewDefTypeDO> getViewDefTypes() {
		return viewDefTypes;
	}

	public void setViewDefTypes(List<ViewDefTypeDO> viewDefTypes) {
		this.viewDefTypes = viewDefTypes;
	}

}