package net.techreadiness.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.dao.ScopeExtDAO;
import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.dao.ViewDefDAO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeExtDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.ViewDefDO;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.Scope;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

@WebService
@Service
@Transactional
public class ScopeServiceImpl extends BaseServiceImpl implements ScopeService {

	@Inject
	private ScopeDAO scopeDAO;
	@Inject
	private ScopeExtDAO scopeExtDAO;
	@Inject
	private UserDAO userDAO;
	@Inject
	private ViewDefDAO viewDefDAO;
	@PersistenceContext
	private EntityManager em;

	@Override
	public Scope getById(ServiceContext context, Long scopeId) {
		return getMappingService().map(scopeDAO.getById(scopeId));
	}

	@Override
	public List<Scope> findAppRootScopes(ServiceContext context) {
		return getMappingService().mapFromDOList(scopeDAO.getAppRootScopes());
	}

	@Override
	public Scope getByScopePath(String scopePath) {
		return getMappingService().map(scopeDAO.getByScopePath(scopePath));
	}

	@Override
	public Scope getScopeWithOrgs(ServiceContext context) {
		return getMappingService().map(scopeDAO.getScopeForOrgs(context.getScopeId()));
	}

	@Override
	public Scope getScopeWithUsers(ServiceContext context) {
		return getMappingService().map(scopeDAO.getScopeForUsers(context.getScopeId()));
	}

	@Override
	public Scope getScopeForUser(ServiceContext context) {
		UserDO userDO = userDAO.getById(context.getUserId());
		ScopeDO scopeDO = null;

		if (userDO != null) {
			scopeDO = userDO.getScope();
		}

		return getMappingService().map(scopeDO);
	}

	@Override
	public Scope getSelectedScopeForUser(ServiceContext context) {
		UserDO userDO = userDAO.getById(context.getUserId());
		Scope scope = null;

		if (userDO.getSelectedScope() == null) {
			scope = Iterables.getFirst(findSelectableScopes(context),
					getMappingService().getMapper().map(userDO.getScope(), Scope.class));
		} else {
			scope = getMappingService().getMapper().map(userDO.getSelectedScope(), Scope.class);
		}

		return scope;
	}

	@Override
	public Map<String, String> getLastUpdatedField(ServiceContext context, ViewDefTypeCode typeCode) {
		ViewDefDO viewDef = viewDefDAO.getByViewTypeAndScopePath(typeCode.toString(), context.getScopeId());

		ScopeDO scopeDO = scopeDAO.getById(context.getScopeId());

		ScopeExtDO scopeExtDO = scopeExtDAO.getMostRecentlyUpdated(scopeDO.getScopeId(), viewDef.getViewDefId());
		if (scopeExtDO == null) {
			return Maps.newHashMap();
		}
		return scopeExtDO.getAsMap();
	}

	@Override
	public Scope getScopeWithOrgParts(ServiceContext context) {
		ScopeDO scope = scopeDAO.getScopeForOrgParts(context.getScopeId());
		return getMappingService().map(scope);
	}

	@Override
	public List<Scope> findDescendantScopes(ServiceContext context, boolean allDescendants) {
		return getMappingService().mapFromDOList(scopeDAO.findDescendantScopes(context.getScopeId(), allDescendants));
	}

	@Override
	public Collection<Scope> findSelectableScopes(ServiceContext context) {
		UserDO user = em.find(UserDO.class, context.getUserId());
		StringBuilder sb = new StringBuilder();
		sb.append("select st.scope from ScopeTreeDO st ");
		sb.append("where st.ancestorScope.scopeId = :scopeId ");
		sb.append("and st.depth = (select max(st2.depth) from ScopeTreeDO st2 where st2.ancestorScope.scopeId = :scopeId)");
		TypedQuery<ScopeDO> query = em.createQuery(sb.toString(), ScopeDO.class);
		query.setParameter("scopeId", user.getScope().getScopeId());
		Collection<ScopeDO> selectableScopes = query.getResultList();
		return getMappingService().getMapper().mapAsSet(selectableScopes, Scope.class);
	}
}
