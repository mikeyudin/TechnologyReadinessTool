package net.techreadiness.persistence.dao;

import java.util.Collection;
import java.util.List;

import net.techreadiness.persistence.domain.PermissionDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.security.PermissionCode;

import org.springframework.security.core.GrantedAuthority;

public interface UserDAO extends BaseDAO<UserDO> {

	public UserDO getByUserId(final Long userId);

	/**
	 * Retrieves a {@link UserDO} based on username.
	 * 
	 * @param username
	 *            The username of the user
	 * @param includeDeleted
	 *            Whether we want to also include deleted users.
	 * @return The {@link UserDO} that matches the username.
	 */
	UserDO findByUsername(String username, boolean includeDeleted);

	boolean isUserInScope(Long scopeId, Long userId);

	/**
	 * Determine is a user has specified permissions.
	 * 
	 * @param permissionCodes
	 *            an array of permission codes required for execution. conjunction only.
	 * @param authorities
	 *            the set of roles the user has been granted
	 * @param scopeId
	 *            the scope point (along with ancestors) to constraint roles
	 * @return true if permission is in any role or user-assigned permissions.
	 */
	boolean hasPermission(PermissionCode[] permissionCodes, Collection<GrantedAuthority> authorities, Long scopeId);

	List<UserDO> findAllUsersWithRole(Long roleId);

	/**
	 * change password in authentication store.
	 * 
	 * @param username
	 * @param formattedPassword
	 *            non-plaintext password, formatted into appropriate encoding (e.g. MD5 hash)
	 */
	void changePassword(String username, String formattedPassword);

	void resetPassword(UserDO userDO);

	String getCurrentResetEmailText(UserDO userDO);

	void newAccountPassword(UserDO userDO);

	void forgotUsername(String email);

	boolean hasAccessToOrg(Long userId, Long orgId);

	boolean hasAccessToOrg(Long userId, String orgCode);

	boolean isTokenValid(String username, String token);

	void clearTokens(String username);

	boolean isUsernameEmailPairValid(String username, String email);

	boolean isEmailInUse(String email);

	List<PermissionDO> findPermissionsByRoles(Collection<GrantedAuthority> roles, Long scopeId);

	UserDO updateAccounts(UserDO user, String oldUsername);

	void unDelete(UserDO userDO);
}
