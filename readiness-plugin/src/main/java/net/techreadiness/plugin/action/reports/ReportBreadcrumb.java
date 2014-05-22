package net.techreadiness.plugin.action.reports;

public class ReportBreadcrumb {

	public enum ProgressReportLink {
		deviceProgress("Progress Report - Device Indicators"), testTakerProgress(
				"Progress Report - Device to Test-Taker Indicators"), networkProgress("Progress Report - Network Indicators"), schoolExceptionReport(
				"School Exception Report");

		public String label;

		ProgressReportLink(String label) {
			this.label = label;
		}
	}

	private String label;
	private String action;
	private String orgCode;
	private String stateCode;
	private String stateName;
	private boolean link;
	private ProgressReportLink progressReportLink;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public boolean isLink() {
		return link;
	}

	public void setLink(boolean link) {
		this.link = link;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getProgressReportLink() {
		return progressReportLink == null ? null : progressReportLink.toString();
	}

	public void setProgressReportLink(ProgressReportLink progressReportLink) {
		label = progressReportLink.label;
		this.progressReportLink = progressReportLink;
	}

}
