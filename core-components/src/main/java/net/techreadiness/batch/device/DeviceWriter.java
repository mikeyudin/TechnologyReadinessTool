package net.techreadiness.batch.device;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.Org;

import org.springframework.batch.item.ItemWriter;

import com.google.common.collect.Lists;

public class DeviceWriter extends AbstractDeviceStep implements ItemWriter<DeviceData> {

	@Inject
	private OrganizationService organizationService;
	int deviceErrorCount = 0;

	@Override
	public void write(List<? extends DeviceData> items) throws Exception {
		ServiceContext serviceContext = getServiceContext();
		for (final DeviceData deviceData : items) {
			List<ValidationError> errors = Lists.newArrayList();
			try {
				if (deviceData.getDevice().getCount() != null && deviceData.getDevice().getCount() == 0) {
					break;
				}

				if (userService.hasAccessToOrgByCode(getServiceContext(), getUserId(), deviceData.getCombinedCode())) {
					String combinedCode = deviceData.getCombinedCode();
					Org org = organizationService.getByCode(getServiceContext(), combinedCode);
					deviceService.persist(serviceContext, deviceData.getDevice(), deviceData.getCombinedCode());
					Device device = deviceData.getDevice();

					Boolean dataIsIncomplete = checkForMissingData(device);
					if (dataIsIncomplete && org.getDataEntryComplete() != null && org.getDataEntryComplete().equals("true")) {
						org.setDataEntryComplete("false");
						org.setDataEntryCompleteUser(getServiceContext().getUserName());
						org.setDataEntryCompleteDate(new Date());
						organizationService.addOrUpdate(getServiceContext(), org);
					}
				} else {
					addNotAllowedError(errors, deviceData.getCombinedCode(), "add");
				}
			} catch (ValidationServiceException vse) {
				errors.addAll(vse.getFaultInfo().getAttributeErrors());
			} catch (Exception e) {
				addGeneralError(errors, e);
			}
			throwIfErrors(errors);
		}
	}

	public Boolean checkForMissingData(Device device) {

		int deviceErrorCount = 0;

		if (device.getOperatingSystem() == null) {
			deviceErrorCount++;
		}
		if (device.getMemory() == null) {
			deviceErrorCount++;
		}
		if (device.getScreenResolution() == null) {
			deviceErrorCount++;
		}
		if (device.getMonitorDisplaySize() == null) {
			deviceErrorCount++;
		}
		if (device.getEnvironment() == null) {
			deviceErrorCount++;
		}

		if (deviceErrorCount == 0) {
			return false;
		}
		return true;

	}
}
