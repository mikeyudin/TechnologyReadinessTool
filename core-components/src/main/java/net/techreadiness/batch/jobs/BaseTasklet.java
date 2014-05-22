package net.techreadiness.batch.jobs;

import javax.inject.Inject;

import net.techreadiness.service.BatchJobSchedulerService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.base.Objects;

public abstract class BaseTasklet {

	@Inject
	JobRepository jobRepository;
	@Inject
	PlatformTransactionManager transactionManager;
	@Inject
	BatchJobSchedulerService batchJobSchedulerService;

	@Inject
	ServiceContext serviceContext;
	@Inject
	ScopeService scopeService;
	@Inject
	UserService userService;
	@Inject
	OrganizationService organizationService;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected ServiceContext getServiceContext(ChunkContext chunkContext) {

		Long scopeId = (Long) chunkContext.getStepContext().getJobParameters().get(BatchJobSchedulerService.JOB_SCOPE_ID);
		Long userId = (Long) chunkContext.getStepContext().getJobParameters().get(BatchJobSchedulerService.JOB_USER_ID);
		Long orgId = (Long) chunkContext.getStepContext().getJobParameters().get(BatchJobSchedulerService.JOB_ORG_ID);

		// do we already have the correct service context?
		if (Objects.equal(serviceContext.getScopeId(), scopeId) && Objects.equal(serviceContext.getOrgId(), orgId)
				&& Objects.equal(serviceContext.getUserId(), userId)) {

			return serviceContext;
		}

		ServiceContext sc = batchJobSchedulerService.buildServiceContext(scopeId, userId, orgId);

		serviceContext.setOrg(sc.getOrg());
		serviceContext.setScope(sc.getScope());
		serviceContext.setUser(sc.getUser());

		return serviceContext;
	}
}
