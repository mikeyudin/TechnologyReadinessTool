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
import javax.persistence.PostLoad;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the entity database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "entity")
public class EntityDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "entity_id", unique = true, nullable = false)
	private Long entityId;

	// bi-directional many-to-one association to EntityTypeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_type_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private EntityTypeDO entityType;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO scope;

	// bi-directional many-to-one association to EntityFieldDO
	@OneToMany(mappedBy = "entity", fetch = FetchType.EAGER)
	@OrderBy("displayOrder")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<EntityFieldDO> entityFields;

	// bi-directional many-to-one association to EntityRuleDO
	@OneToMany(mappedBy = "entity")
	private List<EntityRuleDO> entityRules;

	public EntityDO() {
	}

	@PostLoad
	protected void loadEntitFields() {
		getEntityFields();
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public EntityTypeDO getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityTypeDO entityType) {
		this.entityType = entityType;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public List<EntityFieldDO> getEntityFields() {
		return entityFields;
	}

	public void setEntityFields(List<EntityFieldDO> entityFields) {
		this.entityFields = entityFields;
	}

	public List<EntityRuleDO> getEntityRules() {
		return entityRules;
	}

	public void setEntityRules(List<EntityRuleDO> entityRules) {
		this.entityRules = entityRules;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (entityId == null ? 0 : entityId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EntityDO)) {
			return false;
		}
		EntityDO other = (EntityDO) obj;
		if (entityId == null) {
			if (other.entityId != null) {
				return false;
			}
		} else if (!entityId.equals(other.entityId)) {
			return false;
		}
		return true;
	}
}