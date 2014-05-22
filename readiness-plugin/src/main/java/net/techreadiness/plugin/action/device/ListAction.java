package net.techreadiness.plugin.action.device;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.task.device.DeviceTaskFlowDefinition;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.object.Device;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskControlAction;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", location = "/net/techreadiness/plugin/action/device/list.jsp") })
public class ListAction extends BaseTaskControlAction<DeviceTaskFlowDefinition> implements ConversationAware {
	private static final long serialVersionUID = 1L;

	@Inject
	private DeviceByOrgItemProvider deviceItemProvider;

	@Inject
	private ConfigService configService;

	@Inject
	private OrganizationService organizationService;

	@ConversationScoped(value = "deviceSearchGrid")
	private DataGridState<Device> deviceSearchGrid;

	private ViewDef viewDefinition;
	private Conversation conversation;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_READINESS_ACCESS })
	public String execute() {

		if (getServiceContext().getOrg().getParentOrgId() == null) {
			deviceItemProvider.setDoNotDisplayText(getText("ready.device.root.org.nodisplay"));
		} else {
			deviceItemProvider.setDoNotDisplayText("");
		}

		deviceItemProvider.setOrg(organizationService.getById(getServiceContext(), getServiceContext().getOrgId()));
		viewDefinition = configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.DEVICE_DATAGRID);

		conversation.put("orgFilterSelectionHandler", "orgSelectionFilterForDeviceList");

		return "success";
	}

	public DataGridState<Device> getDeviceSearchGrid() {
		return deviceSearchGrid;
	}

	public DeviceByOrgItemProvider getDeviceItemProvider() {
		return deviceItemProvider;
	}

	public ViewDef getViewDefinition() {
		return viewDefinition;
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
}
