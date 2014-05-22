package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeDO_;
import net.techreadiness.persistence.domain.ScopeTreeDO;
import net.techreadiness.persistence.domain.ScopeTreeDO_;
import net.techreadiness.persistence.domain.ScopeTypeDO;
import net.techreadiness.persistence.domain.ScopeTypeDO_;

import org.springframework.stereotype.Repository;

@Repository
public class ScopeDAOImpl extends BaseDAOImpl<ScopeDO> implements ScopeDAO {

	@Override
	public List<ScopeDO> findByParentPath(String scopePath) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScopeDO> cq = cb.createQuery(ScopeDO.class);
		Root<ScopeTreeDO> root = cq.from(ScopeTreeDO.class);

		Join<ScopeTreeDO, ScopeDO> j = root.join(ScopeTreeDO_.scope);
		cq.select(j);
		Predicate p = cb.equal(root.get(ScopeTreeDO_.ancestorPath), scopePath);

		cq.where(p);
		cq.orderBy(cb.asc(root.get(ScopeTreeDO_.ancestorPath)));

		TypedQuery<ScopeDO> query = em.createQuery(cq);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getResultList(query);
	}

	@Override
	public ScopeDO getByScopePath(String scopePath) {
		TypedQuery<ScopeDO> query = em.createQuery("select s from ScopeDO s where s.path = :scopePath", ScopeDO.class);
		query.setParameter("scopePath", scopePath);

		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}

	@Override
	public List<ScopeDO> getScopesWithNoParent() {
		TypedQuery<ScopeDO> query = em.createQuery("select s from ScopeDO s where s.parentScope = null", ScopeDO.class);
		return getResultList(query);
	}

	@Override
	public List<ScopeDO> getAppRootScopes() {
		TypedQuery<ScopeDO> query = em.createQuery("select st.scope from ScopeTreeDO st where st.depth=2 and distance=0",
				ScopeDO.class);
		return getResultList(query);
	}

	private CriteriaQuery<ScopeDO> getScopeCriteria(Long scopeId, SingularAttribute<ScopeTypeDO, Boolean> scopeTypeAttribute) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScopeDO> cq = cb.createQuery(ScopeDO.class);
		Root<ScopeDO> root = cq.from(ScopeDO.class);

		Join<ScopeDO, ScopeTypeDO> scopeType = root.join(ScopeDO_.scopeType);
		Join<ScopeDO, ScopeTreeDO> scopeTree = root.join(ScopeDO_.ancestorScopeTrees);
		Predicate typeClause = cb.equal(scopeType.get(scopeTypeAttribute), Short.valueOf("1"));
		scopeTree.get(ScopeTreeDO_.scope);
		Predicate pathClause = cb.equal(scopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);
		Predicate notRoot = cb.isNotNull(root.get(ScopeDO_.parentScope));
		cq.where(notRoot, typeClause, pathClause);
		return cq;
	}

	@Override
	public ScopeDO getTopLevelParent(Long scopeId) {
		TypedQuery<ScopeDO> query = em.createQuery(
				"select tree.scope from ScopeTreeDO tree where tree.ancestorDepth=2 and tree.scope.scopeId=:scopeId",
				ScopeDO.class);
		query.setParameter("scopeId", scopeId);
		return getSingleResult(query);
	}

	@Override
	public List<ScopeDO> getDescendantScopes(Long scopeId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select s ");
		sb.append("from ScopeDO s, ScopeTreeDO tree ");
		sb.append("where s.scopeId = tree.scope.scopeId ");
		sb.append("and tree.ancestorScope.scopeId =:scopeId ");

		TypedQuery<ScopeDO> query = em.createQuery(sb.toString(), ScopeDO.class);
		query.setParameter("scopeId", scopeId);

		return query.getResultList();
	}

	@Override
	public ScopeDO getScopeForUsers(Long scopeId) {
		CriteriaQuery<ScopeDO> cq = getScopeCriteria(scopeId, ScopeTypeDO_.allowUser);
		TypedQuery<ScopeDO> query = em.createQuery(cq);
		return getSingleResult(query);
	}

	@Override
	public Collection<ScopeDO> getAncestorsAndChildren(Long scopeId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct s from ScopeTreeDO st ");
		sb.append("join st.scope s ");
		sb.append("left join s.parentScope ");
		sb.append("where st.ancestorScope.scopeId in ( ");
		sb.append("	select ancestorScope.scopeId ");
		sb.append("	from ScopeTreeDO sts ");
		sb.append("	join sts.scope scope ");
		sb.append("	join sts.ancestorScope ancestorScope ");
		sb.append("	where scope.scopeId = :scopeId) ");
		sb.append("and st.distance <= 1 ");
		TypedQuery<ScopeDO> query = em.createQuery(sb.toString(), ScopeDO.class);
		query.setParameter("scopeId", scopeId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);
		return query.getResultList();
	}

	/**
	 * Given a scope point in the tree as identified by id, get:
	 * <ul>
	 * <li>the scope node itself</li>
	 * <li>any ancestor of that node</li>
	 * <li>any descendant of that node</li>
	 * </ul>
	 * 
	 * @param scopeId
	 *            The scope id
	 * @return The collection of scopes
	 */
	@Override
	public Collection<ScopeDO> getAncestorsAndDescendants(Long scopeId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct s from ScopeTreeDO st ");
		sb.append("join st.scope s ");
		sb.append("left join s.parentScope ");
		sb.append("where st.ancestorScope.scopeId in ( ");
		sb.append("	select ancestorScope.scopeId ");
		sb.append("	from ScopeTreeDO sts ");
		sb.append("	join sts.scope scope ");
		sb.append("	join sts.ancestorScope ancestorScope ");
		sb.append("	where scope.scopeId = :scopeId) ");
		sb.append("and ((st.distance <= 0) ");
		sb.append(" or (st.ancestorScope.scopeId = :scopeId)) ");
		TypedQuery<ScopeDO> query = em.createQuery(sb.toString(), ScopeDO.class);
		query.setParameter("scopeId", scopeId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);
		return query.getResultList();
	}

	@Override
	public Collection<ScopeDO> findParentsOfScope(Long scopeId) {
		TypedQuery<ScopeDO> query = em.createQuery(
				"select tree.ancestorScope from ScopeTreeDO tree where tree.scope.scopeId = :scopeId", ScopeDO.class);
		query.setParameter("scopeId", scopeId);
		return query.getResultList();
	}

	@Override
	public ScopeDO getScopeForOrgs(Long scopeId) {
		CriteriaQuery<ScopeDO> cq = getScopeCriteria(scopeId, ScopeTypeDO_.allowOrg);
		TypedQuery<ScopeDO> query = em.createQuery(cq);
		return getSingleResult(query);
	}

	@Override
	public List<ScopeDO> getChildScopesOfRoot() {
		TypedQuery<ScopeDO> query = em.createQuery("select s from ScopeDO s where s.parentScope.scopeId = 1", ScopeDO.class);
		return getResultList(query);
	}

	@Override
	public ScopeDO getScopeForOrgParts(Long scopeId) {
		CriteriaQuery<ScopeDO> cq = getScopeCriteria(scopeId, ScopeTypeDO_.allowOrgPart);
		TypedQuery<ScopeDO> query = em.createQuery(cq);
		return getSingleResult(query);
	}

	@Override
	public List<ScopeDO> findDescendantScopes(Long scopeId, boolean allDescendants) {
		TypedQuery<ScopeDO> query = em.createQuery("select tree.scope from ScopeTreeDO tree "
				+ "where tree.ancestorScope.scopeId = :scopeId "
				+ (allDescendants ? " and tree.distance > 0 " : " and tree.distance = 1 "), ScopeDO.class);
		query.setParameter("scopeId", scopeId);
		return query.getResultList();
	}
}
