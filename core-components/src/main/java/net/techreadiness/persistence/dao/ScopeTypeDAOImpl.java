package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ScopeTypeDO;

import org.springframework.stereotype.Repository;

@Repository
public class ScopeTypeDAOImpl extends BaseDAOImpl<ScopeTypeDO> implements ScopeTypeDAO {

	@Override
	public List<ScopeTypeDO> findChildTypesByScopeId(Long scopeId) {
		StringBuilder qry = new StringBuilder();
		TypedQuery<ScopeTypeDO> query = null;
		if (scopeId == null) {
			qry.append(" select st ");
			qry.append(" from ScopeTypeDO st");
			qry.append(" where");
			qry.append("   st.parentScopeType.scopeTypeId is null");
			query = em.createQuery(qry.toString(), ScopeTypeDO.class);
		} else {
			qry.append(" select st ");
			qry.append(" from ScopeDO s,");
			qry.append("   ScopeTypeDO st");
			qry.append(" where");
			qry.append("   s.scopeType.scopeTypeId = st.parentScopeType.scopeTypeId");
			qry.append("   and s.scopeId = :scopeId");
			query = em.createQuery(qry.toString(), ScopeTypeDO.class);
			query.setParameter("scopeId", scopeId);
		}

		return getResultList(query);
	}

}
