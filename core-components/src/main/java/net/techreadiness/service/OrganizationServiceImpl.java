package net.techreadiness.service;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;
import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.ExtDAO;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.OrgPartDAO;
import net.techreadiness.persistence.dao.OrgTypeDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgPartDO;
import net.techreadiness.persistence.domain.OrgTypeDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.InvalidServiceContextException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class OrganizationServiceImpl extends BaseServiceWithValidationAndExt<OrgDO, AbstractAuditedBaseEntityWithExt<OrgDO>>
		implements OrganizationService {

	@Inject
	private OrgDAO orgDao;

	@Inject
	@Qualifier("orgExtDAOImpl")
	private ExtDAO<OrgDO, AbstractAuditedBaseEntityWithExt<OrgDO>> orgExtDao;

	@Inject
	private OrgPartDAO orgPartDao;
	@Inject
	private OrgTypeDAO orgTypeDao;
	@Inject
	private ScopeDAO scopeDao;

	@Inject
	private ConfigService configService;
	@Inject
	private OrgPartService orgPartService;
	@Inject
	private UserService userService;

	@Override
	public List<Org> findOrgsParticipatingInScope(ServiceContext context) {
		return getMappingService().mapFromDOList(orgPartDao.findOrgsPartForScope(context.getScopeId(), context.getOrgId()));
	}

	@Override
	public Org getById(ServiceContext context, Long orgId) {

		OrgDO orgDO = orgDao.getById(orgId);
		if (orgDO == null) {
			return null;
		}
		Map<String, String> extAttributes = orgDO.getExtAttributes();

		if (extAttributes == null) {
			orgDO.setExtAttributes(orgDO.getAsMap());
		} else {
			orgDO.getExtAttributes().putAll(orgDO.getAsMap());
		}
		return getMappingService().map(orgDO);
	}

	@Override
	public Org getByCode(ServiceContext context, String orgCode) {

		OrgDO orgDO = orgDao.getOrg(orgCode, context.getScopeId());
		if (orgDO == null) {
			return null;
		}

		Map<String, String> extAttributes = orgDO.getExtAttributes();

		if (extAttributes == null) {
			orgDO.setExtAttributes(orgDO.getAsMap());
		} else {
			orgDO.getExtAttributes().putAll(orgDO.getAsMap());
		}
		return getMappingService().map(orgDO);
	}

	@Override
	public List<Org> findOrgsByScope(ServiceContext context, int maxResults) {
		ScopeDO scopeDO = scopeDao.getScopeForOrgs(context.getScopeId());

		List<OrgDO> orgs = orgDao.findByScopeId(scopeDO.getScopeId(), context.getOrgId(), maxResults);
		return getMappingService().mapFromDOList(orgs);
	}

	@Override
	public Org getMatch(ServiceContext context, Org org) {
		OrgDO orgDO = null;

		ScopeDO scopeDO = scopeDao.getScopeForOrgs(context.getScopeId());
		if (scopeDO == null) {
			throw new InvalidServiceContextException("The scope is invalid for org creation.");
		}

		if (org.getOrgId() != null) {
			orgDO = orgDao.getById(org.getOrgId());
		}

		if (orgDO == null && StringUtils.isNotBlank(org.getCode())) {
			orgDO = orgDao.getOrg(org.getCode(), scopeDO.getScopeId());
		}

		return getMappingService().map(orgDO);
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = OrgDO.class)
	public Org addOrUpdate(ServiceContext context, Org org) {
		ScopeDO scopeDO = scopeDao.getScopeForOrgs(context.getScopeId());
		if (scopeDO == null) {
			throw new InvalidServiceContextException("The scope is invalid for org creation.");
		}

		// Attempt to find the organization using the org code if the ID isn't provided.
		if (org.getOrgId() == null && StringUtils.isNotBlank(org.getCode())) {
			OrgDO orgDO = orgDao.getOrg(org.getCode(), scopeDO.getScopeId());
			if (orgDO != null) {
				org.setOrgId(orgDO.getOrgId());
			}
		}

		// Attempt to find the organization using the org code if the ID isn't provided.
		if (org.getParentOrgId() == null && StringUtils.isNotBlank(org.getParentOrgCode())) {
			OrgDO parentOrgDO = orgDao.getOrg(org.getParentOrgCode(), scopeDO.getScopeId());
			if (parentOrgDO != null) {
				org.setParentOrgId(parentOrgDO.getOrgId());
				org.setParentOrgName(parentOrgDO.getName());
			}
		}

		if (org.getOrgTypeId() == null && StringUtils.isNotBlank(org.getOrgTypeCode())) {
			OrgTypeDO orgTypeDO = orgTypeDao.getByCode(org.getOrgTypeCode(), scopeDO.getScopeId());
			if (orgTypeDO != null) {
				org.setOrgTypeId(orgTypeDO.getOrgTypeId());
				org.setOrgTypeName(orgTypeDO.getName());
			}
		}

		OrgDO orgDO = null;
		FaultInfo faultInfo = new FaultInfo();
		if (StringUtils.isNotBlank(org.getParentOrgCode()) && org.getParentOrgId() == null) {
			String message = getMessage("validation.org.parent.not.found", org.getParentOrgCode());
			ValidationError error = new ValidationError("parentOrgCode", "Parent Organization", message);
			faultInfo.getAttributeErrors().add(error);
		}
		try {
			orgDO = updateOrg(context, org, scopeDO);
			boolean newEntity = orgDO.getOrgId() == null;
			hasAccessToOrg(context, orgDO, newEntity);
			orgDao.persist(orgDO);
			storeExtFields(context, orgDO, orgExtDao, EntityTypeCode.ORG, orgDO.getScope().getScopeId());

			addParticipations(context, org, scopeDO, orgDO);
		} catch (ValidationServiceException e) {
			faultInfo.getAttributeErrors().addAll(e.getFaultInfo().getAttributeErrors());
		}

		if (!faultInfo.getAttributeErrors().isEmpty()) {
			throw new ValidationServiceException(faultInfo);
		}

		return getMappingService().map(orgDO);
	}

	protected void hasAccessToOrg(ServiceContext context, OrgDO org, boolean checkParent) {
		if (org.getOrgId() != null) {
			if (!userService.hasAccessToOrg(context, context.getUserId(), org.getOrgId())) {
				throw new AuthorizationException(getMessage("validation.org.access.denied", context.getUserName(),
						org.getName()));
			}
		}

		if (checkParent && org.getParentOrg() != null) {
			if (!userService.hasAccessToOrg(context, context.getUserId(), org.getParentOrg().getOrgId())) {
				throw new AuthorizationException(getMessage("validation.org.access.denied", context.getUserName(), org
						.getParentOrg().getName()));
			}
		}
	}

	private OrgDO updateOrg(ServiceContext context, Org organization, ScopeDO scopeDO) {
		FaultInfo faultInfo = new FaultInfo();

		if (scopeDO == null) {
			String errorMessage = messageSource.getMessage("validation.scope.required", null, Locale.getDefault());
			ValidationError error = new ValidationError("scopeId", "Scope", errorMessage, "validation.scope.required",
					errorMessage);
			faultInfo.getAttributeErrors().add(error);
			throw new ValidationServiceException(faultInfo);
		} else if (!scopeDO.getScopeType().isAllowOrg()) {
			String errorMessage = messageSource.getMessage("validation.scope.orgsNotAllowed", null, Locale.getDefault());
			ValidationError error = new ValidationError("scopeId", "Scope", errorMessage, "validation.scope.orgsNotAllowed",
					errorMessage);
			faultInfo.getAttributeErrors().add(error);
			throw new ValidationServiceException(faultInfo);
		}

		List<ValidationError> errors = performValidation(organization.getAsMap(), scopeDO.getScopeId(), EntityTypeCode.ORG);
		if (errors == null) {
			errors = performCrossFieldValidation(context, organization.getAsMap(), scopeDO.getScopeId(), EntityTypeCode.ORG);
		} else if (errors.size() == 0) { // if the list has an entry, data may be too invalid to do cross field checking.
			errors.addAll(performCrossFieldValidation(context, organization.getAsMap(), scopeDO.getScopeId(),
					EntityTypeCode.ORG));
		}

		faultInfo.getAttributeErrors().addAll(errors);

		OrgDO org;
		if (organization.getOrgId() != null) {
			org = orgDao.getById(organization.getOrgId());
		} else {
			org = new OrgDO();
		}

		OrgDO existingOrg = orgDao.getOrg(organization.getCode(), scopeDO.getScopeId());
		if (existingOrg != null && !existingOrg.getOrgId().equals(org.getOrgId())) {
			String errorMessage = messageSource.getMessage("validation.org.alreadyExists",
					new Object[] { existingOrg.getName(), existingOrg.getCode() }, Locale.getDefault());
			ValidationError error = new ValidationError("code", "Code", errorMessage, "validation.org.alreadyExists",
					errorMessage);
			faultInfo.getAttributeErrors().add(error);
		}

		OrgTypeDO orgType = orgTypeDao.getById(organization.getOrgTypeId());
		if (orgType == null) {
			String errorMessage = messageSource.getMessage("validation.org.type.required", null, Locale.getDefault());
			ValidationError error = new ValidationError("orgTypeId", "Organization Type", errorMessage,
					"validation.org.type.required", errorMessage);
			faultInfo.getAttributeErrors().add(error);
		} else if (orgType.getParentOrgType() == null) {
			List<OrgDO> orgs = orgDao.findOrgsByType(organization.getOrgTypeId(), scopeDO.getScopeId());
			if (!orgs.isEmpty()) {
				String msg = messageSource.getMessage("validation.org.rootOrgExists", new Object[] { orgType.getName() },
						Locale.getDefault());
				ValidationError error = new ValidationError("orgTypeId", "Organization Type", msg);
				faultInfo.getAttributeErrors().add(error);
			}
		} else if (!scopeDO.equals(orgType.getScope())) {
			String errorMessage = messageSource.getMessage("validation.org.type.wrongScope", null, Locale.getDefault());
			ValidationError error = new ValidationError("orgTypeId", "Organization Type", errorMessage,
					"validation.org.type.wrongScope", errorMessage);
			faultInfo.getAttributeErrors().add(error);
		}

		OrgDO parentOrg = null;
		if (organization.getParentOrgId() != null) {
			parentOrg = orgDao.getById(organization.getParentOrgId());
		}

		if (parentOrg != null) {
			if (!parentOrg.getScope().getScopeId().equals(scopeDO.getScopeId())) {
				String errorMessage = messageSource.getMessage("validation.org.parent.scope", null, Locale.getDefault());
				ValidationError error = new ValidationError("scopeId", "Scope", errorMessage, "validation.org.parent.scope",
						errorMessage);
				faultInfo.getAttributeErrors().add(error);
			}

			if (orgType != null) {
				List<Long> orgTypeIds = orgTypeDao.findDisallowedChildOrgTypeIdsForOrg(parentOrg.getOrgId());
				if (orgTypeIds.contains(orgType.getOrgTypeId())) {
					String errorMessage = messageSource.getMessage("validation.org.parent.childOrgType", null,
							Locale.getDefault());
					ValidationError error = new ValidationError("orgTypeId", "Organization Type", errorMessage,
							"validation.org.parent.childOrgType", errorMessage);
					faultInfo.getAttributeErrors().add(error);
				}

				if (!parentOrg.getOrgType().equals(orgType.getParentOrgType())) {
					String errorMessage = messageSource.getMessage("validation.org.orgType.wrongType", new Object[] {
							orgType.getName(), orgType.getParentOrgType().getName(), parentOrg.getOrgType().getName() },
							Locale.getDefault());
					ValidationError error = new ValidationError("orgTypeId", "Organization Type", errorMessage,
							"validation.org.orgType.wrongType", errorMessage);
					faultInfo.getAttributeErrors().add(error);
				}
			}

			if (parentOrg.equals(org)) {
				String errorMessage = messageSource
						.getMessage("validation.org.parentSameAsChild", null, Locale.getDefault());
				ValidationError error = new ValidationError("parentOrgId", "Parent Organization", errorMessage,
						"validation.org.parentSameAsChild", errorMessage);
				faultInfo.getAttributeErrors().add(error);
			}
		} else if (orgType != null && orgType.getParentOrgType() != null) {
			// Require a parent org if the org type has a parent org type
			String errorMessage = messageSource.getMessage("validation.org.orgType.requires.parent",
					new Object[] { orgType.getName() }, Locale.getDefault());
			ValidationError error = new ValidationError("orgTypeId", "Organization Type", errorMessage,
					"validation.org.orgType.requires.parent", errorMessage);
			faultInfo.getAttributeErrors().add(error);
		}

		if (!faultInfo.getAttributeErrors().isEmpty()) {
			throw new ValidationServiceException(faultInfo);
		}

		getMappingService().getMapper().map(organization, org);

		org.setExtAttributes(organization.getExtendedAttributes());

		org.setScope(scopeDO);
		org.setOrgType(orgType);
		org.setParentOrg(parentOrg);
		return org;
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = OrgDO.class)
	public void delete(ServiceContext context, Long orgId) {
		OrgDO orgDO = orgDao.getById(orgId);
		hasAccessToOrg(context, orgDO, true);
		if (orgDao.hasUsers(orgId)) {
			throw new ValidationServiceException(getMessage("validation.org.delete.hasUser", orgDO.getName()));
		}

		try {
			if (orgDO.getOrgs().get(0).getOrgId() != null) {
				throw new ValidationServiceException(getMessage("validation.org.delete.hasChildren", orgDO.getName()));
			}
		} catch (IndexOutOfBoundsException ibe) {
			// this is to be expected at times.
			// we need to look at the first child org above (if it exists) for the lazy loading.
			// if there simply is no child org, we'll be getting this index out of bounds.
		}

		orgPartService.deleteOrgPartsByOrg(context, orgId);
		orgDao.delete(orgDO);
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = OrgDO.class)
	public Org create(ServiceContext context, Org org) {
		ScopeDO scopeDO = scopeDao.getScopeForOrgs(context.getScopeId());

		if (scopeDO == null) {
			throw new InvalidServiceContextException(messageSource.getMessage("validation.scope.orgsNotAllowed", null,
					Locale.getDefault()));
		}

		OrgDO orgDO = updateOrg(context, org, scopeDO);
		hasAccessToOrg(context, orgDO, true);
		orgDao.persist(orgDO);
		storeExtFields(context, orgDO, orgExtDao, EntityTypeCode.ORG, orgDO.getScope().getScopeId());

		addParticipations(context, org, scopeDO, orgDO);

		return getMappingService().map(orgDO);
	}

	private void addParticipations(ServiceContext context, Org org, ScopeDO scopeDO, OrgDO orgDO) {
		if (orgDO.getParentOrg() != null) {
			// is the parent org participating?
			List<OrgPartDO> orgParts = orgPartDao.findOrgPartsForOrg(org.getParentOrgId());

			if (orgParts != null) {
				for (OrgPartDO orgPartDO : orgParts) {
					if (configService.isBooleanActive(context, orgPartDO.getScope().getScopeId(),
							ConfigService.ORG_PART_DESCENDANT_CASCADE_ADD)) {
						orgPartService.createIfNotExistsAlternateScope(context, orgPartDO.getScope().getScopeId(),
								orgDO.getOrgId(), new HashMap<String, String>());
					}
				}
			}
		}
	}

	@Override
	public List<Org> findOrgsThatCanHaveChildren(ServiceContext context, int maxResults) {
		List<OrgDO> orgs = orgDao.findOrgsThatCanHaveChildren(context.getScopeId(), context.getOrgId(), maxResults);
		return getMappingService().mapFromDOList(orgs);
	}

	@Override
	public List<SimpleEntry<Long, String>> findChildOrgTypesByParentOrgType(ServiceContext context, Long parentOrgTypeId) {
		List<OrgTypeDO> orgTypeDOs = orgTypeDao.findChildOrgTypes(parentOrgTypeId, context.getScopeId());

		List<SimpleEntry<Long, String>> list = Lists.newArrayList();

		if (orgTypeDOs != null) {
			for (OrgTypeDO o : orgTypeDOs) {
				list.add(new SimpleEntry<>(o.getOrgTypeId(), o.getName()));
			}
		}

		return list;
	}

	@Override
	public List<Org> findOrgsBySearchTerm(ServiceContext context, String term, int maxResults) {
		ScopeDO scope = scopeDao.getScopeForOrgs(context.getScopeId());
		if (scope == null) {
			return Collections.emptyList();
		}

		ScopeDO scopeDO = scopeDao.getScopeForOrgs(context.getScopeId());
		return getMappingService().mapFromDOList(
				orgDao.findOrgsBySearchTerm(scopeDO.getScopeId(), context.getOrgId(), term, maxResults));
	}

	@Override
	public List<Org> findOrgsParticipatingInScope(ServiceContext context, String term) {
		return getMappingService().mapFromDOList(
				orgPartDao.findOrgPartsForScope(context.getScopeId(), context.getOrgId(), term));
	}

	@Override
	public List<Org> findByIds(ServiceContext context, Collection<Long> orgIds) {
		return getMappingService().mapFromDOList(orgDao.findById(orgIds));
	}

	@Override
	public List<Org> findOrgsParticipatingInScopeThatAllowEnrollments(ServiceContext context) {
		return getMappingService().mapFromDOList(
				orgDao.findOrgsParticipatingInScopeThatAllowEnrollments(context.getScopeId(), context.getOrgId()));
	}

	@Override
	public List<SimpleEntry<Long, String>> findOrgTypes(ServiceContext context) {

		List<SimpleEntry<Long, String>> list = Lists.newArrayList();

		ScopeDO scopeDO = scopeDao.getScopeForOrgs(context.getScopeId());

		List<OrgTypeDO> orgTypes = orgTypeDao.findOrgTypesForScope(scopeDO.getScopeId());

		if (orgTypes != null) {
			for (OrgTypeDO orgTypeDO : orgTypes) {
				list.add(new SimpleEntry<>(orgTypeDO.getOrgTypeId(), orgTypeDO.getName()));
			}
		}

		return list;
	}

	@Override
	public List<SimpleEntry<Long, String>> findOrgTypesByIds(Collection<Long> orgTypeIds) {

		List<SimpleEntry<Long, String>> list = Lists.newArrayList();
		List<OrgTypeDO> orgTypes = orgTypeDao.findOrgTypesByIds(orgTypeIds);

		if (orgTypes != null) {
			for (OrgTypeDO orgTypeDO : orgTypes) {
				list.add(new SimpleEntry<>(orgTypeDO.getOrgTypeId(), orgTypeDO.getName()));
			}
		}

		return list;
	}

	@Override
	public List<Org> findOrgsParticipatingInScopeThatAllowGroups(ServiceContext context) {
		return getMappingService().mapFromDOList(
				orgDao.findOrgsParticipatingInScopeThatAllowGroups(context.getScopeId(), context.getOrgId()));
	}

	@Override
	public List<Org> findOrgsParticipatingInScopeThatAllowGroups(ServiceContext context, String term) {
		return getMappingService().mapFromDOList(
				orgDao.findOrgsParticipatingInScopeThatAllowGroups(context.getScopeId(), context.getOrgId(), term));
	}

	@Override
	public List<Org> findOrgsParticipatingInScopeThatAllowEnrollments(ServiceContext context, String term) {
		return getMappingService().mapFromDOList(
				orgDao.findOrgsParticipatingInScopeThatAllowEnrollments(context.getScopeId(), context.getOrgId(), term));
	}

	@Override
	public Org getOrgForUser(ServiceContext context) {
		ScopeDO scopeDO = scopeDao.getScopeForOrgs(context.getScopeId());

		if (scopeDO == null || context.getUser() == null) {
			return null;
		}

		return getMappingService().map(orgDao.getHighestOrgForUser(scopeDO.getScopeId(), context.getUserId()));
	}

	@Override
	public List<Org> findOrgsForUser(ServiceContext context) {
		ScopeDO scopeDO = scopeDao.getScopeForOrgs(context.getScopeId());

		if (scopeDO == null || context.getUser() == null) {
			return null;
		}

		return getMappingService().mapFromDOList(orgDao.findOrgsForUser(scopeDO.getScopeId(), context.getUserId()));
	}

	@Override
	public List<Org> findChildOrgsThatAllowDevices(ServiceContext context, String term, int limit) {
		return getMappingService().mapFromDOList(orgDao.findChildOrgsThatAllowDevices(context.getOrgId(), term, limit));
	}

	@Override
	public List<Org> findOrgsThatCanHaveChildrenBySearchTerm(ServiceContext context, String term, int maxResults) {
		List<OrgDO> orgs = orgDao.findOrgsThatCanHaveChildrenBySearchTerm(context.getScopeId(), context.getOrgId(), term,
				maxResults);
		return getMappingService().mapFromDOList(orgs);
	}

	@Override
	public List<Org> findOrgsThatCanHaveChildrenBySearchTermByType(ServiceContext context, String term, Long orgId) {
		Org org = getById(context, orgId);
		Org parentOrg = getById(context, org.getParentOrgId());
		if (null == parentOrg) {
			return Collections.emptyList();
		}
		List<OrgDO> orgs = orgDao.findOrgsThatCanHaveChildrenBySearchTermByType(context.getScopeId(), context.getOrgId(),
				term, parentOrg.getOrgTypeId());
		return getMappingService().mapFromDOList(orgs);
	}

	@Override
	public List<Org> findOrgsThatCanHaveChildrenByType(ServiceContext context, Long orgId) {
		List<OrgDO> orgs = orgDao.findOrgsThatCanHaveChildrenByType(context.getScopeId(), context.getOrgId(), orgId);
		return getMappingService().mapFromDOList(orgs);
	}

	@Override
	public List<Org> findDescendantOrgsParticipatingInScope(ServiceContext context, Long orgId, boolean allDescendants) {
		return getMappingService().mapFromDOList(
				orgDao.findDescendantOrgsParticipatingInScope(context.getScopeId(), orgId, allDescendants));
	}

	@Override
	public Org getParentOrgOfType(ServiceContext context, Long orgId, String typeCode) {
		OrgDO org = orgDao.getOrgOfType(orgId, typeCode);
		return getMappingService().map(org);
	}

	@Override
	public Collection<Org> findDescendantOrgs(ServiceContext context, Long orgId) {
		return getMappingService().getMapper().mapAsSet(orgDao.findDescendantOrgs(orgId), Org.class);
	}
}
