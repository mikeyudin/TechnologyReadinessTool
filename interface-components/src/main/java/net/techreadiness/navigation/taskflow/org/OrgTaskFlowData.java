package net.techreadiness.navigation.taskflow.org;

import java.util.List;
import java.util.Set;

import net.techreadiness.service.object.Org;
import net.techreadiness.ui.task.TaskFlowData;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@org.springframework.context.annotation.Scope("session")
public class OrgTaskFlowData extends TaskFlowData {

	private static final long serialVersionUID = 1L;

	private Set<Org> orgs;
	private List<Org> orgSelections;

	public Set<Org> getOrgs() {
		return orgs;
	}

	public void setOrgs(Set<Org> orgs) {
		this.orgs = orgs;
	}

	public List<Org> getOrgSelections() {
		if (null == orgSelections) {
			orgSelections = Lists.newArrayList();
		}
		return orgSelections;
	}

	public void setOrgSelections(List<Org> orgSelections) {
		this.orgSelections = orgSelections;
	}
}
