package net.techreadiness.batch.jobs;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.service.BatchJobSchedulerService;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;

import com.google.common.collect.Maps;

public class SpringBatchJob implements org.quartz.Job {
	@Inject
	protected JobLauncher jobLauncher;
	@Inject
	protected JobRegistry jobRegistry;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Map<String, JobParameter> parameters = getParameters(context);
		String jobName = getJobName(context);
		try {
			Job job = doGetJob(context, jobName);
			runJob(job, new JobParameters(parameters));
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}

	protected void runJob(Job job, JobParameters jobParameters) throws Exception {
		jobLauncher.run(job, jobParameters);
	}

	protected Job doGetJob(JobExecutionContext context, String name) throws NoSuchJobException {
		return jobRegistry.getJob(name);
	}

	protected Map<String, JobParameter> getParameters(JobExecutionContext context) {
		Map<String, JobParameter> parameters = Maps.newHashMap();

		JobDataMap jobDataMap = context.getMergedJobDataMap();

		for (Entry<String, Object> entry : jobDataMap.entrySet()) {
			// only 4 types of jobparameters at this time...
			if (entry.getValue() instanceof String) {
				parameters.put(entry.getKey(), new JobParameter((String) entry.getValue()));
			} else if (entry.getValue() instanceof Long) {
				parameters.put(entry.getKey(), new JobParameter((Long) entry.getValue()));
			} else if (entry.getValue() instanceof Double) {
				parameters.put(entry.getKey(), new JobParameter((Double) entry.getValue()));
			} else if (entry.getValue() instanceof Date) {
				parameters.put(entry.getKey(), new JobParameter((Date) entry.getValue()));
			} else {
				parameters.put(entry.getKey(), new JobParameter(entry.getValue().toString()));
			}
		}
		return parameters;
	}

	protected String getJobName(JobExecutionContext context) {
		return context.getMergedJobDataMap().getString(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME);
	}
}
