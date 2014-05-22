package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ContactDO;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class ContactDAOImpl extends BaseDAOImpl<ContactDO> implements ContactDAO {

	@Override
	public ContactDO getByOrgAndContactType(Long orgId, String contactTypeCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("select c ");
		sb.append("from ContactDO c, ContactTypeDO ct ");
		sb.append("where ct.contactTypeId = c.contactType.contactTypeId ");
		sb.append("and ct.code =:contactTypeCode ");
		sb.append("and c.org.orgId =:orgId ");

		TypedQuery<ContactDO> query = em.createQuery(sb.toString(), ContactDO.class);
		query.setParameter("orgId", orgId);
		query.setParameter("contactTypeCode", contactTypeCode);

		return getSingleResult(query);
	}

	@Override
	public List<ContactDO> findByOrg(Long orgId) {
		TypedQuery<ContactDO> query = em
				.createQuery("select c from ContactDO c where c.org.orgId = :orgId", ContactDO.class);
		query.setParameter("orgId", orgId);
		return query.getResultList();
	}

}
