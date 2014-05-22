package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ScopeTreeDO;

import org.springframework.stereotype.Repository;

@Repository
public class ScopeTreeDAOImpl extends BaseDAOImpl<ScopeTreeDO> implements ScopeTreeDAO {

	@Override
	public List<ScopeTreeDO> findByAncestorPath(String scopePath) {
		StringBuilder sb = new StringBuilder();
		sb.append("select tree ");
		sb.append("from ScopeTreeDO tree  ");
		sb.append("where tree.ancestorPath=:scopePath ");
		sb.append("order by tree.path");

		TypedQuery<ScopeTreeDO> query = em.createQuery(sb.toString(), ScopeTreeDO.class);
		query.setParameter("scopePath", scopePath);

		return getResultList(query);
	}
}
