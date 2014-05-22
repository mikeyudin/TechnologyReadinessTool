package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;
import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.EntityFieldDAO;
import net.techreadiness.persistence.dao.ExtDAO;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.OrgPartDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgPartDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.OrgPart;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@WebService
@Service
@Transactional
public class OrgPartServiceImpl extends
		BaseServiceWithValidationAndExt<OrgPartDO, AbstractAuditedBaseEntityWithExt<OrgPartDO>> implements OrgPartService {
	@Inject
	private OrgPartDAO orgPartDAO;
	@Inject
	private ScopeDAO scopeDAO;
	@Inject
	private OrgDAO orgDAO;
	@Inject
	private ConfigService configService;

	@Inject
	@Qualifier("orgPartExtDAOImpl")
	private ExtDAO<OrgPartDO, AbstractAuditedBaseEntityWithExt<OrgPartDO>> orgPartExtDAO;

	@Inject
	EntityFieldDAO entityFieldDAO;

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = OrgPartDO.class)
	public OrgPart createIfNotExists(ServiceContext context, Long orgId, Map<String, String> exts) {
		ScopeDO scopeDO = scopeDAO.getById(context.getScopeId());

		return addOrUpdate(context, scopeDO, orgId, exts);
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = OrgPartDO.class)
	public OrgPart createIfNotExistsAlternateScope(ServiceContext context, Long scopeId, Long orgId, Map<String, String> exts) {
		ScopeDO scopeDO = scopeDAO.getById(scopeId);

		return addOrUpdate(context, scopeDO, orgId, exts);
	}

	private OrgPart addOrUpdate(ServiceContext context, ScopeDO scopeDO, Long orgId, Map<String, String> exts) {

		OrgDO orgDO = orgDAO.getById(orgId);

		if (!scopeDO.getScopeType().isAllowOrgPart()) {
			throw new ServiceException(messageSource.getMessage("validation.scope.orgPartNotAllowed", null, null));
		}
		
		if (orgDO == null) {
			throw new ServiceException(messageSource.getMessage("validation.org.required", null, null));
		}

		OrgPartDO orgPartDO = null;
		try {
			orgPartDO = orgPartDAO.findOrgPart(orgId, scopeDO.getScopeId());
			if (orgPartDO != null) {
				orgPartDO.setExtAttributes(exts);
			}
		} catch (EmptyResultDataAccessException e) {
			orgPartDO = null;
		}

		if (orgPartDO == null) {
			orgPartDO = new OrgPartDO();
			orgPartDO.setOrg(orgDO);
			orgPartDO.setScope(scopeDO);
			orgPartDO.setExtAttributes(exts);
			orgPartDAO.persist(orgPartDO);
		}

		storeExtFields(context, orgPartDO, orgPartExtDAO, EntityTypeCode.ORG_PART, orgPartDO.getScope().getScopeId());

		if (configService.isBooleanActive(context, scopeDO.getScopeId(), ConfigService.ORG_PART_DESCENDANT_CASCADE_ADD)) {
			orgPartDAO.createOrgPartsForDescendants(scopeDO.getScopeId(), orgDO.getOrgId());
		}

		return getMappingService().map(orgPartDO);
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = OrgPartDO.class)
	public void deleteIfExists(ServiceContext context, Long orgId) {

		OrgPartDO orgPartDO = null;
		ScopeDO scopeDO = null;

		try {
			orgPartDO = orgPartDAO.findOrgPart(orgId, context.getScopeId());
			scopeDO = scopeDAO.getById(context.getScopeId());

			if (orgPartDO != null) {
				List<OrgPartDO> descendants = orgPartDAO.findParticipatingDescendantOrgParts(orgPartDO.getScope()
						.getScopeId(), orgPartDO.getOrg().getOrgId());
				if (descendants != null && descendants.size() > 0) {
					if (!configService.isBooleanActive(context, scopeDO.getScopeId(),
							ConfigService.ORG_PART_DESCENDANT_CASCADE_DELETE)) {
						FaultInfo faultInfo = new FaultInfo();
						String errorMessage = messageSource.getMessage("validation.orgPart.childOrgsParticipating", null,
								null);
						faultInfo.setMessage(errorMessage);
						faultInfo.setAttributeErrors(Lists.<ValidationError> newArrayList());
						throw new ValidationServiceException(errorMessage, faultInfo);
					}
					List<Long> orgPartIds = Lists.newArrayList();
					for (OrgPartDO descendantDO : descendants) {
						orgPartIds.add(descendantDO.getOrgPartId());
					}
					try {
						orgPartDAO.deleteOrgParts(orgPartIds);
					} catch (Exception e) {// safe to catch all exceptions
											// here because any failure is
											// treated the same.
						FaultInfo faultInfo = new FaultInfo();
						String errorMessage = messageSource.getMessage("validation.orgPart.childOrgsParticipating", null,
								null);
						faultInfo.setMessage(errorMessage);
						faultInfo.setAttributeErrors(Lists.<ValidationError> newArrayList());
						throw new ValidationServiceException(errorMessage, faultInfo);
					}

				}
			}
		} catch (EmptyResultDataAccessException e) {
			// no action to take.
		}
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = OrgPartDO.class)
	public void deleteOrgPartsByOrg(ServiceContext context, Long orgId) {
		for (OrgPartDO orgPart : orgPartDAO.findOrgPartsForOrg(orgId)) {
			orgPartDAO.delete(orgPart);
		}
	}

	@Override
	public List<OrgPart> findOrgPartsForOrg(ServiceContext context, Long orgId) {
		return getMappingService().mapFromDOList(orgPartDAO.findOrgPartsForOrg(orgId));
	}

}
