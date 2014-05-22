package net.techreadiness.plugin.action.task.snapshot.remove;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowData;
import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowDefinition;
import net.techreadiness.plugin.action.task.snapshot.edit.SnapshotWindowsByIdItemProvider;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class RemoveAction extends BaseTaskFlowAction<SnapshotTaskFlowData, SnapshotTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	private ViewDef dataGridViewDef;

	@Inject
	private SnapshotWindowsByIdItemProvider snapshotWindowsByIdItemProvider;
	@Inject
	private ConfigService configService;

	@ConversationScoped(value = "snapshotRemoveGrid")
	private DataGridState<SnapshotWindow> snapshotRemoveGrid;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_SNAPSHOT_DELETE })
	@Action(results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/snapshot/remove.jsp"),
			@Result(name = "nosnapshots", location = "/net/techreadiness/plugin/action/snapshot/nosnapshots.jsp") })
	public String execute() {

		if (getTaskFlowData().getSnapshots() == null || getTaskFlowData().getSnapshots().isEmpty()) {
			return "nosnapshots";
		}

		setDataGridViewDef(configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.SNAPSHOT_DATAGRID));

		snapshotWindowsByIdItemProvider.setSnapshotWindows(getTaskFlowData().getSnapshots());

		return SUCCESS;
	}

	public ViewDef getDataGridViewDef() {
		return dataGridViewDef;
	}

	public void setDataGridViewDef(ViewDef dataGridViewDef) {
		this.dataGridViewDef = dataGridViewDef;
	}

	public SnapshotWindowsByIdItemProvider getSnapshotWindowsByIdItemProvider() {
		return snapshotWindowsByIdItemProvider;
	}

	public void setSnapshotWindowsByIdItemProvider(SnapshotWindowsByIdItemProvider snapshotWindowsByIdItemProvider) {
		this.snapshotWindowsByIdItemProvider = snapshotWindowsByIdItemProvider;
	}

	public DataGridState<SnapshotWindow> getSnapshotRemoveGrid() {
		return snapshotRemoveGrid;
	}

	public void setSnapshotRemoveGrid(DataGridState<SnapshotWindow> snapshotRemoveGrid) {
		this.snapshotRemoveGrid = snapshotRemoveGrid;
	}
}
