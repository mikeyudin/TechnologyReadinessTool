package net.techreadiness.plugin.action.task.survey.network;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.datagrid.OrgNetworkItemProvider;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.task.org.survey.SurveyQuestionsTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", location = "/net/techreadiness/plugin/action/org/survey/network.jsp"),
		@Result(name = "noorg", location = "/net/techreadiness/plugin/action/org/survey/noorg.jsp") })
public class NetworkAction extends SurveyQuestionsTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Inject
	private OrgNetworkItemProvider orgNetworkItemProvider;

	@Inject
	private ConfigService configService;

	@ConversationScoped
	private DataGridState<Org> orgNetworkGrid;

	private ViewDef viewDef;

	@Key(Long.class)
	@Element(Org.class)
	private Map<Long, Org> orgs;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_NETWORK_INFRASTRUCTURE })
	@Override
	public String execute() {
		viewDef = configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.ORG_NETWORK_TASK);
		orgNetworkItemProvider.setOrgs(getTaskFlowData().getOrgs());
		return SUCCESS;
	}

	public DataGridState<Org> getOrgNetworkGrid() {
		return orgNetworkGrid;
	}

	public OrgNetworkItemProvider getOrgNetworkItemProvider() {
		return orgNetworkItemProvider;
	}

	public Map<Long, Org> getOrgs() {
		return orgs;
	}

	public void setOrgs(Map<Long, Org> orgs) {
		this.orgs = orgs;
	}

	@Override
	public void prepare() throws Exception {
		orgs = Maps.newHashMap();
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

}
