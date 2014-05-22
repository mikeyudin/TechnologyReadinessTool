package net.techreadiness.customer.action.task.user.enable;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ENABLE;

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
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", location = "/task/user/enable.jsp"),
		@Result(name = "nouser", location = "/task/user/nouser.jsp") })
public class EditAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;
	@Inject
	private ConfigService configService;
	@Inject
	private UserByUserItemProvider itemProvider;
	@Key(Long.class)
	@Element(User.class)
	private Map<Long, User> users;

	private ViewDef viewDef;

	@ConversationScoped
	private DataGridState<User> enableUserGrid;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_ENABLE })
	public String execute() {
		if (getTaskFlowData().getUsers() == null || getTaskFlowData().getUsers().isEmpty()) {
			return "nouser";
		}
		viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.USER_ENABLE);
		itemProvider.setUserIds(getTaskFlowData().getUserIds());
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		users = new HashMap<>();
	}

	public UserByUserItemProvider getItemProvider() {
		return itemProvider;
	}

	public void setEnableUserGrid(DataGridState<User> enableUserGrid) {
		this.enableUserGrid = enableUserGrid;
	}

	public DataGridState<User> getEnableUserGrid() {
		return enableUserGrid;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public Map<Long, User> getUsers() {
		return users;
	}
}
