package net.techreadiness.customer.datagrid;

import java.util.Collection;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.User;

public interface UsersByIdItemProvider extends DataGridItemProvider<User> {
	void setUsers(Collection<User> selectedUsers);

	void setServiceContext(ServiceContext serviceContext);
}