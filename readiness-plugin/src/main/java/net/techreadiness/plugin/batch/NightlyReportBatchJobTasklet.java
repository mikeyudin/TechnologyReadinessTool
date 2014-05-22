package net.techreadiness.plugin.batch;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.batch.jobs.BaseTasklet;
import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class NightlyReportBatchJobTasklet extends BaseTasklet implements Tasklet {

	@Inject
	private ScopeService scopeService;
	@Inject
	private ServiceContext serviceContext;
	@Inject
	private SnapshotWindowService snapshotWindowService;
	@Inject
	private ReportsService reportsService;

	private Logger log = LoggerFactory.getLogger(NightlyReportBatchJobTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Long executeTime = (Long) chunkContext.getStepContext().getJobParameters().get("executeTime");

		log.debug("********************** Nightly Report Generation Run ************************");
		log.debug("Execute time: " + new Date(executeTime).toString());
		log.debug("*****************************************************************************");

		Scope scope = scopeService.getByScopePath(ReportsService.READINESS_SCOPE_PATH);

		serviceContext.setScope(scope);
		// Next find the child scopes for readiness
		List<Scope> childScopes = scopeService.findDescendantScopes(serviceContext, false);

		for (Scope s : childScopes) {
			SnapshotWindow snapshotWindow = snapshotWindowService.getByScopeIdAndName(serviceContext, s.getScopeId(),
					ReportsService.DEFAULT_SNAPSHOT_WINDOW);
			reportsService.createSnapshotRollup(snapshotWindow.getSnapshotWindowId(), true);
		}

		log.debug("********************** Nightly Report Generation Run --Complete-- ************************");
		log.debug("Finish time: " + new Date(System.currentTimeMillis()).toString());
		log.debug("*****************************************************************************");

		return RepeatStatus.FINISHED;
	}
}
