package net.techreadiness.service.rest;

import java.util.Collection;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import net.techreadiness.service.object.Org;

public interface UserRestService {

	Collection<Org> findAuthorizedOrgs(SecurityContext context, UriInfo uriInfo, Long orgId);
}
