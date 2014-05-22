package net.techreadiness.batch.jobs;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;

public abstract class BaseJobBean extends QuartzJobBean {

	@Inject
	JobRepository jobRepository;
	@Inject
	JobLauncher jobLauncher;
	@Inject
	PlatformTransactionManager transactionManager;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected TaskletStep createTaskletStep(String name, Tasklet tasklet) {
		TaskletStep ts = new TaskletStep(name);
		ts.setJobRepository(jobRepository);
		ts.setTransactionManager(transactionManager);
		ts.setTasklet(tasklet);

		return ts;
	}

	protected SimpleJob createSimpleJob(String name, Step initialStep) {
		SimpleJob job = new SimpleJob(name);
		job.setJobRepository(jobRepository);
		job.addStep(initialStep);

		return job;
	}

	@RequestMapping
	protected void runJob(Job job, JobExecutionContext context, Map<String, JobParameter> map) {
		try {

			if (map == null) {
				map = new HashMap<>();
			}

			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

			for (Map.Entry<String, Object> entry : jobDataMap.entrySet()) {
				// only 4 types of jobparameters at this time...
				if (entry.getValue() instanceof String) {
					map.put(entry.getKey(), new JobParameter((String) entry.getValue()));
				} else if (entry.getValue() instanceof Long) {
					map.put(entry.getKey(), new JobParameter((Long) entry.getValue()));
				} else if (entry.getValue() instanceof Double) {
					map.put(entry.getKey(), new JobParameter((Double) entry.getValue()));
				} else if (entry.getValue() instanceof Date) {
					map.put(entry.getKey(), new JobParameter((Date) entry.getValue()));
				} else {
					map.put(entry.getKey(), new JobParameter(entry.getValue().toString()));
				}
			}

			jobLauncher.run(job, new JobParameters(map));
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			log.debug("Job has already run once with these parameters.", e);
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
