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
 * The persistent class for the entity_data_type database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "entity_data_type")
public class EntityDataTypeDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "entity_data_type_id", unique = true, nullable = false)
	private Long entityDataTypeId;

	@Column(nullable = false, length = 100)
	private String code;

	@Column(nullable = false, length = 200)
	private String name;

	// bi-directional many-to-one association to EntityFieldDO
	@OneToMany(mappedBy = "entityDataType")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<EntityFieldDO> entityFields;

	public EntityDataTypeDO() {
	}

	public Long getEntityDataTypeId() {
		return entityDataTypeId;
	}

	public void setEntityDataTypeId(Long entityDataTypeId) {
		this.entityDataTypeId = entityDataTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EntityFieldDO> getEntityFields() {
		return entityFields;
	}

	public void setEntityFields(List<EntityFieldDO> entityFields) {
		this.entityFields = entityFields;
	}

}