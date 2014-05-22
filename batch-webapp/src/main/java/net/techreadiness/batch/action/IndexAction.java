package net.techreadiness.batch.action;

import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.util.Collection;
import java.util.TreeSet;

import javax.inject.Inject;

import net.techreadiness.ui.BaseAction;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class IndexAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@Inject
	private SchedulerFactoryBean schedulerFactoryBean;
	private Collection<JobKey> jobKeys;

	private String jobName;
	private String jobGroup;

	@Override
	@Action(results = { @Result(name = "success", location = "/index.jsp") })
	public String execute() throws Exception {
		jobKeys = new TreeSet<>();
		for (String group : schedulerFactoryBean.getScheduler().getJobGroupNames()) {
			for (JobKey jobKey : schedulerFactoryBean.getScheduler().getJobKeys(jobGroupEquals(group))) {
				jobKeys.add(jobKey);
			}
		}
		return SUCCESS;
	}

	public Collection<JobKey> getJobKeys() {
		return jobKeys;
	}

	@Action(value = "run-job", results = { @Result(name = "success", type = "redirectAction", params = { "actionName",
			"index" }) })
	public String runJob() throws Exception {
		if (StringUtils.isNotBlank(jobName) && StringUtils.isNotBlank(jobGroup)) {
			JobKey jobKey = new JobKey(jobName, jobGroup);
			schedulerFactoryBean.getScheduler().triggerJob(jobKey);
		}

		return SUCCESS;
	}

	public void setJobName(String runJobName) {
		this.jobName = runJobName;
	}

	public void setJobGroup(String runJobGroup) {
		this.jobGroup = runJobGroup;
	}
}
