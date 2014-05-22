package net.techreadiness.plugin.service;

import java.util.List;

import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.ServiceContext;

public interface SnapshotWindowService {

	public SnapshotWindow addOrUpdate(ServiceContext context, SnapshotWindow snapshotWindow);

	public void delete(ServiceContext context, Long snapshotWindowId);

	List<SnapshotWindow> findUnexecutedSnapshots(ServiceContext context);

	public SnapshotWindow getByScopeIdAndName(ServiceContext context, Long scopeId, String snapshotWindowName);

	List<SnapshotWindow> findActiveSnapshots(ServiceContext context, Long scopeId);
}
