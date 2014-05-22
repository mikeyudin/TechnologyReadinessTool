package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgPartDO;

public interface OrgPartDAO extends BaseDAO<OrgPartDO> {

	OrgPartDO findOrgPart(Long orgId, Long scopeId);

	List<OrgDO> findOrgsPartForScope(Long scopeId, Long orgId);

	List<OrgDO> findOrgPartsForScope(Long scopeId, Long orgId, String term);

	List<OrgPartDO> findOrgPartsForOrg(Long orgId);

	List<OrgPartDO> findOrgPartsByScope(Long scopeId);

	void createOrgPartsForDescendants(Long scopeId, Long orgId);

	List<OrgPartDO> findParticipatingChildOrgParts(Long scopeId, Long orgId);

	List<OrgPartDO> findParticipatingDescendantOrgParts(Long scopeId, Long orgId);

	void deleteOrgParts(List<Long> orgPartIds);
}
