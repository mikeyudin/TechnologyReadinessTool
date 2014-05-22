package net.techreadiness.batch.org;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OrgImportBadFileTest {
	@Inject
	private JobLauncher launcher;

	@Inject
	private Job csvOrgImport;

	@Test
	public void testOrgImportFileNotExist() throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("jobFileName", "net/techreadiness/batch/org/org-blah.csv");

		JobExecution execution = launcher.run(csvOrgImport, jobParametersBuilder.toJobParameters());
		Assert.assertEquals(BatchStatus.FAILED, execution.getStatus());
	}

	@Test
	public void testEmptyFile() throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("jobFileName", "net/techreadiness/batch/org/empty.csv");

		JobExecution execution = launcher.run(csvOrgImport, jobParametersBuilder.toJobParameters());
		Assert.assertEquals(BatchStatus.COMPLETED, execution.getStatus());
	}

	@Test
	public void testHeadersOnly() throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("jobFileName", "net/techreadiness/batch/org/headers-only.csv");

		JobExecution execution = launcher.run(csvOrgImport, jobParametersBuilder.toJobParameters());
		Assert.assertEquals(BatchStatus.COMPLETED, execution.getStatus());
	}
}
