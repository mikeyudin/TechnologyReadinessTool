package net.techreadiness.customer.action.task.batch.device;

import static net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition.TASK_FILEBATCH_DEVICES;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_DEVICE;

import java.util.List;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.dao.FileDAO.FileTypeCode;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.ui.action.task.batch.FileBatchTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Lists;

public class DeviceBatchTask extends FileBatchTaskFlowAction {

	private static final long serialVersionUID = 1L;

	@CoreSecured({ CORE_CUSTOMER_FILE_DEVICE })
	@Action(value = "devices", results = { @Result(name = SUCCESS, location = "/task/filebatch/fileBatchTask.jsp") })
	public String processDevices() throws ServiceException {
		initialize(TASK_FILEBATCH_DEVICES);
		return SUCCESS;
	}

	@Override
	public List<FileTypeCode> getTypes() {
		return Lists.newArrayList(FileTypeCode.DEVICE_EXPORT, FileTypeCode.DEVICE_IMPORT);
	}

	@Action(value = "process", results = { @Result(name = "invalid", type = "lastAction", params={"actionName", "devices"}),
			@Result(name = SUCCESS, type = "redirect", location = "%{taskUrl}") })
	@CoreSecured({ CORE_CUSTOMER_FILE_DEVICE })
	public String process() {
		FileTypeCode code = FileTypeCode.valueOf(getServiceFile().getFileTypeCode());
		if (FileTypeCode.DEVICE_IMPORT == code) {
			return scheduleImport();
		} else if (FileTypeCode.DEVICE_EXPORT == code) {
			return scheduleExport();
		} else {
			return "invalid";
		}
	}

	@Override
	public List<BatchMode> getModes() {
		return Lists.newArrayList(BatchMode.values());
	}
}
