package net.techreadiness.persistence.dao;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.EntityDO;

import org.springframework.stereotype.Repository;

@Repository
public class EntityDAOImpl extends BaseDAOImpl<EntityDO> implements EntityDAO {
	@Override
	public EntityDO getHighestPriority(Long scopeId, EntityDAO.EntityTypeCode typeCode) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select e ");
		sb.append(" from EntityDO e, ScopeTreeDO tree  ");
		sb.append(" where e.scope = tree.ancestorScope");
		sb.append("  and tree.scope.scopeId=:scopeId ");
		sb.append("  and e.entityType.code=:typeCode ");
		sb.append(" order by tree.ancestorDepth desc");

		TypedQuery<EntityDO> query = em.createQuery(sb.toString(), EntityDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("typeCode", typeCode.toString());
		query.setMaxResults(1);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}

}
