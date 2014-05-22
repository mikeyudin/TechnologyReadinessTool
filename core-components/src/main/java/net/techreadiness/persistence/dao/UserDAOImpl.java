package net.techreadiness.persistence.dao;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.PermissionDO;
import net.techreadiness.persistence.domain.UserCasDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.security.PermissionCode;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.util.EmailService;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

@Repository
public class UserDAOImpl extends BaseDAOImpl<UserDO> implements UserDAO {
	private static final String RESET_PASSWORD = "ea756843f4a446e3063e5606dc407ec4";

	private static final String CORE_EMAIL_RESET_TITLE = "core.email.reset.title";
	private static final String CORE_EMAIL_RESET_TEXT = "core.email.reset.text";

	private static final String CORE_EMAIL_NEWUSER_TITLE = "core.email.newuser.title";
	private static final String CORE_EMAIL_NEWUSER_TEXT = "core.email.newuser.text";

	private static final String CORE_EMAIL_FORGOT_USERNAME_TITLE = "core.email.forgot.username.title";
	private static final String CORE_EMAIL_FORGOT_USERNAME_TEXT = "core.email.forgot.username.text";

	@Value("${app.customer.host}")
	private String host;
	@Value("${app.customer.protocol}")
	private String protocol;
	@Value("${app.customer.port}")
	private String port;
	@Value("${app.customer.contextPath}")
	private String contextPath;

	@Inject
	ScopeDAO scopeDAO;

	@Inject
	ScopeExtDAO scopeExtDAO;

	@Inject
	UserCasDAO userCasDAO;

	@Inject
	private EmailService emailService;

	@Inject
	private MessageSource bundleSource;

