package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.techreadiness.persistence.domain.EntityDO;
import net.techreadiness.persistence.domain.EntityDO_;
import net.techreadiness.persistence.domain.EntityTypeDO;
import net.techreadiness.persistence.domain.ScopeDO_;

import org.springframework.stereotype.Repository;

@Repository
public class EntityTypeDAOImpl extends BaseDAOImpl<EntityTypeDO> implements EntityTypeDAO {

	@Override
	public List<EntityTypeDO> getEntitiesNotDefinedForScope(Long scopeId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityTypeDO> cq = cb.createQuery(EntityTypeDO.class);
		Root<EntityTypeDO> root = cq.from(EntityTypeDO.class);

		Subquery<EntityTypeDO> subQuery = cq.subquery(EntityTypeDO.class);
		Root<EntityDO> fromEntity = subQuery.from(EntityDO.class);
		subQuery.select(fromEntity.get(EntityDO_.entityType));
		Predicate scope = cb.equal(fromEntity.get(EntityDO_.scope).get(ScopeDO_.scopeId), scopeId);
		subQuery.where(scope);

		cq.where(cb.not(cb.in(root).value(subQuery)));

		return em.createQuery(cq).setHint("org.hibernate.cacheable", Boolean.TRUE).getResultList();
	}
}
