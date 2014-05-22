package net.techreadiness.batch.device;

import java.util.Locale;

import javax.inject.Inject;

import net.techreadiness.batch.listener.JobCompletionListener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.MessageSource;

public class DeviceJobCompletionListener extends JobCompletionListener {
	@Inject
	private MessageSource messageSource;

	@Override
	protected String getStatusMessage(JobExecution execution) {
		return getMessage(execution);
	}

	@Override
	protected String getFailedMessage(JobExecution execution) {
		return getMessage(execution);
	}

	private String getMessage(JobExecution execution) {
		int orgCount = execution.getExecutionContext().getInt("device.org.delete.count", 0);
		int writeCount = execution.getExecutionContext().getInt("file.status.write.count");
		int errorCount = execution.getExecutionContext().getInt("file.status.error.count");
		int totalRecordCount = writeCount + errorCount;
		StringBuilder sb = new StringBuilder();
		for (StepExecution step : execution.getStepExecutions()) {
			if (step.getStepName().equalsIgnoreCase("deviceItemEraser")) {
				sb.append(messageSource.getMessage("ready.device.file.final.status.delete", new Object[] { orgCount },
						Locale.getDefault()));
				sb.append("  ");
				break;
			}
		}
		sb.append(messageSource.getMessage("file.generic.write.status", new Object[] { totalRecordCount, errorCount },
				Locale.getDefault()));
		return sb.toString();
	}
}
