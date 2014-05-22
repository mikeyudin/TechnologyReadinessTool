package net.techreadiness.customer.action.task.org.part;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_PART_UPDATE;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.customer.datagrid.OrgPartTaskItemProvider;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.ui.tags.taskview.TaskViewState;
import net.techreadiness.ui.util.ConversationScoped;
import net.techreadiness.util.MapUtils;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;

public class EditAction extends OrgTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	private ViewDef viewDef;
	private ViewDef detailsViewDef;

	@Inject
	private ConfigService configService;
	@Inject
	private OrgPartTaskItemProvider orgPartTaskItemProvider;
	@Inject
	private ScopeService scopeService;

	@Element(String.class)
	private Map<String, Map<String, String>> participations;

	@ConversationScoped(value = "participationsDataGrid")
	TaskViewState<?> participationsDataGrid;

	@CoreSecured({ CORE_CUSTOMER_ORG_PART_UPDATE })
	@Override
	@Action(results = { @Result(name = "success", location = "/task/org/participations.jsp"),
			@Result(name = "noorg", location = "/task/org/noorg.jsp") })
	public String execute() {

		if (getTaskFlowData().getOrgs() == null || getTaskFlowData().getOrgs().isEmpty()) {
			return "noorg";
		}

		setViewDef(configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.ORG_PART_DATAGRID));
		orgPartTaskItemProvider.setScope(scopeService.getById(getServiceContext(), getServiceContext().getScopeId()));
		orgPartTaskItemProvider.setOrgs(getTaskFlowData().getOrgs());

		detailsViewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.ORG_PART);

		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		participations = MapUtils.makeComputingMap();
	}

	public Map<String, Map<String, String>> getParticipations() {
		return participations;
	}

	public void setParticipations(Map<String, Map<String, String>> participations) {
		this.participations = participations;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public OrgPartTaskItemProvider getOrgPartTaskItemProvider() {
		return orgPartTaskItemProvider;
	}

	public TaskViewState<?> getParticipationsDataGrid() {
		return participationsDataGrid;
	}

	public ViewDef getDetailsViewDef() {
		return detailsViewDef;
	}

	public void setDetailsViewDef(ViewDef detailsViewDef) {
		this.detailsViewDef = detailsViewDef;
	}
}
