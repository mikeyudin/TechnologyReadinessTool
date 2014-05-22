package net.techreadiness.service;

import java.util.Map;

import javax.jws.WebService;

@WebService
public interface ScopeMapService extends BaseServiceWithValidation {

	void update(ServiceContext context, Map<String, String> map);

	Map<String, String> getById(ServiceContext context, Long scopeId);

	void persist(ServiceContext context, Map<String, String> map, Long scopeTypeId, Long parentScopeId);
}
