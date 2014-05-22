package net.techreadiness.persistence.dao;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ViewDefDO;

import org.springframework.stereotype.Repository;

@Repository
public class ViewDefDAOImpl extends BaseDAOImpl<ViewDefDO> implements ViewDefDAO {
	@Override
	public ViewDefDO getByEntityTypeAndScopePath(String entityTypeCode, Long scopeId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select vd ");
		sb.append(" from ViewDefDO vd, ScopeTreeDO tree  ");
		sb.append(" where vd.scope = tree.ancestorScope");
		sb.append("  and tree.scope.scopeId=:scopeId ");
		sb.append("  and vd.viewDefType.entityType.code=:entityTypeCode ");
		sb.append("  and vd.viewDefType.defaultView=true ");
		sb.append(" order by tree.distance");

		TypedQuery<ViewDefDO> query = em.createQuery(sb.toString(), ViewDefDO.class);
		query.setParameter("entityTypeCode", entityTypeCode);
		query.setParameter("scopeId", scopeId);
		query.setMaxResults(1);

		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}

	@Override
	public ViewDefDO getByViewTypeAndScopePath(String viewTypeCode, Long scopeId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select vd ");
		sb.append(" from ViewDefDO vd, ScopeTreeDO tree  ");
		sb.append(" where vd.scope = tree.ancestorScope");
		sb.append("  and tree.scope.scopeId=:scopeId ");
		sb.append("  and vd.viewDefType.code=:viewTypeCode ");
		sb.append(" order by tree.distance");

		TypedQuery<ViewDefDO> query = em.createQuery(sb.toString(), ViewDefDO.class);
		query.setParameter("viewTypeCode", viewTypeCode);
		query.setParameter("scopeId", scopeId);
		query.setMaxResults(1);

		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}
}
