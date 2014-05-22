package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import net.techreadiness.persistence.domain.CustomTextDO;

public interface CustomTextDAO extends BaseDAO<CustomTextDO> {
	List<CustomTextDO> findAllCustomTextForScope(Long scopeId);

	CustomTextDO getCustomTextByScopeAndCode(Long scopeId, String code);

	Collection<CustomTextDO> findTextForAncestorsAndDescendants(Long scopeId, String key);
}
