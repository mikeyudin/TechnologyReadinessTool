package net.techreadiness.plugin.action.task.scope.limits;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", location = "/net/techreadiness/plugin/action/scope/limits.jsp") })
public class LimitsAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private ViewDef viewDef;
	private Map<String, String> scope;
	private String changeDate;
	private String changeUser;
	private boolean firstTime;

	@Inject
	private ScopeService scopeService;

	@Inject
	private ConfigService configService;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_SCOPE_MIN_SPEC })
	public String execute() {
		Scope scopeObj = scopeService.getById(getServiceContext(), getServiceContext().getScopeId());
		scope = scopeObj.getAsMap();
		viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.SCOPE_MINS);

		Map<String, String> map = scopeService.getLastUpdatedField(getServiceContext(), ViewDefTypeCode.SCOPE_MINS);
		if (!map.isEmpty()) {
			changeUser = map.get("changeUser");
			changeDate = map.get("changeDate");
		} else {
			firstTime = true;
		}

		return SUCCESS;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public Map<String, String> getScope() {
		return scope;
	}

	public void setScope(Map<String, String> scope) {
		this.scope = scope;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeUser() {
		return changeUser;
	}

	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

}
