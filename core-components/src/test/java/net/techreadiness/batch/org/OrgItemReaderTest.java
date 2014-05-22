package net.techreadiness.batch.org;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OrgItemReaderTest {
	@Inject
	@Named("orgReader")
	private ItemStreamReader<OrgData> orgReader;

	@After
	public void after() {
		orgReader.close();
	}

	@Test
	public void testReader() throws Exception {
		orgReader.open(new ExecutionContext());
		OrgData orgData = orgReader.read();
		Assert.assertEquals("school", orgData.getOrg().getOrgTypeCode());
		Assert.assertEquals("Icy-400", orgData.getOrg().getParentOrgCode());
		Assert.assertEquals("Icy-r2d2", orgData.getOrg().getCode());
		Assert.assertEquals("Robot Academy", orgData.getOrg().getName());
		Assert.assertEquals(Boolean.FALSE, orgData.getOrg().getInactive());
		Assert.assertEquals("1 X Wing Ave", orgData.getOrg().getAddressLine1());
		Assert.assertEquals("line2", orgData.getOrg().getAddressLine2());
		Assert.assertEquals("Base", orgData.getOrg().getCity());
		Assert.assertEquals("Icy", orgData.getOrg().getState());
		Assert.assertEquals("12345", orgData.getOrg().getZip());
		Assert.assertEquals("319-555-5555", orgData.getOrg().getPhone());
		Assert.assertEquals("123", orgData.getOrg().getPhoneExtension());
		Assert.assertEquals("319-555-5556", orgData.getOrg().getFax());

		Assert.assertEquals("C3P0", orgData.getContacts().get("primary").getName());
		Assert.assertEquals("Protocol Droid", orgData.getContacts().get("primary").getTitle());
		Assert.assertEquals("cLine1", orgData.getContacts().get("primary").getAddressLine1());
		Assert.assertEquals("cLine2", orgData.getContacts().get("primary").getAddressLine2());
		Assert.assertEquals("cCity", orgData.getContacts().get("primary").getCity());
		Assert.assertEquals("cState", orgData.getContacts().get("primary").getState());
		Assert.assertEquals("cZip", orgData.getContacts().get("primary").getZip());
		Assert.assertEquals("319-555-5557", orgData.getContacts().get("primary").getPhone());
		Assert.assertEquals("234", orgData.getContacts().get("primary").getPhoneExtension());
		Assert.assertEquals("319-555-5558", orgData.getContacts().get("primary").getFax());
		Assert.assertEquals("c3p0@galaxy.net", orgData.getContacts().get("primary").getEmail());

		Assert.assertEquals("c2Name", orgData.getContacts().get("secondary").getName());
		Assert.assertEquals("c2Title", orgData.getContacts().get("secondary").getTitle());
		Assert.assertEquals("c2Line1", orgData.getContacts().get("secondary").getAddressLine1());
		Assert.assertEquals("c2Line2", orgData.getContacts().get("secondary").getAddressLine2());
		Assert.assertEquals("c2City", orgData.getContacts().get("secondary").getCity());
		Assert.assertEquals("c2State", orgData.getContacts().get("secondary").getState());
		Assert.assertEquals("c2Zip", orgData.getContacts().get("secondary").getZip());
		Assert.assertEquals("319-555-5559", orgData.getContacts().get("secondary").getPhone());
		Assert.assertEquals("345", orgData.getContacts().get("secondary").getPhoneExtension());
		Assert.assertEquals("319-555-5560", orgData.getContacts().get("secondary").getFax());
		Assert.assertEquals("c2Email@mail.com", orgData.getContacts().get("secondary").getEmail());
		Assert.assertEquals(2, orgData.getLineNumber());
		Assert.assertNotNull(orgData.getRawData());
	}

	@Test
	public void testNullParentCode() throws Exception {
		ExecutionContext context = new ExecutionContext();
		context.putInt("FlatFileItemReader.read.count", 1);
		orgReader.open(context);
		OrgData orgData = orgReader.read();
		Assert.assertNull(orgData.getOrg().getParentOrgCode());
		Assert.assertEquals(3, orgData.getLineNumber());
		Assert.assertNotNull(orgData.getRawData());
	}

	@Test
	public void testInvalidInactive() throws Exception {
		ExecutionContext context = new ExecutionContext();
		context.putInt("FlatFileItemReader.read.count", 2);
		orgReader.open(context);
		OrgData orgData = orgReader.read();
		Assert.assertTrue(orgData.getOrg().getInactive());
		Assert.assertEquals(4, orgData.getLineNumber());
		Assert.assertNotNull(orgData.getRawData());
	}
}
