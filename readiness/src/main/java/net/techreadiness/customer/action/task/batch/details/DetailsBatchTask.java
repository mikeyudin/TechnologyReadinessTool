package net.techreadiness.customer.action.task.batch.details;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_LOADING_ACCESS;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.customer.datagrid.FileByIDsItemProvider;
import net.techreadiness.service.FileService;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.File;
import net.techreadiness.ui.action.task.batch.FileBatchTaskFlowAction;
import net.techreadiness.ui.tags.taskview.TaskViewState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class DetailsBatchTask extends FileBatchTaskFlowAction {

	private static final long serialVersionUID = 1L;

	@Inject
	FileService fileService;

	@Inject
	private FileByIDsItemProvider fileByIDsItemProvider;

	@ConversationScoped
	private TaskViewState<File> fileDetailsGrid;

	@Override
	public void prepare() throws Exception {
		fileDetailsGrid.setDetailMode(true);
	}

	@CoreSecured({ CORE_CUSTOMER_FILE_LOADING_ACCESS })
	@Action(value = "details", results = { @Result(name = "success", location = "/task/filebatch/details.jsp"),
			@Result(name = "nofile", location = "/task/filebatch/nofile.jsp") })
	public String viewDetails() throws ServiceException {

		if (getTaskFlowData().getFiles() == null || getTaskFlowData().getFiles().isEmpty()) {
			return "nofile";
		}

		fileByIDsItemProvider.setFileIds(getTaskFlowData().getFileIds());

		return SUCCESS;
	}

	public FileByIDsItemProvider getItemProvider() {
		return fileByIDsItemProvider;
	}

	public void setItemProvider(FileByIDsItemProvider fileByIDsItemProvider) {
		this.fileByIDsItemProvider = fileByIDsItemProvider;
	}

	public TaskViewState<File> getFileDetailsGrid() {
		return fileDetailsGrid;
	}

	public void setFileDetailsGrid(TaskViewState<File> fileDetailsGrid) {
		this.fileDetailsGrid = fileDetailsGrid;
	}

}