package net.techreadiness.customer.action.task.user.create;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_CREATE;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", location = "/task/user/add.jsp") })
public class AddAction extends BaseUserAction {
	private static final long serialVersionUID = 1L;

	private ViewDef viewDef;

	@Inject
	private ConfigService configService;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_CREATE })
	public String execute() {
		viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.USER_CREATE);
		conversation.put("orgFilterSelectionHandler", "orgFilterSelectionHandlerForUserCreate");
		conversation.put("roleFilterSelectionHandler", "roleFilterSelectionHandlerForUserCreate");
		return SUCCESS;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

}