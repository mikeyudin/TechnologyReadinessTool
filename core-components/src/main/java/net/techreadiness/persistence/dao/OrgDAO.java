package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import net.techreadiness.persistence.domain.OrgDO;

public interface OrgDAO extends BaseDAO<OrgDO> {

	/**
	 * Retrieves the list of {@link OrgDO}s belonging to a particular Scope.
	 * 
	 * @param scopeId
	 *            The scope to which the orgs belong
	 * @return The list of {@link OrgDO}s coming under the scope.
	 */
	List<OrgDO> findByScopeId(Long scopeId, Long orgId, int maxResults);

	List<OrgDO> findOrgsThatCanHaveChildren(Long scopeId, Long orgId, int maxResults);

	List<OrgDO> findOrgsThatCanHaveChildrenBySearchTerm(Long scopeId, Long orgId, String term, int maxResults);

	List<OrgDO> findOrgsThatCanHaveChildrenBySearchTermByType(Long scopeId, Long orgId, String term, Long orgTypeId);

	List<OrgDO> findOrgsThatCanHaveChildrenByType(Long scopeId, Long orgId, Long orgTypeId);

	List<OrgDO> findById(Collection<Long> orgIds);

	/**
	 * Retrieves the parent {@link OrgDO} (if exists) of the passed in orgId.
	 * 
	 * @param orgId
	 *            The id of the {@link OrgDO} of which to retrieve the parent.
	 * 
	 * @return The parent {@link OrgDO} (if exists, otherwise null).
	 */
	OrgDO getParent(Long orgId);

	/**
	 * Find all parent organizations for the organization with the specified id.
	 * 
	 * @param orgId
	 *            The organizationId of the child organization.
	 * @return A list of the parents, does not include the organization of the specified orgId.
	 */
	List<OrgDO> findParents(Long orgId);

	/**
	 * Finds the organization with the specified type code. This method looks at the specified organization and any parents
	 * that organization may have.
	 * 
	 * @param orgId
	 *            The lowest level in the organization hierarchy to search.
	 * @param typeCode
	 *            The type code of the needed organization
	 * @return The organization that is represented by the specified orgId or a parent of that org and of the specified type.
	 *         If no match is found, {@code null} is returned.
	 */
	OrgDO getOrgOfType(Long orgId, String typeCode);

	OrgDO getOrg(String code, Long scopeId);

	OrgDO getHighestOrgForUser(Long scopeId, Long userId);

	List<OrgDO> findOrgsBySearchTerm(Long scopeId, Long orgId, String term, int maxResults);

	List<OrgDO> findOrgsParticipatingInScopeThatAllowGroups(Long scopeId, Long orgId);

	List<OrgDO> findOrgsParticipatingInScopeThatAllowGroups(Long scopeId, Long orgId, String term);

	List<OrgDO> findOrgsParticipatingInScopeThatAllowEnrollments(Long scopeId, Long orgId);

	List<OrgDO> findOrgsParticipatingInScopeThatAllowEnrollments(Long scopeId, Long orgId, String term);

	List<OrgDO> findChildOrgsThatAllowDevices(Long orgId);

	List<OrgDO> findChildOrgsThatAllowDevices(Long orgId, String term, int limit);

	List<OrgDO> findOrgsByType(Long orgTypeId, Long scopeId);

	List<OrgDO> findParticipatingChildOrgs(Long scopeId, Long orgId);

	List<OrgDO> findOrgsForUser(Long scopeId, Long userId);

	List<OrgDO> findByCodes(Long scopeId, List<String> codes);

	/**
	 * Determines if the organization with the id of childOrgId is a child of any of the organizations represented by the
	 * list of orgIds.
	 * 
	 * @param orgIds
	 *            The ids of possible parent organizations.
	 * @param childOrgId
	 *            The id of the child organization
	 * @return True if the childOrgId is a child of any of the specified organizations.
	 */
	boolean isChildOf(List<Long> orgIds, Long childOrgId);

	boolean hasUsers(Long orgId);

	List<OrgDO> findDescendantOrgsParticipatingInScope(Long scopeId, Long orgId, boolean allDescendants);
	
	Collection<OrgDO> findDescendantOrgs(Long orgId);
}
