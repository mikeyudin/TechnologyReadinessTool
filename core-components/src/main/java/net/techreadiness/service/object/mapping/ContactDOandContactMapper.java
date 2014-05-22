package net.techreadiness.service.object.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.ContactDO;
import net.techreadiness.service.object.Contact;

public class ContactDOandContactMapper extends CustomMapper<ContactDO, Contact> {

	@Override
	public void mapAtoB(ContactDO contactDO, Contact contact, MappingContext context) {
		contact.setContactTypeId(contactDO.getContactType().getContactTypeId());
		contact.setContactTypeCode(contactDO.getContactType().getCode());
		contact.setContactTypeName(contactDO.getContactType().getName());
	}
}
