package net.techreadiness.navigation.taskflow.user;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.task.TaskFlowData;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
@org.springframework.context.annotation.Scope("session")
public class UserTaskFlowData extends TaskFlowData {

	private static final long serialVersionUID = 1L;

	private Set<User> users;

	private Set<Org> orgs;

	private Map<String, Set<Org>> orgMap;

	private Set<Role> roles;

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<User> getUsers() {
		return users;
	}

	public Collection<Long> getUserIds() {
		Set<Long> ids = new HashSet<>();
		if (users != null) {
			for (User user : users) {
				ids.add(user.getUserId());
			}
		}
		return ids;
	}

	public synchronized Set<Org> getOrgs() {
		if (orgs == null) {
			orgs = Collections.synchronizedSet(new HashSet<Org>());
		}
		return orgs;
	}

	public synchronized void setOrgs(Set<Org> orgs) {
		this.orgs = orgs;
	}

	public synchronized Set<Role> getRoles() {
		if (roles == null) {
			roles = Collections.synchronizedSet(new HashSet<Role>());
		}
		return roles;
	}

	public synchronized void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public synchronized Map<String, Set<Org>> getOrgMap() {
		if (orgMap == null) {
			orgMap = Maps.newConcurrentMap();
		}
		return orgMap;
	}

	public synchronized void setOrgMap(Map<String, Set<Org>> orgMap) {
		this.orgMap = orgMap;
	}
}
