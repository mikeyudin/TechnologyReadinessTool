package net.techreadiness.plugin.action.org;

import net.techreadiness.service.common.SelectableItemProvider;
import net.techreadiness.service.object.Org;

public interface ReadyOrgItemProvider extends SelectableItemProvider<Org> {
	void setScope(Long scopeId);
}
