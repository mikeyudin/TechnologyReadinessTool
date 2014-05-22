package net.techreadiness.persistence.datagrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.service.common.DataGrid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Scope("prototype")
@Repository
@Transactional(readOnly = true)
public class RoleItemProviderImpl implements RoleItemProvider {

	@PersistenceContext
	private EntityManager em;

	private String searchString;
	private Long scopeId;

	@Override
	public List<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		int start = (grid.getPage() - 1) * grid.getPageSize();
		TypedQuery<RoleDO> query = getQuery();
		query.setFirstResult(start);
		query.setMaxResults(grid.getPageSize());
		return createRoleMap(query.getResultList());
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Map<String, String>> grid) {
		return getQuery().getResultList().size();
	}

	private TypedQuery<RoleDO> getQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("select r ");
		sb.append("from RoleDO r,  ");
		sb.append("   ScopeTreeDO st");
		sb.append(" where r.name LIKE :name  ");
		sb.append("  and  st.scope.scopeId = :scopeid ");
		sb.append("  and  st.ancestorScope.scopeId = r.scope.scopeId");
		sb.append("  order by r.scope.name, r.category, r.displayOrder, r.name");
		TypedQuery<RoleDO> query = em.createQuery(sb.toString(), RoleDO.class);

		query.setParameter("name", "%%");
		query.setParameter("scopeid", scopeId);
		// update search string if it's not null and not empty
		if (!(searchString == null)) {
			if (!searchString.isEmpty()) {
				query.setParameter("name", "%" + searchString + "%");
			}
		}

		return query;
	}

	/**
	 * Takes a list of RoleDO objects and appends their attributes to a map.
	 * 
	 * @param entities
	 * @return
	 */
	private static List<Map<String, String>> createRoleMap(List<RoleDO> entities) {

		List<Map<String, String>> maps = new ArrayList<>();
		for (RoleDO roleDO : entities) {
			Map<String, String> map = roleDO.getAsMap();

			if (roleDO.getScope() != null) {
				map.put("scopeId", String.valueOf(roleDO.getScope().getScopeId()));
				map.put("scopeName", roleDO.getScope().getName());
			}

			maps.add(map);
		}
		return maps;
	}

	@Override
	public void setSearchCriteria(String searchString) {
		this.searchString = searchString;
	}

	@Override
	public void setScope(Long scopeId) {
		this.scopeId = scopeId;
	}
}
