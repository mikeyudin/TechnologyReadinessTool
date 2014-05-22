package net.techreadiness.service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.DateBuilder.IntervalUnit.MINUTE;
import static org.quartz.DateBuilder.IntervalUnit.SECOND;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;

import javax.inject.Inject;

import net.techreadiness.batch.jobs.SpringBatchJob;
import net.techreadiness.persistence.dao.FileDAO;
import net.techreadiness.persistence.dao.FileDAO.FileTypeCode;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.domain.FileDO;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BatchJobSchedulerServiceImpl extends BaseServiceImpl implements BatchJobSchedulerService {

	// defined in spring context
	@Inject
	SchedulerFactoryBean schedulerFactoryBean;

	@Inject
	FileService fileService;

	@Inject
	ScopeDAO scopeDAO;
	@Inject
	UserDAO userDAO;
	@Inject
	OrgDAO orgDAO;
	@Inject
	FileDAO fileDAO;

	@Override
	public void scheduleImmediateOneTimeJob(ServiceContext context, Class<? extends Job> jobClass, String jobName,
			String jobGroupName) {

		JobDataMap jobDataMap = buildInitialJobDataMap(context);

		scheduleImmediateJob(jobClass, jobName, jobGroupName, jobDataMap);
	}

	@Override
	public void scheduleImmediateOneTimeJobNoServiceContext(Class<? extends Job> jobClass, String jobName,
			String jobGroupName) {

		JobDataMap jobDataMap = new JobDataMap();

		scheduleImmediateJob(jobClass, jobName, jobGroupName, jobDataMap);
	}

	@Override
	public SchedulerFactoryBean getQuartzScheduler() {
		return schedulerFactoryBean;
	}

	@Override
	public ServiceContext buildServiceContext(Long scopeId, Long userId, Long orgId) {
		ServiceContext sc = new ServiceContext();

		if (scopeId != null) {
			sc.setScope((Scope) getMappingService().map(scopeDAO.getById(scopeId)));
		}
		if (userId != null) {
			sc.setUser((User) getMappingService().map(userDAO.getById(userId)));
		}
		if (orgId != null) {
			sc.setOrg((Org) getMappingService().map(orgDAO.getById(orgId)));
		}

		return sc;
	}

	private void scheduleImmediateJob(Class<? extends Job> jobClass, String jobName, String jobGroupName,
			JobDataMap jobDataMap) {
		JobDetail jb = newJob(jobClass).withIdentity(jobName, jobGroupName).usingJobData(jobDataMap).build();

		Trigger trigger = newTrigger().withIdentity(jobName, jobGroupName).startAt(futureDate(20, SECOND))
				.withSchedule(simpleSchedule().withRepeatCount(0)).build();

		try {
			schedulerFactoryBean.getScheduler().scheduleJob(jb, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void schedule(ServiceContext context, Long fileId) throws Exception {
		FileDO fileDO = fileDAO.getById(fileId);

		if (fileDO == null) {
			throw new ServiceException("Invalid fileId was received.");
		}

		FileTypeCode fileTypeCode = FileTypeCode.valueOf(fileDO.getFileType().getCode());

		switch (fileTypeCode) {
		case ORG_IMPORT:
		case USER_IMPORT:
		case DEVICE_IMPORT:
		case ORG_INFO_IMPORT:
			scheduleImport(context, fileDO);
			break;
		case ORG_EXPORT:
		case USER_EXPORT:
		case DEVICE_EXPORT:
		case ORG_INFO_EXPORT:
			scheduleExport(context, fileDO);
			break;
		}

	}

	private void scheduleImport(ServiceContext context, FileDO fileDO) {

		if (fileDO == null) {
			throw new ServiceException("Invalid fileId was received.");
		}

		String jobGroupName = "IMPORT";

		JobDataMap jobDataMap = buildInitialJobDataMap(context);
		jobDataMap.put(JOB_FILE_ID, fileDO.getFileId());

		// TODO: ensure how this is expected from the front end...and any validation/verification of
		// the file here? (probably not).
		jobDataMap.put(JOB_FILE_NAME, "file:" + fileDO.getPath() + File.separator + fileDO.getFilename());

		FileTypeCode fileTypeCode = FileTypeCode.valueOf(fileDO.getFileType().getCode());
		switch (fileTypeCode) {
		case ORG_IMPORT: {
			jobDataMap.put(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME, "csvOrgImport");
			// jobname must be unique in quartz, so append the fileid
			scheduleImmediateJob(SpringBatchJob.class, FileTypeCode.ORG_IMPORT.toString() + "-" + fileDO.getFileId(),
					jobGroupName, jobDataMap);
			break;
		}
		case USER_IMPORT: {
			jobDataMap.put(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME, "csvUserImport");
			scheduleImmediateJob(SpringBatchJob.class, FileTypeCode.USER_IMPORT.toString() + "-" + fileDO.getFileId(),
					jobGroupName, jobDataMap);
			break;
		}
		case DEVICE_IMPORT: {
			jobDataMap.put(JOB_MODE, fileDO.getMode());
			jobDataMap.put(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME, "csvDeviceImport");
			scheduleImmediateJob(SpringBatchJob.class, FileTypeCode.DEVICE_IMPORT.toString() + "-" + fileDO.getFileId(),
					jobGroupName, jobDataMap);
			break;
		}
		case ORG_INFO_IMPORT: {
			jobDataMap.put(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME, "csvOrgInfoImport");
			scheduleImmediateJob(SpringBatchJob.class, FileTypeCode.ORG_INFO_IMPORT.toString() + "-" + fileDO.getFileId(),
					jobGroupName, jobDataMap);
			break;
		}
		default:
			break;
		}
	}

	private void scheduleExport(ServiceContext context, FileDO fileDO) {

		FileTypeCode fileTypeCode = FileTypeCode.valueOf(fileDO.getFileType().getCode());
		String jobGroupName = "EXPORT";
		JobDataMap jobDataMap = buildInitialJobDataMap(context);
		jobDataMap.put(JOB_FILE_ID, fileDO.getFileId());
		jobDataMap.put(JOB_FILE_NAME, "file:" + fileDO.getPath() + File.separator + fileDO.getFilename());

		// put in a temp file for directly writing to:
		String tempDir = fileService.getTempExportDir(context);
		jobDataMap.put(JOB_TEMP_FILE_NAME, "file:" + tempDir + File.separator + fileDO.getFilename());

		switch (fileTypeCode) {
		case ORG_EXPORT: {
			jobDataMap.put(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME, "orgExportJob");
			// jobname must be unique in quartz, so append the fileid
			scheduleImmediateJob(SpringBatchJob.class, FileTypeCode.ORG_EXPORT.toString() + "-" + fileDO.getFileId(),
					jobGroupName, jobDataMap);
			break;
		}
		case USER_EXPORT: {
			jobDataMap.put(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME, "userExportJob");
			// jobname must be unique in quartz, so append the fileid
			scheduleImmediateJob(SpringBatchJob.class, FileTypeCode.USER_EXPORT.toString() + "-" + fileDO.getFileId(),
					jobGroupName, jobDataMap);
			break;
		}
		case DEVICE_EXPORT: {
			jobDataMap.put(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME, "deviceExportJob");
			// jobname must be unique in quartz, so append the fileid
			scheduleImmediateJob(SpringBatchJob.class, FileTypeCode.DEVICE_EXPORT.toString() + "-" + fileDO.getFileId(),
					jobGroupName, jobDataMap);

			break;
		}
		case ORG_INFO_EXPORT: {
			jobDataMap.put(BatchJobSchedulerService.SPRING_BATCH_JOB_NAME, "orgInfoExportJob");
			scheduleImmediateJob(SpringBatchJob.class, FileTypeCode.DEVICE_EXPORT.toString() + "-" + fileDO.getFileId(),
					jobGroupName, jobDataMap);
			break;
		}
		default:
			break;
		}
	}

	private static JobDataMap buildInitialJobDataMap(ServiceContext context) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(JOB_SCOPE_ID, context.getScopeId());
		jobDataMap.put(JOB_USER_ID, context.getUserId());
		jobDataMap.put(JOB_ORG_ID, context.getOrgId());

		return jobDataMap;
	}

	@Override
	public JobDetail getJobDetail(JobKey jobKey) {
		try {
			return getQuartzScheduler().getScheduler().getJobDetail(jobKey);
		} catch (SchedulerException e) {
			throw new ServiceException("The quartz scheduler threw an exception", e);
		}
	}

	@Override
	public void scheduleJob(JOB_TIME jobTime, Class<? extends Job> jobClass, String jobName, String jobGroupName) {
		JobDetail jb = newJob(jobClass).withIdentity(jobName, jobGroupName).build();

		CronScheduleBuilder csb = null;

		switch (jobTime) {
		case AT_ONE_AM: {
			csb = cronSchedule("0 0 1am * * ?");
			break;
		}

		case AT_ONE_THIRTY_AM: {
			csb = cronSchedule("0 30 1am * * ?");
			break;
		}

		case AT_TWO_AM: {
			csb = cronSchedule("0 0 2am * * ?");
			break;
		}

		case AT_THREE_AM: {
			csb = cronSchedule("0 0 3am * * ?");
			break;
		}

		case HOURLY: {
			csb = cronSchedule("0 0 0/1 * * ?");
			break;
		}

		default: { // midnight is the default case
			csb = cronSchedule("0 0 12am * * ?");
			break;
		}
		}

		Trigger trigger = newTrigger().withIdentity(jobName, jobGroupName).withSchedule(csb).build();

		try {
			schedulerFactoryBean.getScheduler().scheduleJob(jb, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void scheduleJobWithDelay(Class<? extends Job> jobClass, String jobName, String jobGroupName, int delayMinutes,
			JobDataMap jobDataMap) {

		JobDetail jb = newJob(jobClass).withIdentity(jobName, jobGroupName).usingJobData(jobDataMap).build();

		Trigger trigger = newTrigger().withIdentity(jobName, jobGroupName).startAt(futureDate(delayMinutes, MINUTE))
				.withSchedule(simpleSchedule().withRepeatCount(0)).build();

		try {
			schedulerFactoryBean.getScheduler().scheduleJob(jb, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
