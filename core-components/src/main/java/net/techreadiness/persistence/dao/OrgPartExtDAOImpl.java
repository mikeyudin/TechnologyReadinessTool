package net.techreadiness.persistence.dao;

import java.util.List;

import javax.inject.Named;

import net.techreadiness.persistence.domain.OrgPartDO;
import net.techreadiness.persistence.domain.OrgPartExtDO;

import org.springframework.stereotype.Repository;

@Repository
@Named("orgPartExtDAOImpl")
public class OrgPartExtDAOImpl extends BaseDAOImpl<OrgPartExtDO> implements OrgPartExtDAO, ExtDAO<OrgPartDO, OrgPartExtDO> {

	@Override
	public List<OrgPartExtDO> getExtDOs(OrgPartDO baseEntityWithExt) {
		return baseEntityWithExt.getOrgPartExts();
	}

	@Override
	public OrgPartExtDO getNew() {
		return new OrgPartExtDO();
	}

}
