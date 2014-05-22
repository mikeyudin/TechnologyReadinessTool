package net.techreadiness.ui.action.filters;

import java.util.Collection;

import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.tags.dataview.GenericFilterState;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.opensymphony.xwork2.ActionContext;

public class GenericFilterControl extends BaseAction implements ApplicationContextAware, ConversationAware {
	private static final long serialVersionUID = 1L;
	private ApplicationContext applicationContext;
	private Conversation conversation;
	private Collection<?> values;
	private String valueKey;
	private String nameKey;
	private String filterCode;
	private Long id;
	private boolean multiple = false;
	private boolean promptIfEmpty = false;

	@Action(value = "showOptions", results = { @Result(name = "success", location = "/genericFilter/changeSelection.jsp") })
	public String showOptions() {
		values = getSelectionHandler().getList(ActionContext.getContext().getParameters());
		return SUCCESS;
	}

	@Action(value = "add", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true", "filterCode", "%{filterCode}" }) })
	public String add() {
		FilterSelectionHandler<?> selectionHandler = getSelectionHandler();
		if (!multiple) {
			selectionHandler.clear();
		}
		selectionHandler.add(id);
		return SUCCESS;
	}

	@Action(value = "show", results = { @Result(name = "success", location = "/genericFilter/displaySelection.jsp") })
	public String show() {
		FilterSelectionHandler<?> selectionHandler = getSelectionHandler();
		values = selectionHandler.getSelection();
		return SUCCESS;
	}

	@Action(value = "remove", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true", "filterCode", "%{filterCode}" }) })
	public String remove() {
		FilterSelectionHandler<?> selectionHandler = getSelectionHandler();
		selectionHandler.remove(id);
		return SUCCESS;
	}

	public Collection<?> getValues() {
		return values;
	}

	private FilterSelectionHandler<?> getSelectionHandler() {
		GenericFilterState state = (GenericFilterState) getSession().get(filterCode);
		setMultiple(state.isMultiple());
		setValueKey(state.getValueKey());
		setNameKey(state.getNameKey());
		setPromptIfEmpty(state.isPromptIfEmpty());
		FilterSelectionHandler<?> selectionHandler = applicationContext.getBean(state.getBeanName(),
				FilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		return selectionHandler;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public String getFilterCode() {
		return filterCode;
	}

	public void setFilterCode(String filterCode) {
		this.filterCode = filterCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValueKey() {
		return valueKey;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public boolean isPromptIfEmpty() {
		return promptIfEmpty;
	}

	public void setPromptIfEmpty(boolean promptIfEmpty) {
		this.promptIfEmpty = promptIfEmpty;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

}
