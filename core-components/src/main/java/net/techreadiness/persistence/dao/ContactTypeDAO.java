package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.ContactTypeDO;

public interface ContactTypeDAO extends BaseDAO<ContactTypeDO> {
	List<ContactTypeDO> findContactTypesForScope(Long scopeId);

	List<ContactTypeDO> findContactTypesForOrgPart(Long orgPartId);

	ContactTypeDO getContactType(Long scopeId, String contactTypeCode);
}
