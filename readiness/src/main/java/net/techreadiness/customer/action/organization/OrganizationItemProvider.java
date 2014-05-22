package net.techreadiness.customer.action.organization;

import net.techreadiness.service.common.SelectableItemProvider;
import net.techreadiness.service.object.Org;

public interface OrganizationItemProvider extends SelectableItemProvider<Org> {
	void setScope(Long scopeId);
}
