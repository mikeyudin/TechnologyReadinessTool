package net.techreadiness.plugin.action.task.snapshot.edit;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowData;
import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowDefinition;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.tags.taskview.TaskViewState;
import net.techreadiness.ui.task.BaseTaskFlowAction;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

public class EditAction extends BaseTaskFlowAction<SnapshotTaskFlowData, SnapshotTaskFlowDefinition> implements Preparable {
	private static final long serialVersionUID = 1L;

	private ViewDef dataGridViewDef;
	private ViewDef detailsViewDef;

	@Inject
	private SnapshotWindowsByIdItemProvider snapshotWindowsByIdItemProvider;
	@Inject
	private ConfigService configService;

	@Key(Long.class)
	@Element(Org.class)
	private Map<Long, SnapshotWindow> snapshots;

	@ConversationScoped
	private TaskViewState<SnapshotWindow> editSnapshotWindowDataGrid;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_SNAPSHOT_EDIT })
	@Action(results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/snapshot/edit.jsp"),
			@Result(name = "nosnapshots", location = "/net/techreadiness/plugin/action/snapshot/nosnapshots.jsp") })
	public String execute() {

		if (getTaskFlowData().getSnapshots() == null || getTaskFlowData().getSnapshots().isEmpty()) {
			return "nosnapshots";
		}

		setDataGridViewDef(configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.SNAPSHOT_DATAGRID));
		setDetailsViewDef(configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.SNAPSHOT));

		snapshotWindowsByIdItemProvider.setSnapshotWindows(getTaskFlowData().getSnapshots());

		return SUCCESS;
	}

	public ViewDef getDataGridViewDef() {
		return dataGridViewDef;
	}

	public void setDataGridViewDef(ViewDef dataGridViewDef) {
		this.dataGridViewDef = dataGridViewDef;
	}

	public ViewDef getDetailsViewDef() {
		return detailsViewDef;
	}

	public void setDetailsViewDef(ViewDef detailsViewDef) {
		this.detailsViewDef = detailsViewDef;
	}

	public TaskViewState<SnapshotWindow> getEditSnapshotWindowDataGrid() {
		return editSnapshotWindowDataGrid;
	}

	public void setEditSnapshotWindowDataGrid(TaskViewState<SnapshotWindow> editSnapshotWindowDataGrid) {
		this.editSnapshotWindowDataGrid = editSnapshotWindowDataGrid;
	}

	public Map<Long, SnapshotWindow> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(Map<Long, SnapshotWindow> snapshots) {
		this.snapshots = snapshots;
	}

	@Override
	public void prepare() throws Exception {
		snapshots = Maps.newHashMap();
	}

	public SnapshotWindowsByIdItemProvider getSnapshotWindowsByIdItemProvider() {
		return snapshotWindowsByIdItemProvider;
	}

	public void setSnapshotWindowsByIdItemProvider(SnapshotWindowsByIdItemProvider snapshotWindowsByIdItemProvider) {
		this.snapshotWindowsByIdItemProvider = snapshotWindowsByIdItemProvider;
	}
}
