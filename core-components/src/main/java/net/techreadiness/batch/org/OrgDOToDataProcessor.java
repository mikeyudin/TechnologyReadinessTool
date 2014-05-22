package net.techreadiness.batch.org;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.domain.ContactDO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.service.object.Contact;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.batch.item.ItemProcessor;

import com.google.common.collect.Maps;

public class OrgDOToDataProcessor implements ItemProcessor<OrgDO, OrgData> {

	@Inject
	private MappingService mappingService;

	@Override
	public OrgData process(OrgDO item) throws Exception {
		OrgData orgData = new OrgData();
		Org org = mappingService.map(item);
		orgData.setOrg(org);
		if (item.getContacts() != null) {
			Map<String, Contact> contacts = Maps.newHashMap();
			for (ContactDO contactDo : item.getContacts()) {
				Contact contact = mappingService.map(contactDo);
				contacts.put(contact.getContactTypeCode(), contact);
			}
			orgData.setContacts(contacts);
		}
		return orgData;
	}

}
