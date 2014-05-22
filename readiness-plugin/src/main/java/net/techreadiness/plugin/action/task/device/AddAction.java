package net.techreadiness.plugin.action.task.device;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.device.BaseDeviceTaskAction;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.object.Device;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, location = "/net/techreadiness/plugin/action/device/add.jsp") })
public class AddAction extends BaseDeviceTaskAction {
	private static final long serialVersionUID = 1L;

	private Device device;
	private ViewDef viewDef;

	@Inject
	private ConfigService configService;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DEVICE_CREATE })
	public String execute() {

		if (selectedOrgAllowsDevices()) {
			getSession().put(OrganizationDeviceControl.DEVICE_ADD_ORGID, getServiceContext().getOrgId());
		}
		viewDef = configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.DEVICE);

		return SUCCESS;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

}
