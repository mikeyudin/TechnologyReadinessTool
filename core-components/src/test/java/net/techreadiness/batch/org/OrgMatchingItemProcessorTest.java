package net.techreadiness.batch.org;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.batch.Binder;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OrgMatchingItemProcessorTest {

	private OrganizationService organizationService;
	private OrgMatchingItemProcessor processor;
	private ServiceContext serviceContext;
	private OrgData orgData;
	@Inject
	private Binder<OrgData> binder;

	@Before
	public void setup() {
		serviceContext = Mockito.mock(ServiceContext.class);
		organizationService = Mockito.mock(OrganizationService.class);
		processor = Mockito.spy(new OrgMatchingItemProcessor());
		processor.setOrganizationService(organizationService);
		Mockito.doReturn(serviceContext).when(processor).getServiceContext();
		orgData = new OrgData();
		orgData.getOrg().setOrgTypeCode("school");
		orgData.getOrg().setParentOrgCode("parentCode");
		orgData.getOrg().setParentOrgLocalCode("parentLocalCode");
		processor.setBinder(binder);
		MessageSource messageSource = Mockito.mock(MessageSource.class);
		processor.setMessageSource(messageSource);
	}

	@Test
	public void testNoMatch() throws Exception {
		FieldSet fieldSet = new DefaultFieldSet(OrgLineTokenizer.COL_NAMES, OrgLineTokenizer.COL_NAMES);
		orgData.setFieldSet(fieldSet);
		Mockito.when(
				organizationService.getMatch(org.mockito.Matchers.any(ServiceContext.class),
						org.mockito.Matchers.any(Org.class))).thenReturn(null);
		Map<String, String> asMap = orgData.getOrg().getAsMap();
		OrgData matchedData = processor.process(orgData);
		Assert.assertTrue(matchedData.getOrg().getAsMap().equals(asMap));
	}

	@Test(expected = ValidationServiceException.class)
	public void testParentCodesNotMatch() throws Exception {
		FieldSet fieldSet = Mockito.mock(FieldSet.class);
		Mockito.when(fieldSet.readString(org.mockito.Matchers.argThat(Matchers.is("name")))).thenReturn(null);
		orgData.setFieldSet(fieldSet);

		Org match = new Org();
		String name = "this was not in the fieldset";
		match.setName(name);
		match.setParentOrgLocalCode("localCode");
		Mockito.when(
				organizationService.getMatch(org.mockito.Matchers.any(ServiceContext.class),
						org.mockito.Matchers.any(Org.class))).thenReturn(match);

		OrgData matchedData = processor.process(orgData);
		Assert.assertNull(matchedData.getOrg().getName());
	}

	@Test
	public void testMatch() throws Exception {
		FieldSet fieldSet = Mockito.mock(FieldSet.class);
		Mockito.when(fieldSet.readString(org.mockito.Matchers.argThat(Matchers.is("name")))).thenReturn(null);
		orgData.setFieldSet(fieldSet);

		Org match = new Org();
		String name = "this was not in the fieldset";
		match.setName(name);
		match.setParentOrgLocalCode("parentLocalCode");
		Mockito.when(
				organizationService.getMatch(org.mockito.Matchers.any(ServiceContext.class),
						org.mockito.Matchers.any(Org.class))).thenReturn(match);

		OrgData matchedData = processor.process(orgData);
		Assert.assertNull(matchedData.getOrg().getName());
	}
}
