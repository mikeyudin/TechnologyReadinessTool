package net.techreadiness.customer.action.task.org.edit;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.user.UserTaskFlowData;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.action.filters.MultipleFilterSelectionHandler;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ActionContext;

@Component
@Scope("prototype")
public class OrgFilterSelectionHandlerForOrgEdit implements MultipleFilterSelectionHandler<Org> {
	@Inject
	protected OrganizationService orgService;

	@Inject
	private UserTaskFlowData data;

	@Override
	public List<Org> getList(Map<String, Object> parameters, String key) {

		Object[] term = (Object[]) parameters.get("term");
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		List<Org> orgs;
		Long orgId = Long.valueOf(key);
		if (term != null && term.length > 0) {
			orgs = orgService.findOrgsThatCanHaveChildrenBySearchTermByType(context, (String) term[0], orgId);
		} else {
			orgs = orgService.findOrgsThatCanHaveChildrenByType(context, orgId);
		}

		return orgs;
	}

	@Override
	public List<Org> getSelection(String key) {
		return Lists.newArrayList(data.getOrgMap().get(key));
	}

	@Override
	public void add(Long id, String key) {
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		Org org = orgService.getById(context, id);
		data.getOrgMap().get(key).add(org);
	}

	@Override
	public void addSet(String key) {
		data.getOrgMap().put(key, Collections.synchronizedSet(new HashSet<Org>()));
	}

	@Override
	public void remove(Long id, String key) {
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		Org org = orgService.getById(context, id);
		data.getOrgMap().get(key).remove(org);
	}

	@Override
	public void clear(String key) {
		data.getOrgMap().get(key).clear();
	}
}