	@Override
	public UserDO getByUserId(final Long userId) {

		StringBuilder sb = new StringBuilder();
		sb.append("select distinct u ");
		sb.append("from UserDO u  ");
		sb.append("  join fetch u.scope  ");
		sb.append("where u.userId=:userId ");

		UserDO userDO;
		try {
			userDO = em.createQuery(sb.toString(), UserDO.class).setParameter("userId", userId).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
		return userDO;
	}

	@Override
	public void delete(UserDO user) {
		if (null == user) {
			return;
		}
		// delete only performs a disable, i.e. logical delete
		user.setDeleteDate(new Date());
		update(user);
	}

	@Override
	public void unDelete(UserDO user) {
		if (null == user) {
			return;
		}
		// undelete only performs an enable, i.e. logical undelete
		user.setDeleteDate(null);
		update(user);
	}

	@Override
	public UserDO updateAccounts(UserDO user, String oldUsername) {
		if (null == user) {
			return null;
		}

		if (oldUsername != null && !oldUsername.equals(user.getUsername())) {
			UserCasDO userCasDO = userCasDAO.getByUsername(oldUsername);
			if (userCasDO != null) {
				userCasDO.setUsername(user.getUsername());
				userCasDAO.update(userCasDO);
			}
		}
		return update(user);
	}

	@Override
	public UserDO findByUsername(final String username, final boolean includeDeleted) {

		StringBuilder sb = new StringBuilder();
		sb.append("select distinct u ");
		sb.append("from UserDO u  ");
		sb.append("  left outer join u.userRoles  ");
		sb.append("where u.username=:username ");
		if (!includeDeleted) {
			sb.append("and u.deleteDate is null");
		}

		UserDO userDO;
		try {
			TypedQuery<UserDO> query = em.createQuery(sb.toString(), UserDO.class);
			query.setParameter("username", username);
			userDO = query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
		return userDO;
	}

	@Override
	public boolean hasAccessToOrg(Long userId, Long orgId) {
		TypedQuery<OrgDO> query = em.createQuery("select o from OrgDO o join o.orgTrees ot, UserOrgDO uo "
				+ "where ot.ancestorOrg.orgId = uo.org.orgId " + "and ot.org.orgId = :orgId "
				+ "and uo.user.userId = :userId ", OrgDO.class);

		query.setParameter("orgId", orgId);
		query.setParameter("userId", userId);
		query.setMaxResults(1);

		OrgDO org = getSingleResult(query);
		return org != null;
	}

	@Override
	public boolean hasPermission(PermissionCode[] permissionCodes, Collection<GrantedAuthority> authorities, Long scopeId) {

		if (authorities == null || authorities.isEmpty()) {
			return false;
		}
		if (permissionCodes == null || permissionCodes.length < 1) {
			return false;
		}

		final List<PermissionDO> availablePermissions = findPermissionsByRoles(authorities, scopeId);
		return Iterables.all(Arrays.asList(permissionCodes), new Predicate<PermissionCode>() {
			@Override
			public boolean apply(PermissionCode permissionCode) {
				for (PermissionDO availablePermission : availablePermissions) {
					if (availablePermission.getCode().equalsIgnoreCase(permissionCode.toString())) {
						return true;
					}
				}
				return false;
			}
		});
	}

	@Override
	public List<PermissionDO> findPermissionsByRoles(Collection<GrantedAuthority> roles, Long scopeId) {

		if (null == scopeId || null == roles || roles.isEmpty()) {
			return new ArrayList<>();
		}

		Collection<Long> roleIds = Collections2.transform(roles, new Function<GrantedAuthority, Long>() {
			@Override
			public Long apply(GrantedAuthority authority) {
				return Long.valueOf(authority.getAuthority());
			}
		});

		StringBuilder sb = new StringBuilder();
		sb.append(" select p ");
		sb.append(" from PermissionDO p, RolePermissionDO rp, RoleDO r, ScopeTreeDO st ");
		sb.append(" where r.roleId in (:roleIds)");
		sb.append("  and st.ancestorScope.scopeId = r.scope.scopeId");
		sb.append("  and st.scope.scopeId = :scopeid");
		sb.append("  and r = rp.role ");
		sb.append("  and rp.permission = p ");

		TypedQuery<PermissionDO> query = em.createQuery(sb.toString(), PermissionDO.class);
		query.setParameter("roleIds", roleIds);
		query.setParameter("scopeid", scopeId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return query.getResultList();
	}

	@Override
	public List<UserDO> findAllUsersWithRole(Long roleId) {

		if (null == roleId) {
			return new ArrayList<>();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select ur.user ");
		sb.append(" from UserRoleDO ur ");
		sb.append(" where ur.role.roleId = :roleid");

		TypedQuery<UserDO> query = em.createQuery(sb.toString(), UserDO.class);
		query.setParameter("roleid", roleId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return query.getResultList();
	}

	public List<UserDO> findAllUsersWithEmail(String email) {
		if (null == email) {
			return new ArrayList<>();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(" select u ");
		sb.append(" from UserDO u ");
		sb.append(" where u.email=:email");

		TypedQuery<UserDO> query = em.createQuery(sb.toString(), UserDO.class);
		query.setParameter("email", email);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return query.getResultList();
	}

	@Override
	public void changePassword(String username, final String formattedPassword) {
		UserCasDO userCasDO = userCasDAO.getByUsername(username);
		if (userCasDO != null) {
			userCasDO.setPassword(formattedPassword);
			userCasDO.setFailedAttempts(0);
			userCasDAO.update(userCasDO);
		}
	}

	@Override
	public void resetPassword(UserDO userDO) {
		changePassword(userDO.getUsername(), RESET_PASSWORD);
		String token = setResetToken(userDO);
		sendPasswordResetEmail(userDO, token);
	}

	@Override
	public void newAccountPassword(UserDO userDO) {
		changePassword(userDO.getUsername(), RESET_PASSWORD);
		String token = setResetToken(userDO);
		sendNewAccountEmail(userDO, token);
	}

	@Override
	public void forgotUsername(String email) {
		List<UserDO> users = findAllUsersWithEmail(email);
		sendForgotUsernameEmail(email, users);
	}

	private void sendForgotUsernameEmail(String email, List<UserDO> users) {
		doSendMail(email, users, CORE_EMAIL_FORGOT_USERNAME_TITLE, CORE_EMAIL_FORGOT_USERNAME_TEXT);
	}

	private void sendPasswordResetEmail(UserDO userDO, String token) {
		doSendMail(userDO, token, CORE_EMAIL_RESET_TITLE, CORE_EMAIL_RESET_TEXT);
	}

	private void sendNewAccountEmail(UserDO userDO, String token) {
		doSendMail(userDO, token, CORE_EMAIL_NEWUSER_TITLE, CORE_EMAIL_NEWUSER_TEXT);
	}

	private void doSendMail(String email, List<UserDO> users, String title_key, String key) {
		if (email != null) {
			String title = bundleSource.getMessage(title_key, null, Locale.ENGLISH);
			emailService.sendTextEmail(email, title, getForgotSubstitutedEmailText(email, users, buildUrlString(), key));
		}
	}

	private void doSendMail(UserDO userDO, String token, String title_key, String key) {
		if (userDO.getEmail() != null) {
			String title = bundleSource.getMessage(title_key, null, Locale.ENGLISH);
			emailService.sendTextEmail(userDO.getEmail(), title,
					getTokenSubstitutedEmailText(userDO, token, buildUrlString(), key));
		}
	}

	private String buildUrlString() {
		URL url;
		try {
			url = new URL(protocol.trim(), host.trim(), Integer.valueOf(port.trim()), contextPath.trim());
			return url.toExternalForm();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private String setResetToken(UserDO userDO) {
		String token = String.valueOf(UUID.randomUUID());
		// as tokens are added, they are given a postfix index to help make sure
		// we only overwrite the oldest token if necessary.
		// tokenIndex [pos:val]
		Map<String, String> tokenIndexMap = determineTokenIndexMap(userDO);

		switch (Integer.valueOf(tokenIndexMap.get("position"))) {
		case 1:
			userDO.setResetToken1(token + ":" + tokenIndexMap.get("tokenIndex"));
			break;
		case 2:
			userDO.setResetToken2(token + ":" + tokenIndexMap.get("tokenIndex"));
			break;
		case 3:
			userDO.setResetToken3(token + ":" + tokenIndexMap.get("tokenIndex"));
			break;
		case 4:
			userDO.setResetToken4(token + ":" + tokenIndexMap.get("tokenIndex"));
			break;
		case 5:
			userDO.setResetToken5(token + ":" + tokenIndexMap.get("tokenIndex"));
			break;
		default:
			userDO.setResetToken1(token + ":" + tokenIndexMap.get("tokenIndex"));
			break;
		}
		update(userDO);
		return token + ":" + tokenIndexMap.get("tokenIndex");
	}

	private static Map<String, String> determineTokenIndexMap(UserDO userDO) {
		Map<String, String> map = Maps.newHashMap();
		Integer[] indexes = new Integer[5];

		if (userDO.getResetToken1() != null && !userDO.getResetToken1().isEmpty()) {
			String[] splits = userDO.getResetToken1().split(":");
			indexes[0] = Integer.valueOf(splits[1]);
		} else {
			indexes[0] = 0;
		}
		if (userDO.getResetToken2() != null && !userDO.getResetToken2().isEmpty()) {
			String[] splits = userDO.getResetToken2().split(":");
			indexes[1] = Integer.valueOf(splits[1]);
		} else {
			indexes[1] = 0;
		}
		if (userDO.getResetToken3() != null && !userDO.getResetToken3().isEmpty()) {
			String[] splits = userDO.getResetToken3().split(":");
			indexes[2] = Integer.valueOf(splits[1]);
		} else {
			indexes[2] = 0;
		}
		if (userDO.getResetToken4() != null && !userDO.getResetToken4().isEmpty()) {
			String[] splits = userDO.getResetToken4().split(":");
			indexes[3] = Integer.valueOf(splits[1]);
		} else {
			indexes[3] = 0;
		}
		if (userDO.getResetToken5() != null && !userDO.getResetToken5().isEmpty()) {
			String[] splits = userDO.getResetToken5().split(":");
			indexes[4] = Integer.valueOf(splits[1]);
		} else {
			indexes[4] = 0;
		}
		int highest = 0;
		for (int i = 0; i < 5; i++) {
			if (indexes[i] > highest) {
				highest = indexes[i];
			}
		}
		int lowestPosition = 1;
		int lowestValue = Integer.MAX_VALUE;
		for (int i = 0; i < 5; i++) {
			if (indexes[i] < lowestValue) {
				lowestValue = indexes[i];
				lowestPosition = i + 1;
			}
		}
		map.put("position", String.valueOf(lowestPosition));
		map.put("tokenIndex", String.valueOf(highest + 1));
		return map;
	}

	public String getTokenSubstitutedEmailText(UserDO userDO, String token, String urlString, String key) {

		// core.email.text=Hello ${username}. Please Reset Password. Use
		// ${url}/changeresetpassword?username=${username}&token=${token}
		StringTemplate stDetail = new StringTemplate(bundleSource.getMessage(key, null, Locale.ENGLISH),
				DefaultTemplateLexer.class);

		stDetail.setAttribute("username", userDO.getUsername());
		stDetail.setAttribute("firstname", userDO.getFirstName());
		stDetail.setAttribute("lastname", userDO.getLastName());
		stDetail.setAttribute("email", userDO.getEmail());
		stDetail.setAttribute("url", urlString);
		stDetail.setAttribute("token", token);
		stDetail.setAttribute("customerservicename", bundleSource.getMessage("customerservicename", null, Locale.ENGLISH));
		stDetail.setAttribute("customerserviceemail", bundleSource.getMessage("customerserviceemail", null, Locale.ENGLISH));
		stDetail.setAttribute("customerservicephone", bundleSource.getMessage("customerservicephone", null, Locale.ENGLISH));
		stDetail.setAttribute("customerserviceurl", bundleSource.getMessage("customerserviceurl", null, Locale.ENGLISH));

		return stDetail.toString();
	}

	public String getForgotSubstitutedEmailText(String email, List<UserDO> users, String urlString, String key) {

		StringTemplate stDetail = new StringTemplate(bundleSource.getMessage(key, null, Locale.ENGLISH),
				DefaultTemplateLexer.class);

		stDetail.setAttribute("usernames",
				Joiner.on("\n * ").join(Iterables.transform(users, new Function<UserDO, String>() {
					@Override
					public String apply(UserDO user) {
						return user.getUsername();
					}
				})));

		stDetail.setAttribute("customerservicename", bundleSource.getMessage("customerservicename", null, Locale.ENGLISH));
		stDetail.setAttribute("customerserviceemail", bundleSource.getMessage("customerserviceemail", null, Locale.ENGLISH));
		stDetail.setAttribute("customerservicephone", bundleSource.getMessage("customerservicephone", null, Locale.ENGLISH));
		stDetail.setAttribute("customerserviceurl", bundleSource.getMessage("customerserviceurl", null, Locale.ENGLISH));

		return stDetail.toString();
	}

	@Override
	public boolean isTokenValid(String username, String token) {
		if (username == null || token == null) {
			return false;
		}
		UserDO userDO = findByUsername(username, false);
		if (userDO == null) {
			return false;
		}
		if (token.trim().equalsIgnoreCase(userDO.getResetToken1())) {
			return true;
		}
		if (token.trim().equalsIgnoreCase(userDO.getResetToken2())) {
			return true;
		}
		if (token.trim().equalsIgnoreCase(userDO.getResetToken3())) {
			return true;
		}
		if (token.trim().equalsIgnoreCase(userDO.getResetToken4())) {
			return true;
		}
		if (token.trim().equalsIgnoreCase(userDO.getResetToken5())) {
			return true;
		}
		return false;
	}

	@Override
	public void clearTokens(String username) {
		if (username == null) {
			return;
		}
		UserDO userDO = findByUsername(username, false);
		if (userDO == null) {
			return;
		}
		userDO.setResetToken1(null);
		userDO.setResetToken2(null);
		userDO.setResetToken3(null);
		userDO.setResetToken4(null);
		userDO.setResetToken5(null);
		update(userDO);
	}

	@Override
	public boolean isUserInScope(Long scopeId, Long userId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct u ");
		sb.append("from UserDO u  ");
		sb.append("  join fetch u.scope  ");
		sb.append("where u.userId=:userId ");
		sb.append("and u.scope.scopeId=:scopeId ");

		try {
			TypedQuery<UserDO> query = em.createQuery(sb.toString(), UserDO.class);
			query.setParameter("userId", userId);
			query.setParameter("scopeId", scopeId).getSingleResult();
		} catch (NoResultException nre) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isUsernameEmailPairValid(String username, String email) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct u ");
		sb.append("from UserDO u  ");
		sb.append("where u.username=:username ");
		sb.append("and u.email=:email ");

		try {
			TypedQuery<UserDO> query = em.createQuery(sb.toString(), UserDO.class);
			query.setParameter("username", username);
			query.setParameter("email", email).getSingleResult();
		} catch (NoResultException nre) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isEmailInUse(String email) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct u ");
		sb.append(" from UserDO u ");
		sb.append("where u.email=:email ");

		try {
			TypedQuery<UserDO> query = em.createQuery(sb.toString(), UserDO.class);
			query.setParameter("email", email);
			query.setHint("org.hibernate.cacheable", Boolean.TRUE);

			if (!CollectionUtils.isEmpty(query.getResultList())) {
				return true;
			}
			return false;
		} catch (NoResultException nre) {
			return false;
		}
	}

	@Override
	public boolean hasAccessToOrg(Long userId, String orgCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("select o from OrgDO o join o.orgTrees ot, UserOrgDO uo ");
		sb.append("where ot.ancestorOrg.orgId = uo.org.orgId ");
		sb.append("and ot.org.code = :code ");
		sb.append("and uo.user.userId = :userId ");
		TypedQuery<OrgDO> query = em.createQuery(sb.toString(), OrgDO.class);

		query.setParameter("code", orgCode);
		query.setParameter("userId", userId);
		query.setMaxResults(1);

		OrgDO org = getSingleResult(query);
		return org != null;
	}

	@Override
	public String getCurrentResetEmailText(UserDO userDO) {
		if (StringUtils.isEmpty(userDO.getResetToken1()) && StringUtils.isEmpty(userDO.getResetToken2())
				&& StringUtils.isEmpty(userDO.getResetToken3()) && StringUtils.isEmpty(userDO.getResetToken4())
				&& StringUtils.isEmpty(userDO.getResetToken5())) {
			return "Password token is not available. Please use the Password Reset task to obtain a new password token.";
		}
		String token = "";
		Map<String, String> tokenIndexMap = determineTokenIndexMap(userDO);
		// this tells us the next point for adding a token, so peek back one for the current token
		switch (Integer.valueOf(tokenIndexMap.get("position"))) {
		case 1:
			token = userDO.getResetToken5();
			break;
		case 2:
			token = userDO.getResetToken1();
			break;
		case 3:
			token = userDO.getResetToken2();
			break;
		case 4:
			token = userDO.getResetToken3();
			break;
		case 5:
			token = userDO.getResetToken4();
			break;
		}
		return getTokenSubstitutedEmailText(userDO, token, buildUrlString(), CORE_EMAIL_RESET_TEXT);

	}
}
