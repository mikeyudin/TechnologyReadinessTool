package net.techreadiness.plugin.action.task.device;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.device.BaseDeviceTaskAction;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.DeviceService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.Org;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "add" }),
	@Result(name = Action.ERROR, type = "lastAction", params = { "actionName", "add" }) })
public class CreateAction extends BaseDeviceTaskAction {
	private static final long serialVersionUID = 1L;
	@Inject
	private DataModificationStatus modStatus;
	@Inject
	private OrganizationService organizationService;
	private Device device;
	private Long orgId;

	@Inject
	private DeviceService deviceService;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DEVICE_CREATE })
	public String execute() {
		if (orgId == null) {
			addActionError(getText("validation.org.required"));
		}
		try {
			Device returnedDevice = deviceService.create(getServiceContext(), device, orgId);
			getTaskFlowData().getDevices().add(returnedDevice);
			Long orgId = Long.valueOf(returnedDevice.getOrg().getOrgId());
			Org org = organizationService.getById(getServiceContext(), orgId);
			String dataEntryComplete = org.getDataEntryComplete();

			if (hasMissingData(returnedDevice) && dataEntryComplete != null && dataEntryComplete.equals("true")) {
				org.setDataEntryComplete("false");
				org.setDataEntryCompleteUser(getServiceContext().getUserName());
				org.setDataEntryCompleteDate(new Date());
				organizationService.addOrUpdate(getServiceContext(), org);
				modStatus.setMessage(getText("ready.device.task.add.warning"));
			} else {
				modStatus.setMessage(getText("ready.device.task.add.success"));
			}

		} catch (ValidationServiceException vse) {
			List<ValidationError> errors = vse.getFaultInfo().getAttributeErrors();
			for (ValidationError validationError : errors) {
				addFieldError("device." + validationError.getFieldName(), validationError.getOnlineMessage());
			}
		}

		if (hasErrors()) {
			return ERROR;
		}

		return SUCCESS;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	private static boolean hasMissingData(Device device) {
		if (StringUtils.isBlank(device.getOperatingSystem())) {
			return true;
		}
		if (device.getMemory() == null) {
			return true;
		}
		if (device.getScreenResolution() == null) {
			return true;
		}
		if (device.getScreenResolution() == null) {
			return true;
		}
		if (StringUtils.isBlank(device.getEnvironment())) {
			return true;
		}
		return false;

	}
}
