package net.techreadiness.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.EntityFieldDAO;
import net.techreadiness.persistence.dao.PermissionDAO;
import net.techreadiness.persistence.dao.RoleDAO;
import net.techreadiness.persistence.dao.RoleDelegationDAO;
import net.techreadiness.persistence.dao.RolePermissionDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.persistence.domain.RoleDelegationDO;
import net.techreadiness.persistence.domain.RolePermissionDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.security.PermissionCode;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.Scope;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

@WebService
@Service
@Transactional
public class RoleServiceImpl extends BaseServiceWithValidationImpl implements RoleService {

	@Inject
	private RoleDAO roleDAO;
	@Inject
	private ScopeDAO scopeDAO;
	@Inject
	private PermissionDAO permissionDAO;
	@Inject
	private RolePermissionDAO rolePermissionDAO;
	@Inject
	private RoleDelegationDAO roleDelegationDAO;
	@Inject
	private UserService userService;
	@Inject
	private EntityFieldDAO entityFieldDAO;

	@Override
	public Role getById(ServiceContext context, Long roleId) {
		return getMappingService().map(roleDAO.getById(roleId));
	}

	@Override
	public Set<Long> findAssociatedPermissionIds(ServiceContext context, Long roleId) {
		return roleDAO.getAssociatedPermissionIds(roleId);
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = RolePermissionDO.class)
	public void updateRolePermissions(ServiceContext context, Long roleId, Set<Long> permissions) {
		if (roleId == null) {
			return;
		}

		Set<Long> currentPermissions = Sets.newHashSet();
		Collection<RolePermissionDO> rolePermissions = rolePermissionDAO.findPermissionsForRole(roleId);
		for (RolePermissionDO rolePermissionDO : rolePermissions) {
			currentPermissions.add(rolePermissionDO.getPermission().getPermissionId());
		}

		Set<Long> toAdd = Sets.difference(permissions, currentPermissions);
		Set<Long> toRemove = Sets.difference(currentPermissions, permissions);

		for (Long permissionId : toRemove) {
			RolePermissionDO rpDO = rolePermissionDAO.getByRoleIdPermissionId(roleId, permissionId);
			if (rpDO != null) {
				rolePermissionDAO.delete(rpDO);
			}
		}

		for (Long permissionId : toAdd) {
			RolePermissionDO rpDO = rolePermissionDAO.getByRoleIdPermissionId(roleId, permissionId);

			if (rpDO == null) {
				RolePermissionDO rolePermissionDO = new RolePermissionDO();
				rolePermissionDO.setRole(roleDAO.getById(roleId));
				rolePermissionDO.setPermission(permissionDAO.getById(permissionId));
				rolePermissionDAO.create(rolePermissionDO);
			}
		}
	}

	@Override
	public boolean isUniqueRoleCategoryNameCodeByScope(ServiceContext context, Role role) {
		Collection<ScopeDO> pathScopes = scopeDAO.getAncestorsAndDescendants(role.getScope().getScopeId());

		// RoleDO roleDO = roleDAO.getById(role.getRoleId());

		return roleDAO.isUniqueRoleCategoryNameCodeByScopes(role, pathScopes);
	}

	@Override
	public List<Role> findRolesFromScope(ServiceContext context) {
		PermissionCode[] ignoreConferPerm = { CorePermissionCodes.CORE_SEARCH_IGNORE_ROLECONFER };

		return getMappingService().mapFromDOList(
				roleDAO.findRolesFromScope(context.getScopeId(), context.getUserId(),
						userService.hasPermission(context, ignoreConferPerm)));
	}

	@Override
	public void unassignPermissions(ServiceContext context, Set<Long> permissionIdSet) {
		if (permissionIdSet == null) {
			return;
		}
		for (Long permissionId : permissionIdSet) {
			Collection<RolePermissionDO> rolePermissions = rolePermissionDAO.getRolePermissionsByPermission(permissionId);
			if (rolePermissions != null) {
				for (RolePermissionDO rolePermissionDO : rolePermissions) {
					rolePermissionDAO.delete(rolePermissionDO);
				}
			}
		}
	}

	@Override
	public List<Role> findRolesBySearchTerm(ServiceContext context, String term) {
		PermissionCode[] ignoreConferPerm = { CorePermissionCodes.CORE_SEARCH_IGNORE_ROLECONFER };
		return getMappingService().mapFromDOList(
				roleDAO.getRolesBySearchTerm(context.getScopeId(), term, context.getUserId(),
						userService.hasPermission(context, ignoreConferPerm)));
	}

