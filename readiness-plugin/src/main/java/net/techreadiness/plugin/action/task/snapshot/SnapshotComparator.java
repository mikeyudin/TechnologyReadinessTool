package net.techreadiness.plugin.action.task.snapshot;

import java.util.Comparator;

import net.techreadiness.plugin.service.object.SnapshotWindow;

public class SnapshotComparator implements Comparator<SnapshotWindow> {

	@Override
	public int compare(SnapshotWindow s1, SnapshotWindow s2) {
		return s1.getExecuteDate().before(s2.getExecuteDate()) ? -1 : s1.getExecuteDate().equals(s2.getExecuteDate()) ? 0
				: 1;
	}

}
