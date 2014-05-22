package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import net.techreadiness.service.object.OrgPart;

public interface OrgPartService extends BaseService {

	/**
	 * Creates the organization participation for the given scopeId and orgId if it does not already exist. If it exists,
	 * there is no action.
	 *
	 * This also looks at the scope flag to determine if children organizations are set to auto-participate. If so, creates a
	 * participation for all children organizations (of the passed in orgId) as well.
	 *
	 * @param context
	 *            The {@link ServiceContext} for this call
	 * @param orgId
	 *            The given orgId
	 * @param exts
	 *            The ext attributes for the given orgPart
	 * @return Either the newly created {@link OrgPart}, or the already existing one.
	 */
	OrgPart createIfNotExists(ServiceContext context, Long orgId, Map<String, String> exts);

	/**
	 * Creates the organization participation for the given scopeId and orgId if it does not already exist. If it exists,
	 * there is no action.
	 *
	 * This also looks at the scope flag to determine if children organizations are set to auto-participate. If so, creates a
	 * participation for all children organizations (of the passed in orgId) as well.
	 *
	 * @param context
	 *            The {@link ServiceContext} for this call
	 * @param scopeId
	 *            The scopeId to create the participation under
	 * @param orgId
	 *            The given orgId
	 * @param exts
	 *            The extended attributes for the given orgPart
	 * @return Either the newly created {@link OrgPart}, or the already existing one.
	 */
	OrgPart createIfNotExistsAlternateScope(ServiceContext context, Long scopeId, Long orgId, Map<String, String> exts);

	/**
	 * Deletes the organization participation with the give scopeId and orgId if it exists. If it doesn't exist, no action is
	 * taken.
	 *
	 * @param context
	 *            The {@link ServiceContext} for this call
	 * @param orgId
	 *            The given orgId
	 */
	void deleteIfExists(ServiceContext context, Long orgId);

	void deleteOrgPartsByOrg(ServiceContext context, Long orgId);

	List<OrgPart> findOrgPartsForOrg(ServiceContext context, Long orgId);

}
