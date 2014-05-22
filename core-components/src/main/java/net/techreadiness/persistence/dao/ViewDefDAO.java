package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.ViewDefDO;

public interface ViewDefDAO extends BaseDAO<ViewDefDO> {
	ViewDefDO getByEntityTypeAndScopePath(String entityTypeCode, Long scopeId);

	ViewDefDO getByViewTypeAndScopePath(String viewTypeCode, Long scopeId);
}
