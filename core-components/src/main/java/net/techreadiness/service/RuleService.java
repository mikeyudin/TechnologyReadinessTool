package net.techreadiness.service;

import java.util.Map;

import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.BaseObject;

import org.drools.runtime.StatefulKnowledgeSession;

public interface RuleService extends BaseService {
	StatefulKnowledgeSession getRuleSessionForScopePath(ServiceContext context, Long scopeId, EntityTypeCode entityType);

	void clearAllCached(ServiceContext context);

	void executeEntityRule(ServiceContext context, Long entityRuleId, Map<String, String> entity)
			throws ValidationServiceException;

	boolean executeViewRule(ServiceContext context, Long ruleId, BaseObject<?> baseObject);
}
