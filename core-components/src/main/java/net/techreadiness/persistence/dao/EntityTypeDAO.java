package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.EntityTypeDO;

public interface EntityTypeDAO extends BaseDAO<EntityTypeDO> {

	List<EntityTypeDO> getEntitiesNotDefinedForScope(Long scopeId);
}
