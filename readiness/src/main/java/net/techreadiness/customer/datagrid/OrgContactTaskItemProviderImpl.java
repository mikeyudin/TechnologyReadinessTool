package net.techreadiness.customer.datagrid;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.dao.ContactTypeDAO;
import net.techreadiness.persistence.domain.ContactDO;
import net.techreadiness.persistence.domain.ContactTypeDO;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
@org.springframework.context.annotation.Scope("prototype")
@Transactional(readOnly = true)
public class OrgContactTaskItemProviderImpl implements OrgContactTaskItemProvider {
	@PersistenceContext
	private EntityManager em;
	private Org org;
	private Scope scope;

	@Inject
	private ContactTypeDAO contactTypeDao;

	@Override
	public int getTotalNumberOfItems(DataGrid<Map<String, String>> grid) {
		if (org != null) {
			TypedQuery<ContactDO> query = getQuery();
			return query.getResultList().size();
		}

		return 0;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		int start = (grid.getPage() - 1) * grid.getPageSize();

		List<Map<String, String>> results = Lists.newArrayList();

		if (org != null) {
			TypedQuery<ContactDO> query = getQuery();
			query.setFirstResult(start);
			query.setMaxResults(grid.getPageSize());

			List<ContactDO> contacts = query.getResultList();
			Map<ContactTypeDO, ContactDO> contactMap = Maps.newHashMap();
			for (ContactDO contact : contacts) {
				contactMap.put(contact.getContactType(), contact);
			}

			List<ContactTypeDO> contactTypes = contactTypeDao.findContactTypesForScope(scope.getScopeId());
			for (ContactTypeDO contactType : contactTypes) {
				Map<String, String> row = Maps.newHashMap();
				if (contactMap.containsKey(contactType)) {
					ContactDO contact = contactMap.get(contactType);
					row.putAll(contact.getAsMap());
				}
				row.put("contactTypeId", contactType.getContactTypeId().toString());
				row.put("contactTypeName", contactType.getName());
				row.put("orgId", org.getOrgId().toString());
				results.add(row);
			}

		}
		return results;
	}

	private TypedQuery<ContactDO> getQuery() {
		StringBuilder sb = new StringBuilder();

		sb.append("select c ");
		sb.append("from ContactDO c ");
		sb.append("where c.org.orgId = :orgId");
		TypedQuery<ContactDO> query = em.createQuery(sb.toString(), ContactDO.class);
		query.setParameter("orgId", Long.valueOf(org.getOrgId().toString()));
		return query;
	}

	@Override
	public void setOrg(Org org) {
		this.org = org;
	}

	@Override
	public void setScope(Scope scope) {
		this.scope = scope;
	}
}
