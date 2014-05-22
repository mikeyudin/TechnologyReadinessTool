package net.techreadiness.customer.action.task.org.contact;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_CONTACT_UPDATE;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.customer.datagrid.OrgContactTaskItemProvider;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;
import net.techreadiness.util.MapUtils;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Iterators;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;

public class EditAction extends OrgTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	private ViewDef viewDef;

	private Long orgSelection = Long.valueOf(0);

	@Inject
	private ConfigService configService;
	@Inject
	private OrgContactTaskItemProvider orgContactTaskItemProvider;

	@Element(String.class)
	private Map<String, Map<String, String>> contacts;

	@ConversationScoped(value = "contactsDataGrid")
	DataGridState<?> contactsDataGrid;

	@Override
	public void prepare() throws Exception {
		contacts = MapUtils.makeComputingMap();
	}

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_CONTACT_UPDATE })
	@Action(results = { @Result(name = "success", location = "/task/org/contacts.jsp"),
			@Result(name = "noorg", location = "/task/org/noorg.jsp") })
	public String execute() {

		if (getTaskFlowData().getOrgs() == null || getTaskFlowData().getOrgs().isEmpty()) {
			return "noorg";
		}

		setViewDef(configService.getViewDefinition(getServiceContext(), ViewDef.ViewDefTypeCode.CONTACT_DATAGRID));
		orgContactTaskItemProvider.setScope(getServiceContext().getScope());

		if (orgSelection < 0 || orgSelection >= getTaskFlowData().getOrgs().size()) {
			orgSelection = Long.valueOf(0);
		}

		Org selectedOrg = Iterators.get(getTaskFlowData().getOrgs().iterator(), orgSelection.intValue(), null);

		orgContactTaskItemProvider.setOrg(selectedOrg);

		return SUCCESS;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public OrgContactTaskItemProvider getOrgContactTaskItemProvider() {
		return orgContactTaskItemProvider;
	}

	public DataGridState<?> getContactsDataGrid() {
		return contactsDataGrid;
	}

	public void setOrgSelection(Long orgSelection) {
		this.orgSelection = orgSelection;
	}

	public Long getOrgSelection() {
		return orgSelection;
	}

	public void setContacts(Map<String, Map<String, String>> contacts) {
		this.contacts = contacts;
	}

	public Map<String, Map<String, String>> getContacts() {
		return contacts;
	}
}
