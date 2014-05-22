package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.ViewDefTypeDO;

public interface ViewDefTypeDAO extends BaseDAO<ViewDefTypeDO> {
	ViewDefTypeDO getByCode(String code);
}
