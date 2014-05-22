package net.techreadiness.plugin.action.task.device.edit;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.device.BaseDeviceTaskAction;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.DeviceService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.Org;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "edit" }),
		@Result(name = Action.ERROR, type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends BaseDeviceTaskAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(Device.class)
	private Map<Long, Device> devices;
	private Boolean dataIsIncomplete = false;

	@Inject
	private DeviceService deviceService;
	@Inject
	private OrganizationService organizationService;
	@Inject
	DataModificationStatus dataModificationStatus;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DEVICE_UPDATE })
	public String execute() {
		ServiceContext context = getServiceContext();

		for (Entry<Long, Device> entry : devices.entrySet()) {

			try {

				Device device = deviceService.update(getServiceContext(), entry.getValue(), entry.getValue().getOrg()
						.getOrgId());
				updateTaskFlowData(device);
				Org org = organizationService.getById(context, device.getOrg().getOrgId());
				String dataEntryComplete = org.getDataEntryComplete();
				dataIsIncomplete = checkForMissingData(device);

				if (dataIsIncomplete && dataEntryComplete != null && dataEntryComplete.equals("true")) {
					org.setDataEntryCompleteDate(new Date());
					org.setDataEntryComplete("false");
					org.setDataEntryCompleteUser(context.getUserName());
					organizationService.addOrUpdate(context, org);
					dataModificationStatus.setMessage(getText("task.mark.dataentry.complete.warning"));
				}
			} catch (ValidationServiceException vse) {
				List<ValidationError> errors = vse.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError("devices[" + entry.getKey() + "]." + validationError.getFieldName(),
							validationError.getOnlineMessage());
				}
			}
		}

		if (hasErrors()) {
			return ERROR;
		}

		return SUCCESS;
	}

	private void updateTaskFlowData(Device device) {
		Iterator<Device> i = getTaskFlowData().getDevices().iterator();
		while (i.hasNext()) {
			Device next = i.next();
			if (next.getDeviceId().equals(device.getDeviceId())) {
				i.remove();
				break;
			}
		}
		getTaskFlowData().getDevices().add(device);
	}

	@Override
	public void prepare() throws Exception {
		devices = new HashMap<>();
	}

	public Map<Long, Device> getDevices() {
		return devices;
	}

	private static Boolean checkForMissingData(Device device) {

		int deviceError = 0;

		String operatingSystem = device.getOperatingSystem();
		if (operatingSystem == null || StringUtils.isBlank(operatingSystem)) {
			deviceError++;
		}
		if (device.getMemory() == null) {
			deviceError++;
		}

		if (device.getScreenResolution() == null) {
			deviceError++;
		}

		if (device.getMonitorDisplaySize() == null) {
			deviceError++;
		}
		String environment = device.getEnvironment();
		if (environment == null || StringUtils.isBlank(environment)) {
			deviceError++;
		}

		if (deviceError == 0) {
			return false;
		}
		return true;

	}

}
