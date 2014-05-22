package net.techreadiness.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;
import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.EntityFieldDAO;
import net.techreadiness.persistence.dao.ExtDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.dao.ScopeExtDAO;
import net.techreadiness.persistence.dao.UserCasDAO;
import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.dao.UserOrgDAO;
import net.techreadiness.persistence.dao.UserRoleDAO;
import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.persistence.domain.PermissionDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeExtDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserRoleDO;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.security.PermissionCode;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Permission;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;
import net.techreadiness.util.PasswordComplexityEvaluator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

@WebService
@Service
@Transactional
public class UserServiceImpl extends BaseServiceWithValidationAndExt<UserDO, AbstractAuditedBaseEntityWithExt<UserDO>>
implements UserService {

	@Inject
	@Qualifier("userExtDAOImpl")
	private ExtDAO<UserDO, AbstractAuditedBaseEntityWithExt<UserDO>> userExtDAO;

	@Inject
	private UserDAO userDAO;
	@Inject
	private UserOrgDAO userOrgDAO;
	@Inject
	private UserCasDAO userCasDAO;
	@Inject
	private ScopeDAO scopeDAO;
	@Inject
	private ScopeExtDAO scopeExtDAO;
	@Inject
	private UserRoleService userRoleService;
	@Inject
	private UserOrgService userOrgService;
	@Inject
	private EntityFieldDAO entityFieldDAO;
	@Inject
	private UserRoleDAO userRoleDAO;

	@PersistenceContext
	private EntityManager em;

	private static final DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
	private static final String PASSWORD_COMPLEXITY = "passwordComplexity";
	public static final String RESET_PASSWORD = "ea756843f4a446e3063e5606dc407ec4";

	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	@Override
	@Transactional(readOnly = true)
	public User getByUsername(ServiceContext context, String username) {
		return getMappingService().map(userDAO.findByUsername(username, true));
	}

	@Override
	@WebMethod(exclude = true)
	public User getById(ServiceContext context, Long userId) {
		return getMappingService().map(userDAO.getById(userId));
	}

	@Override
	@WebMethod(exclude = true)
	public boolean userActive(ServiceContext context) {

		UserDO user = userDAO.findByUsername(context.getUserName(), true);
		if (null == user) {
			return false;
		}
		if (null != user.getDeleteDate() || null != user.getDisableDate()) {
			return false;
		}
		Date now = new Date();
		if (null != user.getActiveBeginDate()) {
			if (!now.after(user.getActiveBeginDate())) {
				return false;
			}
		}
		if (null != user.getActiveEndDate()) {
			if (!now.before(user.getActiveEndDate())) {
				return false;
			}
		}
		return true;
	}

	@Override
	@WebMethod(exclude = true)
	@Transactional(readOnly = true)
	@Cacheable(value = "hasPermission")
	public boolean hasPermission(ServiceContext context, final PermissionCode... permissionCodes) {
		if (context == null || context.getUserId() == null) {
			return false;
		}
		Collection<GrantedAuthority> authorities = getGrantedAuthorities(context);
		return userDAO.hasPermission(permissionCodes, authorities, context.getScopeId());
	}

	@Override
	@Transactional(readOnly = true)
	@WebMethod(exclude = true)
	public boolean hasAccessToOrg(ServiceContext context, Long userId, Long orgId) {
		return userDAO.hasAccessToOrg(userId, orgId);
	}

	private Collection<GrantedAuthority> getGrantedAuthorities(ServiceContext context) {
		if (context == null) {
			return Collections.emptySet();
		}
		Collection<GrantedAuthority> authorities = Lists.newArrayList();
		List<UserRoleDO> userRoles = userRoleDAO.findUserRolesByUser(context.getUserId());
		for (UserRoleDO userRole : userRoles) {
			authorities.add(new SimpleGrantedAuthority(String.valueOf(userRole.getRole().getRoleId())));
		}
		return authorities;
	}

	@Override
	@WebMethod(exclude = true)
	public List<Permission> findAvailablePermissions(ServiceContext context) {
		List<PermissionDO> permissionDOs = userDAO.findPermissionsByRoles(getGrantedAuthorities(context),
				context.getScopeId());
		return getMappingService().mapFromDOList(permissionDOs);
	}

	@Override
	/*
	 * at CoreFeature(CORE_USER) at CoreSecured({ CORE_CONFIG_USER_DELETE })
	 */
	@WebMethod(exclude = true)
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = UserDO.class)
	public void delete(ServiceContext context, User user) {

		UserDO userDO = userDAO.getById(user.getUserId());
		// do not reset delete date if already listed with a date
		if (userDO.getDeleteDate() == null) {
			userDAO.delete(userDO);
		}
		user.setDeleteDate(userDO.getDeleteDate());
	}

	@Override
	/*
	 * at CoreFeature(CORE_USER) at CoreSecured({ CORE_CONFIG_USER_DELETE })
	 */
	@WebMethod(exclude = true)
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = UserDO.class)
	public void unDelete(ServiceContext context, User user) {

		UserDO userDO = userDAO.getById(user.getUserId());
		// do not undelete if already cleared
		if (userDO.getDeleteDate() != null) {
			userDAO.unDelete(userDO);
		}
		user.setDeleteDate(null);
	}

	@Override
	@WebMethod(exclude = true)
	public List<User> findAllUsersWithRole(ServiceContext context, Long roleId) {
		return getMappingService().mapFromDOList(userDAO.findAllUsersWithRole(roleId));
	}

	@Override
	@WebMethod(exclude = true)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserDO.class)
	public User disableUser(ServiceContext context, Long userId, Date disableDate, String reason) {
		UserDO userDO = userDAO.getById(userId);
		if (disableDate == null) {
			disableDate = new Date();
		}

		userDO.setDisableDate(DateUtils.truncate(disableDate, Calendar.DAY_OF_MONTH));
		userDO.setDisableReason(reason);

		List<ValidationError> errors = performValidation(userDO.getAsMap(), userDO.getScope().getScopeId(),
				EntityTypeCode.USER);
		errors.addAll(performCrossFieldValidation(context, userDO.getAsMap(), userDO.getScope().getScopeId(),
				EntityTypeCode.USER));
		if (!errors.isEmpty()) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage("User failed validation.");
			faultInfo.setAttributeErrors(errors);
			throw new ValidationServiceException(faultInfo);
		}
		return getMappingService().map(userDAO.update(userDO));
	}

	@Override
	@WebMethod(exclude = true)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserDO.class)
	public User enableUser(ServiceContext context, Long userId) {
		UserDO userDO = userDAO.getById(userId);
		userDO.setDisableDate(null);
		userDO.setDisableReason(null);
		return getMappingService().map(userDAO.update(userDO));
	}

	@Override
	@Transactional(readOnly = true)
	@WebMethod(exclude = true)
	public User getNew(ServiceContext context) {
		User user = new User();

		Scope scope = getMappingService().map(scopeDAO.getScopeForUsers(context.getScopeId()));

		user.setScope(scope);

		return user;
	}

	@Override
	@Transactional(readOnly = true)
	@WebMethod(exclude = true)
	public boolean isUsernameUnique(ServiceContext context, String username, Long userId) throws ServiceException {
		UserDO userDO = userDAO.findByUsername(username, true);
		if (userDO == null) {
			return true;
		}
		if (userDO.getUserId().equals(userId)) {
			return true;
		}
		return false;
	}

	@Override
	@WebMethod(exclude = true)
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_USER_CREATE)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserDO.class)
	public User create(ServiceContext context, User user) throws ValidationServiceException, ServiceException {
		UserDO userDO = getMappingService().map(user);
		trimEmailAndUsername(userDO);
		ScopeDO scopeDO = scopeDAO.getScopeForUsers(context.getScopeId());

		userDO.setScope(scopeDO);
		userDO.setSelectedScope(scopeDAO.getById(context.getScopeId()));
		validate(context, userDO, userDO.getExtAttributes(), new ArrayList<ValidationError>());
		userDAO.persist(userDO);
		if (userDO.isExtAttributesNull()) {
			userDO.setExtAttributes(new HashMap<String, String>());
		}
		storeExtFields(context, userDO, userExtDAO, EntityTypeCode.USER, userDO.getScope().getScopeId());
		userCasDAO.create(userDO, RESET_PASSWORD);
		return getMappingService().map(userDO);
	}

	@Override
	@WebMethod(exclude = true)
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_USER_CREATE)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserDO.class)
	public User createFromMap(ServiceContext context, Map<String, String> map) throws ValidationServiceException,
	ServiceException {

		UserDO userDO = userFromMap(context, map);
		trimEmailAndUsername(userDO);

		try {
			if (StringUtils.isEmpty(map.get("activeBeginDate"))) {
				userDO.setActiveBeginDate(null);
			} else {
				userDO.setActiveBeginDate(formatter.parse(map.get("activeBeginDate")));
			}
			if (StringUtils.isEmpty(map.get("activeEndDate"))) {
				userDO.setActiveEndDate(null);
			} else {
				userDO.setActiveEndDate(formatter.parse(String.valueOf(map.get("activeEndDate"))));
			}
		} catch (ParseException pe) {
			throw new ServiceException(messageSource.getMessage("validation.user.activeDates",
					new Object[] { "MM/DD/YYYY" }, null), pe);
		}

		validate(context, userDO, map, new ArrayList<ValidationError>());
		userDAO.persist(userDO);
		if (userDO.isExtAttributesNull()) {
			userDO.setExtAttributes(new HashMap<String, String>());
		}
		storeExtFields(context, userDO, userExtDAO, EntityTypeCode.USER, userDO.getScope().getScopeId());

		userCasDAO.create(userDO, RESET_PASSWORD);
		return getMappingService().map(userDAO.getById(userDO.getUserId()));
	}

	private UserDO userFromMap(ServiceContext context, Map<String, String> map) {
		UserDO userDO = new UserDO();
		ScopeDO scopeDO = scopeDAO.getScopeForUsers(context.getScopeId());

		userDO.setScope(scopeDO);
		userDO.setSelectedScope(scopeDAO.getById(context.getScopeId()));

		userDO.setUsername(map.get("username") == null ? null : String.valueOf(map.get("username")));
		userDO.setFirstName(map.get("firstName") == null ? null : String.valueOf(map.get("firstName")));
		userDO.setLastName(map.get("lastName") == null ? null : String.valueOf(map.get("lastName")));
		userDO.setEmail(map.get("email") == null ? null : String.valueOf(map.get("email")));

		return userDO;
	}

	@Override
	@WebMethod(exclude = true)
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_USER_CREATE)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserDO.class)
	public User createFromMapWithRoles(ServiceContext context, Map<String, String> map, List<Role> roles, List<Org> orgs)
			throws ValidationServiceException, ServiceException {

		UserDO userDO = userFromMap(context, map);
		trimEmailAndUsername(userDO);
		List<ValidationError> errors = performValidation(userDO.getAsMap(), userDO.getScope().getScopeId(),
				EntityTypeCode.USER);
		errors.addAll(performCrossFieldValidation(context, userDO.getAsMap(), userDO.getScope().getScopeId(),
				EntityTypeCode.USER));

		if (CollectionUtils.isEmpty(roles)) {
			String message = messageSource.getMessage("user.roles.needOne", null, Locale.getDefault());
			errors.add(new ValidationError("roleValidation", "role", message, "validation.user.roles.needOne", message));
		}
		if (CollectionUtils.isEmpty(orgs)) {
			String message = messageSource.getMessage("user.orgs.needOne", null, Locale.getDefault());
			errors.add(new ValidationError("orgValidation", "org", message, "validation.user.orgs.needOne", message));
		}

		if (!errors.isEmpty()) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage("User failed validation.");
			faultInfo.setAttributeErrors(errors);
			throw new ValidationServiceException(faultInfo);
		}

		User user = createFromMap(context, map);
		for (Role role : roles) {
			userRoleService.persist(context, user.getUserId(), role.getRoleId());
		}
		for (Org org : orgs) {
			userOrgService.persist(context, user.getUserId(), org.getOrgId());
		}
		UserDO find = em.find(UserDO.class, user.getUserId());
		em.refresh(find);
		return getMappingService().getMapper().map(find, User.class);
	}

	private static void trimEmailAndUsername(UserDO userDO) {
		if (null != userDO.getEmail()) {
			userDO.setEmail(userDO.getEmail().trim());
		}
		if (null != userDO.getUsername()) {
			userDO.setUsername(userDO.getUsername().trim());
		}
	}

	@Override
	@WebMethod(exclude = true)
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_USER_UPDATE)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserDO.class)
	public User update(ServiceContext context, User user) {
		User oldUser;
		if (user.getUserId() == null) {
			oldUser = getByUsername(context, user.getUsername());
		} else {
			oldUser = getById(context, user.getUserId());
		}

		if (null == oldUser) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage("Unable to find user.");
			throw new ValidationServiceException(faultInfo);
		}

		ScopeDO scope = scopeDAO.getScopeForUsers(context.getScopeId());
		if (!userDAO.isUserInScope(scope.getScopeId(), oldUser.getUserId())) {
			throw new AuthorizationException(messageSource.getMessage("validation.user.updateWrongScope", new Object[] {
					oldUser.getScope().getName(), context.getScopeName() }, Locale.getDefault()));
		}

		UserDO userDO = getMappingService().map(user);
		trimEmailAndUsername(userDO);
		userDO.setScope(scope);
		userDO.setUserId(oldUser.getUserId());

		validate(context, userDO, userDO.getExtAttributes(), new ArrayList<ValidationError>());

		if (userDO.isExtAttributesNull()) {
			userDO.setExtAttributes(new HashMap<String, String>());
		}
		copyExtFieldsToCore(context, userDO);
		storeExtFields(context, userDO, userExtDAO, EntityTypeCode.USER, userDO.getScope().getScopeId());

		userDO = userDAO.updateAccounts(userDO, oldUser.getUsername());

		return getMappingService().map(userDO);
	}

	private void validate(ServiceContext context, UserDO userDO, Map<String, String> extAttributes,
			List<ValidationError> errors) throws ValidationServiceException {

		Map<String, String> userMap = new HashMap<>();

		userMap.putAll(extAttributes);
		userMap.putAll(userDO.getAsMap());

		List<ValidationError> errorsVal = performValidation(userMap, userDO.getScope().getScopeId(), EntityTypeCode.USER);
		errors.addAll(errorsVal);

		if (!isUsernameUnique(context, userDO.getUsername(), userDO.getUserId())) {
			String errorMessage = messageSource.getMessage("validation.user.username.alreadyExists",
					new Object[] { userDO.getUsername() }, null);
			EntityFieldDO usernameField = entityFieldDAO.findByScopeAndTypeAndCode(context.getScopeId(),
					EntityTypeCode.USER, "username");
			errors.add(new ValidationError(usernameField.getCode(), usernameField.getName(), errorMessage,
					"validation.user.username.alreadyExists", errorMessage));
		}

		if (userDO.getActiveBeginDate() != null && userDO.getActiveEndDate() != null) {
			if (userDO.getActiveEndDate().before(userDO.getActiveBeginDate())) {
				EntityFieldDO activeEndDate = entityFieldDAO.findByScopeAndTypeAndCode(context.getScopeId(),
						EntityTypeCode.USER, "activeEndDate");
				EntityFieldDO activeStartDate = entityFieldDAO.findByScopeAndTypeAndCode(context.getScopeId(),
						EntityTypeCode.USER, "activeBeginDate");
				String errorMessage = messageSource.getMessage("validation.user.activeDateRange", new Object[] {
						activeEndDate.getName(), activeStartDate.getName() }, null);
				errors.add(new ValidationError(activeEndDate.getCode(), activeEndDate.getName(), errorMessage,
						"validation.user.activeDateRange", errorMessage));
			}
		}

		if (!errors.isEmpty()) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage("User failed validation.");
			faultInfo.setAttributeErrors(errors);
			throw new ValidationServiceException(faultInfo);
		}
	}

	@Override
	@WebMethod(exclude = true)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserDO.class)
	public void changePassword(ServiceContext context, String username, String password, String confirmPassword) {
		List<ValidationError> errors = new ArrayList<>();

		if (password == null || password.isEmpty() || password.length() < 8) {
			String errorMessage = messageSource.getMessage("validation.user.password.minLength", null, null);
			errors.add(new ValidationError("passwordField", "passwordField", errorMessage,
					"validation.user.password.minLength", errorMessage));
		}
		if (!confirmPassword.equals(password)) {
			String errorMessage = messageSource.getMessage("validation.user.password.notMatch", null, null);
			errors.add(new ValidationError("confirmPasswordField", "confirmPasswordField", errorMessage,
					"validation.user.password.notMatch", errorMessage));
		}

		User user = getByUsername(context, username);

		ScopeExtDO complexitySEDO = scopeExtDAO.getLowestExistingConfigurationItem(user.getScope().getScopeId(),
				PASSWORD_COMPLEXITY);

		if (complexitySEDO == null) {
			// default to complexity of 3 if none found
			complexitySEDO = new ScopeExtDO();
			complexitySEDO.setValue("3");
		}

		int complexity = PasswordComplexityEvaluator.getPasswordComplexity(password);
		int configuredComplexity = 0;
		if (StringUtils.isNotBlank(complexitySEDO.getValue())) {
			configuredComplexity = Integer.valueOf(complexitySEDO.getValue());
		}

		if (complexity < configuredComplexity) {
			String errorMessage = messageSource.getMessage("validation.user.password.complexity", null, null);
			errors.add(new ValidationError("passwordField", "passwordField", errorMessage,
					"validation.user.password.complexity", errorMessage));
		}

		if (!errors.isEmpty()) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage("User failed validation.");
			faultInfo.setAttributeErrors(errors);
			throw new ValidationServiceException(faultInfo);
		}

		userDAO.changePassword(username, BCrypt.hashpw(password, BCrypt.gensalt()));
	}

	@Override
	@WebMethod(exclude = true)
	@CoreDataModificationStatus(modificationType = ModificationType.OTHER, entityClass = UserDO.class)
	public void resetPassword(ServiceContext context, Long userId) {
		UserDO userDO = userDAO.getById(userId);

		userDAO.resetPassword(userDO);
		log.info("ZZZ.ResetPasswordSent. User: " + userDO.getUsername() + " Email: " + userDO.getEmail());
	}

	@Override
	@WebMethod(exclude = true)
	public void newAccountPassword(ServiceContext context, User user) {
		UserDO userDO = userDAO.getById(user.getUserId());

		userDAO.newAccountPassword(userDO);
		log.info("ZZZ.NewAccountSent. User: " + userDO.getUsername() + " Email: " + userDO.getEmail());
	}

	@Override
	@WebMethod(exclude = true)
	public void forgotUsername(ServiceContext context, String email) {
		userDAO.forgotUsername(email);
	}

	@Override
	@WebMethod(exclude = true)
	public boolean isTokenValid(ServiceContext context, String username, String token) {
		return userDAO.isTokenValid(username, token);
	}

	@Override
	@WebMethod(exclude = true)
	public void clearTokens(ServiceContext context, String username) {
		userDAO.clearTokens(username);
	}

	@Override
	@Transactional(readOnly = true)
	@WebMethod(exclude = true)
	public boolean isUsernameEmailPairValid(ServiceContext context, String username, String email) throws ServiceException {
		return userDAO.isUsernameEmailPairValid(username, email);
	}

	@Override
	@WebMethod(exclude = true)
	public boolean isEmailInUse(ServiceContext context, String email) {
		return userDAO.isEmailInUse(email);
	}

	@Override
	@WebMethod(exclude = true)
	public void updateUserSelectedScopeId(ServiceContext context) {

		UserDO userDO = userDAO.getById(context.getUserId());
		ScopeDO scopeDO = scopeDAO.getById(context.getScopeId());

		userDO.setSelectedScope(scopeDO);

		userDAO.update(userDO);
	}

	@Override
	@WebMethod(exclude = true)
	public boolean hasAccessToOrgByCode(ServiceContext context, Long userId, String orgCode) {
		return userDAO.hasAccessToOrg(userId, orgCode);
	}

	@Override
	@WebMethod(exclude = true)
	public String getCurrentResetEmailText(ServiceContext context, Long userId) {
		return userDAO.getCurrentResetEmailText(userDAO.getByUserId(userId));
	}

	@Override
	@WebMethod(exclude = true)
	public List<Org> findOrgsForUsers(ServiceContext context, Long orgId, Long scopeId, Collection<Long> userIds) {
		return getMappingService().mapFromDOList(userOrgDAO.findOrgsForUsers(scopeId, orgId, userIds));
	}

	@Override
	@WebMethod(exclude = true)
	public List<Role> findRolesForUsers(ServiceContext context, Long scopeId, Collection<Long> userIds) {
		return getMappingService().mapFromDOList(userRoleDAO.findRolesForUsers(scopeId, userIds));
	}

	@Override
	public Collection<User> findById(ServiceContext context, Collection<Long> userIds) {
		if (userIds == null || userIds.isEmpty()) {
			return Collections.emptySet();
		}
		ScopeDO scope = scopeDAO.getScopeForUsers(context.getScopeId());
		StringBuilder sb = new StringBuilder();
		sb.append("select user from UserDO user ");
		sb.append("where user.scope.scopeId = :scopeId ");
		sb.append("and user.userId in (:userIds)");
		TypedQuery<UserDO> query = em.createQuery(sb.toString(), UserDO.class);
		query.setParameter("scopeId", scope.getScopeId());
		query.setParameter("userIds", userIds);
		return getMappingService().getMapper().mapAsSet(query.getResultList(), User.class);
	}
}
