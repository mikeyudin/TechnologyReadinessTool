package net.techreadiness.customer.action.task.user.create;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.user.UserTaskFlowData;
import net.techreadiness.service.RoleService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Role;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.action.filters.FilterSelectionHandler;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ActionContext;

@Component
@Scope("prototype")
public class RoleFilterSelectionHandlerForUserCreate implements FilterSelectionHandler<Role> {

	@Inject
	private RoleService roleService;

	@Inject
	private UserTaskFlowData data;

	@Override
	public List<Role> getList(Map<String, Object> parameters) {
		Object[] objTerm = (Object[]) parameters.get("term");
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		List<Role> roles = roleService.findRolesBySearchTerm(context, objTerm[0].toString());
		return roles;
	}

	@Override
	public List<Role> getSelection() {
		List<Role> list = Lists.newArrayList();
		if (data.getRoles().size() > 0) {
			list.addAll(data.getRoles());
		}
		return list;
	}

	@Override
	public void add(Long id) {
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		Role role = roleService.getById(context, id);
		data.getRoles().add(role);
	}

	@Override
	public void remove(Long id) {
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		Role role = roleService.getById(context, id);
		data.getRoles().remove(role);
	}

	@Override
	public void clear() {
		data.getRoles().clear();
	}
}