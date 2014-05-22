package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.CustomTextDO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class CustomTextDAOImpl extends BaseDAOImpl<CustomTextDO> implements CustomTextDAO {

	@Override
	public List<CustomTextDO> findAllCustomTextForScope(Long scopeId) {

		String query = new StringBuilder().append(" select text                                ")
				.append(" from CustomTextDO text,                    ")
				.append("      ScopeDO scope,                        ")
				.append("      ScopeTreeDO scopeTree                 ")
				.append(" where scope.scopeId = :scopeId             ")
				.append("   and scopeTree.scope = scope              ")
				.append("   and scopeTree.ancestorScope = text.scope ")
				.append(" order by scopeTree.distance desc           ").toString();

		return em.createQuery(query, CustomTextDO.class).setParameter("scopeId", scopeId)
				.setHint("org.hibernate.cacheable", Boolean.TRUE).getResultList();
	}

	@Override
	public CustomTextDO getCustomTextByScopeAndCode(Long scopeId, String code) {
		String query = new StringBuilder().append(" select text                                ")
				.append(" from CustomTextDO text,                    ")
				.append("      ScopeDO scope,                        ")
				.append("      ScopeTreeDO scopeTree                 ")
				.append(" where scope.scopeId = :scopeId             ")
				.append("   and scopeTree.scope = scope              ")
				.append("   and scopeTree.ancestorScope = text.scope ")
				.append("   and text.code = :code                    ")
				.append(" order by scopeTree.distance                ")

				.toString();
		try {
			return em.createQuery(query, CustomTextDO.class).setParameter("scopeId", scopeId).setParameter("code", code)
					.setMaxResults(1).setHint("org.hibernate.cacheable", Boolean.TRUE).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Collection<CustomTextDO> findTextForAncestorsAndDescendants(Long scopeId, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append("select text from CustomTextDO text ");
		sb.append("join text.scope s ");
		sb.append("join s.scopeTrees st ");
		sb.append("where (text.scope.scopeId in (select s.scope.scopeId from ScopeTreeDO s where s.ancestorScope.scopeId = :scopeId) ");
		sb.append("or text.scope.scopeId in (select s.ancestorScope.scopeId from ScopeTreeDO s where s.scope.scopeId = :scopeId)) ");
		sb.append("and s.scopeId = st.ancestorScope.scopeId ");
		if (StringUtils.isNotBlank(key)) {
			sb.append("and text.code = :key ");
		}
		sb.append("order by st.depth desc, text.code ");
		TypedQuery<CustomTextDO> query = em.createQuery(sb.toString(), CustomTextDO.class);
		query.setParameter("scopeId", scopeId);
		if (StringUtils.isNotBlank(key)) {
			query.setParameter("key", key);
		}
		return getResultList(query);
	}
}
