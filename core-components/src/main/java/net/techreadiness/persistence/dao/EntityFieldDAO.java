package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.EntityFieldDO;

public interface EntityFieldDAO extends BaseDAO<EntityFieldDO> {
	List<EntityFieldDO> findByScopePathAndType(Long scopeId, EntityDAO.EntityTypeCode typeCode);

	EntityFieldDO findByScopeAndTypeAndCode(Long scopeId, EntityDAO.EntityTypeCode type, String code);

	void setOptionListIdToNull(Long optionListId);

	EntityFieldDO getRootFieldDefinition(Long entityTypeId, String code);
}
