package net.techreadiness.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

import java.util.List;

import net.techreadiness.persistence.dao.RoleDAO;
import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.dao.UserRoleDAO;
import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserRoleDO;
import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.User;
import net.techreadiness.service.object.mapping.MappingServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;

public class UserRoleServiceTest {

	private UserRoleServiceImpl userRoleService;
	private RoleDAO roleDAO;
	private UserRoleDAO userRoleDAO;
	private UserDAO userDAO;

	@Before
	public void setup() {
		userRoleService = new UserRoleServiceImpl();
		roleDAO = Mockito.mock(RoleDAO.class);
		userRoleDAO = Mockito.mock(UserRoleDAO.class);
		userDAO = Mockito.mock(UserDAO.class);
		userRoleService.setRoleDao(roleDAO);
		userRoleService.setUserRoleDao(userRoleDAO);
		userRoleService.setUserDao(userDAO);
		userRoleService.messageSource = Mockito.mock(MessageSource.class);
		userRoleService.setMappingService(new MappingServiceImpl());
	}

	@Test
	public void testPersistNotExist() {
		Mockito.when(userRoleDAO.getUserRoleByUserIdAndRoleId(any(Long.class), any(Long.class))).thenReturn(null);
		RoleDO role = new RoleDO();
		Long roleId = Long.valueOf(3);
		role.setRoleId(roleId);
		UserDO user = new UserDO();
		Mockito.when(roleDAO.getById(any(Long.class))).thenReturn(role);
		Mockito.when(userDAO.getById(any(Long.class))).thenReturn(user);
		Mockito.when(roleDAO.findDelegatableRoles(any(Long.class), any(Long.class))).thenReturn(Lists.newArrayList(role));
		userRoleService.persist(new ServiceContext(), Long.valueOf(1), roleId);
		Mockito.verify(userRoleDAO, times(1)).persist(any(UserRoleDO.class));
	}

	@Test
	public void testPersistExist() {
		UserRoleDO userRole = new UserRoleDO();
		Mockito.when(userRoleDAO.getUserRoleByUserIdAndRoleId(any(Long.class), any(Long.class))).thenReturn(userRole);
		RoleDO role = new RoleDO();
		Long roleId = Long.valueOf(3);
		role.setRoleId(roleId);
		UserDO user = new UserDO();
		userRole.setRole(role);
		userRole.setUser(user);
		Mockito.when(roleDAO.getById(any(Long.class))).thenReturn(role);
		Mockito.when(userDAO.getById(any(Long.class))).thenReturn(user);
		Mockito.when(roleDAO.findDelegatableRoles(any(Long.class), any(Long.class))).thenReturn(Lists.newArrayList(role));
		userRoleService.persist(new ServiceContext(), Long.valueOf(1), roleId);
		Mockito.verify(userRoleDAO, Mockito.never()).persist(any(UserRoleDO.class));
	}

	@Test(expected = AuthorizationException.class)
	public void testPersistRoleNotConferrable() {
		Mockito.when(userRoleDAO.getUserRoleByUserIdAndRoleId(any(Long.class), any(Long.class))).thenReturn(null);
		RoleDO role = new RoleDO();
		Long roleId = Long.valueOf(3);
		role.setRoleId(roleId);
		UserDO user = new UserDO();
		Mockito.when(roleDAO.getById(any(Long.class))).thenReturn(role);
		Mockito.when(userDAO.getById(any(Long.class))).thenReturn(user);
		Mockito.when(roleDAO.findDelegatableRoles(any(Long.class), any(Long.class))).thenReturn(
				Lists.<RoleDO> newArrayList());
		try {
			userRoleService.persist(new ServiceContext(), Long.valueOf(1), roleId);
		} finally {
			Mockito.verify(userRoleDAO, Mockito.never()).persist(any(UserRoleDO.class));
		}
	}

	@Test(expected = ValidationServiceException.class)
	public void testMergeAddNotDelegatable() {
		RoleDO delegatable = new RoleDO();
		delegatable.setRoleId(Long.valueOf(2));
		delegatable.setName("delgatable");

		Mockito.when(roleDAO.findDelegatableRoles(any(Long.class), any(Long.class))).thenReturn(
				Lists.<RoleDO> newArrayList());
		Mockito.when(roleDAO.findRolesByCode(any(Long.class), any(List.class))).thenReturn(Lists.newArrayList(delegatable));
		Mockito.when(userDAO.getById(Long.valueOf(5))).thenReturn(new UserDO());
		try {
			ServiceContext context = new ServiceContext();
			User user = new User();
			user.setUserId(Long.valueOf(5));
			context.setUser(user);
			userRoleService.mergeUserRoles(context, Long.valueOf(1), Lists.<String> newArrayList("foo"));
		} finally {
			Mockito.verify(userRoleDAO, Mockito.never()).persist(any(UserRoleDO.class));
		}
	}

	@Test
	public void testDeleteExists() {
		RoleDO delegatable = new RoleDO();
		Long roleId = Long.valueOf(2);
		delegatable.setRoleId(roleId);
		delegatable.setName("delgatable");

		Mockito.when(userRoleDAO.getUserRoleByUserIdAndRoleId(any(Long.class), any(Long.class)))
				.thenReturn(new UserRoleDO());
		Mockito.when(roleDAO.findDelegatableRoles(any(Long.class), any(Long.class))).thenReturn(
				Lists.newArrayList(delegatable));

		userRoleService.delete(new ServiceContext(), Long.valueOf(1), roleId);
		Mockito.verify(userRoleDAO, times(1)).delete(any(UserRoleDO.class));
	}

	@Test(expected = AuthorizationException.class)
	public void testDeleteExistsNotDelegatable() {
		RoleDO notDelegatable = new RoleDO();
		Long roleId = Long.valueOf(2);
		notDelegatable.setRoleId(roleId);
		notDelegatable.setName("notDelgatable");
		Mockito.when(roleDAO.getById(Matchers.eq(roleId))).thenReturn(notDelegatable);

		Mockito.when(userRoleDAO.getUserRoleByUserIdAndRoleId(any(Long.class), any(Long.class)))
				.thenReturn(new UserRoleDO());
		Mockito.when(roleDAO.findDelegatableRoles(any(Long.class), any(Long.class))).thenReturn(
				Lists.<RoleDO> newArrayList());
		try {
			userRoleService.delete(new ServiceContext(), Long.valueOf(1), roleId);
		} finally {
			Mockito.verify(userRoleDAO, Mockito.never()).delete(any(UserRoleDO.class));
		}
	}
}
