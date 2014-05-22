package net.techreadiness.persistence.dao;

import java.util.List;

import javax.inject.Named;

import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgExtDO;

import org.springframework.stereotype.Repository;

@Repository
@Named("orgExtDAOImpl")
public class OrgExtDAOImpl extends BaseDAOImpl<OrgExtDO> implements OrgExtDAO, ExtDAO<OrgDO, OrgExtDO> {

	@Override
	public List<OrgExtDO> getExtDOs(OrgDO baseEntityWithExt) {
		return baseEntityWithExt.getOrgExts();
	}

	@Override
	public OrgExtDO getNew() {
		return new OrgExtDO();
	}

}
