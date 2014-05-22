package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.OptionListValueDO;

public interface OptionListValueDAO extends BaseDAO<OptionListValueDO> {

	List<OptionListValueDO> getOptionListValuesByOptionListId(Long optionListId);

}
