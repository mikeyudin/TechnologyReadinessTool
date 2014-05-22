package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.ContactDO;

public interface ContactDAO extends BaseDAO<ContactDO> {
	ContactDO getByOrgAndContactType(Long orgId, String contactTypeCode);

	List<ContactDO> findByOrg(Long orgId);
}
