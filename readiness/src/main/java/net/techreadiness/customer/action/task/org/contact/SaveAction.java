package net.techreadiness.customer.action.task.org.contact;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_CONTACT_UPDATE;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.ContactService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.util.MapUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;

@Results({
		@Result(name = "success", type = "redirectAction", params = { "orgSelection", "%{orgSelection}", "namespace",
				"/task/org/contact", "actionName", "edit" }),
		@Result(name = "invalid", type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends OrgTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Element(String.class)
	private Map<String, Map<String, String>> contacts;

	@Inject
	ContactService contactService;

	private Long orgSelection;

	@Override
	public void prepare() throws Exception {
		contacts = MapUtils.makeComputingMap();
	}

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_CONTACT_UPDATE })
	public String execute() {
		for (Entry<String, Map<String, String>> entry : contacts.entrySet()) {
			Map<String, String> contact = entry.getValue();
			Long contactTypeId = Long.valueOf(entry.getKey());
			Long orgId = Long.valueOf(contact.get("orgId"));
			Long contactId = null;
			try {
				if (StringUtils.isNotBlank(contact.get("contactId"))) {
					contactId = Long.valueOf(contact.get("contactId"));
					contactService.update(getServiceContext(), contactId, contact, contactTypeId);
				} else {
					contactService.create(getServiceContext(), contact, contactTypeId, orgId);
				}
			} catch (ValidationServiceException e) {
				List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError("contacts['" + contactTypeId + "']." + validationError.getFieldName(),
							validationError.getOnlineMessage());
				}
			}
		}
		if (hasErrors()) {
			return "invalid";
		}
		return SUCCESS;
	}

	public Map<String, Map<String, String>> getContacts() {
		return contacts;
	}

	public void setContacts(Map<String, Map<String, String>> contacts) {
		this.contacts = contacts;
	}

	public void setOrgSelection(Long orgSelection) {
		this.orgSelection = orgSelection;
	}

	public Long getOrgSelection() {
		return orgSelection;
	}
}
