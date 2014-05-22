package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.OptionListDO;

public interface OptionListDAO extends BaseDAO<OptionListDO> {
	List<OptionListDO> getOptionListsForScope(Long scopeId);

	OptionListDO getOptionListByCode(String code);

	OptionListDO getOptionListByCode(String code, Long scopeId);

	OptionListDO getRootScopeRegExOptionList(Long scopeId);
}
