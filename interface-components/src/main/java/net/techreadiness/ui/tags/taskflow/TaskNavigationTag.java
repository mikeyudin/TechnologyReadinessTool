package net.techreadiness.ui.tags.taskflow;

import net.techreadiness.ui.tags.BaseTag;
import net.techreadiness.ui.task.TaskFlowState;

public class TaskNavigationTag extends BaseTag {
	private String saveButton = "core.save";
	private String resetButton = "core.reset";
	private TaskFlowState taskFlow;
	private boolean detailMode;
	private boolean allowModeSwitch;
	private String value;
	private boolean suppressSave;

	@Override
	public String execute() throws Exception {
		return "/taskFlow/taskNavigation.jsp";
	}

	public boolean isPreviousAvailable() {
		return taskFlow.hasPrevious();
	}

	public boolean isNextAvailable() {
		return taskFlow.hasNext();
	}

	public void setSaveButton(String saveButton) {
		this.saveButton = saveButton;
	}

	public String getSaveButton() {
		return saveButton;
	}

	public void setResetButton(String resetButton) {
		this.resetButton = resetButton;
	}

	public String getResetButton() {
		return resetButton;
	}

	public TaskFlowState getTaskFlow() {
		return taskFlow;
	}

	public void setTaskFlow(TaskFlowState taskFlow) {
		this.taskFlow = taskFlow;
	}

	public boolean isDetailMode() {
		return detailMode;
	}

	public void setDetailMode(boolean detailMode) {
		this.detailMode = detailMode;
	}

	public boolean isAllowModeSwitch() {
		return allowModeSwitch;
	}

	public void setAllowModeSwitch(boolean allowModeSwitch) {
		this.allowModeSwitch = allowModeSwitch;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSuppressSave() {
		return suppressSave;
	}

	public void setSuppressSave(boolean suppressSave) {
		this.suppressSave = suppressSave;
	}

}
