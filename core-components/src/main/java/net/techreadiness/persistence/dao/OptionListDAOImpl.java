package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.techreadiness.persistence.domain.OptionListDO;
import net.techreadiness.persistence.domain.OptionListDO_;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeDO_;
import net.techreadiness.persistence.domain.ScopeTreeDO;
import net.techreadiness.persistence.domain.ScopeTreeDO_;

import org.springframework.stereotype.Repository;

@Repository
public class OptionListDAOImpl extends BaseDAOImpl<OptionListDO> implements OptionListDAO {

	@Override
	public List<OptionListDO> getOptionListsForScope(Long scopeId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OptionListDO> cq = cb.createQuery(OptionListDO.class);
		Root<OptionListDO> fromOptionList = cq.from(OptionListDO.class);
		Path<ScopeDO> scopePath = fromOptionList.get(OptionListDO_.scope);
		Predicate shared = cb.isTrue(fromOptionList.get(OptionListDO_.shared));

		Subquery<ScopeDO> subQuery = cq.subquery(ScopeDO.class);
		Root<ScopeTreeDO> fromScopeTree = subQuery.from(ScopeTreeDO.class);
		subQuery.select(fromScopeTree.get(ScopeTreeDO_.ancestorScope));
		Predicate scope = cb.equal(fromScopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);
		Predicate ancestorNotNull = cb.isNotNull(fromScopeTree.get(ScopeTreeDO_.ancestorScope));
		subQuery.where(scope, ancestorNotNull);

		cq.where(cb.and(cb.in(scopePath).value(subQuery), shared));

		return getResultList(em.createQuery(cq).setHint("org.hibernate.cacheable", Boolean.TRUE));
	}

	@Override
	public OptionListDO getOptionListByCode(String code) {

		StringBuilder sb = new StringBuilder();
		sb.append("select ol ");
		sb.append("from OptionListDO ol ");
		sb.append("where ol.code =:code ");

		TypedQuery<OptionListDO> query = em.createQuery(sb.toString(), OptionListDO.class);
		query.setParameter("code", code);
		query.setMaxResults(1);

		return getSingleResult(query);
	}

	@Override
	public OptionListDO getOptionListByCode(String code, Long scopeId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OptionListDO> cq = cb.createQuery(OptionListDO.class);
		Root<OptionListDO> fromOptionList = cq.from(OptionListDO.class);
		Path<ScopeDO> scopePath = fromOptionList.get(OptionListDO_.scope);
		Predicate shared = cb.isTrue(fromOptionList.get(OptionListDO_.shared));
		Predicate codeClause = cb.equal(fromOptionList.get(OptionListDO_.code), code);

		Subquery<ScopeDO> subQuery = cq.subquery(ScopeDO.class);
		Root<ScopeTreeDO> fromScopeTree = subQuery.from(ScopeTreeDO.class);
		subQuery.select(fromScopeTree.get(ScopeTreeDO_.ancestorScope));
		Predicate scope = cb.equal(fromScopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);
		Predicate ancestorNotNull = cb.isNotNull(fromScopeTree.get(ScopeTreeDO_.ancestorScope));
		subQuery.where(scope, ancestorNotNull);

		cq.where(cb.and(cb.in(scopePath).value(subQuery), shared, codeClause));
		TypedQuery<OptionListDO> query = em.createQuery(cq);
		query.setMaxResults(1);
		return getSingleResult(query);
	}

	@Override
	public OptionListDO getRootScopeRegExOptionList(Long scopeId) {

		StringBuilder sb = new StringBuilder();
		sb.append("select ol ");
		sb.append("from OptionListDO ol, ScopeTreeDO tree ");
		sb.append("where ol.scope.scopeId = tree.ancestorScope.scopeId ");
		sb.append("and tree.scope.scopeId =:scopeId ");
		sb.append("and ol.code='regex' ");
		sb.append("order by tree.depth desc ");

		TypedQuery<OptionListDO> query = em.createQuery(sb.toString(), OptionListDO.class);
		query.setParameter("scopeId", scopeId);
		query.setMaxResults(1);

		return getSingleResult(query);
	}

}
