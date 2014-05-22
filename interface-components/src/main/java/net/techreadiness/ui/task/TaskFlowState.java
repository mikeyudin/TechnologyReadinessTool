package net.techreadiness.ui.task;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.list.PredicatedList;

import com.google.common.collect.Lists;

public class TaskFlowState implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Task> tasks;
	private int index;
	private boolean initialized;

	public TaskFlowState() {
		tasks = PredicatedList.decorate(Lists.newArrayList(), PredicateUtils.notNullPredicate());
	}

	public void next() {
		if (hasNext()) {
			ListIterator<Task> currentIterator = getCurrentIterator();
			currentIterator.next();
			setIndex(currentIterator.nextIndex());
		}
	}

	public void previous() {
		if (hasPrevious()) {
			setIndex(getCurrentIterator().previousIndex());
		}
	}

	public boolean hasNext() {
		if (!isIndexValid()) {
			return false;
		}
		ListIterator<Task> currentIterator = getCurrentIterator();
		if (currentIterator.hasNext()) {
			currentIterator.next();
		}
		return currentIterator.hasNext();
	}

	public boolean hasPrevious() {
		if (!isIndexValid()) {
			return false;
		}
		return getCurrentIterator().hasPrevious();
	}

	public Task getCurrentTask() {
		if (!isIndexValid()) {
			return null;
		}
		return getCurrentIterator().next();
	}

	protected boolean isIndexValid() {
		return !(getIndex() < 0 || tasks.isEmpty());
	}

	protected ListIterator<Task> getCurrentIterator() {
		ListIterator<Task> listIterator = getTasks().listIterator(getIndex());

		return listIterator;
	}

	public void setCurrentTask(Task task) {
		int newIndex = getTasks().indexOf(task);
		if (newIndex == -1) {
			return;
		}
		setIndex(newIndex);
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks.clear();
		if (tasks != null) {
			this.tasks.addAll(tasks);
			setIndex(getTasks().listIterator().nextIndex());
		}
	}

	private int getIndex() {
		return index;
	}

	private void setIndex(int index) {
		this.index = index;
		initialized = false;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void initialized() {
		initialized = true;
	}
}
