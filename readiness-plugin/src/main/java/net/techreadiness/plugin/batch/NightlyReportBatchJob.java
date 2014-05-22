package net.techreadiness.plugin.batch;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.batch.jobs.BaseJobBean;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.stereotype.Component;

/*
 * This is the job wrapper for the nightly report runs.  The injected tasklet is
 * where the actual work is accomplished (or at least initiated).
 * 
 */

@Component
public class NightlyReportBatchJob extends BaseJobBean {

	@Inject
	NightlyReportBatchJobTasklet nightlyReportBatchJobTasklet;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		TaskletStep ts = createTaskletStep("nightlyRun", nightlyReportBatchJobTasklet);

		SimpleJob job = createSimpleJob("nightlyReportBatchJob", ts);

		// by default, jobs are assumed to have been already run if the exact same
		// parameters are seen. Pass in the time to always be unique.
		Map<String, JobParameter> map = new HashMap<>();
		map.put("executeTime", new JobParameter(System.currentTimeMillis()));

		runJob(job, context, map);
	}
}
