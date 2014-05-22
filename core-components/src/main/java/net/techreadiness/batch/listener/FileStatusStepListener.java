package net.techreadiness.batch.listener;

import java.util.Locale;

import javax.inject.Inject;

import net.techreadiness.batch.AbstractServiceContextProvider;
import net.techreadiness.service.FileService;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.MessageSource;

public class FileStatusStepListener extends AbstractServiceContextProvider implements StepExecutionListener {
	@Inject
	private FileService fileService;
	@Inject
	private MessageSource messageSource;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		Integer errors = Integer.valueOf(stepExecution.getProcessSkipCount() + stepExecution.getWriteSkipCount());
		Integer writeCount = Integer.valueOf(stepExecution.getWriteCount());
		String msg = messageSource.getMessage("file.generic.write.status",
				new Object[] { writeCount.intValue() + errors.intValue(), errors }, Locale.getDefault());
		fileService.setFileStatusMessage(getServiceContext(), getFileId(), msg);
		stepExecution.getJobExecution().getExecutionContext().putInt("file.status.write.count", writeCount);
		stepExecution.getJobExecution().getExecutionContext().putInt("file.status.error.count", errors);
		if (errors.intValue() > 0) {
			return ExitStatus.FAILED;
		}
		return null;
	}

}
