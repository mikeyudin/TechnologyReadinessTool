package net.techreadiness.plugin;

import javax.inject.Inject;
import javax.inject.Named;

import net.techreadiness.navigation.DefaultSubTab;
import net.techreadiness.navigation.Tab;
import net.techreadiness.navigation.taskflow.org.survey.SurveyQuestionsTaskFlowDefinition;
import net.techreadiness.plugin.batch.HourlyReportBatchJob;
import net.techreadiness.plugin.batch.NightlyReportBatchJob;
import net.techreadiness.plugin.batch.SnapshotBatchJob;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.BatchJobSchedulerService;
import net.techreadiness.service.BatchJobSchedulerService.JOB_TIME;
import net.techreadiness.ui.task.Task;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.beans.factory.InitializingBean;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.PackageProvider;

@Named
public class ReadinessPluginInitialize implements PackageProvider, InitializingBean {
	@Inject
	private SurveyQuestionsTaskFlowDefinition surveyOrgTaskFlow;

	@Inject
	private BatchJobSchedulerService batchJobSchedulerService;

	private static String HOURLY_REPORT_JOB_NAME = "HourlyReportBatchJob";
	private static String HOURLY_REPORT_JOB_GROUP = "HourlyReportBatchJobGroup";

	private static String NIGHTLY_REPORT_JOB_NAME = "NightlyReportBatchJob";
	private static String NIGHTLY_REPORT_JOB_GROUP = "NightlyReportBatchJobGroup";

	private static String SNAPSHOT_JOB_NAME = "SnapshotBatchJob";
	private static String SNAPSHOT_JOB_GROUP = "SnapshotBatchJobGroup";

	@Override
	public void init(Configuration configuration) throws ConfigurationException {
		Tab setupTab = configuration.getContainer().getInstance(Tab.class, "setupTab");
		if (setupTab != null) {
			// The text for the keys used below can be found in "customer.properties"
			setupTab.addChild(
					new DefaultSubTab("deviceTab", "ready.tab.device.title", "/device", "list",
							"ready.tab.device.description", Integer.valueOf(50),
							CorePermissionCodes.READY_CUSTOMER_READINESS_ACCESS), "menu.group.title.data");
			setupTab.addChild(new DefaultSubTab("limitsTab", "ready.tab.minimumRequirements.title", "/task/scope/limits",
					"limits", "ready.tab.minimumRequirements.description", Integer.valueOf(51),
					CorePermissionCodes.READY_CUSTOMER_SCOPE_MIN_SPEC), "menu.group.title.admin");
			setupTab.addChild(new DefaultSubTab("snapshotsTab", "ready.tab.snapshots.title", "/snapshot", "list",
					"ready.tab.snapshots.description", Integer.valueOf(52), CorePermissionCodes.READY_CUSTOMER_SNAPSHOT),
					"menu.group.title.admin");
			setupTab.addChild(new DefaultSubTab("surveyQuestionsTab", "ready.tab.surveyQuestions.title", "/org/survey",
					"list", "ready.tab.surveyQuestions.description", Integer.valueOf(53),
					CorePermissionCodes.READY_CUSTOMER_READINESS_ACCESS), "menu.group.title.data");
			setupTab.addChild(new DefaultSubTab("dataEntryCompleteTab", "ready.tab.dataEntryComplete.title",
					"/org/dataentry", "list", "ready.tab.dataEntryComplete.description", Integer.valueOf(54),
					CorePermissionCodes.READY_CUSTOMER_READINESS_ACCESS), "menu.group.title.data");
		}
	}

	@Override
	public boolean needsReload() {
		return false;
	}

	@Override
	public void loadPackages() throws ConfigurationException {
		// No packages are loaded
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// The text for the keys used below can be found in "customer.properties"
		surveyOrgTaskFlow.addTask(new Task("/task/survey/network", "network", "ready.org.task.surveyQ",
				CorePermissionCodes.READY_CUSTOMER_NETWORK_INFRASTRUCTURE));

		// see if our hourly report job already exists. if not, schedule it
		JobDetail jobDetail = batchJobSchedulerService.getJobDetail(new JobKey(HOURLY_REPORT_JOB_NAME,
				HOURLY_REPORT_JOB_GROUP));
		if (jobDetail == null) {
			batchJobSchedulerService.scheduleJob(JOB_TIME.HOURLY, HourlyReportBatchJob.class, HOURLY_REPORT_JOB_NAME,
					HOURLY_REPORT_JOB_GROUP);
		}

		jobDetail = batchJobSchedulerService.getJobDetail(new JobKey(NIGHTLY_REPORT_JOB_NAME, NIGHTLY_REPORT_JOB_GROUP));
		if (jobDetail == null) {
			// schedule the nightly job on the half hour so that it doesn't run at same time as hourly
			batchJobSchedulerService.scheduleJob(JOB_TIME.AT_ONE_THIRTY_AM, NightlyReportBatchJob.class,
					NIGHTLY_REPORT_JOB_NAME, NIGHTLY_REPORT_JOB_GROUP);
		}

		jobDetail = batchJobSchedulerService.getJobDetail(new JobKey(SNAPSHOT_JOB_NAME, SNAPSHOT_JOB_GROUP));

		if (jobDetail == null) {
			batchJobSchedulerService.scheduleJob(JOB_TIME.AT_TWO_AM, SnapshotBatchJob.class, SNAPSHOT_JOB_NAME,
					SNAPSHOT_JOB_GROUP);
		}
	}
}
