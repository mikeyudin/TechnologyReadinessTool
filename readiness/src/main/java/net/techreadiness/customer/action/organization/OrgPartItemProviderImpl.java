package net.techreadiness.customer.action.organization;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.OrgPartDO;
import net.techreadiness.service.common.DataGrid;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
@org.springframework.context.annotation.Scope("prototype")
@Transactional(readOnly = true)
public class OrgPartItemProviderImpl implements OrgPartItemProvider {

	private boolean ignoreScope;
	private Long scopeId;
	private Long orgId;

	@PersistenceContext
	protected EntityManager em;

	@Override
	public List<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		if (grid.getFilters().containsKey("currentScope")) {
			ignoreScope = true;
		}
		List<Map<String, String>> maps = Lists.newArrayList();

		List<OrgPartDO> results = getQuery();
		for (OrgPartDO orgPart : results) {
			Map<String, String> row = Maps.newHashMap();
			row.putAll(orgPart.getAsMap());
			row.putAll(orgPart.getExtAttributes());
			row.put("orgId", orgPart.getOrg().getOrgId().toString());
			row.put("scopeName", orgPart.getScope().getName());
			row.put("participating", "Yes");
			maps.add(row);
		}

		return maps;
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Map<String, String>> grid) {
		return getQuery().size();
	}

	private List<OrgPartDO> getQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("select op ");
		sb.append("from OrgPartDO op ");
		sb.append("where op.org.orgId =:orgId ");
		if (!ignoreScope) {
			sb.append("and op.scope.scopeId =:scopeId ");
			sb.append("and op.scope.scopeId in (select st.ancestorScope.scopeId from ScopeTreeDO st where st.scope.scopeId =:scopeId) ");
		}
		TypedQuery<OrgPartDO> query = em.createQuery(sb.toString(), OrgPartDO.class);
		query.setParameter("orgId", orgId);
		if (!ignoreScope) {
			query.setParameter("scopeId", scopeId);
		}
		return query.getResultList();
	}

	@Override
	public void setIgnoreScope(boolean ignoreScope) {
		this.ignoreScope = ignoreScope;
	}

	@Override
	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	@Override
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
