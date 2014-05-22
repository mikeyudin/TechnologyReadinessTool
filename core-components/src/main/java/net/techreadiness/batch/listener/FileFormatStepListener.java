package net.techreadiness.batch.listener;

import java.util.Locale;

import javax.inject.Inject;

import net.techreadiness.batch.AbstractServiceContextProvider;
import net.techreadiness.service.FileService;
import net.techreadiness.service.ServiceContext;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.MessageSource;

public class FileFormatStepListener extends AbstractServiceContextProvider implements StepExecutionListener {

	@Inject
	private FileService fileService;
	@Inject
	private MessageSource messageSource;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Nothing to do before the step
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (stepExecution.getReadSkipCount() > 0 || stepExecution.getProcessSkipCount() > 0) {
			ServiceContext context = getServiceContext();
			String msg = messageSource.getMessage("file.formatVerificationFailure",
					new Object[] { stepExecution.getReadSkipCount() + stepExecution.getProcessSkipCount() },
					Locale.getDefault());
			fileService.setFileStatusMessage(context, getFileId(), msg);
			return ExitStatus.FAILED;
		}
		return null;
	}

}
