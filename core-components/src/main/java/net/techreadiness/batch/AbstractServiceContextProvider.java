package net.techreadiness.batch;

import javax.inject.Inject;

import net.techreadiness.service.BatchJobSchedulerService;
import net.techreadiness.service.ServiceContext;

import com.google.common.base.Objects;

public abstract class AbstractServiceContextProvider {
	@Inject
	private BatchJobSchedulerService batchJobSchedulerService;
	@Inject
	protected ServiceContext serviceContext;

	private Long scopeId;
	private Long userId;
	private Long orgId;
	private Long fileId;
	private String jobMode;

	public Long getScopeId() {
		return scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getJobMode() {
		return jobMode;
	}

	public void setJobMode(String jobMode) {
		this.jobMode = jobMode;
	}

	public ServiceContext getServiceContext() {

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

	public void setBatchJobSchedulerService(BatchJobSchedulerService batchJobSchedulerService) {
		this.batchJobSchedulerService = batchJobSchedulerService;
	}
}
