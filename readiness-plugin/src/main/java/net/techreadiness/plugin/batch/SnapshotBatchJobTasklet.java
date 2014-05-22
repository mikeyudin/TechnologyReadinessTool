package net.techreadiness.plugin.batch;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.batch.jobs.BaseTasklet;
import net.techreadiness.plugin.persistence.dao.SnapshotWindowDao;
import net.techreadiness.plugin.persistence.domain.SnapshotWindowDO;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.ScopeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class SnapshotBatchJobTasklet extends BaseTasklet implements Tasklet {

	@Inject
	ScopeService scopeService;
	@Inject
	ReportsService reportsService;
	@Inject
	SnapshotWindowDao snapshotWindowDao;

	private Logger log = LoggerFactory.getLogger(SnapshotBatchJobTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Long executeTime = (Long) chunkContext.getStepContext().getJobParameters().get("executeTime");

		log.debug("********************** Snapshot Generation Run ************************");
		log.debug("Execute time: " + new Date(executeTime).toString());
		log.debug("*****************************************************************************");

		List<SnapshotWindowDO> snapshots = snapshotWindowDao.findUnexecutedSnapshots();

		for (SnapshotWindowDO snapshotWindowDO : snapshots) {
			reportsService.createSnapshotRollup(snapshotWindowDO.getSnapshotWindowId(), true);
		}

		log.debug("********************** Snapshot Generation Run --Complete-- ************************");
		log.debug("Finish time: " + new Date(System.currentTimeMillis()).toString());
		log.debug("*****************************************************************************");

		return RepeatStatus.FINISHED;
	}
}
