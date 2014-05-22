package net.techreadiness.batch.listener;

import java.util.Locale;

import javax.inject.Inject;

import net.techreadiness.batch.AbstractServiceContextProvider;
import net.techreadiness.service.FileService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.File;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.MessageSource;

public class ExportStepListener extends AbstractServiceContextProvider implements StepExecutionListener {
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
		ServiceContext context = getServiceContext();
		File file = fileService.getById(context, getFileId());
		file.setTotalRecordCount(stepExecution.getWriteCount());

		String message = messageSource.getMessage("file.generic.export", null, Locale.getDefault());
		file.setStatusMessage(message);
		fileService.addOrUpdate(context, file);
		return null;
	}

}
