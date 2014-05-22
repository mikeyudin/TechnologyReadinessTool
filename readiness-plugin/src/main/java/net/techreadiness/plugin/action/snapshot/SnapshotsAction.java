package net.techreadiness.plugin.action.snapshot;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowDefinition;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskControlAction;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class SnapshotsAction extends BaseTaskControlAction<SnapshotTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	private ViewDef dataGridView;

	@Inject
	private SnapshotItemProvider snapshotItemProvider;

	@Inject
	private ConfigService configService;

	@ConversationScoped(value = "snapshotSearchGrid")
	private DataGridState<?> snapshotSearchGrid;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_SNAPSHOT })
	@Action(value = "list", results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/snapshot/list.jsp") })
	public String list() {

		snapshotItemProvider.setServiceContext(getServiceContext());

		setDataGridView(configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.SNAPSHOT_DATAGRID));

		return "success";
	}

	public SnapshotItemProvider getSnapshotItemProvider() {
		return snapshotItemProvider;
	}

	public DataGridState<?> getSnapshotSearchGrid() {
		return snapshotSearchGrid;
	}

	public ViewDef getDataGridView() {
		return dataGridView;
	}

	public void setDataGridView(ViewDef dataGridView) {
		this.dataGridView = dataGridView;
	}
}
