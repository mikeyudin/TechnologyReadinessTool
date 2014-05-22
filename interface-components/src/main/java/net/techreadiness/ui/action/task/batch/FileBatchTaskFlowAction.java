package net.techreadiness.ui.action.task.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowData;
import net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition;
import net.techreadiness.persistence.dao.FileDAO.FileTypeCode;
import net.techreadiness.service.BatchJobSchedulerService;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.DataModificationStatus.ModificationState;
import net.techreadiness.service.FileService;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskFlowAction;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowState;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.CreateIfNull;

public class FileBatchTaskFlowAction extends BaseTaskFlowAction<FileBatchTaskFlowData, FileBatchTaskFlowDefinition>
		implements Preparable {

	public enum BatchMode {
		Append, Replace;

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	private static final long serialVersionUID = 1L;
	private static final String FILE_REQUIRED = "Incomplete information, a file must be selected.";
	private static final String AN_ERROR_OCCURRED_FOR_THIS_REQUEST = "An error occurred for this request";

	protected File upload;
	protected String uploadFileName;
	protected String uploadContentType;

	@Inject
	private FileService fileService;
	@Inject
	private BatchJobSchedulerService bsService;
	@Inject
	private DataModificationStatus dataModificationStatus;
	@Inject
	private FileBatchTaskFlowDefinition tfd;

	// Matches what is defined on the file action for the datagrid
	// @ConversationScoped(value="fileDataGrid")
	private static String FILE_DATA_GRID = "fileDataGrid";
	@CreateIfNull
	private net.techreadiness.service.object.File serviceFile;

	public List<FileTypeCode> getTypes() {
		return Collections.emptyList();
	}

	@Action(value = "fileBatchTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String beginTaskFlow() {
		startNewTaskFlow();

		DataGridState<net.techreadiness.service.object.File> fileGrid = conversation
				.get(DataGridState.class, FILE_DATA_GRID);
		List<net.techreadiness.service.object.File> list = fileGrid.getSelectedItems();
		getTaskFlowData().setFiles(new ArrayList<>(list));

		return SUCCESS;
	}

	protected String scheduleImport() {
		try {
			if (upload == null) {
				addFieldError("upload", FILE_REQUIRED);
				dataModificationStatus.setModificationState(ModificationState.FAILURE);
				return "invalid";
			}
			moveFileToUploadFolder();
			serviceFile.setDisplayFilename(uploadFileName);
			serviceFile.setFilename(upload.getName());
			serviceFile.setPath(upload.getParentFile().getPath());
			serviceFile.setRequestDate(new Date());
			serviceFile.setStatus("pending");
			serviceFile = fileService.addOrUpdate(getServiceContext(), serviceFile);
			bsService.schedule(getServiceContext(), serviceFile.getFileId());
			dataModificationStatus.setModificationState(ModificationState.SUCCESS);
			return SUCCESS;
		} catch (Exception e) {
			addActionError(AN_ERROR_OCCURRED_FOR_THIS_REQUEST);
			dataModificationStatus.setModificationState(ModificationState.FAILURE);
			return "invalid";
		}
	}

	protected String scheduleExport() {
		try {
			serviceFile.setPath(fileService.getUploadDir(getServiceContext()));
			String internalFileName = getInternalFileName();
			serviceFile.setFilename(internalFileName);
			serviceFile.setDisplayFilename(internalFileName);
			serviceFile.setStatus("pending");
			serviceFile.setRequestDate(new Date());
			serviceFile = fileService.addOrUpdate(getServiceContext(), serviceFile);
			bsService.schedule(getServiceContext(), serviceFile.getFileId());
			dataModificationStatus.setModificationState(ModificationState.SUCCESS);
			return SUCCESS;
		} catch (Exception e) {
			addActionError(AN_ERROR_OCCURRED_FOR_THIS_REQUEST);
			dataModificationStatus.setModificationState(ModificationState.FAILURE);
			return "invalid";
		}

	}

	public List<BatchMode> getModes() {
		return Collections.emptyList();
	}

	@Override
	public void prepare() throws Exception {
		serviceFile = new net.techreadiness.service.object.File();
	}

	private void moveFileToUploadFolder() throws IOException {
		String internalFileName = getInternalFileName();
		File moved = new File(fileService.getUploadDir(getServiceContext()), internalFileName);
		try {
			Files.move(upload, moved);
		} catch (Exception e) {
			throw new FileNotFoundException("Failed to rename/move uploaded file: " + moved.getName());
		}
		upload = moved;
	}

	private String getInternalFileName() {
		StringBuilder sb = new StringBuilder();
		sb.append(serviceFile.getFileTypeCode());
		sb.append("-");
		sb.append(getServiceContext().getOrgId());
		sb.append("-");
		sb.append(System.currentTimeMillis());
		sb.append(".csv");
		return sb.toString();
	}

	@Action(value = "altFileTaskFlowBegin", results = { @Result(name = "success", type = "redirectAction", params = {
			"actionName", "${taskFlowData.taskFlowState.currentTask.action}", "namespace",
			"${taskFlowData.taskFlowState.currentTask.namespace}" }) })
	public String altBeginTaskFlow() throws ServiceException {
		getTaskFlowData().setStandardInvocation(false);

		return SUCCESS;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public net.techreadiness.service.object.File getServiceFile() {
		return serviceFile;
	}

	public void setServiceFile(net.techreadiness.service.object.File serviceFile) {
		this.serviceFile = serviceFile;
	}

	protected void initialize(String task) {
		// if invoked thru external task link, bootstrap state if necessary
		if (taskFlowStateNeedsReset(task)) {
			TaskFlowState state = new TaskFlowState();
			state.setTasks(Lists.newArrayList(tfd.getTask(task)));
			getTaskFlowData().setTaskFlowState(state);
		}
	}

	private boolean taskFlowStateNeedsReset(String task) {
		if (getTaskFlowData().getTaskFlowState() == null) {
			return true;
		}
		for (Task stateTask : getTaskFlowData().getTaskFlowState().getTasks()) {
			if (stateTask.getTaskName().equals(task)) {
				return false;
			}
		}
		return true;
	}
}
