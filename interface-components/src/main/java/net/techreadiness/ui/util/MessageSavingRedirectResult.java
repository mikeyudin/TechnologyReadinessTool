package net.techreadiness.ui.util;

import java.util.Collection;

import org.apache.struts2.dispatcher.ServletRedirectResult;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;

public class MessageSavingRedirectResult extends ServletRedirectResult {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {

		if (invocation.getAction() instanceof ValidationAware) {
			ValidationAware validationAction = (ValidationAware) invocation.getAction();
			if (validationAction.hasActionMessages()) {
				Collection<String> messages = validationAction.getActionMessages();
				invocation.getInvocationContext().getSession().put("last.action.actionMessages", messages);
				invocation.getInvocationContext().getSession().put("last.action.actionMessages.populate.prev", Boolean.TRUE);
			}
		}

		super.doExecute(finalLocation, invocation);
	}
}
