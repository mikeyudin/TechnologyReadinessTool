package net.techreadiness.customer.action.task.batch.org;

import static net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition.TASK_FILEBATCH_ORGANIZATIONS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_ORG;

import java.util.List;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.dao.FileDAO.FileTypeCode;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.ui.action.task.batch.FileBatchTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Lists;

@Results({ @Result(name = "success", location = "/task/filebatch/fileBatchTask.jsp") })
public class OrgBatchTask extends FileBatchTaskFlowAction {

	private static final long serialVersionUID = 1L;

	@CoreSecured({ CORE_CUSTOMER_FILE_ORG })
	@Override
	public String execute() throws ServiceException {
		initialize(TASK_FILEBATCH_ORGANIZATIONS);
		return SUCCESS;
	}

	@Override
	public List<FileTypeCode> getTypes() {
		return Lists.newArrayList(FileTypeCode.ORG_EXPORT, FileTypeCode.ORG_IMPORT);
	}

	@Action(value = "process", results = {
			@Result(name = "invalid", type = "lastAction", params = { "actionName", "org-batch-task" }),
			@Result(name = SUCCESS, type = "redirect", location = "%{taskUrl}") })
	@CoreSecured({ CORE_CUSTOMER_FILE_ORG })
	public String process() {
		FileTypeCode code = FileTypeCode.valueOf(getServiceFile().getFileTypeCode());
		if (FileTypeCode.ORG_IMPORT == code) {
			return scheduleImport();
		} else if (FileTypeCode.ORG_EXPORT == code) {
			return scheduleExport();
		} else {
			return "invalid";
		}
	}
}