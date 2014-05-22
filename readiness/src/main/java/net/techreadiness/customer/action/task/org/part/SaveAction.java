package net.techreadiness.customer.action.task.org.part;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_PART_UPDATE;

import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.OrgPartService;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.util.MapUtils;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;

@Results({
		@Result(name = Action.SUCCESS, type = "redirectAction", params = { "namespace", "/task/org/part", "actionName",
				"edit" }), @Result(name = "invalid", type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends OrgTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Element(String.class)
	private Map<String, Map<String, String>> participations;

	@Inject
	OrgPartService orgPartService;

	@Override
	public void prepare() throws Exception {
		participations = MapUtils.makeComputingMap();
	}

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_PART_UPDATE })
	public String execute() {

		for (Entry<String, Map<String, String>> entry : participations.entrySet()) {
			Long orgId = Long.valueOf(entry.getKey().toString());

			Map<String, String> values = entry.getValue();
			String participation = values.get("participation");
			Boolean assigned = Boolean.valueOf(participation);
			values.remove("participation");// not an ext attribute, just a flag

			if (orgId != null) {
				try {
					if (assigned) {
						orgPartService.createIfNotExists(getServiceContext(), orgId, values);
					} else {
						orgPartService.deleteIfExists(getServiceContext(), orgId);
					}
				} catch (ValidationServiceException e) {
					addFieldError("participations['" + orgId + "'].participation", e.getFaultInfo().getMessage());
				}
			}
			values.put("participation", participation);
		}
		if (hasErrors()) {
			return "invalid";
		}
		return SUCCESS;
	}

	public Map<String, Map<String, String>> getParticipations() {
		return participations;
	}

	public void setParticipations(Map<String, Map<String, String>> participations) {
		this.participations = participations;
	}
}
