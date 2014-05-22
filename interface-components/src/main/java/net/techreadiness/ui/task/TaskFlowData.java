package net.techreadiness.ui.task;

import java.io.Serializable;

public abstract class TaskFlowData implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean standardInvocation;

	private TaskFlowState taskFlowState;

	public TaskFlowState getTaskFlowState() {
		return taskFlowState;
	}

	public void setTaskFlowState(TaskFlowState taskFlowState) {
		this.taskFlowState = taskFlowState;
	}

	public boolean isStandardInvocation() {
		return standardInvocation;
	}

	public void setStandardInvocation(boolean standardInvocation) {
		this.standardInvocation = standardInvocation;
	}

}
