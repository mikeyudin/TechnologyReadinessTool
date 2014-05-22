package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.persistence.domain.UserRoleDO;
import net.techreadiness.service.exception.ValidationServiceException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class UserRoleDAOImpl extends BaseDAOImpl<UserRoleDO> implements UserRoleDAO {

	@Override
	public void delete(UserRoleDO t) {
		TypedQuery<Long> query = em.createQuery(
				"select count(ur.userRoleId) from UserRoleDO ur where ur.user.userId = :userId", Long.class);
		query.setParameter("userId", t.getUser().getUserId());
		Long count = query.getSingleResult();
		if (count.compareTo(Long.valueOf(1)) > 0) {
			super.delete(t);
		} else {
			throw new ValidationServiceException(
					"Attempting to delete the last role for a user.  A user must always have at least role.");
		}
	}

	@Override
	public UserRoleDO getUserRoleByUserIdAndRoleId(Long userId, Long roleId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ur ");
		sb.append("from UserRoleDO ur ");
		sb.append("where ur.user.userId =:userId ");
		sb.append("and ur.role.roleId =:roleId ");

		TypedQuery<UserRoleDO> query = em.createQuery(sb.toString(), UserRoleDO.class);
		query.setParameter("userId", userId);
		query.setParameter("roleId", roleId);
		query.setMaxResults(1);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}

	@Override
	public List<UserRoleDO> findUserRolesByUser(Long userId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ur ");
		sb.append("from UserRoleDO ur ");
		sb.append("join fetch ur.role ");
		sb.append("where ur.user.userId =:userId ");

		TypedQuery<UserRoleDO> query = em.createQuery(sb.toString(), UserRoleDO.class);
		query.setParameter("userId", userId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getResultList(query);
	}

	@Override
	public List<RoleDO> findRolesForUsers(Long scopeId, Collection<Long> userIds) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct r from RoleDO r ");
		sb.append("join r.userRoles ur ");
		sb.append("where ur.user.userId in (:userIds) ");
		sb.append("and ur.user.scope.scopeId in (select st.ancestorScope.scopeId from ScopeTreeDO st where st.scope.scopeId = :scopeId and st.ancestorScope.scopeType.allowUser = true) ");
		TypedQuery<RoleDO> query = em.createQuery(sb.toString(), RoleDO.class);
		query.setParameter("userIds", userIds);
		query.setParameter("scopeId", scopeId);
		return query.getResultList();
	}
}
