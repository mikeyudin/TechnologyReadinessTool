package net.techreadiness.customer.action.task.org.edit;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_UPDATE;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.customer.datagrid.OrgsByIdEditTaskItemProvider;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.filters.MultipleFilterSelectionHandler;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationAware;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

public class EditAction extends OrgTaskFlowAction implements Preparable, ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	private Map<Long, List<SimpleEntry<Long, String>>> orgTypesMap;

	@Inject
	private ConfigService configService;
	@Inject
	private OrganizationService organizationService;

	@Inject
	OrgsByIdEditTaskItemProvider orgsByIdItemProvider;

	private ViewDef detailsViewDef;

	@Key(Long.class)
	@Element(Org.class)
	private Map<Long, Org> orgs;

	@ConversationScoped
	private DataGridState<Map<String, String>> editOrgsDataGrid;

	private ApplicationContext applicationContext;

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_UPDATE })
	@Action(results = { @Result(name = "success", location = "/task/org/edit.jsp"),
			@Result(name = "noorg", location = "/task/org/noorg.jsp") })
	public String execute() {

		if (getTaskFlowData().getOrgs() == null || getTaskFlowData().getOrgs().isEmpty()) {
			return "noorg";
		}

		conversation.put("orgFilterSelectionHandler", "orgFilterSelectionHandlerForOrgEdit");
		Collection<Org> orgs = Lists.newArrayList();
		for (Org org : getTaskFlowData().getOrgs()) {
			orgs.add(org);
			Org parentOrg = organizationService.getById(getServiceContext(), org.getParentOrgId());
			List<SimpleEntry<Long, String>> orgTypes = Lists.newArrayList();
			if (org.getParentOrgId() != null) {
				orgTypes = organizationService.findChildOrgTypesByParentOrgType(getServiceContext(),
						parentOrg.getOrgTypeId());
			} else {
				orgTypes = organizationService.findChildOrgTypesByParentOrgType(getServiceContext(), null);
			}
			getOrgSelectionHandler().addSet(org.getOrgId().toString());
			getOrgSelectionHandler().add(org.getParentOrgId(), org.getOrgId().toString());
			orgTypesMap.put(org.getOrgId(), orgTypes);
		}

		orgsByIdItemProvider.setOrgs(orgs);
		orgsByIdItemProvider.setServiceContext(getServiceContext());
		detailsViewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.ORG);

		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		orgs = Maps.newHashMap();
		orgTypesMap = Maps.newHashMap();
	}

	public DataGridState<Map<String, String>> getEditOrgsDataGrid() {
		return editOrgsDataGrid;
	}

	public void setEditOrgsDataGrid(DataGridState<Map<String, String>> editOrgsDataGrid) {
		this.editOrgsDataGrid = editOrgsDataGrid;
	}

	public Map<Long, Org> getOrgs() {
		return orgs;
	}

	public void setOrgs(Map<Long, Org> orgs) {
		this.orgs = orgs;
	}

	public OrgsByIdEditTaskItemProvider getOrgsByIdItemProvider() {
		return orgsByIdItemProvider;
	}

	public void setOrgsByIdItemProvider(OrgsByIdEditTaskItemProvider orgsByIdItemProvider) {
		this.orgsByIdItemProvider = orgsByIdItemProvider;
	}

	public ViewDef getDetailsViewDef() {
		return detailsViewDef;
	}

	public void setDetailsViewDef(ViewDef detailsViewDef) {
		this.detailsViewDef = detailsViewDef;
	}

	public Map<Long, List<SimpleEntry<Long, String>>> getOrgTypesMap() {
		return orgTypesMap;
	}

	public void setOrgTypesMap(Map<Long, List<SimpleEntry<Long, String>>> orgTypesMap) {
		this.orgTypesMap = orgTypesMap;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	protected MultipleFilterSelectionHandler<Org> getOrgSelectionHandler() {
		String beanName = conversation.get(String.class, "orgFilterSelectionHandler");
		MultipleFilterSelectionHandler<Org> selectionHandler = applicationContext.getBean(beanName,
				MultipleFilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		return selectionHandler;
	}
}
