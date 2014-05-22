package net.techreadiness.ui.task;

import java.util.List;

import com.google.common.collect.Lists;

public abstract class TaskFlowDefinition {
	public abstract String getNamespace();

	public abstract String getStartAction();

	public abstract String getReturnUrl();

	public abstract String getEndTaskFlowUrl();

	protected List<Task> tasks;
	protected List<Task> externalTasks;

	public void addTask(Task task) {
		if (tasks == null) {
			tasks = Lists.newArrayList();
		}

		tasks.add(task);
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task getTask(String taskCode) {
		for (Task task : tasks) {
			if (task.getTaskName().equals(taskCode)) {
				return task;
			}
		}
		return null;
	}

	public String getTaskCode(Task task) {
		return task.getTaskName();
	}

	public void addExternalTask(Task task) {
		if (externalTasks == null) {
			externalTasks = Lists.newArrayList();
		}

		externalTasks.add(task);
	}

	public List<Task> getExternalTasks() {
		return externalTasks;
	}

	public void setExternalTasks(List<Task> externalTasks) {
		this.externalTasks = externalTasks;
	}

	public Task getExternalTask(String taskCode) {
		for (Task task : externalTasks) {
			if (task.getTaskName().equals(taskCode)) {
				return task;
			}
		}
		return null;
	}
}
