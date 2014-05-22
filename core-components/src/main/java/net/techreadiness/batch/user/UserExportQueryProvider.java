package net.techreadiness.batch.user;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.dao.RoleDAO;
import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.service.ServiceContext;

import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.context.annotation.Scope;

import com.google.common.collect.Lists;

@Named
@Scope("prototype")
public class UserExportQueryProvider implements JpaQueryProvider {
	private EntityManager entityManager;
	@Inject
	private ServiceContext serviceContext;
	@Inject
	private RoleDAO roleDAO;

	@Override
	public Query createQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("select u from UserDO u ");
		sb.append("where u.userId in (select urd.user.userId from UserRoleDO urd where urd.role.roleId in (:roleIds)) ");
		sb.append("and u.userId in (select uo.user.userId from UserOrgDO uo join uo.org o join o.orgTrees aot where aot.ancestorOrg.orgId = :orgId)");

		TypedQuery<UserDO> query = entityManager.createQuery(sb.toString(), UserDO.class);
		query.setParameter("orgId", serviceContext.getOrgId());
		List<RoleDO> roles = roleDAO.findDelegatableRoles(serviceContext.getUserId(), serviceContext.getScopeId());
		List<Long> roleIds = Lists.newArrayList();
		for (RoleDO role : roles) {
			roleIds.add(role.getRoleId());
		}
		query.setParameter("roleIds", roleIds);
		return query;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
