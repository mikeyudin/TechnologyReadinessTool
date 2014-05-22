package net.techreadiness.plugin.action.task.snapshot.edit;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowData;
import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowDefinition;
import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.ui.task.BaseTaskFlowAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results(value = { @Result(name = "success", location = "/task/snapshot/edit/edit", type = "redirect"),
		@Result(name = "invalid", type = "lastAction", params={"actionName", "edit"}) })
public class SaveAction extends BaseTaskFlowAction<SnapshotTaskFlowData, SnapshotTaskFlowDefinition> implements Preparable {
	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(SnapshotWindow.class)
	private Map<Long, SnapshotWindow> snapshots;

	@Inject
	SnapshotWindowService snapshotWindowService;

	// @CoreSecured({ })
	@Override
	public String execute() {
		for (Entry<Long, SnapshotWindow> entry : snapshots.entrySet()) {
			SnapshotWindow snapshotWindow = new SnapshotWindow();

			snapshotWindow.setSnapshotWindowId(Long.valueOf(entry.getKey()));

			if (entry.getValue().getVisible() != null) {
				Boolean visible = entry.getValue().getVisible();
				snapshotWindow.setVisible(visible);
			}

			if (entry.getValue().getName() != null) {
				String name = entry.getValue().getName();
				snapshotWindow.setName(name);
			}

			try {
				SnapshotWindow updatedSnapshot = snapshotWindowService.addOrUpdate(getServiceContext(), snapshotWindow);

				updateTaskFlowData(updatedSnapshot);
			} catch (ValidationServiceException vse) {
				List<ValidationError> errors = vse.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError(
							"snapshots['" + snapshotWindow.getSnapshotWindowId() + "']." + validationError.getFieldName(),
							validationError.getOnlineMessage());
				}
			} catch (ServiceException se) {
				addActionError(se.getMessage());
			}
		}

		if (hasErrors()) {
			return "invalid";
		}

		return SUCCESS;
	}

	private void updateTaskFlowData(SnapshotWindow updatedSnapshot) {

		List<SnapshotWindow> snapshots = getTaskFlowData().getSnapshots();

		for (SnapshotWindow sw : snapshots) {
			if (sw.getSnapshotWindowId().equals(updatedSnapshot.getSnapshotWindowId())) {
				sw = updatedSnapshot;
			}
		}
	}

	@Override
	public void prepare() throws Exception {
		snapshots = Maps.newHashMap();
	}

	public Map<Long, SnapshotWindow> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(Map<Long, SnapshotWindow> snapshots) {
		this.snapshots = snapshots;
	}
}
