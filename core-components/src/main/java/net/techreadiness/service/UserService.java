package net.techreadiness.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import net.techreadiness.security.PermissionCode;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Permission;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.User;

@WebService
public interface UserService extends BaseService {

	User getByUsername(ServiceContext context, String username);

	User getById(ServiceContext context, Long userId);

	Collection<User> findById(ServiceContext context, Collection<Long> userIds);

	boolean userActive(ServiceContext context);

	boolean hasPermission(ServiceContext context, PermissionCode... permissions);

	boolean hasAccessToOrg(ServiceContext context, Long userId, Long orgId);

	boolean hasAccessToOrgByCode(ServiceContext context, Long userId, String orgCode);

	List<Permission> findAvailablePermissions(ServiceContext context);

	boolean isUsernameUnique(ServiceContext context, String username, Long userId);

	void delete(ServiceContext context, User user);

	void unDelete(ServiceContext context, User user);

	List<User> findAllUsersWithRole(ServiceContext context, Long roleId);

	User disableUser(ServiceContext context, Long userId, Date disableDate, String reason);

	User enableUser(ServiceContext context, Long userId);

	/**
	 * Creates a new user in the DB and returns the newly created user.
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param user
	 *            The {@code User} to put in the DB.
	 * @return The newly created {@code User}, with userId populated.
	 * @throws ValidationServiceException
	 *             Thrown if user can't be saved due to validation errors.
	 * @throws ServiceException
	 *             Thrown for any other error condition.
	 */
	User create(ServiceContext context, User user) throws ValidationServiceException, ServiceException;

	User createFromMap(ServiceContext context, Map<String, String> map) throws ValidationServiceException, ServiceException;

	User createFromMapWithRoles(ServiceContext context, Map<String, String> map, List<Role> roles, List<Org> orgs)
			throws ValidationServiceException, ServiceException;

	User getNew(ServiceContext context);

	User update(ServiceContext context, User user);

	void changePassword(ServiceContext context, String username, String password, String confirmPassword);

	void resetPassword(ServiceContext context, Long userId);

	String getCurrentResetEmailText(ServiceContext context, Long userId);

	void newAccountPassword(ServiceContext context, User user);

	void forgotUsername(ServiceContext context, String email);

	boolean isTokenValid(ServiceContext context, String username, String token);

	boolean isUsernameEmailPairValid(ServiceContext context, String username, String email);

	boolean isEmailInUse(ServiceContext context, String email);

	void clearTokens(ServiceContext context, String username);

	void updateUserSelectedScopeId(ServiceContext context);

	List<Org> findOrgsForUsers(ServiceContext context, Long orgId, Long scopeId, Collection<Long> userIds);

	List<Role> findRolesForUsers(ServiceContext context, Long scopeId, Collection<Long> userIds);
}
