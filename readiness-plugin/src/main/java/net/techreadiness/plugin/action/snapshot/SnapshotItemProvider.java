package net.techreadiness.plugin.action.snapshot;

import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGridItemProvider;

public interface SnapshotItemProvider extends DataGridItemProvider<SnapshotWindow> {
	public void setServiceContext(ServiceContext serviceContext);
}
