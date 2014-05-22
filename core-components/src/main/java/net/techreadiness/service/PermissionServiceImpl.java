package net.techreadiness.service;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.persistence.dao.PermissionDAO;
import net.techreadiness.service.object.Permission;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@WebService
@Service
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl implements PermissionService {

	@Inject
	private PermissionDAO permissionDAO;

	@Override
	public List<Permission> findAllAncestorPermissions(ServiceContext context) {
		return getMappingService().mapFromDOList(permissionDAO.findAllAncestorPermissions(context.getScopeId()));
	}
}
