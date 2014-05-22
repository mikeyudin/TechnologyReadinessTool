package net.techreadiness.customer.action.task.batch.user;

import static net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition.TASK_FILEBATCH_USERS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_USER;

import java.util.List;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.dao.FileDAO.FileTypeCode;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.ui.action.task.batch.FileBatchTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Lists;

public class UserBatchTask extends FileBatchTaskFlowAction {

	private static final long serialVersionUID = 1L;

	@CoreSecured({ CORE_CUSTOMER_FILE_USER })
	@Action(value = "users", results = { @Result(name = "success", location = "/task/filebatch/fileBatchTask.jsp") })
	public String processUsers() throws ServiceException {
		initialize(TASK_FILEBATCH_USERS);
		return SUCCESS;
	}

	@Override
	public List<FileTypeCode> getTypes() {
		return Lists.newArrayList(FileTypeCode.USER_EXPORT, FileTypeCode.USER_IMPORT);
	}

	@Action(value = "process", results = {
			@Result(name = "invalid", type = "lastAction", params = { "actionName", "users" }),
			@Result(name = SUCCESS, type = "redirect", location = "%{taskUrl}") })
	@CoreSecured({ CORE_CUSTOMER_FILE_USER })
	public String process() {
		FileTypeCode code = FileTypeCode.valueOf(getServiceFile().getFileTypeCode());
		if (FileTypeCode.USER_IMPORT == code) {
			return scheduleImport();
		} else if (FileTypeCode.USER_EXPORT == code) {
			return scheduleExport();
		} else {
			return "invalid";
		}
	}
}