package net.techreadiness.ui.util;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.opensymphony.xwork2.ActionInvocation;

public class MessageSavingRedirectActionResult extends MessageSavingRedirectResult {
	private static final long serialVersionUID = 1L;
	protected String actionName;
	protected String namespace;

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		actionName = conditionalParse(actionName, invocation);
		if (namespace == null) {
			namespace = invocation.getProxy().getNamespace();
		} else {
			namespace = conditionalParse(namespace, invocation);
		}

		String tmpLocation = actionMapper.getUriFromActionMapping(new ActionMapping(actionName, namespace, null, null));

		setLocation(tmpLocation);

		super.execute(invocation);
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Override
	protected List<String> getProhibitedResultParams() {
		return Arrays.asList("actionName", "namespace", "method", "encode", "parse", "location", "prependServletContext",
				"suppressEmptyParameters", "anchor");
	}
}
