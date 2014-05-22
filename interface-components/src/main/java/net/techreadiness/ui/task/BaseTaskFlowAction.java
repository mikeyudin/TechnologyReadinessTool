package net.techreadiness.ui.task;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;
import net.techreadiness.util.GenericTypeUtils;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;

public abstract class BaseTaskFlowAction<T extends TaskFlowData, E extends TaskFlowDefinition> extends BaseAction implements
		ConversationAware {
	private static final long serialVersionUID = 1L;
	private String taskName;
	private List<String> selectedTasks;
	protected Conversation conversation;
	@Inject
	private ApplicationContext applicationContext;

	public T getTaskFlowData() {
		return applicationContext.getBean(getTaskFlowDataTypeArgument());
	}

	public E getTaskFlowDefinition() {
		return applicationContext.getBean(getTaskFlowDefinitionTypeArgument());
	}

	protected void startNewTaskFlow() {
		List<Task> tasks = Lists.newArrayList();
		for (String taskName : selectedTasks) {
			Task task = getTaskFlowDefinition().getTask(taskName);
			if (task != null) {
				tasks.add(task);
			}
		}
		TaskFlowState state = new TaskFlowState();
		state.setTasks(tasks);
		getTaskFlowData().setTaskFlowState(state);
		getTaskFlowData().setStandardInvocation(true);
	}

	@Action(value = "next", results = { @Result(name = "success", type = "redirectAction", params = { "actionName",
			"${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String previousInTaskFlow() throws ServiceException {
		getTaskFlowData().getTaskFlowState().next();
		return SUCCESS;
	}

	@Action(value = "prev", results = { @Result(name = "success", type = "redirectAction", params = { "actionName",
			"${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String nextInTaskFlow() throws ServiceException {
		getTaskFlowData().getTaskFlowState().previous();
		return SUCCESS;
	}

	protected String endTaskFlow() {
		return SUCCESS;
	}

	@Action(value = "end", results = { @Result(name = "success", type = "redirect", location = "%{taskFlowDefinition.returnUrl}") })
	public String end() {
		return endTaskFlow();
	}

	@Action(value = "navigateToTask", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String navigateToTask() {
		Task task = getTaskFlowDefinition().getTask(taskName);
		getTaskFlowData().getTaskFlowState().setCurrentTask(task);
		return SUCCESS;
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public String getTaskUrl() {
		return getTaskFlowData().getTaskFlowState().getCurrentTask().getTaskUrl();
	}

	public String getReturnUrl() {
		return getTaskFlowDefinition().getReturnUrl();
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public List<String> getSelectedTasks() {
		return selectedTasks;
	}

	public void setSelectedTasks(List<String> selectedTasks) {
		this.selectedTasks = selectedTasks;
	}

	protected Class<T> getTaskFlowDataTypeArgument() {
		return (Class<T>) GenericTypeUtils.getTypeArguments(BaseTaskFlowAction.class, getClass()).get(0);
	}

	protected Class<E> getTaskFlowDefinitionTypeArgument() {
		return (Class<E>) GenericTypeUtils.getTypeArguments(BaseTaskFlowAction.class, getClass()).get(1);
	}
}
