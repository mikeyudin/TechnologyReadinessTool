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
 * The persistent class for the device_ext database table.
 * 
 */
@Entity
@Table(name = "device_ext")
public class DeviceExtDO extends AbstractAuditedBaseEntityWithExt<DeviceDO> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "device_ext_id", unique = true, nullable = false)
	private Long deviceExtId;

	@Column(nullable = false, length = 30000)
	private String value;

	// bi-directional many-to-one association to EntityFieldDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_field_id", nullable = false)
	private EntityFieldDO entityField;

	// bi-directional many-to-one association to OrgDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id", nullable = false)
	private DeviceDO device;

	public DeviceExtDO() {

	}

	public Long getDeviceExtId() {
		return deviceExtId;
	}

	public void setDeviceExtId(Long deviceExtId) {
		this.deviceExtId = deviceExtId;
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

	public DeviceDO getDevice() {
		return device;
	}

	public void setDevice(DeviceDO device) {
		this.device = device;
	}

	@Override
	public DeviceDO getParent() {
		return device;
	}

	@Override
	public void setParent(DeviceDO baseEntityWithExt) {
		device = baseEntityWithExt;

	}

}
