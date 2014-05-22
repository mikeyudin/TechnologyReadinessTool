package net.techreadiness.service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.dao.UserOrgDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserOrgDO;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.UserOrg;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Sets;

@WebService
@Service
@Transactional
public class UserOrgServiceImpl extends BaseServiceImpl implements UserOrgService {

	@Inject
	UserOrgDAO userOrgDao;

	@Inject
	UserDAO userDao;

	@Inject
	OrgDAO orgDao;
	@Inject
	ScopeDAO scopeDao;

	@Override
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_ORGANIZATION_ACCESS)
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = UserOrgDO.class)
	public UserOrg persist(ServiceContext context, Long userId, Long orgId) {
		UserOrgDO userOrgDO = userOrgDao.getUserOrgByUserIdAndOrgId(userId, orgId);
		if (userOrgDO == null) {
			if (userOrgDao.isOrgModifiable(context.getUserId(), orgId, context.getScopeId())) {
				userOrgDO = new UserOrgDO();

				UserDO userDO = userDao.getById(userId);
				OrgDO orgDO = orgDao.getById(orgId);

				userOrgDO.setOrg(orgDO);
				userOrgDO.setUser(userDO);

				userOrgDao.persist(userOrgDO);
			} else {
				OrgDO org = orgDao.getById(orgId);
				throw new AuthorizationException(getMessage("validation.user.org.delegationNotAllowed",
						context.getUserName(), org.getName()));
			}
		}
		return getMappingService().getMapper().map(userOrgDO, UserOrg.class);

	}

	@Override
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_ORGANIZATION_ACCESS)
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = UserOrgDO.class)
	public void delete(ServiceContext context, Long userId, Long orgId) {
		UserOrgDO userOrgDO = userOrgDao.getUserOrgByUserIdAndOrgId(userId, orgId);
		if (userOrgDO != null) {
			if (userOrgDao.isOrgModifiable(context.getUserId(), orgId, context.getScopeId())) {
				userOrgDao.delete(userOrgDO);
			} else {
				OrgDO org = orgDao.getById(orgId);
				throw new AuthorizationException(getMessage("validation.user.org.delegationNotAllowed",
						context.getUserName(), org.getName()));
			}

		}
	}

	@Override
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_ORGANIZATION_ACCESS)
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = UserOrgDO.class)
	public void deleteAllForUser(ServiceContext context, Long userId) {
		ScopeDO scope = scopeDao.getScopeForOrgs(context.getScopeId());
		List<UserOrgDO> userOrgs = userOrgDao.getUserOrgByUserId(scope.getScopeId(), userId);
		for (UserOrgDO userOrgDO : userOrgs) {
			delete(context, userId, userOrgDO.getOrg().getOrgId());
		}
	}

	@Override
	@CoreSecured(CorePermissionCodes.CORE_CUSTOMER_ORGANIZATION_ACCESS)
	public void mergeUserOrgs(ServiceContext context, Long userId, List<String> orgCodes) {
		// cannot accept empty org code list
		if (CollectionUtils.isEmpty(orgCodes)) {
			ValidationError error = new ValidationError("code", "Code", messageSource.getMessage("validation.org.required",
					new Object[] {}, Locale.getDefault()));
			ValidationServiceException e = new ValidationServiceException(new FaultInfo());
			e.getFaultInfo().getAttributeErrors().add(error);
			throw e;
		}
		ScopeDO scope = scopeDao.getScopeForOrgs(context.getScopeId());
		final List<OrgDO> requestedOrgs = orgDao.findByCodes(scope.getScopeId(), orgCodes);
		Set<String> reqCodes = Sets.newHashSet(orgCodes);

		for (OrgDO org : requestedOrgs) {
			reqCodes.remove(org.getCode());
			persist(context, userId, org.getOrgId());
		}

		if (!reqCodes.isEmpty()) {
			ValidationError error = new ValidationError("code", "Code", messageSource.getMessage(
					"validation.org.invalid.code", new Object[] { reqCodes }, Locale.getDefault()));
			ValidationServiceException e = new ValidationServiceException(new FaultInfo());
			e.getFaultInfo().getAttributeErrors().add(error);
			throw e;
		}

		List<UserOrgDO> authorizedOrgs = userOrgDao.getUserOrgByUserId(scope.getScopeId(), userId);
		for (UserOrgDO userOrg : authorizedOrgs) {
			if (!requestedOrgs.contains(userOrg.getOrg())) {
				delete(context, userId, userOrg.getOrg().getOrgId());
			}
		}
	}
}
