package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;
import net.techreadiness.persistence.dao.EntityDAO;
import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.EntityFieldDAO;
import net.techreadiness.persistence.dao.ExtDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.dao.ScopeTypeDAO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeTypeDO;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is a service class for handling scope map objects from a viewDef form or datagrid.
 * 
 */
@WebService
@Service
public class ScopeMapServiceImpl extends BaseServiceWithValidationAndExt<ScopeDO, AbstractAuditedBaseEntityWithExt<ScopeDO>>
		implements ScopeMapService {

	@Inject
	ScopeDAO scopeDao;

	@Inject
	ScopeTypeDAO scopeTypeDao;

	@Inject
	@Qualifier("scopeExtDAOImpl")
	ExtDAO<ScopeDO, AbstractAuditedBaseEntityWithExt<ScopeDO>> scopeExtDao;

	@Inject
	EntityFieldDAO entityFieldDao;

	@Override
	@Transactional
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = ScopeDO.class)
	public void update(ServiceContext context, Map<String, String> map) {
		ScopeDO scopeDO = scopeDao.getById(Long.valueOf(map.get("scopeId")));

		scopeDO.getExtAttributes().putAll(map);

		validate(scopeDO);

		copyExtFieldsToCore(context, scopeDO);

		storeExtFields(context, scopeDO, scopeExtDao, EntityTypeCode.SCOPE, scopeDO.getScopeId());

		scopeDao.update(scopeDO);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getById(ServiceContext context, Long scopeId) {
		ScopeDO scopeDO = scopeDao.getById(scopeId);
		Map<String, String> scopeAsMap = scopeDO.getExtAttributes();

		if (scopeAsMap == null) {
			scopeDO.setExtAttributes(scopeDO.getAsMap());
		} else {
			scopeDO.getExtAttributes().putAll(scopeDO.getAsMap());
		}
		return scopeAsMap;
	}

	@Override
	@Transactional
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = ScopeDO.class)
	public void persist(ServiceContext context, Map<String, String> map, Long scopeTypeId, Long parentScopeId) {
		ScopeDO scopeDO = new ScopeDO();
		scopeDO.setExtAttributes(map);

		if (parentScopeId != null) {
			ScopeDO parentScope = scopeDao.getById(parentScopeId);
			scopeDO.setParentScope(parentScope);
		}
		ScopeTypeDO scopeTypeDO = scopeTypeDao.getById(scopeTypeId);
		scopeDO.setScopeType(scopeTypeDO);

		validate(scopeDO);

		copyExtFieldsToCore(context, scopeDO);

		scopeDao.persist(scopeDO);
		// store ext's after saving new scopeDO object, and now scopeDO has a ScopeId
		storeExtFields(context, scopeDO, scopeExtDao, EntityTypeCode.SCOPE, scopeDO.getScopeId());
	}

	private void validate(ScopeDO scope) {
		if (scope.getScopeId() == null && scope.getParentScope() == null) {// new root scope
			return;
		}

		Long scopeToLookup;
		if (scope.getScopeId() == null) {
			scopeToLookup = scope.getParentScope().getScopeId();
		} else {
			scopeToLookup = scope.getScopeId();
		}
		List<ValidationError> errors = performValidation(scope.getExtAttributes(), scopeToLookup,
				EntityDAO.EntityTypeCode.SCOPE);
		if (errors == null || !errors.isEmpty()) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage("Scope failed validation.");
			faultInfo.setAttributeErrors(errors);
			throw new ValidationServiceException(faultInfo);
		}
	}

}
