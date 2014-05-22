package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.persistence.domain.RoleDO_;
import net.techreadiness.persistence.domain.RoleDelegationDO;
import net.techreadiness.persistence.domain.RoleDelegationDO_;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeDO_;
import net.techreadiness.persistence.domain.ScopeTreeDO;
import net.techreadiness.persistence.domain.ScopeTreeDO_;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Role;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Repository
public class RoleDAOImpl extends BaseDAOImpl<RoleDO> implements RoleDAO {

	@Override
	public Set<Long> getAssociatedPermissionIds(Long roleId) {
		if (null == roleId) {
			return new HashSet<>();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select rp.permission.permissionId ");
		sb.append(" from RolePermissionDO rp ");
		sb.append(" where rp.role.roleId = :roleid");

		TypedQuery<Long> query = em.createQuery(sb.toString(), Long.class);
		query.setParameter("roleid", roleId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return Sets.newHashSet(query.getResultList());
	}

	@Override
	public boolean isUniqueRoleCategoryNameCodeByScopes(Role role, Collection<ScopeDO> pathScopes) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select r ");
		sb.append(" from RoleDO r ");
		sb.append(" where r.scope.scopeId = :scopeid");
		sb.append("  and (r.category = :category");
		sb.append("  and r.name = :name) ");

		TypedQuery<RoleDO> query = em.createQuery(sb.toString(), RoleDO.class);
		query.setParameter("category", role.getCategory());
		query.setParameter("name", role.getName());
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		for (ScopeDO scope : pathScopes) {
			query.setParameter("scopeid", scope.getScopeId());
			List<RoleDO> rolesFound = getResultList(query);
			if (rolesFound != null && !rolesFound.isEmpty()) {
				for (RoleDO roleFound : rolesFound) {
					if (roleFound != null && !roleFound.getRoleId().equals(role.getRoleId())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public List<RoleDO> findRolesFromScope(Long scopeId, Long userId, boolean ignoreConfer) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct r ");
		sb.append("from RoleDO r, ScopeTreeDO st  ");
		sb.append("   left outer join r.rolePermissions ");
		sb.append(" where st.scope.scopeId =:scopeid ");
		sb.append(" and st.ancestorScope.scopeId = r.scope.scopeId ");
		if (!ignoreConfer) {
			sb.append(" and r.roleId in (select rd.delegRole.roleId ");
			sb.append("   from  RoleDelegationDO rd ");
			sb.append("   where rd.role.roleId in (select ur.role.roleId ");
			sb.append("     from  UserRoleDO ur ");
			sb.append("     where ur.user.userId = :userid ) ) ");
		}
		sb.append(" order by r.category, r.displayOrder, r.name, r.shortName");
		TypedQuery<RoleDO> query = em.createQuery(sb.toString(), RoleDO.class);
		query.setParameter("scopeid", scopeId);
		if (!ignoreConfer) {
			query.setParameter("userid", userId);
		}

		return getResultList(query);
	}

	@Override
	public List<RoleDO> getRolesBySearchTerm(Long scopeId, String term, Long userId, boolean ignoreConfer) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct r from RoleDO r, ScopeTreeDO st ");
		sb.append(" where st.scope.scopeId =:scopeId ");
		sb.append(" and st.ancestorScope.scopeId = r.scope.scopeId ");
		sb.append(" AND (r.code LIKE :term ");
		sb.append("      OR r.name LIKE :term ) ");
		if (!ignoreConfer) {
			sb.append(" and r.roleId in (select rd.delegRole.roleId ");
			sb.append("   from  RoleDelegationDO rd ");
			sb.append("   where rd.role.roleId in (select ur.role.roleId ");
			sb.append("     from  UserRoleDO ur ");
			sb.append("     where ur.user.userId = :userid ) ) ");
		}
		sb.append(" order by r.displayOrder ");

		TypedQuery<RoleDO> query = em.createQuery(sb.toString(), RoleDO.class);
		query.setParameter("term", "%%");
		query.setParameter("scopeId", scopeId);
		if (!ignoreConfer) {
			query.setParameter("userid", userId);
		}

		if (!StringUtils.isEmpty(term)) {
			query.setParameter("term", "%" + term + "%");
		}

		return getResultList(query);
	}

	@Override
	public RoleDO getRoleByCode(Long scopeId, String code) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleDO> cq = cb.createQuery(RoleDO.class);
		Root<RoleDO> role = cq.from(RoleDO.class);
		Join<RoleDO, ScopeDO> scopeJoin = role.join(RoleDO_.scope);
		Join<ScopeDO, ScopeTreeDO> scopeTree = scopeJoin.join(ScopeDO_.ancestorScopeTrees);
		Predicate scopeClause = cb.equal(scopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);
		Predicate codeClause = cb.equal(role.get(RoleDO_.code), code);

		cq.where(scopeClause, codeClause);
		return getSingleResult(em.createQuery(cq));
	}

	@Override
	public RoleDO getRoleByName(Long scopeId, String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleDO> cq = cb.createQuery(RoleDO.class);
		Root<RoleDO> role = cq.from(RoleDO.class);
		Join<RoleDO, ScopeDO> scopeJoin = role.join(RoleDO_.scope);
		Predicate scopeClause = cb.equal(scopeJoin.get(ScopeDO_.scopeId), scopeId);
		Predicate nameClause = cb.equal(role.get(RoleDO_.name), name);

		cq.where(scopeClause, nameClause);
		return getSingleResult(em.createQuery(cq));
	}

	@Override
	public List<RoleDO> findById(Collection<Long> roleIds) {
		if (roleIds.isEmpty()) {
			return Lists.<RoleDO> newArrayList();
		}
		TypedQuery<RoleDO> query = em.createQuery("select role from RoleDO role where role.roleId in (:roleIds)",
				RoleDO.class);
		query.setParameter("roleIds", roleIds);
		return query.getResultList();
	}

	@Override
	public Map<String, Boolean> getRoleConferAsMap(ServiceContext context) {
		List<RoleDO> roles = findRolesFromScope(context.getScopeId(), context.getUserId(), true);
		Map<String, Boolean> roleConferMap = Maps.newHashMap();
		for (RoleDO role : roles) {
			for (RoleDO delegRole : roles) {
				roleConferMap.put(role.getRoleId() + "." + delegRole.getRoleId(),
						isDelegated(role.getRoleId(), delegRole.getRoleId()));
			}
		}
		return roleConferMap;
	}

	@Override
	public Boolean isDelegated(Long roleId, Long delegRoleId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleDelegationDO> cq = cb.createQuery(RoleDelegationDO.class);
		Root<RoleDelegationDO> role = cq.from(RoleDelegationDO.class);
		Join<RoleDelegationDO, RoleDO> roleJoin = role.join(RoleDelegationDO_.role);
		Predicate roleClause = cb.equal(roleJoin.get(RoleDO_.roleId), roleId);
		Join<RoleDelegationDO, RoleDO> roleDelegJoin = role.join(RoleDelegationDO_.delegRole);
		Predicate roleDelegClause = cb.equal(roleDelegJoin.get(RoleDO_.roleId), delegRoleId);

		cq.where(roleClause, roleDelegClause);
		RoleDelegationDO rdDO = getSingleResult(em.createQuery(cq));
		if (rdDO != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<RoleDO> findDelegatableRoles(Long userId, Long scopeId) {
		TypedQuery<RoleDO> query = em.createQuery("select rd.delegRole from RoleDelegationDO rd " + "join rd.role r "
				+ "join r.scope s " + "join s.ancestorScopeTrees ast " + "join r.userRoles ur "
				+ "where ast.scope.scopeId = :scopeId and ur.user.userId = :userId ", RoleDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("userId", userId);
		return getResultList(query);
	}

	@Override
	public List<RoleDO> findRolesByCode(Long scopeId, List<String> codes) {
		if (codes == null || codes.isEmpty()) {
			return Collections.emptyList();
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RoleDO> cq = cb.createQuery(RoleDO.class);
		Root<RoleDO> role = cq.from(RoleDO.class);
		Join<RoleDO, ScopeDO> scopeJoin = role.join(RoleDO_.scope);
		Join<ScopeDO, ScopeTreeDO> scopeTree = scopeJoin.join(ScopeDO_.ancestorScopeTrees);
		Predicate scopeClause = cb.equal(scopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);
		Predicate codeClause = role.get(RoleDO_.code).in(codes);

		cq.where(scopeClause, codeClause);
		return getResultList(em.createQuery(cq));
	}
}
