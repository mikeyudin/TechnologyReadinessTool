package net.techreadiness.batch.listener;

import javax.inject.Inject;

import net.techreadiness.batch.AbstractServiceContextProvider;
import net.techreadiness.service.FileService;
import net.techreadiness.service.object.File;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;

public class ItemCountStepListener extends AbstractServiceContextProvider {
	@Inject
	private FileService fileService;

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		File file = fileService.getById(getServiceContext(), getFileId());
		file.setTotalRecordCount(stepExecution.getReadCount() + stepExecution.getReadSkipCount());
		fileService.addOrUpdate(getServiceContext(), file);
		return null;
	}
}
