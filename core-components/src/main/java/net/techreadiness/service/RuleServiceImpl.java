package net.techreadiness.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.jws.WebService;

import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.EntityRuleDAO;
import net.techreadiness.persistence.domain.EntityRuleDO;
import net.techreadiness.rules.EndsWithEvaluatorDefinition;
import net.techreadiness.rules.StartsWithEvaluatorDefinition;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.Permission;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.builder.conf.EvaluatorOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@WebService
@Service
@Transactional
public class RuleServiceImpl extends BaseServiceImpl implements RuleService {

	@Inject
	private EntityRuleDAO entityRuleDAO;

	@Inject
	private UserService userService;

	@Override
	public StatefulKnowledgeSession getRuleSessionForScopePath(ServiceContext context, Long scopeId,
			EntityTypeCode entityType) {
		List<EntityRuleDO> entityRuleDOs = entityRuleDAO.findValidationRules(scopeId, entityType);
		return getRuleSessionForEntityRules(context, entityRuleDOs);
	}

	private StatefulKnowledgeSession getRuleSessionForEntityRules(ServiceContext context, List<EntityRuleDO> entityRuleDOs) {
		KnowledgeBase kbase = getKnowlegeBase(entityRuleDOs);

		if (kbase != null) {
			StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();

			for (Permission permission : userService.findAvailablePermissions(context)) {
				session.insert(permission);
			}
			return session;
		}

		return null;
	}

	private static KnowledgeBase getKnowlegeBase(List<EntityRuleDO> rules) {
		if (rules == null || rules.size() == 0) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		for (EntityRuleDO entityRuleDO : rules) {
			sb.append(entityRuleDO.getRule()).append("\n");
		}

		KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		config.setOption(EvaluatorOption.get("startsWith", new StartsWithEvaluatorDefinition()));
		config.setOption(EvaluatorOption.get("endsWith", new EndsWithEvaluatorDefinition()));

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(config);
		kbuilder.add(ResourceFactory.newByteArrayResource(sb.toString().getBytes()), ResourceType.DRL);

		if (kbuilder.hasErrors()) {
			throw new ServiceException("Problem building cross field validation rules: " + kbuilder.getErrors());
		}

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

	@Override
	public void clearAllCached(ServiceContext context) {
		throw new UnsupportedOperationException("caching is not yet implemented for rules");
	}

	/**
	 * Execute a given entity rule
	 * 
	 * @throws ValidationServiceException
	 *             if the rule does not pass validations
	 */
	@Override
	public void executeEntityRule(ServiceContext context, Long entityRuleId, Map<String, String> entity)
			throws ValidationServiceException {
		EntityRuleDO entityRule = entityRuleDAO.getById(entityRuleId);
		List<EntityRuleDO> asList = Arrays.asList(entityRule);
		StatefulKnowledgeSession ruleSession = getRuleSessionForEntityRules(context, asList);
		List<ValidationError> errorList = Lists.newArrayList();
		ruleSession.setGlobal("errorList", errorList);
		ruleSession.insert(entity);
		ruleSession.fireAllRules();
		ruleSession.dispose();
	}

	@Override
	public boolean executeViewRule(ServiceContext context, Long ruleId, BaseObject<?> baseObject) {
		EntityRuleDO rule = entityRuleDAO.getById(ruleId);

		if (rule != null) {
			KnowledgeBase knowlegeBase = getKnowlegeBase(Lists.newArrayList(rule));
			StatefulKnowledgeSession session = knowlegeBase.newStatefulKnowledgeSession();
			for (Permission permission : userService.findAvailablePermissions(context)) {
				session.insert(permission);
			}
			List<ValidationError> errorList = Lists.newArrayList();

			session.setGlobal("errorList", errorList);
			session.insert(baseObject);
			session.fireAllRules();
			session.dispose();
			return errorList.isEmpty();
		}

		return true;
	}
}
