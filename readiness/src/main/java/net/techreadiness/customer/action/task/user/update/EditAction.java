package net.techreadiness.customer.action.task.user.update;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_UPDATE;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.datagrid.UserByUserItemProvider;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;
import net.techreadiness.ui.tags.taskview.TaskViewState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", location = "/task/user/edit.jsp"),
		@Result(name = "nouser", location = "/task/user/nouser.jsp") })
public class EditAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	private ViewDef detailsViewDef;
	private ViewDef gridViewDef;

	@Inject
	private ConfigService configService;
	@Inject
	private UserByUserItemProvider itemProvider;
	@Key(Long.class)
	@Element(User.class)
	private Map<Long, User> users;

	@ConversationScoped
	private TaskViewState<User> editUserGrid;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_UPDATE })
	public String execute() {

		if (getTaskFlowData().getUsers() == null || getTaskFlowData().getUsers().isEmpty()) {
			return "nouser";
		}

		detailsViewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.USER_DATAGRID_EDIT);
		gridViewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.USER_DATAGRID_EDIT);

		itemProvider.setUserIds(getTaskFlowData().getUserIds());
		return SUCCESS;
	}

	@Override
	public void prepare() {
		users = new HashMap<>();
	}

	public Map<Long, User> getUsers() {
		return users;
	}

	public void setItemProvider(UserByUserItemProvider itemProvider) {
		this.itemProvider = itemProvider;
	}

	public UserByUserItemProvider getItemProvider() {
		return itemProvider;
	}

	public void setEditUserGrid(TaskViewState<User> editUserGrid) {
		this.editUserGrid = editUserGrid;
	}

	public TaskViewState<User> getEditUserGrid() {
		return editUserGrid;
	}

	public ViewDef getDetailsViewDef() {
		return detailsViewDef;
	}

	public void setDetailsViewDef(ViewDef detailsViewDef) {
		this.detailsViewDef = detailsViewDef;
	}

	public ViewDef getGridViewDef() {
		return gridViewDef;
	}

	public void setGridViewDef(ViewDef gridViewDef) {
		this.gridViewDef = gridViewDef;
	}
}
