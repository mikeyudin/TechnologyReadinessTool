package net.techreadiness.ui.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.service.DataModificationStatus;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

/**
 * setup service based errors and messages
 */
public class DataModificationInterceptor extends AbstractInterceptor implements PreResultListener {
	private static final long serialVersionUID = 1L;

	@Inject
	DataModificationStatus dataModificationStatus;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// as the save previousaction and lastactionresult make actionerrors behave differently
		// we need to treat them differently
		invocation.addPreResultListener(this);
		return invocation.invoke();
	}

	@Override
	public void beforeResult(ActionInvocation invocation, String resultCode) {
		// MessageSavingRedirectResult saves action messages if any exist
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		Boolean saved = (Boolean) session.get("last.action.actionMessages.populate.prev");
		if (saved != null && saved.booleanValue() && invocation.getAction() instanceof ValidationAware) {
			/*
			 * Action messages have been saved from the previous request, use them to populate the current action's messages.
			 */
			Collection<String> previousMessages = (Collection<String>) session.get("last.action.actionMessages");
			ValidationAware action = (ValidationAware) invocation.getAction();
			if (action.hasActionMessages()) {
				previousMessages.addAll(action.getActionMessages());
			}
			action.setActionMessages(previousMessages);

			// Remove state from the session
			session.put("last.action.actionMessages.populate.prev", null);
			session.put("last.action.actionMessages", null);
		} else if (invocation.getAction() instanceof ValidationAware) {
			// Add the data modification status message or error to the current action.
			ValidationAware action = (ValidationAware) invocation.getAction();

			switch (dataModificationStatus.getModificationState()) {
			case FAILURE:
				List<String> actionErrors = Lists.newArrayList();
				actionErrors.add(StringUtils.defaultIfBlank(dataModificationStatus.getMessage(),
						"Errors have occurred, see messages below."));

				if (action.hasActionErrors()) {
					actionErrors.addAll(action.getActionErrors());
				}

				action.setActionErrors(actionErrors);
				break;
			case SUCCESS:
			case REQUESTED:
				List<String> actionMessages = Lists.newArrayList();

				if (dataModificationStatus.getModificationType() == ModificationType.UPDATE) {
					actionMessages.add(StringUtils.defaultIfBlank(dataModificationStatus.getMessage(),
							"Changes saved successfully."));
				} else {
					actionMessages.add(StringUtils.defaultIfBlank(dataModificationStatus.getMessage(),
							"Operation request was successful."));
				}

				if (action.hasActionMessages()) {
					actionMessages.addAll(action.getActionMessages());
				}

				action.setActionMessages(actionMessages);
				break;
			case NONE:
				break;
			}

		}
		dataModificationStatus.reset();
	}
}
