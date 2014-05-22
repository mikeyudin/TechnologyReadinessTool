package net.techreadiness.service;

import java.util.List;

import net.techreadiness.service.object.OrgType;

public interface OrgTypeService extends BaseService {

	List<OrgType> findChildOrgTypes(ServiceContext context, Long parentOrgTypeId);

	OrgType getById(ServiceContext context, Long orgTypeId);
}
