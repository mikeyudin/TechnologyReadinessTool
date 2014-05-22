package net.techreadiness.plugin.action.org;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Contact;

public interface ContactByReadyOrgItemProvider extends DataGridItemProvider<Contact> {
	void setOrgId(Long orgId);
}
