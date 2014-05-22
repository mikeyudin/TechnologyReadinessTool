package net.techreadiness.persistence.dao;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.RoleDelegationDO;

import org.springframework.stereotype.Repository;

@Repository
public class RoleDelegationDAOImpl extends BaseDAOImpl<RoleDelegationDO> implements RoleDelegationDAO {

	@Override
	public RoleDelegationDO getByRoleIdDelegRoleId(Long roleId, Long delegRoleId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select rd ");
		sb.append("from RoleDelegationDO rd ");
		sb.append("where rd.delegRole.roleId =:delegRoleId");
		sb.append(" and rd.role.roleId =:roleId");

		TypedQuery<RoleDelegationDO> query = em.createQuery(sb.toString(), RoleDelegationDO.class);
		query.setParameter("delegRoleId", delegRoleId);
		query.setParameter("roleId", roleId);
		return getSingleResult(query);
	}
}
