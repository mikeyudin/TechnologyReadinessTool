package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import net.techreadiness.persistence.dao.EntityDAO;
import net.techreadiness.service.common.ValidationError;

public interface BaseServiceWithValidation extends BaseService {

	List<ValidationError> performValidation(final Map<String, String> extAttributes, Long scopeId,
			EntityDAO.EntityTypeCode code);
}
