package net.techreadiness.customer.action.filebatch;

import java.io.FileInputStream;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowData;
import net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition;
import net.techreadiness.service.FileService;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.File;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskControlAction;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Lists;

public class ListAction extends BaseTaskControlAction<FileBatchTaskFlowDefinition> {

	private static final long serialVersionUID = 1L;

	@ConversationScoped(value = "fileDataGrid")
	private DataGridState<File> fileDataGrid;
	private String fileId;
	private FileInputStream fileInputStream;
	private String fileType;
	private Long Id;
	private String fileName;

	@Inject
	private FileItemProvider itemProvider;
	@Inject
	private FileService fileService;
	@Inject
	private FileBatchTaskFlowDefinition fileBatchTaskFlow;
	@Inject
	private FileBatchTaskFlowData fileBatchTaskFlowData;

	@Action(value = "list", results = { @Result(name = "success", location = "/filebatch/list.jsp") })
	@Override
	public String execute() throws Exception {
		getItemProvider().setServiceContext(getServiceContext());
		return SUCCESS;
	}

	@Action(value = "download", results = { @Result(name = "success", type = "stream", params = { "contentType",
			"application/octet-stream", "inputName", "fileInputStream", "contentDisposition",
			"attachment;filename=\"%{fileName}\"", "bufferSize", "1024" }) })
	public String download() throws Exception {
		File file = fileService.getById(getServiceContext(), Long.valueOf(fileId));

		// determine which file requested based on fileType:
		// f = filename
		// m = error messages
		// d = error data
		if ("m".equals(fileType)) {
			fileName = file.getErrorMessageFilename();
		} else if ("d".equals(fileType)) {
			fileName = file.getErrorDataFilename();
		} else {
			fileName = file.getFilename();
		}
		fileInputStream = FileUtils
				.openInputStream(new java.io.File(fileService.getUploadDir(getServiceContext()), fileName));
		return SUCCESS;
	}

	@Action(value = "viewFileDetails", results = { @Result(name = "success", type = "redirectAction", params = {
			"namespace", "/task/batch/details", "actionName", "details" }) })
	public String detailsEditParticipations() throws ServiceException {
		return routeToTask(FileBatchTaskFlowDefinition.TASK_FILEBATCH_DETAILS);
	}

	private String routeToTask(String taskName) {
		List<File> files = Lists.newArrayList();
		files.add(fileService.getById(getServiceContext(), getId()));
		Task task = fileBatchTaskFlow.getTask(taskName);
		TaskFlowState state = new TaskFlowState();
		state.setTasks(Lists.newArrayList(task));
		fileBatchTaskFlowData.setTaskFlowState(state);
		fileBatchTaskFlowData.setFiles(files);

		return SUCCESS;
	}

	public String getFileName() {
		return fileName;
	}

	public DataGridState<File> getFileDataGrid() {
		return fileDataGrid;
	}

	public FileItemProvider getItemProvider() {
		return itemProvider;
	}

	public FileInputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(FileInputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setFileDataGrid(DataGridState<File> dataGrid) {
		fileDataGrid = dataGrid;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}
}
