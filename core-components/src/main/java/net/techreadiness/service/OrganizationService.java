package net.techreadiness.service;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;

import net.techreadiness.service.object.Org;

public interface OrganizationService extends BaseService {

	List<Org> findOrgsParticipatingInScope(ServiceContext context);

	List<Org> findOrgsParticipatingInScope(ServiceContext context, String term);

	/**
	 * Returns a list of all the child/descendant organizations that are participating in the servicecontext scope.
	 *
	 * @param context
	 * @param orgId
	 *            - The org to use as the parent.
	 * @param allDescendants
	 *            - flag to determine if only direct descendants or all descendants are returned. true = all descendants
	 *            false = direct descendants
	 *
	 * @return list of matching organizations
	 */
	List<Org> findDescendantOrgsParticipatingInScope(ServiceContext context, Long orgId, boolean allDescendants);

	Collection<Org> findDescendantOrgs(ServiceContext context, Long orgId);

	List<Org> findOrgsParticipatingInScopeThatAllowGroups(ServiceContext context);

	List<Org> findOrgsParticipatingInScopeThatAllowGroups(ServiceContext context, String term);

	List<Org> findOrgsParticipatingInScopeThatAllowEnrollments(ServiceContext context);

	List<Org> findOrgsParticipatingInScopeThatAllowEnrollments(ServiceContext context, String term);

	List<Org> findChildOrgsThatAllowDevices(ServiceContext context, String term, int limit);

	Org getById(ServiceContext context, Long orgId);

	Org getByCode(ServiceContext context, String orgCode);

	Org getMatch(ServiceContext context, Org org);

	/**
	 * Gets an organization for a particular user. Used to set the org in the service context. Assumes the scope and the user
	 * are set within the passed in service context.
	 *
	 * @param context
	 *            - The {@code ServiceContext} with the {@code Scope} and {@code User} set.
	 *
	 * @return - The highest level {@code Org} assigned to the {@code User} within the given {@code Scope}. Null if one is
	 *         not found.
	 */
	Org getOrgForUser(ServiceContext context);

	/**
	 * Gets all the organizations for a particular user (within the scope of the service context). Assumes the scope and the
	 * user are set within the passed in service context.
	 *
	 * @param context
	 *            The {@code ServiceContext} with the {@code Scope} and {@code User} set.
	 *
	 * @return All the {@code Org}s assigned to the {@code User} (and descendant organizations) within the given
	 *         {@code Scope}. Empty list if none are found.
	 */
	List<Org> findOrgsForUser(ServiceContext context);

	List<Org> findByIds(ServiceContext context, Collection<Long> orgIds);

	/**
	 * Finds all organizations given the scope in the service context, and limited by the organization in the service context
	 * (only organizations at or below the one in the service context).
	 *
	 * @param context
	 *            The service context.
	 * @param maxResults
	 *            The max number of results to return. Set to 0 or less to get all results.
	 * @return Organizations in for the scope
	 */
	List<Org> findOrgsByScope(ServiceContext context, int maxResults);

	/**
	 * Creates a {@link Org} and returns the newly created {@link Org} instance.
	 *
	 * @param context
	 * @param organization
	 * @return The new {@link Org} object which has been created with the org_id field be populated.
	 */
	Org create(ServiceContext context, Org organization);

	/**
	 * Finds all organizations given the scope in the service context, and limited by the organization in the service context
	 * (only organizations at or below the one in the service context) that can have children.
	 *
	 * @param context
	 *            The service context.
	 * @param maxResults
	 *            The max number of results to return. Set to 0 or less to get all results.
	 * @return Organizations that can have child organizations for the scope
	 */
	List<Org> findOrgsThatCanHaveChildren(ServiceContext context, int maxResults);

	List<Org> findOrgsThatCanHaveChildrenByType(ServiceContext context, Long orgId);

	/**
	 * Finds all organizations given the scope in the service context, and limited by the organization in the service context
	 * (only organizations at or below the one in the service context) and by the search term that can have children.
	 *
	 * @param context
	 *            The service context.
	 * @param term
	 *            The search term to use.
	 * @param maxResults
	 *            The max number of results to return. Set to 0 or less to get all results.
	 * @return Organizations that can have child organizations filtered by the search term and scope.
	 */
	List<Org> findOrgsThatCanHaveChildrenBySearchTerm(ServiceContext context, String term, int maxResults);

	List<Org> findOrgsThatCanHaveChildrenBySearchTermByType(ServiceContext context, String term, Long orgId);

	/**
	 * Gets a list of key/value pairs of an org_type.org_type_id, and org_type.name
	 *
	 * @param context
	 * @return Organization type identifiers mapped to the name of the organization type
	 */
	List<SimpleEntry<Long, String>> findOrgTypes(ServiceContext context);

	List<SimpleEntry<Long, String>> findOrgTypesByIds(Collection<Long> orgTypeIds);

	/**
	 * Finds all organizations given the scope in the service context, and limited by the organization in the service context
	 * (only organizations at or below the one in the service context) and by the search term.
	 *
	 * @param context
	 *            The service context.
	 * @param term
	 *            The search term to use.
	 * @param maxResults
	 *            The max number of results to return. Set to 0 or less to get all results.
	 * @return Organizations filtered by the search term and scope
	 */
	List<Org> findOrgsBySearchTerm(ServiceContext context, String term, int maxResults);

	void delete(ServiceContext context, Long orgId);

	List<SimpleEntry<Long, String>> findChildOrgTypesByParentOrgType(ServiceContext context, Long parentOrgTypeId);

	Org addOrUpdate(ServiceContext context, Org org);

	Org getParentOrgOfType(ServiceContext context, Long orgId, String typeCode);
}
