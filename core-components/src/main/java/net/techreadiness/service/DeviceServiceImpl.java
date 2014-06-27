package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;
import net.techreadiness.persistence.dao.DeviceDAO;
import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.ExtDAO;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.domain.DeviceDO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Device;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeviceServiceImpl extends BaseServiceWithValidationAndExt<DeviceDO, AbstractAuditedBaseEntityWithExt<DeviceDO>>
implements DeviceService {

	@Inject
	private DeviceDAO deviceDAO;
	@Inject
	private OrgDAO orgDAO;

	@Inject
	@Qualifier("deviceExtDAOImpl")
	private ExtDAO<DeviceDO, AbstractAuditedBaseEntityWithExt<DeviceDO>> deviceExtDao;

	@Override
	public Device getById(ServiceContext context, Long deviceId) {
		return getMappingService().map(deviceDAO.getById(deviceId));
	}

	@Override
	public List<Device> findByOrgId(ServiceContext context, Long orgId) {
		return getMappingService().mapFromDOList(deviceDAO.findByOrgId(orgId));

	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = DeviceDO.class)
	public Device create(ServiceContext context, Device device, Long orgId) {
		DeviceDO deviceDO = updateDevice(context, device.getAsMap(), context.getScopeId(), orgId);
		DeviceDO newDeviceDO = deviceDAO.create(deviceDO);
		newDeviceDO.setExtAttributes(device.getExtendedAttributes());
		storeExtFields(context, newDeviceDO, deviceExtDao, EntityTypeCode.DEVICE, newDeviceDO.getOrg().getScope()
				.getScopeId());

		return getMappingService().map(newDeviceDO);
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = DeviceDO.class)
	public void persist(ServiceContext context, Device device, Long orgId) {
		DeviceDO deviceDO = updateDevice(context, device.getAsMap(), context.getScopeId(), orgId);
		deviceDAO.persist(deviceDO);
		storeExtFields(context, deviceDO, deviceExtDao, EntityTypeCode.DEVICE, deviceDO.getOrg().getScope().getScopeId());
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = DeviceDO.class)
	public Device update(ServiceContext context, Device device, Long orgId) {
		DeviceDO deviceDO = updateDevice(context, device.getAsMap(), context.getScopeId(), orgId);
		storeExtFields(context, deviceDO, deviceExtDao, EntityTypeCode.DEVICE, deviceDO.getOrg().getScope().getScopeId());
		return getMappingService().getMapper().map(deviceDAO.update(deviceDO), Device.class);
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = DeviceDO.class)
	public void delete(ServiceContext context, Device device) {
		deviceDAO.delete(getMappingService().map(device));

	}

	private DeviceDO updateDevice(ServiceContext context, Map<String, String> values, Long scopeId, Long orgId) {
		List<ValidationError> errors = performValidation(values, scopeId, EntityTypeCode.DEVICE);
		// custom validations go here
		OrgDO orgDO = orgDAO.getById(orgId);
		if (orgDO == null) {
			String message = getMessage("ready.device.org.required");
			ValidationError error = new ValidationError("orgId", "Organization", message, "orgCode", message);
			errors.add(error);
		} else if (!orgDO.getOrgType().isAllowDevice()) {
			String message = getMessage("ready.device.not.allowed.for.org", orgDO.getName());
			ValidationError error = new ValidationError("orgId", "Organization", message, "orgCode", message);
			errors.add(error);
		}

		// check the list at the end
		if (errors != null && errors.size() > 0) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setAttributeErrors(errors);
			faultInfo.setMessage("Device failed validation.");
			throw new ValidationServiceException(faultInfo);
		}
		DeviceDO deviceDO = new DeviceDO();
		if (values.containsKey("deviceId")) {
			deviceDO = deviceDAO.getById(Long.valueOf(values.get("deviceId")));
		}

		copyMapFieldsToEntity(context, deviceDO, values);
		deviceDO.setExtAttributes(values);
		deviceDO.setOrg(orgDO);

		return deviceDO;
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = DeviceDO.class)
	public void delete(ServiceContext context, Long deviceId) {
		DeviceDO deviceDO = deviceDAO.getById(deviceId);
		deviceDAO.delete(deviceDO);
	}

	@Override
	public void deleteAllByOrgCode(ServiceContext context, String orgCode) {
		OrgDO org = orgDAO.getOrg(orgCode, context.getScopeId());
		deviceDAO.deleteDevicesForOrg(org.getOrgId());
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = DeviceDO.class)
	public void persist(ServiceContext context, Device device, String orgCode) {
		persist(context, device, orgDAO.getOrg(orgCode, context.getScopeId()).getOrgId());
	}
}
