package net.techreadiness.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.Scope;

@WebService
public interface ScopeService extends BaseService {

	List<Scope> findAppRootScopes(ServiceContext context);

	Scope getById(ServiceContext context, Long scopeId);

	Scope getByScopePath(String scopePath);

	Scope getScopeWithOrgs(ServiceContext context);

	Scope getScopeWithUsers(ServiceContext context);

	Scope getScopeWithOrgParts(ServiceContext context);

	Scope getScopeForUser(ServiceContext context);

	Scope getSelectedScopeForUser(ServiceContext context);

	Collection<Scope> findSelectableScopes(ServiceContext context);

	Map<String, String> getLastUpdatedField(ServiceContext context, ViewDefTypeCode typeCode);

	/**
	 * Retrieves a list of descendant scopes give the scope in the service context.
	 *
	 * @param context
	 *            the service context
	 * @param allDescendants
	 *            flag to indicate if direct descendants or all descendants should be retrieved. true = all false = only
	 *            direct descendants
	 * @return The descendant scopes of the scope in the {@link ServiceContext}
	 */
	List<Scope> findDescendantScopes(ServiceContext context, boolean allDescendants);
}
