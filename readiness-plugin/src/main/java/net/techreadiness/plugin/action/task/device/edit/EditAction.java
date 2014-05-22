package net.techreadiness.plugin.action.task.device.edit;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.device.BaseDeviceTaskAction;
import net.techreadiness.plugin.action.device.DeviceItemProvider;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.object.Device;
import net.techreadiness.ui.tags.taskview.TaskViewState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = Action.SUCCESS, location = "/net/techreadiness/plugin/action/device/edit.jsp"),
	@Result(name = "nodevice", location = "/net/techreadiness/plugin/action/device/nodevice.jsp") })
public class EditAction extends BaseDeviceTaskAction implements Preparable {
	private static final long serialVersionUID = 1L;

	private ViewDef viewDef;
	private ViewDef detailsViewDef;

	@Key(Long.class)
	@Element(Device.class)
	private Map<Long, Device> devices;

	@Inject
	private DeviceItemProvider deviceItemProvider;

	@Inject
	private ConfigService configService;

	@ConversationScoped(value = "deviceEditGrid")
	private TaskViewState<?> deviceEditGrid;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DEVICE_UPDATE })
	public String execute() {
		deviceItemProvider.setDevices(getTaskFlowData().getDevices());
		viewDef = configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.DEVICE_DATAGRID);
		detailsViewDef = configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.DEVICE);

		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		devices = new HashMap<>();
	}

	public TaskViewState<?> getDeviceEditGrid() {
		return deviceEditGrid;
	}

	public DeviceItemProvider getDeviceItemProvider() {
		return deviceItemProvider;
	}

	public Map<Long, Device> getDevices() {
		return devices;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setDetailsViewDef(ViewDef detailsViewDef) {
		this.detailsViewDef = detailsViewDef;
	}

	public ViewDef getDetailsViewDef() {
		return detailsViewDef;
	}
}
