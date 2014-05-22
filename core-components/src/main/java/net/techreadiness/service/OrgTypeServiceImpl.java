package net.techreadiness.service;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.persistence.dao.OrgTypeDAO;
import net.techreadiness.service.object.OrgType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@WebService
@Service
@Transactional
public class OrgTypeServiceImpl extends BaseServiceImpl implements OrgTypeService {

	@Inject
	OrgTypeDAO orgTypeDao;

	@Override
	public List<OrgType> findChildOrgTypes(ServiceContext context, Long parentOrgTypeId) {
		return getMappingService().mapFromDOList(orgTypeDao.findChildOrgTypes(parentOrgTypeId, context.getScopeId()));
	}

	@Override
	public OrgType getById(ServiceContext context, Long orgTypeId) {
		return getMappingService().map(orgTypeDao.getById(orgTypeId));
	}
}
