package net.techreadiness.customer.datagrid;

public class OrgDeleteRowInfo {
	private Long orgId;
	private String name;
	private String code;
	private boolean inactive;
	private Long contacts;
	// private Long enrolls;
	// private Long groups;
	private Long participations;

	public OrgDeleteRowInfo(Long orgId, String name, String code, boolean inactive, Long contacts, Long participations) {
		super();
		this.orgId = orgId;
		this.name = name;
		this.code = code;
		this.inactive = inactive;
		this.contacts = contacts;
		// this.enrolls = enrolls;
		// this.groups = groups;
		this.participations = participations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getContacts() {
		return contacts;
	}

	public void setContacts(Long contacts) {
		this.contacts = contacts;
	}

	// public Long getEnrolls() {
	// return enrolls;
	// }
	// public void setEnrolls(Long enrolls) {
	// this.enrolls = enrolls;
	// }
	// public Long getGroups() {
	// return groups;
	// }
	// public void setGroups(Long groups) {
	// this.groups = groups;
	// }
	public Long getParticipations() {
		return participations;
	}

	public void setParticipations(Long participations) {
		this.participations = participations;
	}

	public boolean getInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}