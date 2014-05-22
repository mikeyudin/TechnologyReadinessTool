package net.techreadiness.customer.action.task.user.create;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.user.UserTaskFlowData;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.action.filters.FilterSelectionHandler;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ActionContext;

@Component
@Scope("prototype")
public class OrgFilterSelectionHandlerForUserCreate implements FilterSelectionHandler<Org> {

	@Inject
	protected OrganizationService orgService;

	@Inject
	private UserTaskFlowData data;

	@Override
	public List<Org> getList(Map<String, Object> parameters) {

		Object[] term = (Object[]) parameters.get("term");
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		List<Org> orgs;

		if (term != null && term.length > 0) {
			orgs = orgService.findOrgsBySearchTerm(context, (String) term[0], 50);
		} else {
			orgs = orgService.findOrgsByScope(context, 50);
		}

		return orgs;
	}

	@Override
	public List<Org> getSelection() {
		return Lists.newArrayList(data.getOrgs());
	}

	@Override
	public void add(Long id) {
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		Org org = orgService.getById(context, id);
		data.getOrgs().add(org);
	}

	@Override
	public void remove(Long id) {
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		Org org = orgService.getById(context, id);
		data.getOrgs().remove(org);
	}

	@Override
	public void clear() {
		data.getOrgs().clear();
	}
}