package net.techreadiness.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.dao.RoleDAO;
import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.dao.UserRoleDAO;
import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserRoleDO;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.security.PermissionCode;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.UserRole;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

@WebService
@Service
@Transactional
public class UserRoleServiceImpl extends BaseServiceImpl implements UserRoleService {

	@Inject
	private UserService userService;

	@Inject
	private UserRoleDAO userRoleDao;

	@Inject
	private UserDAO userDao;

	@Inject
	private RoleDAO roleDao;

	@PersistenceContext
	private EntityManager em;

	@Override
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_USER_ROLE_UPDATE)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserRoleDO.class)
	public UserRole persist(ServiceContext context, Long userId, final Long roleId) {
		UserRoleDO userRoleDO = userRoleDao.getUserRoleByUserIdAndRoleId(userId, roleId);
		if (userRoleDO != null) {
			return getMappingService().getMapper().map(userRoleDO, UserRole.class);
		}

		RoleDO roleDO = roleDao.getById(roleId);
		if (isDelegatabale(context, roleId)) {
			userRoleDO = new UserRoleDO();

			UserDO userDO = userDao.getById(userId);

			userRoleDO.setRole(roleDO);
			userRoleDO.setUser(userDO);

			userRoleDao.persist(userRoleDO);
			return getMappingService().getMapper().map(userRoleDO, UserRole.class);
		}
		throw new AuthorizationException(messageSource.getMessage("validation.userRole.delegationNotAllowed", new Object[] {
				context.getUserName(), roleDO.getName() }, Locale.getDefault()));
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = UserRoleDO.class)
	public void delete(ServiceContext context, Long userId, Long roleId) {
		if (isDelegatabale(context, roleId)) {
			UserRoleDO userRoleDO = userRoleDao.getUserRoleByUserIdAndRoleId(userId, roleId);
			if (userRoleDO != null) {
				userRoleDao.delete(userRoleDO);
			}
		} else {
			RoleDO roleDO = roleDao.getById(roleId);
			throw new AuthorizationException(messageSource.getMessage("validation.userRole.delegationNotAllowed",
					new Object[] { context.getUserName(), roleDO.getName() }, Locale.getDefault()));
		}
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = UserRoleDO.class)
	public void deleteAllUserRoles(ServiceContext context, Long userId) {
		List<UserRoleDO> userRoles = userRoleDao.findUserRolesByUser(userId);
		for (UserRoleDO userRole : userRoles) {
			if (isDelegatabale(context, userRole.getRole().getRoleId())) {
				userRoleDao.delete(userRole);
			}
		}
	}

	private boolean isDelegatabale(final ServiceContext context, final Long roleId) {
		if (ignoreConferrability(context)) {
			return true;
		}
		List<RoleDO> delegatableRoles = roleDao.findDelegatableRoles(context.getUserId(), context.getScopeId());
		Iterable<RoleDO> filter = Iterables.filter(delegatableRoles, new Predicate<RoleDO>() {
			@Override
			public boolean apply(RoleDO input) {
				return input.getRoleId().equals(roleId);
			}
		});
		return filter.iterator().hasNext();
	}

	private boolean ignoreConferrability(ServiceContext context) {
		PermissionCode[] ignoreConferPerm = { CorePermissionCodes.CORE_SEARCH_IGNORE_ROLECONFER };
		if (null == context.getUser()) {
			return false;
		}
		return userService.hasPermission(context, ignoreConferPerm);
	}

	@Override
	public void mergeUserRoles(ServiceContext context, Long userId, List<String> roleCodes) {
		// cannot accept empty role code list
		if (CollectionUtils.isEmpty(roleCodes)) {
			ValidationError error = new ValidationError("code", "Code", messageSource.getMessage(
					"validation.user.roleCodeRequired", new Object[] {}, Locale.getDefault()));
			ValidationServiceException e = new ValidationServiceException(new FaultInfo());
			e.getFaultInfo().getAttributeErrors().add(error);
			throw e;
		}
		Set<RoleDO> requestedRoles = Sets.newHashSet(roleDao.findRolesByCode(context.getScopeId(), roleCodes));

		Set<String> existing = new HashSet<>();
		for (RoleDO role : requestedRoles) {
			existing.add(role.getCode());
		}
		Set<String> requestedCodes = new HashSet<>(roleCodes);
		SetView<String> difference = Sets.difference(requestedCodes, existing);
		if (!difference.isEmpty()) {
			ValidationError error = new ValidationError("code", "Code", messageSource.getMessage(
					"validation.role.invalid.code", new Object[] { difference }, Locale.getDefault()));
			ValidationServiceException e = new ValidationServiceException(new FaultInfo());
			e.getFaultInfo().getAttributeErrors().add(error);
			throw e;
		}

		mergeRoles(context.getUserId(), context.getScopeId(), userId, requestedRoles);
	}

	@Override
	public void mergeUserRoles(ServiceContext context, Long userId, Collection<Long> roleIds) {
		List<RoleDO> roles = roleDao.findById(roleIds);
		mergeRoles(context.getUserId(), context.getScopeId(), userId, new HashSet<>(roles));
	}

	private void mergeRoles(Long conferingUserId, Long scopeId, Long userId, Set<RoleDO> roles) {
		Set<RoleDO> delegatableRoles = Sets.newHashSet(roleDao.findDelegatableRoles(conferingUserId, scopeId));

		SetView<RoleDO> nonDelegatableRoles = Sets.difference(roles, delegatableRoles);

		if (nonDelegatableRoles.isEmpty()) {
			SetView<RoleDO> rolesToAdd = Sets.intersection(delegatableRoles, roles);
			SetView<RoleDO> rolesToDelete = Sets.difference(delegatableRoles, rolesToAdd);
			UserDO user = userDao.getById(userId);
			for (RoleDO role : rolesToAdd) {
				UserRoleDO userRole = userRoleDao.getUserRoleByUserIdAndRoleId(userId, role.getRoleId());
				if (userRole == null) {
					userRole = new UserRoleDO();
					userRole.setUser(user);
					userRole.setRole(role);
					userRoleDao.create(userRole);
				}
			}

			for (RoleDO role : rolesToDelete) {
				UserRoleDO userRole = userRoleDao.getUserRoleByUserIdAndRoleId(userId, role.getRoleId());
				if (userRole != null) {
					userRoleDao.delete(userRole);
				}
			}
		} else {
			StringBuilder sb = new StringBuilder();
			Iterator<RoleDO> i = nonDelegatableRoles.iterator();
			while (i.hasNext()) {
				RoleDO role = i.next();
				sb.append(role.getName());
				if (i.hasNext()) {
					sb.append(", ");
				}
			}
			UserDO user = userDao.getById(conferingUserId);
			throw new AuthorizationException(messageSource.getMessage("validation.userRole.delegationNotAllowed",
					new Object[] { user.getUsername(), sb.toString() }, Locale.getDefault()));
		}
	}

	public void setRoleDao(RoleDAO roleDao) {
		this.roleDao = roleDao;
	}

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

	public void setUserRoleDao(UserRoleDAO userRoleDao) {
		this.userRoleDao = userRoleDao;
	}
}
