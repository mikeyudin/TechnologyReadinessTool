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
 * The persistent class for the org_part_ext database table.
 * 
 */
@Entity
@Table(name = "org_part_ext")
public class OrgPartExtDO extends AbstractAuditedBaseEntityWithExt<OrgPartDO> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "org_part_ext_id", unique = true, nullable = false)
	private Long orgPartExtId;

	@Column(nullable = false, length = 30000)
	private String value;

	// bi-directional many-to-one association to EntityFieldDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_field_id", nullable = false)
	private EntityFieldDO entityField;

	// bi-directional many-to-one association to OrgPartDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_part_id", nullable = false)
	private OrgPartDO orgPart;

	public OrgPartExtDO() {
	}

	public Long getOrgPartExtId() {
		return orgPartExtId;
	}

	public void setOrgPartExtId(Long orgPartExtId) {
		this.orgPartExtId = orgPartExtId;
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

	public OrgPartDO getOrgPart() {
		return orgPart;
	}

	public void setOrgPart(OrgPartDO orgPart) {
		this.orgPart = orgPart;
	}

	@Override
	public OrgPartDO getParent() {
		return orgPart;
	}

	@Override
	public void setParent(OrgPartDO baseEntityWithExt) {
		orgPart = baseEntityWithExt;
	}

}