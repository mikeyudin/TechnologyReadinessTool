package net.techreadiness.plugin.action.task.snapshot;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.ui.task.BaseTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class SaveAction extends BaseTaskFlowAction<SnapshotTaskFlowData, SnapshotTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	private SnapshotWindow snapshotWindow;

	@Inject
	SnapshotWindowService snapshotWindowService;
	@Inject
	DataModificationStatus modStatus;

	// @CoreSecured({ })
	@Action(value = "save", results = { @Result(name = "success", location = "/task/snapshot/add", type = "redirect"),
			@Result(name = "invalid", type = "lastAction", params = { "fieldName", "snapshotWindow", "actionName", "add" }) })
	public String save() {

		SnapshotWindow newSnapshotWindow = null;

		try {
			newSnapshotWindow = snapshotWindowService.addOrUpdate(getServiceContext(), snapshotWindow);
			modStatus.setMessage(getText("ready.task.snapshot.add.success"));
		} catch (ValidationServiceException vse) {
			List<ValidationError> errors = vse.getFaultInfo().getAttributeErrors();
			for (ValidationError validationError : errors) {
				addFieldError("snapshotWindow." + validationError.getFieldName(), validationError.getOnlineMessage());
			}
		}

		if (hasErrors()) {
			return "invalid";
		}

		getTaskFlowData().getSnapshots().add(newSnapshotWindow);

		return SUCCESS;
	}

	public SnapshotWindow getSnapshotWindow() {
		return snapshotWindow;
	}

	public void setSnapshotWindow(SnapshotWindow snapshotWindow) {
		this.snapshotWindow = snapshotWindow;
	}
}
