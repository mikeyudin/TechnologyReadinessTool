package net.techreadiness.customer.action.task.batch.org.info;

import static net.techreadiness.navigation.taskflow.filebatch.FileBatchTaskFlowDefinition.TASK_FILEBATCH_ORG_INFO;
import static net.techreadiness.security.CorePermissionCodes.READY_CUSTOMER_FILE_ORG_INFO;

import java.util.List;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.dao.FileDAO.FileTypeCode;
import net.techreadiness.ui.action.task.batch.FileBatchTaskFlowAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Lists;

@Results({ @Result(name = "success", location = "/task/filebatch/fileBatchTask.jsp") })
public class OrgInfoBatchTask extends FileBatchTaskFlowAction {
	private static final long serialVersionUID = 1L;

	@Override
	@CoreSecured({ READY_CUSTOMER_FILE_ORG_INFO })
	public String execute() throws Exception {
		initialize(TASK_FILEBATCH_ORG_INFO);
		return SUCCESS;
	}

	@Override
	public List<FileTypeCode> getTypes() {
		return Lists.newArrayList(FileTypeCode.ORG_INFO_EXPORT, FileTypeCode.ORG_INFO_IMPORT);
	}

	@Action(value = "process", results = { @Result(name = "invalid", type = "lastAction", params={"actionName", "org-info-batch-task"}),
			@Result(name = SUCCESS, type = "redirect", location = "%{taskUrl}") })
	@CoreSecured({ READY_CUSTOMER_FILE_ORG_INFO })
	public String process() {
		FileTypeCode code = FileTypeCode.valueOf(getServiceFile().getFileTypeCode());
		if (FileTypeCode.ORG_INFO_IMPORT == code) {
			return scheduleImport();
		} else if (FileTypeCode.ORG_INFO_EXPORT == code) {
			return scheduleExport();
		} else {
			return "invalid";
		}
	}
}
