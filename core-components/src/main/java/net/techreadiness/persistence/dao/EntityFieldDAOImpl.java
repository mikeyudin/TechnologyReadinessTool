package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.domain.EntityDO;
import net.techreadiness.persistence.domain.EntityDO_;
import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.persistence.domain.EntityFieldDO_;
import net.techreadiness.persistence.domain.EntityTypeDO_;
import net.techreadiness.persistence.domain.ScopeDO_;

import org.springframework.stereotype.Repository;

@Repository
public class EntityFieldDAOImpl extends BaseDAOImpl<EntityFieldDO> implements EntityFieldDAO {

	@Override
	public List<EntityFieldDO> findByScopePathAndType(Long scopeId, EntityDAO.EntityTypeCode typeCode) {

		// StringBuilder sb = new StringBuilder();
		// sb.append(" select ef ");
		// sb.append(" from EntityFieldDO ef");
		// sb.append(" where ef.entity = ( ");

		StringBuilder sb = new StringBuilder();
		sb.append("select e ");
		sb.append("from EntityDO e, ScopeTreeDO tree, EntityTypeDO et ");
		sb.append("where e.scope = tree.ancestorScope ");
		sb.append("	and tree.scope.scopeId =:scopeId ");
		sb.append("	and e.entityType.entityTypeId = et.entityTypeId ");
		sb.append("	and et.code =:typeCode ");
		sb.append("	order by tree.distance asc");

		TypedQuery<EntityDO> query = em.createQuery(sb.toString(), EntityDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("typeCode", typeCode.toString());
		query.setMaxResults(1);
		EntityDO entityDO = getSingleResult(query);

		sb = new StringBuilder();
		sb.append(" select ef ");
		sb.append(" from EntityFieldDO ef");
		sb.append(" where ef.entity = :entity ");

		TypedQuery<EntityFieldDO> q2 = em.createQuery(sb.toString(), EntityFieldDO.class);

		q2.setParameter("entity", entityDO);

		// query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getResultList(q2);
	}

	@Override
	public EntityFieldDO findByScopeAndTypeAndCode(Long scopeId, EntityDAO.EntityTypeCode type, String code) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ef ");
		sb.append(" from EntityFieldDO ef, ScopeTreeDO tree  ");
		sb.append(" where ef.entity.scope = tree.ancestorScope");
		sb.append("  and tree.scope.scopeId=:scopeId ");
		sb.append("  and ef.entity.entityType.code=:typeCode ");
		sb.append("  and ef.code=:code ");
		sb.append(" order by ef.displayOrder, ef.name, tree.distance asc");

		TypedQuery<EntityFieldDO> query = em.createQuery(sb.toString(), EntityFieldDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("typeCode", type.toString());
		query.setParameter("code", code);

		query.setHint("org.hibernate.cacheable", Boolean.TRUE);
		query.setMaxResults(1);

		return getSingleResult(query);
	}

	@Override
	public void setOptionListIdToNull(Long optionListId) {
		List<EntityFieldDO> entityFields = getEntityFieldsByOptionListId(optionListId);

		for (EntityFieldDO ef : entityFields) {
			ef.setOptionList(null);
			update(ef);
		}
	}

	private List<EntityFieldDO> getEntityFieldsByOptionListId(Long optionListId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ef ");
		sb.append("from EntityFieldDO ef ");
		sb.append("where ef.optionList.optionListId=:optionListId ");

		TypedQuery<EntityFieldDO> query = em.createQuery(sb.toString(), EntityFieldDO.class);
		query.setParameter("optionListId", optionListId);

		return getResultList(query);

	}

	@Override
	public EntityFieldDO getRootFieldDefinition(Long entityTypeId, String code) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityFieldDO> query = cb.createQuery(EntityFieldDO.class);
		Root<EntityFieldDO> entityField = query.from(EntityFieldDO.class);
		Join<EntityFieldDO, EntityDO> entity = entityField.join(EntityFieldDO_.entity);
		Predicate entityType = cb.equal(entity.get(EntityDO_.entityType).get(EntityTypeDO_.entityTypeId), entityTypeId);
		Predicate fieldCode = cb.equal(entityField.get(EntityFieldDO_.code), code);
		Predicate nullParent = cb.isNull(entity.get(EntityDO_.scope).get(ScopeDO_.parentScope));
		query.where(entityType, fieldCode, nullParent);
		return getSingleResult(em.createQuery(query));
	}

}
