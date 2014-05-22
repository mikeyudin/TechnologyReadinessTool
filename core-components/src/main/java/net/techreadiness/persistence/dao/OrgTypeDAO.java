package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import net.techreadiness.persistence.domain.OrgTypeDO;

public interface OrgTypeDAO extends BaseDAO<OrgTypeDO> {

	List<OrgTypeDO> findOrgTypesForScope(Long scopeId);

	List<OrgTypeDO> findOrgTypesForScope(Long scopeId, Collection<String> orgTypeCodes);

	List<OrgTypeDO> findOrgTypesByIds(Collection<Long> orgTypeIds);

	/**
	 * Get the organization types that are direct descendants of the specified organization type.
	 *
	 * @param parentOrgTypeId
	 *            The parent id to find the child. Can be null, as this would indicate the top level parent.
	 *
	 * @param scopeId
	 *            The scope to use to find the type. **Assumes this is at the level that allows organizations.
	 * @return All organization types that have the specified type as a parent.
	 */
	List<OrgTypeDO> findChildOrgTypes(Long parentOrgTypeId, Long scopeId);

	OrgTypeDO getByCode(String orgTypeCode, Long scopeId);

	List<Long> findDisallowedChildOrgTypeIdsForOrg(Long orgId);
}
