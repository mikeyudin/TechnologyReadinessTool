package net.techreadiness.plugin.action.reports;

import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class ReportSampleAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private String imageName;

	@Action(value = "deviceAssessmentSample", results = { @Result(name = SUCCESS, location = "/net/techreadiness/plugin/action/reports/sample.jsp") })
	public String deviceAssessmentSample() {
		imageName = "sample_device_state_rec.png";
		return SUCCESS;
	}

	@Action(value = "testAssessmentSample", results = { @Result(name = SUCCESS, location = "/net/techreadiness/plugin/action/reports/sample.jsp") })
	public String testAssessmentSample() {
		imageName = "sample_device-test-taker_state_rec.png";
		return SUCCESS;
	}

	@Action(value = "networkAssessmentSample", results = { @Result(name = SUCCESS, location = "/net/techreadiness/plugin/action/reports/sample.jsp") })
	public String networkAssessmentSample() {
		imageName = "sample_network_state.png";
		return SUCCESS;
	}

	@Action(value = "staffReportSample", results = { @Result(name = SUCCESS, location = "/net/techreadiness/plugin/action/reports/sample.jsp") })
	public String staffReportSample() {
		imageName = "sample_staff_personnel.png";
		return SUCCESS;
	}

	@Action(value = "completionStatusSample", results = { @Result(name = SUCCESS, location = "/net/techreadiness/plugin/action/reports/sample.jsp") })
	public String completionStatusSample() {
		imageName = "sample_completion_status.png";
		return SUCCESS;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
