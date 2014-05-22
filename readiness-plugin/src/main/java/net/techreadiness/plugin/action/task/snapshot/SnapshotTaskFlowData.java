package net.techreadiness.plugin.action.task.snapshot;

import java.util.List;

import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.ui.task.TaskFlowData;

import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Scope("session")
public class SnapshotTaskFlowData extends TaskFlowData {
	private static final long serialVersionUID = 1L;

	private List<SnapshotWindow> snapshots;

	public List<SnapshotWindow> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(List<SnapshotWindow> snapshots) {
		this.snapshots = snapshots;
	}
}
