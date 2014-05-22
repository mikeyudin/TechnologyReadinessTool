package net.techreadiness.service;

import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.object.UserRole;

@WebService
public interface UserRoleService extends BaseService {
	UserRole persist(ServiceContext context, Long userId, Long roleId);

	void delete(ServiceContext context, Long userId, Long roleId);

	void deleteAllUserRoles(ServiceContext context, Long userId);

	/**
	 * Merge the state of the user with the provided role codes. The codes should be for roles the context user has
	 * delegation permissions to. Any roles that the context user has delegation permissions to and are not included in the
	 * list of codes will be removed from the user with the userId parameter.
	 * 
	 * If the context user has delegation permissions to roles A, B and C and the {@code roleCodes} parameter includes roles
	 * A. The user represented by the {@code userId} currently has the B role. This method would remove B and C (C would be a
	 * no-op) and add role A.
	 * 
	 * @param context
	 *            The context of the calling user.
	 * @param userId
	 *            The id of the user to modify roles
	 * @param roleCodes
	 *            The roles the user should have after this method returns. This is limited to the delegation permissions of
	 *            the context user.
	 * @throws AuthorizationException
	 *             When the context user does not have permission to modify other user's roles or includes a role that they
	 *             can not delegate to another user.
	 */
	void mergeUserRoles(ServiceContext context, Long userId, List<String> roleCodes);

	void mergeUserRoles(ServiceContext context, Long userId, Collection<Long> roleIds);
}
