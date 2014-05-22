package net.techreadiness.persistence.datagrid;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProvider;

public abstract class AbstractCriteriaItemProvider<T> implements DataGridItemProvider<T> {
	@PersistenceContext
	protected EntityManager em;

	protected abstract CriteriaQuery<T> getCriteria(DataGrid<T> grid);

	@Override
	public int getTotalNumberOfItems(DataGrid<T> grid) {
		return em.createQuery(getCriteria(grid)).getResultList().size();
	}

	@Override
	public List<T> getPage(DataGrid<T> grid) {
		int start = (grid.getPage() - 1) * grid.getPageSize();
		TypedQuery<T> query = em.createQuery(getCriteria(grid));
		query.setFirstResult(start);
		query.setMaxResults(grid.getPageSize());
		return query.getResultList();
	}

}
