package net.techreadiness.batch.listener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.batch.AbstractServiceContextProvider;
import net.techreadiness.persistence.dao.FileDAO.FileTypeCode;
import net.techreadiness.service.BatchJobSchedulerService;
import net.techreadiness.service.FileService;
import net.techreadiness.service.FileService.FileStatus;
import net.techreadiness.service.object.File;
import net.techreadiness.util.EmailService;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.context.MessageSource;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

public class JobCompletionListener extends AbstractServiceContextProvider implements JobExecutionListener {
	protected final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
	@Inject
	private FileService fileService;
	@Inject
	private EmailService emailService;
	@Inject
	private MessageSource messageSource;

	private static String PROCESSING_COMPLETE_EMAIL_TITLE = "core.email.batch.file.complete.title";
	private static String PROCESSING_COMPLETE_EMAIL_TEXT = "core.email.batch.file.complete.text";

	@Override
	public void beforeJob(JobExecution jobExecution) {
		setupParams(jobExecution);
		fileService.setFileStatus(getServiceContext(), getFileId(), FileStatus.PROCESSING);
		setupLogging();
	}

	private void setupLogging() {
		// add in our custom per-file logging appender. anything that happens for this particular
		// file should go in this log file
		FileAppender appender = new FileAppender();

		File file = fileService.getById(getServiceContext(), getFileId());

		appender.setName("batch-" + getFileId());
		appender.setLayout(new PatternLayout("%d %-5p %c %x - %m%n"));
		appender.setFile(file.getPath() + java.io.File.separator + file.getFilename() + ".log");
		appender.setAppend(true);
		appender.activateOptions();

		Logger logger = Logger.getRootLogger();
		logger.addAppender(appender);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		int recordsInError = 0;
		int writeCount = 0;
		boolean isExport = false;

		File file = fileService.getById(getServiceContext(), getFileId());

		// get/set the file size
		int length = 0;
		InputStream stream = null;
		java.io.File newFile = null;

		java.io.File to = new java.io.File(file.getPath(), file.getFilename());
		try {
			try {
				FileTypeCode fileTypeCode = FileTypeCode.valueOf(file.getFileTypeCode());

				switch (fileTypeCode) {
				case ORG_EXPORT:
				case USER_EXPORT:
				case DEVICE_EXPORT:
				case ORG_INFO_EXPORT:
					isExport = true;
					String tmpFile = jobExecution.getJobInstance().getJobParameters()
							.getString(BatchJobSchedulerService.JOB_TEMP_FILE_NAME).replaceFirst("file:", "");
					newFile = new java.io.File(tmpFile);
					break;
				default:
					newFile = to;
					break;
				}
				URL url = newFile.toURI().toURL();
				stream = url.openStream();
				length = stream.available();

				// convert to lowest kilobyte, if greater than zero, we are equal to 1.
				if (length > 0 && length < 1024) {
					length = 1024;
				}
				length = length >> 10;
			} finally {
				IOUtils.closeQuietly(stream);
			}
		} catch (Exception e) {
			// we didn't get the file length for some reason...
		}
		file.setKilobytes(length);
		file = fileService.addOrUpdate(getServiceContext(), file);

		if (isExport && !newFile.equals(to)) {
			try {
				Files.move(newFile, to);
			} catch (IOException e) {
				log.error("Unable to move new export file to final directory.", e);
			}
		}

		for (StepExecution se : jobExecution.getStepExecutions()) {
			recordsInError += se.getRollbackCount() + se.getReadSkipCount();
			writeCount += se.getWriteCount();
		}

		switch (jobExecution.getStatus()) {
		case FAILED:
			fileService.setFileStatus(getServiceContext(), getFileId(), FileStatus.ERRORS);
			String failedMessage = getFailedMessage(jobExecution);
			if (failedMessage == null) {

				List<Throwable> exceptions = jobExecution.getAllFailureExceptions();
				for (Throwable exception : exceptions) {
					if (exception instanceof SkipLimitExceededException) {
						SkipLimitExceededException e = (SkipLimitExceededException) exception;
						StepExecution last = Iterables.getLast(jobExecution.getStepExecutions());
						failedMessage = messageSource.getMessage("file.skip.limit.exceeded",
								new Object[] { last.getWriteCount(), e.getSkipLimit() }, Locale.getDefault());
						break;
					}
				}
			}
			if (failedMessage != null) {
				fileService.setFileStatusMessage(getServiceContext(), getFileId(), failedMessage);
			}
			break;
		case STOPPED:
			fileService.setFileStatus(getServiceContext(), getFileId(), FileStatus.STOPPED);
			String stoppedMessage = getStoppedMessage(jobExecution);
			if (stoppedMessage == null) {
				stoppedMessage = messageSource.getMessage("file.job.stopped", new Object[] { writeCount },
						Locale.getDefault());
			}
			fileService.setFileStatusMessage(getServiceContext(), getFileId(), stoppedMessage);
			break;

		default:
			fileService.setFileStatus(getServiceContext(), getFileId(), FileStatus.COMPLETE);
			String statusMessage = getStatusMessage(jobExecution);
			if (statusMessage != null) {
				fileService.setFileStatusMessage(getServiceContext(), getFileId(), statusMessage);
			}
			break;
		}

		Map<String, String> values = buildEmailValues(recordsInError);
		try {
			emailService.sendSubstitutedTextEmail(getServiceContext().getUser().getEmail(), PROCESSING_COMPLETE_EMAIL_TITLE,
					PROCESSING_COMPLETE_EMAIL_TEXT, values);
		} catch (Exception e) {
			log.error("Failed to send email.", e);
		}

		Logger logger = Logger.getRootLogger();
		logger.removeAppender("batch-" + getFileId());
	}

	protected String getStoppedMessage(JobExecution jobExecution) {
		return null;
	}

	protected String getStatusMessage(JobExecution execution) {
		return null;
	}

	protected String getFailedMessage(JobExecution execution) {
		return null;
	}

	private Map<String, String> buildEmailValues(int recordsInError) {
		Map<String, String> map = Maps.newHashMap();

		File file = fileService.getById(getServiceContext(), getFileId());

		map.put("firstname", getServiceContext().getUser().getFirstName());
		map.put("fileImportExport", file.getFileTypeName());
		map.put("fileStatus", file.getStatus());
		map.put("statusMessage", file.getStatusMessage());
		map.put("fileType", file.getFileTypeName());
		map.put("fileName", file.getDisplayFilename());
		map.put("requestDate",
				file.getRequestDate() == null ? "" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z").format(file
						.getRequestDate()));
		map.put("completionDate", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z").format(new Date()));

		return map;
	}

	private void setupParams(JobExecution jobExecution) {
		setScopeId(jobExecution.getJobInstance().getJobParameters().getLong(BatchJobSchedulerService.JOB_SCOPE_ID));
		setOrgId(jobExecution.getJobInstance().getJobParameters().getLong(BatchJobSchedulerService.JOB_ORG_ID));
		setUserId(jobExecution.getJobInstance().getJobParameters().getLong(BatchJobSchedulerService.JOB_USER_ID));
		setFileId(jobExecution.getJobInstance().getJobParameters().getLong(BatchJobSchedulerService.JOB_FILE_ID));

		// the typesafe setting above is not really what we want...
		if (getScopeId() == 0) {
			setScopeId(null);
		}
		if (getOrgId() == 0) {
			setOrgId(null);
		}
		if (getUserId() == 0) {
			setUserId(null);
		}
		if (getFileId() == 0) {
			setFileId(null);
		}
	}
}
