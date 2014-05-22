package net.techreadiness.persistence.datagrid;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.security.PermissionCode;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserService;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@org.springframework.context.annotation.Scope("prototype")
@Transactional(readOnly = true)
public class UserByScopeItemProviderImpl extends DataGridItemProviderImpl<User> implements UserByScopeItemProvider {
	private static final String SHOW_DELETED = "SHOW_DELETED";
	private static final String ORG_FILTER_KEY = "orgId";
	private static final String ROLE_FILTER_KEY = "roleId";

	private Scope scope;
	private ServiceContext serviceContext;

	PermissionCode[] ignoreConferPerm = { CorePermissionCodes.CORE_SEARCH_IGNORE_ROLECONFER };

	@Inject
	MappingService mappingService;
	@Inject
	UserService userService;

	@Autowired
	CriteriaQuery<UserDO> criteriaQuery;

	@Override
	public List<User> getPage(DataGrid<User> grid) {

		boolean showDeleted = false;
		if (grid.getFilters().containsKey(SHOW_DELETED)) {
			showDeleted = true;
		}
		Criteria criteria = createCriteria(grid, SHOW_DELETED);
		criteriaQuery.setFullTextSearchColumns(new String[] { "username", "first_name", "last_name" });

		// Create the base query to limit to non deleted users within the user's scope.
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from user ");

		if (showDeleted) {
			sb.append(" where scope_id=:scope_id");
		} else {
			sb.append(" where delete_date is null and scope_id=:scope_id");
		}
		criteria.getParameters().put("scope_id", scope.getScopeId().toString());

		// Filter by the organizations to which the user is associated.
		if (grid.getFilters().containsKey(ORG_FILTER_KEY)) {
			sb.append(" and user_id in (select user_id from user_org where org_id in (:" + ORG_FILTER_KEY + ")) ");
			criteria.getParameters().putAll(ORG_FILTER_KEY, grid.getFilters().get(ORG_FILTER_KEY));
		} else {
			// if no org filter, constrain by global org.
			sb.append(" and user_id in (select uo.user_id from user_org uo, org_tree ot ");
			sb.append("                 where ot.ancestor_org_id = :org_id ");
			sb.append("                 and ot.org_id = uo.org_id ) ");
			criteria.getParameters().put("org_id", serviceContext.getOrgId().toString());
		}

		// Filter by the roles.
		if (grid.getFilters().containsKey(ROLE_FILTER_KEY)) {
			sb.append(" and user_id in (select user_id from user_role where role_id in (:" + ROLE_FILTER_KEY + ")) ");
			criteria.getParameters().putAll(ROLE_FILTER_KEY, grid.getFilters().get(ROLE_FILTER_KEY));
		}

		if (!userService.hasPermission(serviceContext, ignoreConferPerm)) {
			// Constrain by conferrability
			// this basically says, only those searched for users that have a role that matches a delegated role that was
			// found
			// from any of the current user's roles.
			sb.append(" and user_id in (select u.user_id from user u, user_role ur ");
			sb.append("             where u.user_id = ur.user_id ");
			sb.append("             and ur.role_id in ( select rd.delegated_role_id from role_delegation rd, user_role uur ");
			sb.append("                                 where rd.role_id = uur.role_id");
			sb.append("                                 and uur.user_id = :user_id ) ) ");
			criteria.getParameters().put("user_id", serviceContext.getUserId().toString());
		}

		criteriaQuery.setBaseSubSelect(sb.toString());

		QueryResult<UserDO> result = criteriaQuery.getData(criteria, UserDO.class);
		setTotalNumberOfItems(result.getTotalRowCount());
		return mappingService.mapFromDOList(result.getRows());
	}

	@Override
	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Scope getScope() {
		return scope;
	}

	@Override
	public void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}

	@Override
	public User getObjectForKey(String rowKey) {
		Long userId = Long.valueOf(rowKey);
		return userService.getById(serviceContext, userId);
	}

}