	@Override
	public void validateRole(ServiceContext context, Map<String, String> map, Role role) {
		List<ValidationError> errors = performValidation(map, context.getScopeId(), EntityTypeCode.ROLE);
		RoleDO dbRole = roleDAO.getRoleByCode(role.getScope().getScopeId(), role.getCode());
		if (dbRole != null) {
			if (role.getRoleId() == null || !role.getRoleId().equals(dbRole.getRoleId())) {
				EntityFieldDO code = entityFieldDAO.findByScopeAndTypeAndCode(role.getScope().getId(), EntityTypeCode.ROLE,
						"code");
				String message = messageSource.getMessage("validation.role.code.alreadyExists", null, null);
				ValidationError e = new ValidationError(code.getCode(), code.getName(), message,
						"validation.role.code.alreadyExists", message);
				errors.add(e);
			}
		}

		dbRole = roleDAO.getRoleByName(role.getScope().getScopeId(), role.getName());
		if (dbRole != null) {
			if (role.getRoleId() == null || !role.getRoleId().equals(dbRole.getRoleId())) {
				EntityFieldDO name = entityFieldDAO.findByScopeAndTypeAndCode(role.getScope().getId(), EntityTypeCode.ROLE,
						"name");
				String message = messageSource.getMessage("validation.role.name.alreadyExists", null, null);
				ValidationError e = new ValidationError(name.getCode(), name.getName(), message,
						"validation.role.name.alreadyExists", message);
				errors.add(e);
			}
		}

		if (!isUniqueRoleCategoryNameCodeByScope(context, role)) {
			EntityFieldDO name = entityFieldDAO.findByScopeAndTypeAndCode(role.getScope().getId(), EntityTypeCode.ROLE,
					"name");
			String message = messageSource.getMessage("validation.role.uniqueNameAndCodeAndCategory", null, null);
			ValidationError e = new ValidationError(name.getCode(), name.getName(), message,
					"validation.role.uniqueNameAndCodeAndCategory", message);
			errors.add(e);
		}

		if (errors == null || !errors.isEmpty()) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage("Role failed validation.");
			faultInfo.setAttributeErrors(errors);
			throw new ValidationServiceException(faultInfo);
		}
	}

	@Override
	@Transactional
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = RolePermissionDO.class)
	public Role update(ServiceContext context, Long scopeId, Role role) {

		RoleDO roleDO = roleDAO.getById(role.getRoleId());
		ScopeDO scopeDO = scopeDAO.getById(scopeId);
		Scope scope = getMappingService().map(scopeDO);
		role.setScope(scope);

		roleDO.setCategory(role.getCategory());
		roleDO.setName(role.getName());
		roleDO.setShortName(role.getShortName());
		roleDO.setCode(role.getCode());
		roleDO.setDescription(role.getDescription());
		roleDO.setDisplayOrder(role.getDisplayOrder());
		roleDO.setScope(scopeDO);
		validateRole(context, roleDO.getAsMap(), role);
		return getMappingService().map(roleDAO.update(roleDO));
	}

	@Override
	@Transactional
	public Role create(ServiceContext context, Role role) {

		RoleDO roleDO = getMappingService().map(role);

		roleDO.setScope(scopeDAO.getById(context.getScopeId()));
		role = getMappingService().map(roleDO);

		validateRole(context, roleDO.getAsMap(), role);
		return getMappingService().map(roleDAO.create(roleDO));
	}

	@Override
	public List<Role> findByIds(ServiceContext context, Collection<Long> roleIds) {
		return getMappingService().mapFromDOList(roleDAO.findById(roleIds));
	}

	@Override
	public Map<String, Boolean> getRoleConferAsMap(ServiceContext context) {
		return roleDAO.getRoleConferAsMap(context);
	}

	@Override
	public Boolean isDelegated(Long roleId, Long delegRoleId) {
		return roleDAO.isDelegated(roleId, delegRoleId);
	}

	@Override
	public void updateRoleDelegations(ServiceContext context, Long roleId, Set<Long> addDelegationSet,
			Set<Long> delDelegationSet) {
		if (roleId == null) {
			return;
		}
		if (delDelegationSet != null) {
			for (Long delegRoleId : delDelegationSet) {
				RoleDelegationDO rdDO = roleDelegationDAO.getByRoleIdDelegRoleId(roleId, delegRoleId);
				if (rdDO != null) {
					roleDelegationDAO.delete(rdDO);
				}
			}
		}
		if (addDelegationSet != null) {
			for (Long delegRoleId : addDelegationSet) {
				RoleDelegationDO rdDO = roleDelegationDAO.getByRoleIdDelegRoleId(roleId, delegRoleId);

				if (rdDO == null) {
					RoleDelegationDO roleDelegationDO = new RoleDelegationDO();
					roleDelegationDO.setRole(roleDAO.getById(roleId));
					roleDelegationDO.setDelegRole(roleDAO.getById(delegRoleId));
					roleDelegationDAO.create(roleDelegationDO);
				}
			}
		}
	}

	@Override
	public Role getRoleByCode(ServiceContext context, String code) {
		RoleDO role = roleDAO.getRoleByCode(context.getScopeId(), code);
		return getMappingService().map(role);
	}
}
