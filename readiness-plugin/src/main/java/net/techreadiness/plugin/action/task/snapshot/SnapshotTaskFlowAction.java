package net.techreadiness.plugin.action.task.snapshot;

import java.util.List;

import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class SnapshotTaskFlowAction extends BaseTaskFlowAction<SnapshotTaskFlowData, SnapshotTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	@Action(value = "snapshotTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String beginTaskFlow() throws ServiceException {
		startNewTaskFlow();

		DataGridState<SnapshotWindow> dataGridState = conversation.get(DataGridState.class, "snapshotSearchGrid");

		getTaskFlowData().setSnapshots(dataGridState.getSelectedItems());

		return SUCCESS;
	}

	@Override
	public String endTaskFlow() throws ServiceException {

		if (getTaskFlowData().isStandardInvocation()) {
			DataGridState<SnapshotWindow> dataGridState = conversation.get(DataGridState.class, "snapshotSearchGrid");

			List<SnapshotWindow> selectedSnapshots = dataGridState.getSelectedItems();

			for (SnapshotWindow selectedSnapshotWindow : selectedSnapshots) {

				boolean found = false;
				for (SnapshotWindow s : getTaskFlowData().getSnapshots()) {
					if (s.getSnapshotWindowId().equals(selectedSnapshotWindow.getSnapshotWindowId())) {
						found = true;
						break;
					}
				}

				if (!found) {
					dataGridState.deSelectItem(selectedSnapshotWindow.getSnapshotWindowId().toString());
				}
			}

			for (SnapshotWindow s : getTaskFlowData().getSnapshots()) {
				dataGridState.selectItem(s.getSnapshotWindowId().toString(), s);
			}
		}

		return SUCCESS;
	}
}
