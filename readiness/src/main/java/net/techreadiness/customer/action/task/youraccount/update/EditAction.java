package net.techreadiness.customer.action.task.youraccount.update;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.datagrid.UserByUserItemProvider;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.youraccount.YourAccountTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", location = "/task/youraccount/edit.jsp") })
public class EditAction extends YourAccountTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;
	@Inject
	private UserByUserItemProvider itemProvider;
	@Key(Long.class)
	@Element(User.class)
	private Map<Long, User> users;

	@ConversationScoped
	private DataGridState<User> editYourAcccountGrid;

	@Override
	public String execute() {
		itemProvider.setUserIds(getTaskFlowData().getUserIds());
		return SUCCESS;
	}

	@Override
	public void prepare() {
		users = new HashMap<>();
	}

	public void setItemProvider(UserByUserItemProvider itemProvider) {
		this.itemProvider = itemProvider;
	}

	public UserByUserItemProvider getItemProvider() {
		return itemProvider;
	}

	public void setEditYourAccountGrid(DataGridState<User> editYourAcccountGrid) {
		this.editYourAcccountGrid = editYourAcccountGrid;
	}

	public DataGridState<User> getEditYourAccountGrid() {
		return editYourAcccountGrid;
	}

	public Map<Long, User> getUsers() {
		return users;
	}
}
