package net.techreadiness.ui.task;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.ui.BaseAction;
import net.techreadiness.util.GenericTypeUtils;

import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.Preparable;

public abstract class BaseTaskControlAction<T extends TaskFlowDefinition> extends BaseAction implements Preparable {
	private static final long serialVersionUID = 1L;
	private List<Task> availableTasks;
	private List<Task> availableExternalTasks;
	@Inject
	private ApplicationContext applicationContext;

	public T getTaskFlow() {
		return (T) applicationContext.getBean(GenericTypeUtils.getTypeArguments(BaseTaskControlAction.class, getClass())
				.get(0));
	}

	@Override
	public void prepare() throws Exception {
		availableTasks = Lists.newArrayList();
		availableExternalTasks = Lists.newArrayList();

		for (Task task : getTaskFlow().getTasks()) {
			if (task.getPermissionCodes() == null
					|| userService.hasPermission(getServiceContext(), task.getPermissionCodes())) {
				availableTasks.add(task);
			}
		}
		if (null != getTaskFlow().getExternalTasks()) {
			for (Task task : getTaskFlow().getExternalTasks()) {
				if (task.getPermissionCodes() == null
						|| userService.hasPermission(getServiceContext(), task.getPermissionCodes())) {
					availableExternalTasks.add(task);
				}
			}
		}
	}

	public List<Task> getAvailableTasks() {
		return availableTasks;
	}

	public List<Task> getAvailableExternalTasks() {
		return availableExternalTasks;
	}
}
