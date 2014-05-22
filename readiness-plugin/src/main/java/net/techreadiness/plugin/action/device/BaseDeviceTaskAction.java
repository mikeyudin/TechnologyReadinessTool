package net.techreadiness.plugin.action.device;

import javax.inject.Inject;

import net.techreadiness.plugin.action.task.device.DeviceTaskFlowData;
import net.techreadiness.plugin.action.task.device.DeviceTaskFlowDefinition;
import net.techreadiness.service.OrgTypeService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.OrgType;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;
import net.techreadiness.ui.util.ConversationScoped;

public class BaseDeviceTaskAction extends BaseTaskFlowAction<DeviceTaskFlowData, DeviceTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	protected Org org;

	@Inject
	protected OrganizationService orgService;
	@Inject
	protected OrgTypeService orgTypeService;
	@ConversationScoped(value = "deviceSearchGrid")
	private DataGridState<Device> deviceSearchGrid;

	protected boolean selectedOrgAllowsDevices() {
		org = orgService.getById(getServiceContext(), getServiceContext().getOrgId());
		OrgType orgType = orgTypeService.getById(getServiceContext(), org.getOrgTypeId());
		return orgType.getAllowDevice();
	}

	@Override
	protected String endTaskFlow() {
		deviceSearchGrid.clearSelectedItems();
		for (Device device : getTaskFlowData().getDevices()) {
			deviceSearchGrid.selectItem(device.getDeviceId().toString(), device);
		}

		return super.endTaskFlow();
	}
}
