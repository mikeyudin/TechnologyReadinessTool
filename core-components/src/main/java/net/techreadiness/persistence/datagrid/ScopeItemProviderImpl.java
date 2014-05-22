package net.techreadiness.persistence.datagrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.service.common.DataGrid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Scope("prototype")
@Repository
@Transactional(readOnly = true)
public class ScopeItemProviderImpl implements ScopeItemProvider {

	@PersistenceContext
	private EntityManager em;

	private String searchString;

	@Override
	public List<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		TypedQuery<ScopeDO> query = getQuery();
		int start = (grid.getPage() - 1) * grid.getPageSize();
		query.setFirstResult(start);
		query.setMaxResults(grid.getPageSize());
		List<ScopeDO> results = query.getResultList();

		return createScopeMap(results);

	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Map<String, String>> grid) {
		return getQuery().getResultList().size();
	}

	/**
	 * Method to get the eligible scopes with parents attached.
	 * 
	 * @return
	 */
	private TypedQuery<ScopeDO> getQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("select s ");
		sb.append("from ScopeDO s  ");
		sb.append("left join fetch s.parentScope  ");
		sb.append(" where s.code LIKE :term  ");
		sb.append(" OR s.name LIKE :term ");
		TypedQuery<ScopeDO> query = em.createQuery(sb.toString(), ScopeDO.class);

		query.setParameter("term", "%%");
		// update search string if it's not null and not empty
		if (!StringUtils.isEmpty(searchString)) {
			query.setParameter("term", "%" + searchString + "%");
		}

		return query;
	}

	/**
	 * Takes a list of ScopeDO objects and appends their attributes to a map and also attaches the parent code and name for
	 * display purposes.
	 * 
	 * @param entities
	 * @return
	 */
	private static List<Map<String, String>> createScopeMap(List<ScopeDO> entities) {

		List<Map<String, String>> maps = new ArrayList<>();
		for (ScopeDO scopeDO : entities) {
			Map<String, String> map = scopeDO.getExtAttributes();

			if (scopeDO.getParentScope() != null) {
				map.put("parentCode", scopeDO.getParentScope().getCode());
				map.put("parentName", scopeDO.getParentScope().getName());
			}

			maps.add(map);
		}
		return maps;
	}

	@Override
	public void setSearchCriteria(String searchString) {
		this.searchString = searchString;
	}
}
