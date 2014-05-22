package net.techreadiness.service;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public interface BatchJobSchedulerService extends BaseService {

	public static String JOB_SCOPE_ID = "jobScopeId";
	public static String JOB_USER_ID = "jobUserId";
	public static String JOB_ORG_ID = "jobOrgId";

	public static String JOB_FILE_ID = "jobFileId";
	public static String JOB_FILE_NAME = "jobFileName";
	public static String JOB_TEMP_FILE_NAME = "jobTempFileName";
	public static String SPRING_BATCH_JOB_NAME = "spring.batch.job.name";
	public static String JOB_MODE = "jobMode";

	public enum JOB_TIME {
		AT_MIDNIGHT, AT_ONE_AM, AT_ONE_THIRTY_AM, AT_TWO_AM, AT_THREE_AM, HOURLY
	}

	public void schedule(ServiceContext context, Long fileId) throws Exception;

	public void scheduleImmediateOneTimeJob(ServiceContext context, Class<? extends Job> jobClass, String jobName,
			String jobGroupName);

	public void scheduleImmediateOneTimeJobNoServiceContext(Class<? extends Job> jobClass, String jobName,
			String jobGroupName);

	public SchedulerFactoryBean getQuartzScheduler();

	public JobDetail getJobDetail(JobKey jobKey);

	// jobs will run outside of any servlet context, but will need the servicecontext. they have the data,
	// just need to put in a more friendly format.
	public ServiceContext buildServiceContext(Long scopeId, Long userId, Long orgId);

	public void scheduleJob(JOB_TIME jobTime, Class<? extends Job> jobClass, String jobName, String jobGroupName);

	public void scheduleJobWithDelay(Class<? extends Job> jobClass, String jobName, String jobGroupName, int delayMinutes,
			JobDataMap jobDataMap);
}
