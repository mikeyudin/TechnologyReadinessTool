package net.techreadiness.customer.action.task.org.create;

import java.util.List;
import java.util.Map;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.action.filters.AbstractOrgFilterSelectionHandler;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;

@Component
@Scope("prototype")
public class OrgFilterSelectionHandlerForOrgCreate extends AbstractOrgFilterSelectionHandler {

	@Override
	public List<Org> getList(Map<String, Object> parameters) {

		Object[] term = (Object[]) parameters.get("term");
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		List<Org> orgs;

		if (term != null && term.length > 0) {
			orgs = orgService.findOrgsThatCanHaveChildrenBySearchTerm(context, (String) term[0], 50);
		} else {
			orgs = orgService.findOrgsThatCanHaveChildren(context, 50);
		}

		return orgs;
	}
}
