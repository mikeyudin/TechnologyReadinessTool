package net.techreadiness.service;

import java.util.Locale;

import javax.inject.Inject;

import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.context.MessageSource;

/**
 * Provides the base class for which Services should be derived. This class contains a Spring autowired
 * {@code ServiceContext} at the request level (which equates to a particular webservice call). Therefore, the service
 * context will always be pertinent to the particular call you are servicing.
 * 
 */
public abstract class BaseServiceImpl implements BaseService {
	@Inject
	private MappingService mappingService;
	@Inject
	protected MessageSource messageSource;

	public void setMappingService(MappingService mappingService) {
		this.mappingService = mappingService;
	}

	public MappingService getMappingService() {
		return mappingService;
	}

	protected String getMessage(String key, Object... params) {
		return messageSource.getMessage(key, params, Locale.getDefault());
	}
}
