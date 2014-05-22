package net.techreadiness.persistence.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.PermissionDO;

import org.springframework.stereotype.Repository;

@Repository
public class PermissionDAOImpl extends BaseDAOImpl<PermissionDO> implements PermissionDAO {

	@Override
	public List<PermissionDO> findAllAncestorPermissions(Long scopeId) {
		if (null == scopeId) {
			return Collections.emptyList();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct p ");
		sb.append(" from PermissionDO p, ScopeTreeDO st ");
		sb.append(" where st.ancestorScope.scopeId = p.scope.scopeId");
		sb.append("  and st.scope.scopeId = :scopeid");
		sb.append("  order by p.displayOrder, p.name");

		TypedQuery<PermissionDO> query = em.createQuery(sb.toString(), PermissionDO.class);
		query.setParameter("scopeid", scopeId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getResultList(query);
	}

}
