package net.techreadiness.customer.action.filebatch;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.File;

public interface FileItemProvider extends DataGridItemProvider<File> {
	public void setServiceContext(ServiceContext serviceContext);
}