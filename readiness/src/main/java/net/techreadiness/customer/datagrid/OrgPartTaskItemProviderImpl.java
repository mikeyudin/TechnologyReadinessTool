package net.techreadiness.customer.datagrid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.OrgPartDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgDO_;
import net.techreadiness.persistence.domain.OrgPartDO;
import net.techreadiness.persistence.domain.OrgPartDO_;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeDO_;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
@org.springframework.context.annotation.Scope("prototype")
@Transactional(readOnly = true)
public class OrgPartTaskItemProviderImpl implements OrgPartTaskItemProvider {
	@PersistenceContext
	private EntityManager em;
	private Collection<Org> orgs;
	private Scope scope;

	@Inject
	private OrgDAO orgDAO;
	@Inject
	private OrgPartDAO orgPartDAO;
	@Inject
	private ScopeDAO scopeDAO;

	@Override
	public int getTotalNumberOfItems(DataGrid<Map<String, String>> grid) {
		TypedQuery<OrgPartDO> query = getQuery();

		return query.getResultList().size();
	}

	@Override
	public List<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		int start = (grid.getPage() - 1) * grid.getPageSize();
		TypedQuery<OrgPartDO> query = getQuery();
		query.setFirstResult(start);
		query.setMaxResults(grid.getPageSize());

		List<OrgPartDO> orgParts = query.getResultList();

		List<Map<String, String>> list = Lists.newArrayList();

		ScopeDO scopeDO = scopeDAO.getById(scope.getScopeId());

		boolean allowAssignments = scopeDO.getScopeType().isAllowOrgPart();
		if (!allowAssignments) {
			throw new IllegalStateException("The scope does not allow organizations participations.");
		}

		for (Org org : orgs) {

			boolean found = false;
			Map<String, String> map = Maps.newHashMap();

			map.put("orgId", org.getOrgId().toString());
			map.put("orgName", org.getName());
			map.put("orgCode", org.getCode());

			if (allowAssignments) {
				map.put("assignmentAllowed", "true");
			} else {
				map.put("assignmentAllowed", "false");
			}

			OrgDO parentOrg = orgDAO.getParent(org.getOrgId());
			if (parentOrg != null) {
				try {
					OrgPartDO parentOrgPart = orgPartDAO.findOrgPart(parentOrg.getOrgId(), scope.getScopeId());
					if (parentOrgPart == null) {
						map.put("assignmentAllowed", "false");
					}
				} catch (EmptyResultDataAccessException e) {
					map.put("assignmentAllowed", "false");
				}
			}

			for (OrgPartDO orgPart : orgParts) {
				if (orgPart.getOrg().getOrgId().equals(org.getOrgId())) {
					found = true;

					map.putAll(orgPart.getAsMap());
					map.putAll(orgPart.getExtAttributes());
					map.put("assigned", "true");
					break;
				}
			}

			if (!found) {
				OrgPartDO newOrgPartDO = new OrgPartDO();
				OrgDO orgDO = orgDAO.getById(org.getOrgId());
				newOrgPartDO.setOrg(orgDO);
				newOrgPartDO.setScope(scopeDO);

				map.putAll(newOrgPartDO.getAsMap());
				map.put("assigned", "false");
			}

			list.add(map);
		}
		return list;
	}

	private TypedQuery<OrgPartDO> getQuery() {
		CriteriaQuery<OrgPartDO> query = em.getCriteriaBuilder().createQuery(OrgPartDO.class);
		Root<OrgPartDO> orgPart = query.from(OrgPartDO.class);
		orgPart.fetch(OrgPartDO_.org);
		Predicate scopeClause = em.getCriteriaBuilder().equal(orgPart.get(OrgPartDO_.scope).get(ScopeDO_.scopeId),
				scope.getScopeId());
		In<Long> inOrg = em.getCriteriaBuilder().in(orgPart.get(OrgPartDO_.org).get(OrgDO_.orgId));
		if (orgs.isEmpty()) {
			throw new IllegalStateException("No organizations are selected.");
		}
		for (Org org : orgs) {
			inOrg.value(org.getOrgId());
		}
		query.where(scopeClause, inOrg);

		return em.createQuery(query);
	}

	@Override
	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public void setOrgs(Collection<Org> orgs) {
		this.orgs = orgs;
	}
}
