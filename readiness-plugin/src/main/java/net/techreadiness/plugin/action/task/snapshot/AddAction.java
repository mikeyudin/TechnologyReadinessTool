package net.techreadiness.plugin.action.task.snapshot;

import java.util.Date;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.ui.task.BaseTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class AddAction extends BaseTaskFlowAction<SnapshotTaskFlowData, SnapshotTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	private ViewDef viewDef;
	private SnapshotWindow snapshotWindow;

	@Inject
	private ConfigService configService;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_SNAPSHOT_CREATE })
	@Action(value = "add", results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/snapshot/add.jsp") })
	public String add() {

		setViewDef(configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.SNAPSHOT));

		snapshotWindow = new SnapshotWindow();

		snapshotWindow.setRequestDate(new Date(System.currentTimeMillis()));
		snapshotWindow.setRequestUser(getServiceContext().getUserName());

		return SUCCESS;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public SnapshotWindow getSnapshotWindow() {
		return snapshotWindow;
	}

	public void setSnapshotWindow(SnapshotWindow snapshotWindow) {
		this.snapshotWindow = snapshotWindow;
	}
}
