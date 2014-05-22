package net.techreadiness.customer.action.organization;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Contact;

public interface ContactByOrgItemProvider extends DataGridItemProvider<Contact> {
	void setOrgId(Long orgId);
}
