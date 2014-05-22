package net.techreadiness.batch.device;

import net.techreadiness.batch.AbstractServiceContextProvider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class EraserDecider extends AbstractServiceContextProvider implements JobExecutionDecider {
	private static final String REPLACE = "replace";

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		String jobMode = jobExecution.getJobInstance().getJobParameters().getString("jobMode");
		if (REPLACE.equalsIgnoreCase(jobMode)) {
			return new FlowExecutionStatus("erase");
		}
		return FlowExecutionStatus.COMPLETED;
	}

}
