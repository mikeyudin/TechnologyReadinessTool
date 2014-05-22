package net.techreadiness.plugin.action.task.snapshot.remove;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowData;
import net.techreadiness.plugin.action.task.snapshot.SnapshotTaskFlowDefinition;
import net.techreadiness.plugin.batch.DeleteSnapshotBatchJob;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.BatchJobSchedulerService;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.DataModificationStatus.ModificationState;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.quartz.JobDataMap;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results(value = { @Result(name = "success", location = "/task/snapshot/remove/remove", type = "redirect"),
		@Result(name = "invalid", type = "lastAction", params={"actionName", "remove"}) })
public class SaveAction extends BaseTaskFlowAction<SnapshotTaskFlowData, SnapshotTaskFlowDefinition> {
	private static final long serialVersionUID = 1L;

	@ConversationScoped(value = "snapshotSearchGrid")
	private DataGridState<?> snapshotSearchGrid;

	@Key(Long.class)
	@Element(Boolean.class)
	private Map<Long, Boolean> snapshots = Maps.newHashMap();

	@Inject
	BatchJobSchedulerService batchJobSchedulerService;

	@Inject
	DataModificationStatus dataModificationStatus;

	// @CoreSecured({ })
	@Override
	public String execute() {

		int delayMinutes = 5;

		for (Entry<Long, Boolean> snapshot : snapshots.entrySet()) {
			if (snapshot.getValue().booleanValue()) {
				try {
					Long snapshotWindowId = snapshot.getKey();

					snapshotSearchGrid.deSelectItem(snapshotWindowId.toString());

					JobDataMap jobDataMap = new JobDataMap();

					jobDataMap.put("snapshotWindowId", snapshotWindowId);

					batchJobSchedulerService.scheduleJobWithDelay(DeleteSnapshotBatchJob.class, "DeleteSnapshot_"
							+ snapshotWindowId.toString(), "DeleteSnapshot_" + snapshotWindowId.toString(), delayMinutes,
							jobDataMap);

					delayMinutes += 5;

					Iterator<SnapshotWindow> iterator = getTaskFlowData().getSnapshots().iterator();
					while (iterator.hasNext()) {
						SnapshotWindow next = iterator.next();
						if (next.getSnapshotWindowId().equals(snapshot.getKey())) {
							iterator.remove();
							break;
						}
					}
				} catch (ServiceException se) {
					addActionError(se.getMessage());
				}
			}
		}

		if (hasErrors()) {
			return "invalid";
		}

		dataModificationStatus.setModificationState(ModificationState.SUCCESS);
		dataModificationStatus
				.setMessage("The selected Snapshots have been marked for deletion.  The system will remove them shortly.  They will continue to show in the system in the meantime.");

		return SUCCESS;
	}

	public Map<Long, Boolean> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(Map<Long, Boolean> snapshots) {
		this.snapshots = snapshots;
	}
}
