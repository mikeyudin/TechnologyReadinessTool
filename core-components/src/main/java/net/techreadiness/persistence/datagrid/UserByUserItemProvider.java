package net.techreadiness.persistence.datagrid;

import java.util.Collection;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.User;

public interface UserByUserItemProvider extends DataGridItemProvider<User> {

	void setUserIds(Collection<Long> user);
}
