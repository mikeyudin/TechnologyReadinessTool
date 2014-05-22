package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgDO_;
import net.techreadiness.persistence.domain.OrgPartDO;
import net.techreadiness.persistence.domain.OrgPartDO_;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeDO_;

import org.springframework.stereotype.Repository;

@Repository
public class OrgPartDAOImpl extends BaseDAOImpl<OrgPartDO> implements OrgPartDAO {

	@Override
	public OrgPartDO findOrgPart(Long orgId, Long scopeId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrgPartDO> cq = cb.createQuery(OrgPartDO.class);
		Root<OrgPartDO> root = cq.from(OrgPartDO.class);

		Join<OrgPartDO, OrgDO> j = root.join(OrgPartDO_.org);
		Predicate orgClause = cb.equal(j.get(OrgDO_.orgId), orgId);

		Join<OrgPartDO, ScopeDO> joinScope = root.join(OrgPartDO_.scope);
		Predicate scopeClause = cb.equal(joinScope.get(ScopeDO_.scopeId), scopeId);

		cq.where(orgClause, scopeClause);

		return getSingleResult(em.createQuery(cq));
	}

	@Override
	public List<OrgDO> findOrgsPartForScope(Long scopeId, Long orgId) {
		String query = "  select org from OrgDO org               " + "    join org.orgParts orgPart             "
				+ "    join org.orgTrees tree                " + "  where orgPart.scope.scopeId = :scopeId  "
				+ "    and tree.ancestorOrg.orgId = :orgId   " + "  order by org.name asc , org.code asc    ";
		return em.createQuery(query, OrgDO.class).setParameter("scopeId", scopeId).setParameter("orgId", orgId)
				.getResultList();
	}

	@Override
	public List<OrgPartDO> findOrgPartsByScope(Long scopeId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrgPartDO> cq = cb.createQuery(OrgPartDO.class);
		Root<OrgPartDO> root = cq.from(OrgPartDO.class);

		Join<OrgPartDO, ScopeDO> joinScope = root.join(OrgPartDO_.scope);
		Predicate scopeClause = cb.equal(joinScope.get(ScopeDO_.scopeId), scopeId);

		cq.where(scopeClause);
		cq.orderBy(cb.asc(root.get(OrgPartDO_.org).get(OrgDO_.name)), cb.asc(root.get(OrgPartDO_.org).get(OrgDO_.code)));

		return getResultList(em.createQuery(cq));
		// return em.createQuery(cq).getResultList();
	}

	@Override
	public List<OrgPartDO> findOrgPartsForOrg(Long orgId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrgPartDO> criteria = cb.createQuery(OrgPartDO.class);
		Root<OrgPartDO> root = criteria.from(OrgPartDO.class);
		criteria.where(cb.equal(root.get(OrgPartDO_.org).get(OrgDO_.orgId), orgId));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public List<OrgDO> findOrgPartsForScope(Long scopeId, Long orgId, String term) {

		String query = "  select org from OrgDO org              " + "    join org.orgParts orgPart            "
				+ "    join org.orgTrees tree               " + "  where orgPart.scope.scopeId = :scopeId "
				+ "    and tree.ancestorOrg.orgId = :orgId  " + "    and (org.name like :term             "
				+ "         or org.code like :term)         " + "  order by org.name asc , org.code asc   ";

		return em.createQuery(query, OrgDO.class).setParameter("scopeId", scopeId).setParameter("orgId", orgId)
				.setParameter("term", "%" + term + "%").getResultList();
	}

	@Override
	public void createOrgPartsForDescendants(Long scopeId, Long orgId) {

		String sql = "insert into OrgPartDO (org, scope) "
				+ "  select ot.org, ( select s from ScopeDO s where s.scopeId = :scopeId) from OrgTreeDO ot "
				+ "    where ancestorOrg.orgId = :orgId and distance > 0 and " + "    ot.org not in "
				+ "(select op.org from OrgPartDO op, OrgTreeDO tree where "
				+ "op.org = tree.org and op.scope.scopeId = :scopeId and "
				+ "tree.ancestorOrg.orgId = :orgId and tree.distance > 0 )";

		Query query = em.createQuery(sql);
		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);

		query.executeUpdate();
	}

	@Override
	public List<OrgPartDO> findParticipatingChildOrgParts(Long scopeId, Long orgId) {
		TypedQuery<OrgPartDO> query = em.createQuery("select orgPart from OrgPartDO orgPart join orgPart.org org "
				+ "where org.parentOrg.orgId = :orgId and orgPart.scope.scopeId = :scopeId", OrgPartDO.class);
		query.setParameter("orgId", orgId);
		query.setParameter("scopeId", scopeId);
		return getResultList(query);
	}

	@Override
	public List<OrgPartDO> findParticipatingDescendantOrgParts(Long scopeId, Long orgId) {
		TypedQuery<OrgPartDO> query = em.createQuery("select op from OrgPartDO op, OrgTreeDO ot "
				+ "where ot.ancestorOrg.orgId =:orgId and op.org.orgId=ot.org.orgId and op.scope.scopeId=:scopeId",
				OrgPartDO.class);
		query.setParameter("orgId", orgId);
		query.setParameter("scopeId", scopeId);

		return getResultList(query);
	}

	@Override
	public void deleteOrgParts(List<Long> orgPartIds) {
		Query query = em.createQuery("delete from OrgPartDO op where op.orgPartId in (:orgPartIds)");
		query.setParameter("orgPartIds", orgPartIds);

		query.executeUpdate();
	}
}
