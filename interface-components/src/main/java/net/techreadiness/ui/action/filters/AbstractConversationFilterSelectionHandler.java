package net.techreadiness.ui.action.filters;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;

import com.opensymphony.xwork2.ActionContext;

public abstract class AbstractConversationFilterSelectionHandler<T> implements ConversationAware, FilterSelectionHandler<T> {
	protected Conversation conversation;

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	protected ServiceContext getServiceContext() {
		return (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
	}

	protected DataGridState<?> getDataGridState(String dataGridId) {
		return conversation.get(DataGridState.class, dataGridId);
	}
}
