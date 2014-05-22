package net.techreadiness.plugin.action.reports;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class ConsortiaReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DEVICE_RPT_ACCESS })
	@Action(value = "consortiareport", results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/reports/consortiareport.jsp") })
	public String consortiaReport() {
		return SUCCESS;
	}

}
