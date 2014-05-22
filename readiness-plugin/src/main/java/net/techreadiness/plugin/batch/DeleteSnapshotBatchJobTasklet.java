package net.techreadiness.plugin.batch;

import javax.inject.Inject;

import net.techreadiness.batch.jobs.BaseTasklet;
import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.service.ServiceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class DeleteSnapshotBatchJobTasklet extends BaseTasklet implements Tasklet {

	@Inject
	private ServiceContext serviceContext;
	@Inject
	private SnapshotWindowService snapshotWindowService;

	private Logger log = LoggerFactory.getLogger(DeleteSnapshotBatchJobTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Long snapshotWindowId = (Long) chunkContext.getStepContext().getJobParameters().get("snapshotWindowId");

		log.debug("********************** Deleting Snapshot Window ************************");
		log.debug("ID: " + snapshotWindowId.toString());
		log.debug("************************************************************************");

		snapshotWindowService.delete(serviceContext, snapshotWindowId);

		return RepeatStatus.FINISHED;
	}
}
