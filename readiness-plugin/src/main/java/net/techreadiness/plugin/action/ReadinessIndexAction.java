package net.techreadiness.plugin.action;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.org.OrgTaskFlowData;
import net.techreadiness.navigation.taskflow.org.dataentry.DataEntryTaskFlowDefinition;
import net.techreadiness.navigation.taskflow.org.survey.SurveyQuestionsTaskFlowDefinition;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowState;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Results({ @Result(name = "success", location = "/net/techreadiness/plugin/action/readiness-index.jsp") })
public class ReadinessIndexAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Inject
	private ReportsService reportService;
	@Inject
	private SnapshotWindowService snapshotWindowService;
	@Inject
	private OrganizationService organizationService;
	@Inject
	private OrgTaskFlowData orgTaskFlowData;
	@Inject
	private DataEntryTaskFlowDefinition dataEntryTasks;
	@Inject
	private SurveyQuestionsTaskFlowDefinition surveyTasks;

	private Map<String, String> minSummary;
	private Map<String, String> recSummary;
	private SnapshotWindow snapshot;
	private Org org;

	@Override
	public String execute() throws Exception {
		snapshot = snapshotWindowService.getByScopeIdAndName(getServiceContext(), getServiceContext().getScopeId(),
				ReportsService.DEFAULT_SNAPSHOT_WINDOW);
		if (snapshot != null) {
			QueryResult<Map<String, String>> minResult = reportService.retrieveSummaryForOrg(snapshot.getSnapshotWindowId(),
					getServiceContext().getOrgId(), MinimumRecommendedFlag.MINIMUM);
			minSummary = Iterables.getFirst(minResult.getRows(), Collections.<String, String> emptyMap());
			QueryResult<Map<String, String>> recResult = reportService.retrieveSummaryForOrg(snapshot.getSnapshotWindowId(),
					getServiceContext().getOrgId(), MinimumRecommendedFlag.RECOMMENDED);
			recSummary = Iterables.getFirst(recResult.getRows(), Collections.<String, String> emptyMap());
		}

		org = organizationService.getById(getServiceContext(), getServiceContext().getOrgId());
		return SUCCESS;
	}

	@Action(value = "data-entry-task", results = { @Result(name = "success", type = "redirect", location = "/task/dataentry/altDataEntryTaskFlowBegin") })
	public String dataEntryTask() throws ServiceException {
		Set<Org> orgs = new HashSet<>();
		orgs.add(organizationService.getById(getServiceContext(), getServiceContext().getOrgId()));
		Task task = dataEntryTasks.getTask("ready.org.task.dataEntry");
		TaskFlowState state = new TaskFlowState();
		state.setTasks(Lists.newArrayList(task));
		orgTaskFlowData.setTaskFlowState(state);
		orgTaskFlowData.setOrgs(orgs);

		return SUCCESS;
	}

	@Action(value = "survey-task", results = { @Result(name = "success", type = "redirect", location = "/task/org/survey/altSurveyTaskFlowBegin") })
	public String surveyTask() throws ServiceException {
		Set<Org> orgs = new HashSet<>();
		orgs.add(organizationService.getById(getServiceContext(), getServiceContext().getOrgId()));
		Task task = surveyTasks.getTask("ready.org.task.surveyQ");
		TaskFlowState state = new TaskFlowState();
		state.setTasks(Lists.newArrayList(task));
		orgTaskFlowData.setTaskFlowState(state);
		orgTaskFlowData.setOrgs(orgs);

		return SUCCESS;
	}

	public Map<String, String> getMinSummary() {
		return minSummary;
	}

	public Map<String, String> getRecSummary() {
		return recSummary;
	}

	public SnapshotWindow getSnapshot() {
		return snapshot;
	}

	public Org getOrg() {
		return org;
	}

	public int getNumberOfUnansweredSurveys() {
		int i = 0;

		Collection<String> answers = Lists.newArrayList(org.getSurveyAdminCount(), org.getSurveyAdminTraining(),
				org.getSurveyAdminUnderstanding(), org.getSurveyTechstaffCount(), org.getSurveyTechstaffTraining(),
				org.getSurveyTechstaffUnderstanding(), org.getInternetSpeed(), org.getInternetUtilization(),
				org.getNetworkSpeed(), org.getNetworkUtilization(), org.getWirelessAccessPoints(),
				org.getTestingWindowLength(), org.getSessionsPerDay(), org.getSchoolType());
		for (String answer : answers) {
			if (StringUtils.isBlank(answer)) {
				i++;
			}
		}

		Collection<String> enrollments = Lists.newArrayList(org.getEnrollmentCount1(), org.getEnrollmentCount10(),
				org.getEnrollmentCount11(), org.getEnrollmentCount12(), org.getEnrollmentCount2(),
				org.getEnrollmentCount3(), org.getEnrollmentCount4(), org.getEnrollmentCount5(), org.getEnrollmentCount6(),
				org.getEnrollmentCount7(), org.getEnrollmentCount8(), org.getEnrollmentCount9(), org.getEnrollmentCountK());
		boolean allBlank = true;
		for (String enrollment : enrollments) {
			if (StringUtils.isNotBlank(enrollment)) {
				allBlank = false;
				break;
			}
		}
		if (allBlank) {
			i++;
		}
		return i;
	}
}
