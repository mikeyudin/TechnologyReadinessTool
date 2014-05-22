package net.techreadiness.plugin;

import net.techreadiness.navigation.DefaultSubTab;
import net.techreadiness.navigation.DefaultTab;
import net.techreadiness.navigation.Group;
import net.techreadiness.security.CorePermissionCodes;

public class ReportsTab extends DefaultTab {
	protected ReportsTab() {
		// The text for the keys used below can be found in "customer.properties"
		super("reports", "ready.tab.reports.update.note", "ready.tab.reports.title", "", "",
				"ready.tab.reports.description", 6, true);
		Group reports = new Group(this, "ready.tab.reports.group.name", Integer.valueOf(0));
		int i = 0;
		reports.addSubTab(new DefaultSubTab("overallAssessment", "ready.tab.reports.overallAssessment.title",
				"/reports/overall", "overallAssessment", "ready.tab.reports.overallAssessment.description", Integer
						.valueOf(i++), CorePermissionCodes.READY_CUSTOMER_OVERALL_ASSESSMENT_RPT));
		reports.addSubTab(new DefaultSubTab("deviceAssessment", "ready.tab.reports.deviceAssessment.title",
				"/reports/device", "deviceAssessment", "ready.tab.reports.deviceAssessment.description", Integer
						.valueOf(i++), CorePermissionCodes.READY_CUSTOMER_DEVICE_ASSESSMENT_RPT));
		reports.addSubTab(new DefaultSubTab("testAssessment", "ready.tab.reports.testAssessment.title", "/reports/tester",
				"/testerAssessment", "ready.tab.reports.testAssessment.description", Integer.valueOf(i++),
				CorePermissionCodes.READY_CUSTOMER_DEVICE_TO_TEST_RPT));
		reports.addSubTab(new DefaultSubTab("networkAssessment", "ready.tab.reports.networkAssessment.title",
				"/reports/network", "networkAssessment", "ready.tab.reports.networkAssessment.description", Integer
						.valueOf(i++), CorePermissionCodes.READY_CUSTOMER_NETWORK_ASSESSMENT_RPT));
		reports.addSubTab(new DefaultSubTab("staffReport", "ready.tab.reports.staffAssessment.title", "/reports/staff",
				"staffReport", "ready.tab.reports.staffAssessment.description", Integer.valueOf(i++),
				CorePermissionCodes.READY_CUSTOMER_STAFF_PERSONNEL_RPT));
		reports.addSubTab(new DefaultSubTab("schoolException", "ready.tab.reports.schoolException.title", "/reports/school",
				"schoolExceptionReport", "ready.tab.reports.schoolException.description", Integer.valueOf(i++),
				CorePermissionCodes.READY_CUSTOMER_SCHOOL_EXCEPTION_RPT));
		reports.addSubTab(new DefaultSubTab("completionStatus", "ready.tab.reports.completionStatus.title",
				"/reports/completion", "completionStatus", "ready.tab.reports.completionStatus.description", Integer
						.valueOf(i++), CorePermissionCodes.READY_CUSTOMER_ASSESSMENT_COMPLETION_RPT));

		getGroups().add(reports);

		Group progressReports = new Group(this, "ready.tab.reports.progress.group.name", Integer.valueOf(1));

		progressReports.addSubTab(new DefaultSubTab("deviceProgress", "ready.tab.reports.progress.device.title",
				"/reports/device/progress", "deviceProgress", "ready.tab.reports.progress.device.description", Integer
						.valueOf(i++), CorePermissionCodes.READY_CUSTOMER_PROGESS_DEVICE_RPT));
		progressReports.addSubTab(new DefaultSubTab("testTakerProgress", "ready.tab.reports.progress.testTaker.title",
				"/reports/tester/progress", "testerProgress", "ready.tab.reports.progress.testTaker.description", Integer
						.valueOf(i++), CorePermissionCodes.READY_CUSTOMER_PROGESS_DEVICE_TEST_TAKER_RPT));
		progressReports.addSubTab(new DefaultSubTab("networkProgress", "ready.tab.reports.progress.network.title",
				"/reports/network/progress", "networkProgress", "ready.tab.reports.progress.network.description", Integer
						.valueOf(i++), CorePermissionCodes.READY_CUSTOMER_PROGESS_NETWORK_RPT));
		getGroups().add(progressReports);
	}
}
