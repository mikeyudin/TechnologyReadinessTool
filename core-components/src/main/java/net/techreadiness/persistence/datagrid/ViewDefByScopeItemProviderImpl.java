package net.techreadiness.persistence.datagrid;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ViewDefDO;
import net.techreadiness.service.common.DataGrid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Scope("prototype")
@Repository
@Transactional(readOnly = true)
public class ViewDefByScopeItemProviderImpl implements ViewDefByScopeItemProvider {

	@PersistenceContext
	protected EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	private Long scopeId;

	public ViewDefByScopeItemProviderImpl() {
		super();
	}

	@Override
	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	@Override
	@Transactional(readOnly = true)
	public int getTotalNumberOfItems(DataGrid<ViewDefDO> dataGrid) {
		if (scopeId == null) {
			return 0;
		}

		TypedQuery<Long> query = getFindByScopeIdQueryCount(scopeId);

		Long count = query.getSingleResult();

		return count.intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ViewDefDO> getPage(DataGrid<ViewDefDO> dataGrid) {

		int start = (dataGrid.getPage() - 1) * dataGrid.getPageSize();
		if (scopeId == null) {
			return Lists.newArrayList();
		}

		TypedQuery<ViewDefDO> query = getFindByScopeIdQuery(scopeId);
		if (dataGrid.isPaging()) {
			query.setFirstResult(start);
			query.setMaxResults(dataGrid.getPageSize());
		}

		return Lists.<ViewDefDO> newArrayList(query.getResultList());
	}

	private TypedQuery<ViewDefDO> getFindByScopeIdQuery(final Long scopeId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct vd ");
		sb.append(" from ViewDefDO vd");
		sb.append("   join vd.scope as s");
		sb.append("   join vd.viewDefType as vdt");
		sb.append("   join s.scopeTrees as tree");
		sb.append("   join s.ancestorScopeTrees as aTree");
		sb.append(" where ");
		sb.append("  tree.ancestorScope.scopeId=:scopeId ");
		sb.append(" or aTree.scope.scopeId=:scopeId ");
		TypedQuery<ViewDefDO> query = em.createQuery(sb.toString(), ViewDefDO.class);

		query.setParameter("scopeId", scopeId);
		return query;
	}

	private TypedQuery<Long> getFindByScopeIdQueryCount(final Long scopeId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(distinct vd) ");
		sb.append(" from ViewDefDO vd");
		sb.append("   join vd.scope as s");
		sb.append("   join vd.viewDefType as vdt");
		sb.append("   join s.scopeTrees as tree");
		sb.append("   join s.ancestorScopeTrees as aTree");
		sb.append(" where ");
		sb.append("  tree.ancestorScope.scopeId=:scopeId ");
		sb.append(" or aTree.scope.scopeId=:scopeId ");

		TypedQuery<Long> query = em.createQuery(sb.toString(), Long.class);
		query.setParameter("scopeId", scopeId);
		return query;
	}

}
