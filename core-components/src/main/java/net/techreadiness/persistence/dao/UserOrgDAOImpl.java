package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.UserOrgDO;
import net.techreadiness.service.exception.ValidationServiceException;

import org.springframework.stereotype.Repository;

@Repository
public class UserOrgDAOImpl extends BaseDAOImpl<UserOrgDO> implements UserOrgDAO {

	@Override
	public void delete(UserOrgDO t) {
		TypedQuery<Long> query = em.createQuery(
				"select count(uo.userOrgId) from UserOrgDO uo where uo.user.userId = :userId", Long.class);
		query.setParameter("userId", t.getUser().getUserId());
		Long count = query.getSingleResult();
		if (count.compareTo(Long.valueOf(1)) > 0) {
			super.delete(t);
		} else {
			throw new ValidationServiceException(
					"Attempting to delete the last authorized organization for a user.  A user must always have at least one authorized org.");
		}
	}

	@Override
	public UserOrgDO getUserOrgByUserIdAndOrgId(Long userId, Long orgId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select uo ");
		sb.append("from UserOrgDO uo ");
		sb.append("where uo.user.userId =:userId ");
		sb.append("and uo.org.orgId =:orgId ");

		TypedQuery<UserOrgDO> query = em.createQuery(sb.toString(), UserOrgDO.class);
		query.setParameter("userId", userId);
		query.setParameter("orgId", orgId);
		query.setMaxResults(1);

		return getSingleResult(query);
	}

	@Override
	public List<UserOrgDO> getUserOrgByUserId(Long scopeId, Long userId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select uo ");
		sb.append("from UserOrgDO uo ");
		sb.append("join fetch uo.org o ");
		sb.append("join fetch uo.user ");
		sb.append("join fetch o.scope s ");
		sb.append("where uo.user.userId =:userId and s.scopeId = :scopeId");

		TypedQuery<UserOrgDO> query = em.createQuery(sb.toString(), UserOrgDO.class);
		query.setParameter("userId", userId);
		query.setParameter("scopeId", scopeId);
		return getResultList(query);
	}

	@Override
	public boolean isOrgModifiable(Long userId, Long orgId, Long scopeId) {
		TypedQuery<OrgDO> query = em.createQuery("select o from OrgDO o join o.orgTrees ot, UserOrgDO uo, ScopeTreeDO st "
				+ "where ot.ancestorOrg.orgId = uo.org.orgId "
				+ "and ot.ancestorOrg.scope.scopeId = st.ancestorScope.scopeId " + "and st.scope.scopeId = :scopeId "
				+ "and uo.user.userId = :userId and o.orgId = :orgId", OrgDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("userId", userId);
		query.setParameter("orgId", orgId);
		OrgDO org = getSingleResult(query);
		return org != null;
	}

	@Override
	public List<OrgDO> findOrgsForUsers(Long scopeId, Long orgId, Collection<Long> userIds) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct o from OrgDO o ");
		sb.append("join o.userOrgs uo ");
		sb.append("where uo.user.userId in (:userIds)  ");
		sb.append("and uo.org.orgId in (select ot.org.orgId from OrgTreeDO ot where ot.ancestorOrg.orgId = :orgId) ");
		sb.append("and uo.user.scope.scopeId in (select st.ancestorScope.scopeId from ScopeTreeDO st where st.scope.scopeId = :scopeId and st.ancestorScope.scopeType.allowUser = true) ");

		TypedQuery<OrgDO> query = em.createQuery(sb.toString(), OrgDO.class);
		query.setParameter("userIds", userIds);
		query.setParameter("scopeId", scopeId);
		query.setParameter("orgId", orgId);
		return query.getResultList();
	}
}
