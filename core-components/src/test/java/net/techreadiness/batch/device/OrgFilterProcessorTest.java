package net.techreadiness.batch.device;

import net.techreadiness.service.BatchJobSchedulerService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserService;
import net.techreadiness.service.exception.ValidationServiceException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

public class OrgFilterProcessorTest {

	private OrgFilterProcessor processor;

	@Before
	public void setup() {
		processor = Mockito.spy(new OrgFilterProcessor());
		processor.messageSource = Mockito.mock(MessageSource.class);
		processor.userService = Mockito.mock(UserService.class);
		Mockito.doReturn(new ServiceContext()).when(processor).getServiceContext();
		BatchJobSchedulerService jobService = Mockito.mock(BatchJobSchedulerService.class);
		Mockito.when(jobService.buildServiceContext(Matchers.<Long> any(), Matchers.<Long> any(), Matchers.<Long> any()))
				.thenReturn(new ServiceContext());
		processor.setBatchJobSchedulerService(jobService);
	}

	@Test
	public void testAuthorizedOrg() throws Exception {
		DeviceData item = new DeviceData();
		item.getDevice().getOrg().setLocalCode("local");
		item.setStateCode("state");

		Mockito.when(
				processor.userService.hasAccessToOrgByCode(Matchers.<ServiceContext> any(), Matchers.<Long> any(),
						Matchers.eq("state-local"))).thenReturn(Boolean.TRUE);

		Assert.assertNotNull(processor.process(item));
	}

	@Test(expected = ValidationServiceException.class)
	public void testNotAuthorizedOrg() throws Exception {
		DeviceData item = new DeviceData();
		item.getDevice().getOrg().setLocalCode("local");
		item.setStateCode("state");

		Mockito.when(
				processor.userService.hasAccessToOrgByCode(Matchers.<ServiceContext> any(), Matchers.<Long> any(),
						Matchers.anyString())).thenReturn(Boolean.FALSE);

		processor.process(item);
	}

	@Test
	public void testAuthorizedOrgTwice() throws Exception {
		DeviceData item = new DeviceData();
		item.getDevice().getOrg().setLocalCode("local");
		item.setStateCode("state");

		Mockito.when(
				processor.userService.hasAccessToOrgByCode(Matchers.<ServiceContext> any(), Matchers.<Long> any(),
						Matchers.eq("state-local"))).thenReturn(Boolean.TRUE);

		Assert.assertNotNull(processor.process(item));
		Assert.assertNotNull(processor.process(item));
	}
}
