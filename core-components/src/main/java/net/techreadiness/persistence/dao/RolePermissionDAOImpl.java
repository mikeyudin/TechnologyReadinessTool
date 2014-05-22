package net.techreadiness.persistence.dao;

import java.util.Collection;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.RolePermissionDO;

import org.springframework.stereotype.Repository;

@Repository
public class RolePermissionDAOImpl extends BaseDAOImpl<RolePermissionDO> implements RolePermissionDAO {

	@Override
	public Collection<RolePermissionDO> getRolePermissionsByPermission(Long permissionId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select rp ");
		sb.append("from RolePermissionDO rp ");
		sb.append("where rp.permission.permissionId =:permissionId");

		TypedQuery<RolePermissionDO> query = em.createQuery(sb.toString(), RolePermissionDO.class);
		query.setParameter("permissionId", permissionId);

		return getResultList(query);
	}

	@Override
	public RolePermissionDO getByRoleIdPermissionId(Long roleId, Long permissionId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select rp ");
		sb.append("from RolePermissionDO rp ");
		sb.append("where rp.permission.permissionId =:permissionId");
		sb.append(" and rp.role.roleId =:roleId");

		TypedQuery<RolePermissionDO> query = em.createQuery(sb.toString(), RolePermissionDO.class);
		query.setParameter("permissionId", permissionId);
		query.setParameter("roleId", roleId);
		return getSingleResult(query);
	}

	@Override
	public Collection<RolePermissionDO> findPermissionsForRole(Long roleId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select rp ");
		sb.append("from RolePermissionDO rp ");
		sb.append("where rp.role.roleId = :roleId");

		TypedQuery<RolePermissionDO> query = em.createQuery(sb.toString(), RolePermissionDO.class);
		query.setParameter("roleId", roleId);

		return getResultList(query);
	}

}
