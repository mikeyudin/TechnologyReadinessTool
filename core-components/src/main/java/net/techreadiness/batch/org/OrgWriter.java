package net.techreadiness.batch.org;

import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.batch.BaseItemWriter;
import net.techreadiness.service.ContactService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Contact;
import net.techreadiness.service.object.Org;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemWriter;

public class OrgWriter extends BaseItemWriter implements ItemWriter<OrgData> {
	@Inject
	private OrganizationService orgService;
	@Inject
	private ContactService contactService;

	@Override
	public void write(List<? extends OrgData> items) throws Exception {
		ServiceContext context = getServiceContext();
		for (OrgData orgData : items) {
			Org org = orgService.addOrUpdate(context, orgData.getOrg());
			contactService.deleteContactsForOrg(context, org.getOrgId());
			for (Entry<String, Contact> entry : orgData.getContacts().entrySet()) {
				if (entry.getKey().equalsIgnoreCase("primary") || entry.getKey().equalsIgnoreCase("secondary")
						&& StringUtils.isNotBlank(entry.getValue().getName())) {
					entry.getValue().setContactTypeCode(entry.getKey());
					entry.getValue().setOrg(org);
					contactService.add(context, entry.getValue());
				}
			}
		}
	}

}
