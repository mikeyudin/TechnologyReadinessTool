package net.techreadiness.service;

import java.util.Map;

import javax.jws.WebService;

import net.techreadiness.service.object.Contact;

@WebService
public interface ContactService extends BaseServiceWithValidation {

	Contact add(ServiceContext context, Contact contact);

	Contact update(ServiceContext context, Long contactId, Map<String, String> attributes, Long contactTypeId);

	Contact create(ServiceContext context, Map<String, String> attributes, Long contactTypeId, Long orgId);

	void validate(ServiceContext context, Contact contact);

	void deleteContactsForOrg(ServiceContext context, Long orgId);
}
