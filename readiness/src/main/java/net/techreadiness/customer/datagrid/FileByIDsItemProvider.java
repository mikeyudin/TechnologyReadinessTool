package net.techreadiness.customer.datagrid;

import java.util.Collection;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.File;

public interface FileByIDsItemProvider extends DataGridItemProvider<File> {
	void setFileIds(Collection<Long> file);
}