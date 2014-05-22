package net.techreadiness.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.persistence.dao.ContactDAO;
import net.techreadiness.persistence.dao.ContactTypeDAO;
import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.domain.ContactDO;
import net.techreadiness.persistence.domain.ContactTypeDO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Contact;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@WebService
@Service
@Transactional
public class ContactServiceImpl extends BaseServiceWithValidationImpl implements ContactService {

	@Inject
	private ContactDAO contactDAO;
	@Inject
	private ContactTypeDAO contactTypeDao;
	@Inject
	private OrgDAO orgDao;

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = ContactDO.class)
	public Contact add(ServiceContext context, Contact contact) {
		ContactDO contactDO = contactDAO.getByOrgAndContactType(contact.getOrg().getOrgId(), contact.getContactTypeCode());
		if (contactDO != null) {
			throw new ServiceException("Contact of that type already exists.");
		}
		ContactDO newContact = new ContactDO();
		copyMapFieldsToEntity(context, newContact, contact.getAsMap());
		ContactTypeDO contactTypeDO = contactTypeDao.getContactType(context.getScopeId(), contact.getContactTypeCode());
		OrgDO org = orgDao.getById(contact.getOrg().getOrgId());
		newContact.setContactType(contactTypeDO);
		newContact.setOrg(org);
		doValidation(context, newContact);
		return getMappingService().map(contactDAO.create(newContact));
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = ContactDO.class)
	public Contact update(ServiceContext context, Long contactId, Map<String, String> attributes, Long contactTypeId) {
		ContactDO contactDO = contactDAO.getById(contactId);

		if (allUserEnteredAttributesAreBlank(attributes)) {
			contactDAO.delete(contactDO);
			return null;
		}
		copyMapFieldsToEntity(context, contactDO, attributes);
		ContactTypeDO contactType = contactTypeDao.getById(contactTypeId);
		contactDO.setContactType(contactType);

		doValidation(context, contactDO);

		return getMappingService().map(contactDAO.update(contactDO));
	}

	private void doValidation(ServiceContext context, ContactDO contactDO) throws ValidationServiceException,
			ServiceException {
		List<ValidationError> list = performValidation(contactDO.getAsMap(), context.getScopeId(), EntityTypeCode.CONTACT);

		if (list != null && list.size() > 0) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage("Contact failed validation.");
			faultInfo.setAttributeErrors(list);
			throw new ValidationServiceException(faultInfo);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void validate(ServiceContext context, Contact contact) throws ValidationServiceException, ServiceException {
		doValidation(context, getMappingService().map(contact));
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = ContactDO.class)
	public Contact create(ServiceContext context, Map<String, String> attributes, Long contactTypeId, Long orgId)
			throws ValidationServiceException, ServiceException {
		ContactDO contactDO = new ContactDO();

		if (allUserEnteredAttributesAreBlank(attributes)) {
			return null;
		}
		copyMapFieldsToEntity(context, contactDO, attributes);

		ContactTypeDO contactType = contactTypeDao.getById(contactTypeId);
		contactDO.setContactType(contactType);
		OrgDO org = orgDao.getById(orgId);
		contactDO.setOrg(org);

		doValidation(context, contactDO);

		return getMappingService().map(contactDAO.create(contactDO));
	}

	private static boolean allUserEnteredAttributesAreBlank(Map<String, String> attributes) {
		Iterator<Entry<String, String>> iterator = attributes.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();

			// ignore orgId, contactTypeId and contactTypeName, they are not set by user
			if (!(key.equalsIgnoreCase("orgId") || key.equalsIgnoreCase("contactTypeId")
					|| key.equalsIgnoreCase("contactId") || key.equalsIgnoreCase("contactTypeName"))) {
				if (!value.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = ContactDO.class)
	public void deleteContactsForOrg(ServiceContext context, Long orgId) {
		for (ContactDO contact : contactDAO.findByOrg(orgId)) {
			contactDAO.delete(contact);
		}
	}
}
