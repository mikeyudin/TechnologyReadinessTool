package net.techreadiness.service;

import java.io.Serializable;

import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;

import com.google.common.base.Objects;

/**
 * Provides contextual information to the services. The service context aggregates information which will be used by the
 * service API's to control the subset of data which will be considered when servicing requests. The context is used to
 * define the visibility of data and to allow tailoring of the service behavior.
 * 
 * The {@link ServiceContext} contains 3 objects, the scope, the user, and the org. All pertinent values of the logged in
 * user. There are also convenience methods to get data from within one of these objects (ie. scope path).
 * 
 * <b>scope</b> The unique scope for the current user. This value used to control the service API's and data to which is
 * scoped.
 * 
 * <p>
 * The scopePath is a {@code String} which specifies the exact location of the scope in the scope hierarchy. A scopePath is
 * the root owner of all data (configuration, organizations) in the system.
 * <p>
 * 
 * <p>
 * <b>user</b> The unique user. This value used to control the service API's and data to which the user has access.
 * 
 * <p>
 * <b>userName</b> The userName is the "human readable" name for the current user. This is used for logging and for storing
 * detailed audit history.
 * 
 * <p>
 * <b>org</b> The selected organization is used by the service to filter the data on which the user can interact with. For
 * example if a request was made to the service to return a list of classes, only the classes which are owned by the current
 * organization (or it's descendants) would be returned.
 */
public class ServiceContext implements Serializable {
	private static final long serialVersionUID = 1L;

	private Scope scope;
	private User user;
	private Org org;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("scope", scope).add("user", user).add("org", org).toString();
	}

	public ServiceContext(Scope scope, User user, Org org) {
		super();
		setScope(scope);
		setUser(user);
		setOrg(org);
	}

	public ServiceContext() {
		super();
	}

	/**
	 * Retrieves the scope path for this service context. The scope is used to control the visibility of data and to allow
	 * tailoring of the service behavior.
	 * 
	 * If no scope is set, the default root scope is returned.
	 * 
	 * @return The scope path for this context
	 */
	public String getScopePath() {
		if (scope != null) {
			return scope.getPath();
		}

		return "/";
	}

	public Long getScopeId() {
		if (scope != null) {
			return scope.getScopeId();
		}

		return null;
	}

	public String getScopeName() {
		if (scope != null) {
			return scope.getName();
		}

		return "";
	}

	public Long getOrgId() {
		if (org != null) {
			return org.getOrgId();
		}

		return null;
	}

	public String getOrgName() {
		if (org != null) {
			return org.getName();
		}

		return "";
	}

	/**
	 * Sets the scope for this service context. The scope is used to control the visibility of data and to allow tailoring of
	 * the service behavior.
	 * @param scope New scope for the context
	 */
	public void setScope(Scope scope) {
		this.scope = scope;
	}

	/**
	 * Sets the user for this service context.
	 * @param user New user for the context
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Sets the organization for this service context.
	 * @param org New organization for the context
	 */
	public void setOrg(Org org) {
		this.org = org;
	}

	/**
	 * Sets the username (the credentials used by the user to access the site) the to the current context. The username is
	 * for logging and for storing detailed audit history.
	 * 
	 * @return Username used by the user to access the site
	 */
	public String getUserName() {
		if (user != null) {
			return user.getUsername();
		}

		return "";
	}

	public Long getUserId() {
		if (user != null) {
			return user.getUserId();
		}

		return null;
	}

	public Scope getScope() {
		return scope;
	}

	public User getUser() {
		return user;
	}

	public Org getOrg() {
		return org;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (org == null ? 0 : org.hashCode());
		result = prime * result + (scope == null ? 0 : scope.hashCode());
		result = prime * result + (user == null ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ServiceContext)) {
			return false;
		}
		ServiceContext other = (ServiceContext) obj;
		if (org == null) {
			if (other.org != null) {
				return false;
			}
		} else if (!org.equals(other.org)) {
			return false;
		}
		if (scope == null) {
			if (other.scope != null) {
				return false;
			}
		} else if (!scope.equals(other.scope)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}
}
