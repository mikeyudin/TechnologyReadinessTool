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

import com.google.common.base.Objects;

/**
 * The persistent class for the org_ext database table.
 * 
 */
@Entity
@Table(name = "org_ext")
public class OrgExtDO extends AbstractAuditedBaseEntityWithExt<OrgDO> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "org_ext_id", unique = true, nullable = false)
	private Long orgExtId;

	@Column(nullable = false, length = 30000)
	private String value;

	// bi-directional many-to-one association to EntityFieldDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_field_id", nullable = false)
	private EntityFieldDO entityField;

	// bi-directional many-to-one association to OrgDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = false)
	private OrgDO org;

	public OrgExtDO() {
	}

	public Long getOrgExtId() {
		return orgExtId;
	}

	public void setOrgExtId(Long orgExtId) {
		this.orgExtId = orgExtId;
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

	public OrgDO getOrg() {
		return org;
	}

	public void setOrg(OrgDO org) {
		this.org = org;
	}

	@Override
	public OrgDO getParent() {
		return org;
	}

	@Override
	public void setParent(OrgDO baseEntityWithExt) {
		org = baseEntityWithExt;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("orgExtId", orgExtId).add("value", value).toString();
	}
}