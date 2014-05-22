package net.techreadiness.plugin.action.task.scope.limits;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ScopeMapService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({
		@Result(name = Action.SUCCESS, params = { "namespace", "/task/scope/limits", "actionName", "limits" }, type = "redirectAction"),
		@Result(name = "invalid", type = "lastAction", params = { "fieldName", "scope", "actionName", "limits" }) })
public class SaveAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private Map<String, String> scope;

	@Inject
	private ScopeMapService scopeMapService;

	@Override
	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_SCOPE_MIN_SPEC })
	public String execute() {
		try {
			scopeMapService.update(getServiceContext(), scope);
		} catch (ValidationServiceException vse) {
			List<ValidationError> errors = vse.getFaultInfo().getAttributeErrors();
			for (ValidationError validationError : errors) {
				addFieldError("scope." + validationError.getFieldName(), validationError.getOnlineMessage());
			}
		}
		if (hasErrors()) {
			return "invalid";
		}

		return SUCCESS;
	}

	public Map<String, String> getScope() {
		return scope;
	}

	public void setScope(Map<String, String> scope) {
		this.scope = scope;
	}

}
