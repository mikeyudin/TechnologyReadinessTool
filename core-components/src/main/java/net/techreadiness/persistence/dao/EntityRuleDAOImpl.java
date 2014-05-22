package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.domain.EntityRuleDO;

import org.springframework.stereotype.Repository;

@Repository
public class EntityRuleDAOImpl extends BaseDAOImpl<EntityRuleDO> implements EntityRuleDAO {

	@Override
	public List<EntityRuleDO> findValidationRules(Long scopeId, EntityTypeCode entityType) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select er ");
		sb.append(" from EntityRuleDO er, ScopeTreeDO tree  ");
		sb.append(" where er.entity.scope = tree.ancestorScope");
		sb.append("  and tree.scope.scopeId=:scopeId ");
		sb.append("  and er.entity.entityType.code=:typeCode ");
		sb.append("  and er.type='validation' ");
		sb.append("  and (er.disabled is null or er.disabled != 1) ");

		TypedQuery<EntityRuleDO> query = em.createQuery(sb.toString(), EntityRuleDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("typeCode", entityType.toString());

		return getResultList(query);
	}

	@Override
	public List<EntityRuleDO> findViewRules(Long scopeId, EntityTypeCode entityType) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select er ");
		sb.append(" from EntityRuleDO er, ScopeTreeDO tree  ");
		sb.append(" where er.entity.scope = tree.ancestorScope");
		sb.append("  and tree.scope.scopeId=:scopeId ");
		sb.append("  and er.entity.entityType.code=:typeCode ");
		sb.append("  and er.type='view' ");
		sb.append("  and (er.disabled is null or er.disabled != 1) ");

		TypedQuery<EntityRuleDO> query = em.createQuery(sb.toString(), EntityRuleDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("typeCode", entityType.toString());

		return getResultList(query);
	}
}
