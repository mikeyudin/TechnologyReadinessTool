package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgDO_;
import net.techreadiness.persistence.domain.OrgTypeDO_;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeDO_;
import net.techreadiness.persistence.domain.ScopeTreeDO;
import net.techreadiness.persistence.domain.ScopeTreeDO_;
import net.techreadiness.persistence.domain.ScopeTypeDO;
import net.techreadiness.persistence.domain.ScopeTypeDO_;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository
public class OrgDAOImpl extends BaseDAOImpl<OrgDO> implements OrgDAO {

	@Override
	public List<OrgDO> findByScopeId(Long scopeId, Long orgId, int maxResults) {
		TypedQuery<OrgDO> query = em.createQuery("select distinct o from OrgDO o join o.orgTrees tree "
				+ "where o.scope.scopeId = :scopeId and tree.ancestorOrg.orgId = :orgId", OrgDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);

		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}

		return getResultList(query);
	}

	@Override
	public List<OrgDO> findOrgsThatCanHaveChildren(Long scopeId, Long orgId, int maxResults) {
		TypedQuery<OrgDO> query = em.createQuery("select distinct o "
				+ "from OrgDO o join o.orgTrees tree, OrgTypeDO ot, ScopeTreeDO st, ScopeDO s, ScopeTypeDO stype "
				+ "where o.orgType.orgTypeId = ot.parentOrgType.orgTypeId "
				+ "and ot.scope.scopeId = st.ancestorScope.scopeId " + "and s.scopeId = st.ancestorScope.scopeId "
				+ "and s.scopeType.scopeTypeId = stype.scopeTypeId "
				+ "and stype.allowOrg = 1 and st.scope.scopeId = :scopeId " + "and tree.ancestorOrg.orgId = :orgId",
				OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);

		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}

		return query.getResultList();
	}

	@Override
	public List<OrgDO> findOrgsThatCanHaveChildrenBySearchTerm(Long scopeId, Long orgId, String term, int maxResults) {
		TypedQuery<OrgDO> query = em.createQuery("select distinct o "
				+ "from OrgDO o join o.orgTrees tree, OrgTypeDO ot, ScopeTreeDO st, ScopeDO s, ScopeTypeDO stype "
				+ "where o.orgType.orgTypeId = ot.parentOrgType.orgTypeId " + "and (o.code LIKE :term "
				+ "or o.name LIKE :term) " + "and ot.scope.scopeId = st.ancestorScope.scopeId "
				+ "and s.scopeId = st.ancestorScope.scopeId " + "and s.scopeType.scopeTypeId = stype.scopeTypeId "
				+ "and stype.allowOrg = 1 and st.scope.scopeId = :scopeId " + "and tree.ancestorOrg.orgId = :orgId",
				OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);
		query.setParameter("term", "%%");

		if (!StringUtils.isEmpty(term)) {
			query.setParameter("term", "%" + term + "%");
		}

		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}

		return query.getResultList();
	}

	@Override
	public OrgDO getParent(Long orgId) {
		TypedQuery<OrgDO> query = em.createQuery("select o from OrgDO o, OrgTreeDO ot "
				+ "where o.orgId = ot.ancestorOrg.orgId " + "and ot.org.orgId = :orgId " + "and ot.distance = 1",
				OrgDO.class);
		query.setParameter("orgId", orgId);

		return getSingleResult(query);
	}

	@Override
	public List<OrgDO> findOrgsBySearchTerm(Long scopeId, Long orgId, String term, int maxResults) {
		TypedQuery<OrgDO> query = em.createQuery("select o from OrgDO o join o.orgTrees tree "
				+ "where o.scope.scopeId =:scopeId " + "and tree.ancestorOrg.orgId = :orgId " + "AND (o.code LIKE :term "
				+ "OR o.name LIKE :term) " + "order by o.name, o.code ", OrgDO.class);
		query.setParameter("term", "%%");
		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);

		if (!StringUtils.isEmpty(term)) {
			query.setParameter("term", "%" + term + "%");
		}

		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}

		return getResultList(query);
	}

	@Override
	public List<OrgDO> findById(Collection<Long> orgIds) {
		if (orgIds.isEmpty()) {
			return Lists.<OrgDO> newArrayList();
		}
		TypedQuery<OrgDO> query = em.createQuery("select org from OrgDO org where org.orgId in (:orgIds)", OrgDO.class);
		query.setParameter("orgIds", orgIds);
		return query.getResultList();
	}

	@Override
	public List<OrgDO> findOrgsParticipatingInScopeThatAllowGroups(Long scopeId, Long orgId) {
		TypedQuery<OrgDO> query = em.createQuery("  select org from OrgDO org                       "
				+ "    join org.orgParts orgPart                     "
				+ "    join org.orgTrees orgTree                     "
				+ "  where orgTree.ancestorOrg.orgId = :orgId        "
				+ "    and orgPart.scope.scopeId = :scopeId          "
				+ "    and orgPart.scope.scopeType.allowGroup = true "
				+ "    and org.orgType.allowGroup = true             "
				+ "  order by org.name, org.code                     ", OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);

		return query.getResultList();
	}

	@Override
	public List<OrgDO> findOrgsParticipatingInScopeThatAllowGroups(Long scopeId, Long orgId, String term) {
		TypedQuery<OrgDO> query = em.createQuery("  select org from OrgDO org                          "
				+ "    join org.orgParts orgPart                        "
				+ "    join org.orgTrees orgTree                        "
				+ "  where orgTree.ancestorOrg.orgId = :orgId           "
				+ "    and orgPart.scope.scopeId = :scopeId             "
				+ "    and orgPart.scope.scopeType.allowGroup = true    "
				+ "    and org.orgType.allowGroup = true                "
				+ "    and (org.name like :term or org.code like :term) "
				+ "  order by org.name, org.code                        ", OrgDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);
		query.setParameter("term", "%" + term + "%");
		return query.getResultList();
	}

	@Override
	public List<OrgDO> findOrgsParticipatingInScopeThatAllowEnrollments(Long scopeId, Long orgId) {
		TypedQuery<OrgDO> query = em.createQuery("  select org from OrgDO org                                "
				+ "    join org.orgParts orgPart                              "
				+ "    join org.orgTrees tree                                 "
				+ "  where  orgPart.scope.scopeId = :scopeId                  "
				+ "     and orgPart.scope.scopeType.allowStudentEnroll = true "
				+ "     and tree.ancestorOrg.orgId = :orgId                   "
				+ "  order by org.name, org.code                              ",

		OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);

		return query.getResultList();
	}

	@Override
	public List<OrgDO> findOrgsParticipatingInScopeThatAllowEnrollments(Long scopeId, Long orgId, String term) {
		TypedQuery<OrgDO> query = em.createQuery("  select org from OrgDO org                                "
				+ "    join org.orgParts orgPart                              "
				+ "    join org.orgTrees tree                                 "
				+ "  where orgPart.scope.scopeId = :scopeId                   "
				+ "    and tree.ancestorOrg.orgId = :orgId                    "
				+ "    and orgPart.scope.scopeType.allowStudentEnroll = true  "
				+ "    and (org.name like :term or org.code like :term)       "
				+ "  order by org.name, org.code                              ", OrgDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);
		query.setParameter("term", "%" + term + "%");
		return query.getResultList();
	}

	@Override
	public OrgDO getOrg(String code, Long scopeId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrgDO> criteria = cb.createQuery(OrgDO.class);
		Root<OrgDO> org = criteria.from(OrgDO.class);
		Join<OrgDO, ScopeDO> scope = org.join(OrgDO_.scope);
		Join<ScopeDO, ScopeTypeDO> scopeType = scope.join(ScopeDO_.scopeType);
		Join<ScopeDO, ScopeTreeDO> scopeTree = scope.join(ScopeDO_.ancestorScopeTrees);
		Predicate typeClause = cb.equal(scopeType.get(ScopeTypeDO_.allowOrg), Short.valueOf("1"));
		Predicate pathClause = cb.equal(scopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);
		Predicate orgCode = cb.equal(org.get(OrgDO_.code), code);
		criteria.where(orgCode, typeClause, pathClause);

		return getSingleResult(em.createQuery(criteria));
	}

	@Override
	public List<OrgDO> findOrgsByType(Long orgTypeId, Long scopeId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrgDO> criteria = cb.createQuery(OrgDO.class);
		Root<OrgDO> org = criteria.from(OrgDO.class);
		Predicate p = cb.equal(org.get(OrgDO_.orgType).get(OrgTypeDO_.orgTypeId), orgTypeId);

		Join<OrgDO, ScopeDO> scope = org.join(OrgDO_.scope);
		Join<ScopeDO, ScopeTypeDO> scopeType = scope.join(ScopeDO_.scopeType);
		Join<ScopeDO, ScopeTreeDO> scopeTree = scope.join(ScopeDO_.ancestorScopeTrees);
		Predicate typeClause = cb.equal(scopeType.get(ScopeTypeDO_.allowOrg), Short.valueOf("1"));
		Predicate pathClause = cb.equal(scopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);

		criteria.where(p, typeClause, pathClause);
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public List<OrgDO> findParticipatingChildOrgs(Long scopeId, Long orgId) {
		TypedQuery<OrgDO> query = em
				.createQuery(
						"select org from OrgPartDO orgPart join orgPart.org org where org.parentOrg.orgId = :orgId and orgPart.scope.scopeId = :scopeId",
						OrgDO.class);
		query.setParameter("orgId", orgId);
		query.setParameter("scopeId", scopeId);
		return getResultList(query);
	}

	@Override
	public OrgDO getHighestOrgForUser(Long scopeId, Long userId) {
		TypedQuery<OrgDO> query = em.createQuery("select o from OrgDO o, OrgTreeDO ot, UserOrgDO uo "
				+ "where o.orgId = uo.org.orgId " + "and uo.org.orgId = ot.org.orgId " + "and o.scope.scopeId = :scopeId "
				+ "and uo.user.userId = :userId " + "and ot.distance = 0 " + "order by ot.depth", OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("userId", userId);
		query.setMaxResults(1);

		return getSingleResult(query);
	}

	@Override
	public List<OrgDO> findOrgsForUser(Long scopeId, Long userId) {
		TypedQuery<OrgDO> query = em.createQuery("select o from OrgDO o join o.orgTrees ot, UserOrgDO uo "
				+ "where ot.ancestorOrg.orgId = uo.org.orgId " + "and ot.ancestorOrg.scope.scopeId = :scopeId "
				+ "and uo.user.userId = :userId ", OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("userId", userId);

		return getResultList(query);
	}

	@Override
	public List<OrgDO> findByCodes(Long scopeId, List<String> codes) {
		if (codes == null || codes.isEmpty()) {
			return Collections.emptyList();
		}
		TypedQuery<OrgDO> query = em.createQuery(
				"select o from OrgDO o where o.scope.scopeId = :scopeId and o.code in (:codes)", OrgDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("codes", codes);
		return getResultList(query);
	}

	@Override
	public boolean isChildOf(List<Long> orgIds, Long childOrgId) {
		if (orgIds == null || orgIds.isEmpty()) {
			return false;
		}
		TypedQuery<OrgDO> query = em.createQuery(
				"select ot.org from OrgTreeDO ot where ot.ancestorOrg.orgId in (:orgIds) and ot.org.orgId = :orgId",
				OrgDO.class);
		query.setParameter("orgIds", orgIds);
		query.setParameter("orgId", childOrgId);
		return !getResultList(query).isEmpty();
	}

	@Override
	public List<OrgDO> findChildOrgsThatAllowDevices(Long orgId) {
		TypedQuery<OrgDO> query = em.createQuery("  select org from OrgDO org                                "
				+ "    join org.orgTrees tree                                 "
				+ "  where tree.ancestorOrg.orgId = :orgId                    " + "    and org.orgType.allowDevice = true  "
				+ "  order by org.name, org.code                              ", OrgDO.class);
		query.setParameter("orgId", orgId);
		return query.getResultList();
	}

	@Override
	public List<OrgDO> findChildOrgsThatAllowDevices(Long orgId, String term, int limit) {
		TypedQuery<OrgDO> query = em.createQuery("  select org from OrgDO org                                "
				+ "    join org.orgTrees tree                                 "
				+ "  where tree.ancestorOrg.orgId = :orgId                    " + "    and org.orgType.allowDevice = true  "
				+ "    and (org.name like :term or org.code like :term)       "
				+ "  order by org.name, org.code                              ", OrgDO.class);
		query.setParameter("orgId", orgId);
		query.setParameter("term", "%" + term + "%");
		query.setMaxResults(limit);
		return query.getResultList();
	}

	@Override
	public List<OrgDO> findOrgsThatCanHaveChildrenByType(Long scopeId, Long orgId, Long orgTypeId) {
		TypedQuery<OrgDO> query = em.createQuery("select distinct o "
				+ "from OrgDO o join o.orgTrees tree, OrgTypeDO ot, ScopeTreeDO st, ScopeDO s, ScopeTypeDO stype "
				+ "where o.orgType.orgTypeId = ot.parentOrgType.orgTypeId " + "and o.orgType.orgTypeId = :orgTypeId "
				+ "and ot.scope.scopeId = st.ancestorScope.scopeId " + "and s.scopeId = st.ancestorScope.scopeId "
				+ "and s.scopeType.scopeTypeId = stype.scopeTypeId "
				+ "and stype.allowOrg = 1 and st.scope.scopeId = :scopeId " + "and tree.ancestorOrg.orgId = :orgId",
				OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);
		query.setParameter("orgTypeId", orgTypeId);

		return query.getResultList();
	}

	@Override
	public List<OrgDO> findOrgsThatCanHaveChildrenBySearchTermByType(Long scopeId, Long orgId, String term, Long orgTypeId) {
		TypedQuery<OrgDO> query = em.createQuery("select distinct o "
				+ "from OrgDO o join o.orgTrees tree, OrgTypeDO ot, ScopeTreeDO st, ScopeDO s, ScopeTypeDO stype "
				+ "where o.orgType.orgTypeId = ot.parentOrgType.orgTypeId " + "and o.orgType.orgTypeId = :orgTypeId "
				+ "and (o.code LIKE :term " + "or o.name LIKE :term) " + "and ot.scope.scopeId = st.ancestorScope.scopeId "
				+ "and s.scopeId = st.ancestorScope.scopeId " + "and s.scopeType.scopeTypeId = stype.scopeTypeId "
				+ "and stype.allowOrg = 1 and st.scope.scopeId = :scopeId " + "and tree.ancestorOrg.orgId = :orgId",
				OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);
		query.setParameter("orgTypeId", orgTypeId);
		query.setParameter("term", "%%");

		if (!StringUtils.isEmpty(term)) {
			query.setParameter("term", "%" + term + "%");
		}

		return query.getResultList();
	}

	@Override
	public boolean hasUsers(Long orgId) {
		TypedQuery<Long> query = em.createQuery("select count(uo.userOrgId) from UserOrgDO uo where uo.org.orgId = :orgId",
				Long.class);
		query.setParameter("orgId", orgId);
		Long count = query.getSingleResult();
		return count.compareTo(Long.valueOf(0)) > 0;
	}

	@Override
	public List<OrgDO> findDescendantOrgsParticipatingInScope(Long scopeId, Long orgId, boolean allDescendants) {
		TypedQuery<OrgDO> query = em.createQuery("  select org from OrgDO org              "
				+ "    join org.orgParts orgPart            " + "    join org.orgTrees tree               "
				+ "  where orgPart.scope.scopeId = :scopeId "
				+ (allDescendants ? " and tree.distance > 0 " : " and tree.distance = 1 ")
				+ "    and tree.ancestorOrg.orgId = :orgId  ", OrgDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);
		return query.getResultList();
	}

	@Override
	public List<OrgDO> findParents(Long orgId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ot.ancestorOrg from OrgTreeDO ot ");
		sb.append("where ot.org.orgId = :orgId and ot.distance != 0");
		TypedQuery<OrgDO> query = em.createQuery(sb.toString(), OrgDO.class);
		query.setParameter("orgId", orgId);

		return getResultList(query);
	}

	@Override
	public OrgDO getOrgOfType(Long orgId, String typeCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ot.ancestorOrg from OrgTreeDO ot ");
		sb.append("where ot.org.orgId = :orgId and ot.ancestorOrg.orgType.code = :typeCode");
		TypedQuery<OrgDO> query = em.createQuery(sb.toString(), OrgDO.class);
		query.setParameter("orgId", orgId);
		query.setParameter("typeCode", typeCode);

		return getSingleResult(query);
	}

	@Override
	public Collection<OrgDO> findDescendantOrgs(Long orgId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select org from OrgDO org ");
		sb.append("join org.orgTrees tree ");
		sb.append("where tree.distance > 0 ");
		sb.append("and tree.ancestorOrg.orgId = :orgId");
		TypedQuery<OrgDO> query = em.createQuery(sb.toString(), OrgDO.class);
		query.setParameter("orgId", orgId);
		return query.getResultList();
	}
}
