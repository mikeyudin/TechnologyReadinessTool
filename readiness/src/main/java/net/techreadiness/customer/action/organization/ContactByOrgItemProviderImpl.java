package net.techreadiness.customer.action.organization;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ContactDO;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Contact;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope("prototype")
@Transactional(readOnly = true)
public class ContactByOrgItemProviderImpl implements ContactByOrgItemProvider {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	private MappingService mappingService;
	private Long orgId;

	@Override
	public Collection<Contact> getPage(DataGrid<Contact> grid) {
		TypedQuery<ContactDO> query = getQuery();
		return mappingService.mapFromDOList(query.getResultList());
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Contact> grid) {
		return getQuery().getResultList().size();
	}

	@Override
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	private TypedQuery<ContactDO> getQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("select c ");
		sb.append("from ContactDO c ");
		sb.append("where c.org.orgId = :orgId ");

		TypedQuery<ContactDO> query = em.createQuery(sb.toString(), ContactDO.class);
		query.setParameter("orgId", orgId);

		return query;
	}

}
