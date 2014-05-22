package net.techreadiness.ui.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.interceptor.ParametersInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

public class SavePreviousAction extends ParametersInterceptor implements PreResultListener {
	private static final long serialVersionUID = 1L;
	public static final String fieldErrorsSessionKey = "__LastActionInterceptor_FieldErrors_SessionKey";
	public static final String actionErrorsSessionKey = "__LastActionInterceptor_ActionErrors_SessionKey";
	public static final String actionMessagesSessionKey = "__LastActionInterceptor_ActionMessages_SessionKey";
	public static final String actionParametersSessionKey = "__LastActionInterceptor_ActionParametersMessages_SessionKey";

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		invocation.addPreResultListener(this);
		Object action = invocation.getAction();
		ActionContext context = invocation.getInvocationContext();
		if (action instanceof ValidationAware && isPopulatePrevious(context)) {
			ValidationAware validationAwareAction = (ValidationAware) action;
			Map<String, List<String>> fieldErrors = (Map<String, List<String>>) context.getSession().get(fieldErrorsSessionKey);
			Collection<String> actionErrors = (Collection<String>) context.getSession().get(actionErrorsSessionKey);
			Collection<String> actionMessages = (Collection<String>) context.getSession().get(actionMessagesSessionKey);

			if (actionErrors != null) {
				if (validationAwareAction.getActionErrors() == null) {
					validationAwareAction.setActionErrors(actionErrors);
				} else {
					for (String error : actionErrors) {
						validationAwareAction.addActionError(error);
					}
				}
			}

			if (fieldErrors != null) {
				if (validationAwareAction.getFieldErrors() == null) {
					validationAwareAction.setFieldErrors(fieldErrors);
				} else {
					for (Entry<String, List<String>> entry : fieldErrors.entrySet()) {
						for (String fieldError : entry.getValue()) {
							validationAwareAction.addFieldError(entry.getKey(), fieldError);
						}
					}
				}
			}

			if (actionMessages != null) {
				if (validationAwareAction.getActionMessages() == null) {
					validationAwareAction.setActionMessages(actionMessages);
				} else {
					for (String message : actionMessages) {
						validationAwareAction.addActionMessage(message);
					}
				}
			}

		}
		return super.intercept(invocation);
	}

	@Override
	protected Map<String, Object> retrieveParameters(ActionContext context) {
		HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
		if (request.getSession(false) != null && isPopulatePrevious(context)) {
			return (Map<String, Object>) context.getSession().get(actionParametersSessionKey);
		}
		return Collections.emptyMap();
	}

	protected boolean isPopulatePrevious(ActionContext context) {
		return context.getParameters().containsKey("populatePrevious");
	}

	@Override
	public void beforeResult(ActionInvocation invocation, String resultCode) {
		ResultConfig result = invocation.getProxy().getConfig().getResults().get(resultCode);
		ActionContext context = invocation.getInvocationContext();
		if (result != null && LastActionResult.class.getName().equals(result.getClassName())) {
			Map<String, Object> params = Maps.newHashMap();
			for (Entry<String, Object> entry : context.getParameters().entrySet()) {
				if (acceptableName(entry.getKey())) {
					params.put(entry.getKey(), entry.getValue());
				}
			}

			context.getSession().put(actionParametersSessionKey, params);
			Object action = invocation.getAction();
			if (action instanceof ValidationAware) {
				ValidationAware validationAwareAction = (ValidationAware) action;
				if (validationAwareAction.hasFieldErrors()) {
					context.getSession().put(fieldErrorsSessionKey, validationAwareAction.getFieldErrors());
				}

				if (validationAwareAction.hasActionErrors()) {
					context.getSession().put(actionErrorsSessionKey, validationAwareAction.getActionErrors());
				}

				if (validationAwareAction.hasActionMessages()) {
					context.getSession().put(actionMessagesSessionKey, validationAwareAction.getActionMessages());
				}
			}

		} else {
			context.getSession().remove(actionParametersSessionKey);
			context.getSession().remove(fieldErrorsSessionKey);
			context.getSession().remove(actionErrorsSessionKey);
			context.getSession().remove(actionMessagesSessionKey);
			context.getSession().remove("last.action.populate.previous");
		}
	}
}
