package net.techreadiness.plugin.action.task.snapshot.edit;

import java.util.List;

import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.common.DataGridItemProvider;

public interface SnapshotWindowsByIdItemProvider extends DataGridItemProvider<SnapshotWindow> {
	void setSnapshotWindows(List<SnapshotWindow> snapshots);
}
