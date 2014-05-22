package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.domain.EntityRuleDO;

public interface EntityRuleDAO extends BaseDAO<EntityRuleDO> {

	List<EntityRuleDO> findValidationRules(Long scopeId, EntityTypeCode entityType);

	List<EntityRuleDO> findViewRules(Long scopeId, EntityTypeCode entityType);
}
