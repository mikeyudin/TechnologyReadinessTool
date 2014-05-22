package net.techreadiness.plugin.action.org;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("prototype")
@Transactional(readOnly = true)
public class ReadyOrgItemProviderImpl extends DataGridItemProviderImpl<Org> implements ReadyOrgItemProvider {

	static final String SCOPE_PATH = "scopePath";

	@Inject
	private ScopeDAO scopeDAO;
	@Inject
	private OrgDAO orgDao;

	@Inject
	private CriteriaQuery<OrgDO> criteriaQuery;

	@Inject
	private MappingService mappingService;

	@Inject
	private ServiceContext serviceContext;

	private Long scopeId;

	@Override
	public Collection<Org> getPage(DataGrid<Org> grid) {
		Criteria criteria = createCriteria(grid, "SHOW_ALL");

		ScopeDO scope;

		criteriaQuery.setFullTextSearchColumns(new String[] { "code", "name" });

		StringBuilder sb = new StringBuilder();
		if (grid.getFilters().get("SHOW_ALL").isEmpty()) {
			scope = scopeDAO.getScopeForOrgParts(scopeId);

			sb.append("select org.* ");
			sb.append("from org ");
			sb.append("join org_tree tree on org.org_id = tree.org_id ");
			sb.append("join org_part op on op.org_id = org.org_id ");
			sb.append("join org_type ot on ot.org_type_id = org.org_type_id ");
			sb.append("where op.scope_id = :scopeId ");
			sb.append("and tree.ancestor_org_id = :orgId ");
			sb.append("and ot.allow_device = 1 ");
			criteriaQuery.setBaseSubSelect(sb.toString());
		} else {
			scope = scopeDAO.getScopeForOrgs(scopeId);
			if (scope == null) {
				throw new IllegalStateException("The scope selected does not support organizations.");
			}
			sb.append("SELECT org.* ");
			sb.append("FROM   org ");
			sb.append("JOIN   org_tree tree ON org.org_id = tree.org_id ");
			sb.append("WHERE  org.scope_id = :scopeId AND tree.ancestor_org_id = :orgId ");
			criteriaQuery.setBaseSubSelect(sb.toString());
		}

		criteria.getParameters().put("scopeId", scope.getScopeId());
		criteria.getParameters().put("orgId", serviceContext.getOrgId());
		QueryResult<OrgDO> result = criteriaQuery.getData(criteria, OrgDO.class);

		setTotalNumberOfItems(result.getTotalRowCount());

		Collection<OrgDO> orgDOList = result.getRows();

		return mappingService.getMapper().mapAsList(orgDOList, Org.class);
	}

	protected Map<String, String> convertToMap(OrgDO org) {
		Map<String, String> map = mappingService.map(org).getAsMap();

		map.put("orgTypeName", org.getOrgType().getName());
		map.put("hasDevices", String.valueOf(org.getOrgType().isAllowDevice()));
		if (org.getParentOrg() != null) {
			map.put("parentOrgName", org.getParentOrg().getName());
		}
		return map;
	}

	@Override
	public void setScope(Long scopeId) {
		this.scopeId = scopeId;
	}

	@Override
	public Org getObjectForKey(String rowKey) {
		Long orgId = Long.valueOf(rowKey);
		OrgDO org = orgDao.getById(orgId);
		return mappingService.getMapper().map(org, Org.class);
	}
}
