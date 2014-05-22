package net.techreadiness.service.rest;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.mapping.MappingService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserRestServiceImpl implements UserRestService {
	@PersistenceContext
	private EntityManager em;
	@Inject
	private UserDAO userDAO;
	@Inject
	private MappingService mappingService;

	@GET
	@Path("{userId: [0-9]+}/authorized-orgs")
	@Override
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Org> findAuthorizedOrgs(@Context SecurityContext context, @Context UriInfo uriInfo,
			@PathParam("userId") Long userId) {
		String search = uriInfo.getQueryParameters().getFirst("q");
		UserDO user = userDAO.findByUsername(context.getUserPrincipal().getName(), false);
		StringBuilder sb = new StringBuilder();
		sb.append("select o from OrgPartDO op ");
		sb.append("join op.scope scope ");
		sb.append("join op.org o ");
		sb.append("join o.orgTrees ot ");
		sb.append("join ot.ancestorOrg ancestorOrg ");
		sb.append("join ancestorOrg.userOrgs uo ");
		sb.append("join uo.user u ");
		sb.append("where scope.scopeId = :scopeId ");
		if (StringUtils.isNotBlank(search)) {
			sb.append("and (o.name like :search or o.code like :search or o.orgType.name like :search) ");
		}
		sb.append("and u.username = :username ");

		TypedQuery<OrgDO> query = em.createQuery(sb.toString(), OrgDO.class);

		query.setParameter("scopeId", user.getSelectedScope().getScopeId());
		query.setParameter("username", context.getUserPrincipal().getName());
		if (StringUtils.isNotBlank(search)) {
			query.setParameter("search", StringUtils.join("%", search, "%"));
		}
		query.setMaxResults(10);

		return mappingService.getMapper().mapAsList(query.getResultList(), Org.class);
	}

}
