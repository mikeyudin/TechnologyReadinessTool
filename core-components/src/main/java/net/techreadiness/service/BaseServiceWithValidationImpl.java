package net.techreadiness.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import net.techreadiness.persistence.AuditedBaseEntity;
import net.techreadiness.persistence.AuditedBaseEntityWithExt;
import net.techreadiness.persistence.dao.EntityDAO;
import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.EntityDataTypeDAO;
import net.techreadiness.persistence.dao.EntityFieldDAO;
import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.persistence.domain.OptionListValueDO;
import net.techreadiness.service.common.ValidationError;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class BaseServiceWithValidationImpl extends BaseServiceImpl implements BaseServiceWithValidation {
	private Logger log = LoggerFactory.getLogger(BaseServiceWithValidationImpl.class);
	@Inject
	protected EntityFieldDAO entityFieldDAO;

	@Inject
	private RuleService ruleService;

	public BaseServiceWithValidationImpl() {
		DateTimeConverter dtConverter = new DateConverter();
		// TODO This list of patterns will need to be database driven in the future
		// Also this kind of thing could be handled in the validation layer as well. Do the convert, validate the converted
		// Date Obj etc....
		String[] patterns = { "MM/dd/yyyy", "M/d/yyyy", "M/dd/yyyy", "MM/d/yyyy", "yyyy-MM-dd", "yyyy-M-d", "yyyy-M-dd",
				"yyyy-MM-d", "yyyy-MM-dd HH:mm:ss.SSS" };
		dtConverter.setPatterns(patterns);
		ConvertUtils.register(dtConverter, java.util.Date.class);
	}

	public List<ValidationError> performCrossFieldValidation(ServiceContext context,
			final Map<String, String> extAttributes, Long scopeId, EntityTypeCode entityType) {
		List<ValidationError> errorList = new ArrayList<>();

		if (extAttributes != null) {
			StatefulKnowledgeSession ksession = ruleService.getRuleSessionForScopePath(context, scopeId, entityType);

			if (ksession != null) {
				ksession.setGlobal("errorList", errorList);
				ksession.insert(extAttributes);
				ksession.fireAllRules();
				ksession.dispose();
			}
		}
		return errorList;
	}

	@Override
	public List<ValidationError> performValidation(Map<String, String> extAttributes, Long scopeId,
			EntityDAO.EntityTypeCode code) {
		List<ValidationError> errorList = Lists.newArrayList();
		List<EntityFieldDO> entityFieldDOs = entityFieldDAO.findByScopePathAndType(scopeId, code);
		if (extAttributes != null) {
			// iterate through the map, and check for existence and valid use
			for (EntityFieldDO entityFieldDO : entityFieldDOs) {
				String attCode = entityFieldDO.getCode();
				String attValue = extAttributes.get(attCode);

				EntityDataTypeDAO.EntityDataTypeCodes dtCode = EntityDataTypeDAO.EntityDataTypeCodes.valueOf(entityFieldDO
						.getEntityDataType().getCode().toUpperCase());

				switch (dtCode) {
				case STRING:
				case DATE:
				case DATETIME: {
					if (validateRequired(entityFieldDO, attValue, errorList)) {
						if (validateLength(entityFieldDO, attValue, errorList)) {
							validateRegEx(entityFieldDO, attValue, errorList);
						}
					}
					break;
				}
				case NUMBER: {
					if (validateRequired(entityFieldDO, attValue, errorList)) {
						if (validateLength(entityFieldDO, attValue, errorList)) {
							if (validateNumeric(entityFieldDO, attValue, errorList)) {
								validateRegEx(entityFieldDO, attValue, errorList);
							}
						}
					}
					break;
				}
				case BOOLEAN: {
					validateRequired(entityFieldDO, attValue, errorList);
					validateRegEx(entityFieldDO, attValue, errorList);
				}
				}
				if (entityFieldDO.getOptionList() != null) {
					String newValue = validateOption(entityFieldDO, attValue, errorList);
					extAttributes.put(attCode, newValue);
				}

			}
		}
		return errorList;
	}

	private String validateOption(EntityFieldDO entityFieldDO, String attValue, List<ValidationError> errorList) {
		if (StringUtils.isBlank(attValue)) {
			return null;
		}
		for (OptionListValueDO listVal : entityFieldDO.getOptionList().getOptionListValues()) {
			if (listVal.getValue().equalsIgnoreCase(attValue)) {
				return listVal.getValue();
			}
		}
		String errorMessage = messageSource.getMessage("validation.optionList", new Object[] { entityFieldDO.getName(),
				attValue }, null);
		errorList.add(new ValidationError(entityFieldDO.getCode(), entityFieldDO.getName(), errorMessage,
				"validation.optionList", errorMessage));
		return attValue;
	}

	private boolean validateNumeric(EntityFieldDO entityFieldDO, String attValue, List<ValidationError> errorList) {
		if (StringUtils.isNotBlank(attValue) && !StringUtils.isNumeric(attValue)) {
			String errorMessage = messageSource.getMessage("validation.numeric", new Object[] { entityFieldDO.getName() },
					null);
			errorList.add(new ValidationError(entityFieldDO.getCode(), entityFieldDO.getName(), errorMessage,
					"validation.numeric", errorMessage));
			return false;
		}
		return true;
	}

	protected void validateRegEx(EntityFieldDO entityFieldDO, String attValue, List<ValidationError> errorList) {

		String regex = entityFieldDO.getRegex();

		String value = attValue != null ? attValue : "";

		if (StringUtils.isNotBlank(regex) && StringUtils.isNotBlank(value)) {

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(value);

			if (!matcher.matches()) {
				String msg = messageSource.getMessage("validation.regex", new Object[] { entityFieldDO.getName() }, null);
				if (StringUtils.isNotBlank(entityFieldDO.getRegexDisplay())) {
					msg = entityFieldDO.getName() + " " + entityFieldDO.getRegexDisplay();
				}
				errorList.add(new ValidationError(entityFieldDO.getCode(), entityFieldDO.getName(), msg, "validation.regex",
						msg));
			}
		}

	}

	private boolean validateLength(EntityFieldDO entityFieldDO, String attValue, List<ValidationError> errorList) {

		boolean isValid = true;
		String value = attValue != null ? attValue : "";

		if (StringUtils.isBlank(value)) {
			return true;
		}

		if (entityFieldDO.getMinLength() != null) {
			if (entityFieldDO.getMinLength().intValue() > value.length()) {
				String errorMessage = messageSource.getMessage("validation.minLength",
						new Object[] { entityFieldDO.getName(), entityFieldDO.getMinLength() }, null);
				errorList.add(new ValidationError(entityFieldDO.getCode(), entityFieldDO.getName(), errorMessage,
						"validation.minLength", errorMessage));
				isValid = false;
			}
		}

		if (entityFieldDO.getMaxLength() != null) {
			if (entityFieldDO.getMaxLength().intValue() < value.length()) {
				String errorMessage = messageSource.getMessage("validation.maxLength",
						new Object[] { entityFieldDO.getName(), entityFieldDO.getMaxLength() }, null);
				errorList.add(new ValidationError(entityFieldDO.getCode(), entityFieldDO.getName(), errorMessage,
						"validation.maxLength", errorMessage));
				isValid = false;
			}
		}
		return isValid;
	}

	protected boolean validateRequired(EntityFieldDO entityFieldDO, String attValue, List<ValidationError> errorList) {
		if (entityFieldDO.getRequired()) {
			if (StringUtils.isBlank(attValue)) {
				String errorMessage = entityFieldDO.getName() + " is required.";
				errorList.add(new ValidationError(entityFieldDO.getCode(), entityFieldDO.getName(), errorMessage,
						"validation.required", errorMessage));
				return false;
			}
		}
		return true;
	}

	protected void copyExtFieldsToCore(ServiceContext context, AuditedBaseEntityWithExt entity) {
		copyMapFieldsToEntity(context, entity, entity.getExtAttributes());
	}

	protected void copyMapFieldsToEntity(ServiceContext context, AuditedBaseEntity entity, Map<String, String> map) {
		Map<String, String> entityFields = entity.getAsMap();
		Map<String, Method> methods = Maps.newHashMap();

		for (Method method : entity.getClass().getMethods()) {
			methods.put(method.getName(), method);
		}
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();

			if (entityFields.containsKey(key)) {
				try {
					String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);

					if (methods.containsKey(methodName)) {
						Method method = methods.get(methodName);
						Class<?>[] parameters = method.getParameterTypes();
						if (ArrayUtils.isNotEmpty(parameters) && parameters.length == 1) {
							Object convertedValue;
							if (value == null) {
								convertedValue = null;
							} else if (Number.class.isAssignableFrom(parameters[0]) && StringUtils.isEmpty(value)) {
								convertedValue = null;
							} else {
								convertedValue = ConvertUtils.convert(value, parameters[0]);
							}
							method.invoke(entity, convertedValue);
							iterator.remove();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.warn("Unable to copy map entry: {}, to member variable on entity: {}.", key, entity.getClass());
				}
			}
		}
	}

	protected EntityFieldDO getEntityFieldByScopeAndTypeAndCode(Long scopeId, EntityDAO.EntityTypeCode type, String code) {
		return entityFieldDAO.findByScopeAndTypeAndCode(scopeId, type, code);
	}

	public void setEntityFieldDAO(EntityFieldDAO entityFieldDAO) {
		this.entityFieldDAO = entityFieldDAO;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
