package net.techreadiness.customer.datagrid;

import java.util.Collection;
import java.util.List;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.User;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Scope("prototype")
@Component
public class UsersByIdItemProviderImpl implements UsersByIdItemProvider {

	private Collection<User> selectedUsers;

	@Override
	public List<User> getPage(DataGrid<User> grid) {
		List<User> users = Lists.newArrayList();

		for (User user : getUsers()) {
			users.add(user);
		}
		return users;
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<User> grid) {
		return getUsers().size();
	}

	@Override
	public void setUsers(Collection<User> users) {
		selectedUsers = users;

	}

	private Collection<User> getUsers() {
		if (selectedUsers == null || selectedUsers.isEmpty()) {
			throw new IllegalStateException("No users are selected.");
		}

		return selectedUsers;
	}

	@Override
	public void setServiceContext(ServiceContext serviceContext) {
		// No service context required
	}
}
