package net.techreadiness.service;

import java.util.List;

import net.techreadiness.service.object.UserOrg;

public interface UserOrgService extends BaseService {
	/**
	 * Grant access the user access to an organization and its children.
	 * 
	 * @param context
	 *            The context for the user that is granting access.
	 * @param userId
	 *            The id of the user to grant access to.
	 * @param orgId
	 *            The id of the organization the user should have access to.
	 * @param scopeId
	 * @return New user organization access
	 */
	UserOrg persist(ServiceContext context, Long userId, Long orgId);

	void delete(ServiceContext context, Long userId, Long orgId);

	void deleteAllForUser(ServiceContext context, Long userId);

	void mergeUserOrgs(ServiceContext context, Long userId, List<String> orgCodes);
}
