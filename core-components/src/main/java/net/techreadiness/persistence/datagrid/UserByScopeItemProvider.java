package net.techreadiness.persistence.datagrid;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.SelectableItemProvider;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;

public interface UserByScopeItemProvider extends SelectableItemProvider<User> {
	public void setScope(Scope scope);

	public void setServiceContext(ServiceContext serviceContext);
}
