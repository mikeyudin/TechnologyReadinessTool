package net.techreadiness.batch.org;

import net.techreadiness.batch.CoreFieldExtractor;
import net.techreadiness.service.object.Contact;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Maps;

public class OrgExportFieldExtractorTest {
	private OrgData org;
	private CoreFieldExtractor<OrgData> fieldExtractor;

	@Before
	public void setup() {
		org = new OrgData();
		org.getOrg().setExtendedAttributes(Maps.<String, String> newHashMap());
		fieldExtractor = new CoreFieldExtractor<>();
		fieldExtractor.setResource(new ClassPathResource("net/techreadiness/batch/org/org-export-mapping.txt"));
	}

	@Test
	public void testEmptyOrg() {
		Object[] fields = fieldExtractor.extract(org);

		Assert.assertEquals(35, fields.length);
	}

	@Test
	public void testOrg() {
		org.getOrg().setOrgTypeCode("school");
		org.getOrg().setParentOrgLocalCode("parent");
		org.getOrg().setLocalCode("code");
		org.getOrg().setName("name");
		org.getOrg().setInactive(false);
		org.getOrg().setAddressLine1("addressLine1");
		org.getOrg().setAddressLine2("addressLine2");
		org.getOrg().setCity("city");
		org.getOrg().setState("state");
		org.getOrg().setZip("zip");
		org.getOrg().setPhone("phone");
		org.getOrg().setPhoneExtension("phoneExtension");
		org.getOrg().setFax("fax");

		Object[] fields = fieldExtractor.extract(org);
		Assert.assertEquals("school", fields[0]);
		Assert.assertEquals("parent", fields[1]);
		Assert.assertEquals("code", fields[2]);
		Assert.assertEquals("name", fields[3]);
		Assert.assertEquals("false", fields[4]);
		Assert.assertEquals("addressLine1", fields[5]);
		Assert.assertEquals("addressLine2", fields[6]);
		Assert.assertEquals("city", fields[7]);
		Assert.assertEquals("state", fields[8]);
		Assert.assertEquals("zip", fields[9]);
		Assert.assertEquals("phone", fields[10]);
		Assert.assertEquals("phoneExtension", fields[11]);
		Assert.assertEquals("fax", fields[12]);
		Assert.assertEquals(35, fields.length);
	}

	@Test
	public void testContact1() {
		Contact contact = new Contact();
		contact.setContactTypeCode("primary");
		contact.setName("name");
		contact.setTitle("title");
		contact.setAddressLine1("addressLine1");
		contact.setAddressLine2("addressLine2");
		contact.setCity("city");
		contact.setState("state");
		contact.setZip("zip");
		contact.setPhone("phone");
		contact.setPhoneExtension("phoneExtension");
		contact.setFax("fax");
		contact.setEmail("email");
		org.getContacts().put("primary", contact);

		Object[] fields = fieldExtractor.extract(org);
		Assert.assertEquals("name", fields[13]);
		Assert.assertEquals("title", fields[14]);
		Assert.assertEquals("addressLine1", fields[15]);
		Assert.assertEquals("addressLine2", fields[16]);
		Assert.assertEquals("city", fields[17]);
		Assert.assertEquals("state", fields[18]);
		Assert.assertEquals("zip", fields[19]);
		Assert.assertEquals("phone", fields[20]);
		Assert.assertEquals("phoneExtension", fields[21]);
		Assert.assertEquals("fax", fields[22]);
		Assert.assertEquals("email", fields[23]);
		Assert.assertEquals(35, fields.length);
	}

	@Test
	public void testContact2() {
		Contact contact = new Contact();
		contact.setContactTypeCode("secondary");
		contact.setName("name");
		contact.setTitle("title");
		contact.setAddressLine1("addressLine1");
		contact.setAddressLine2("addressLine2");
		contact.setCity("city");
		contact.setState("state");
		contact.setZip("zip");
		contact.setPhone("phone");
		contact.setPhoneExtension("phoneExtension");
		contact.setFax("fax");
		contact.setEmail("email");
		org.getContacts().put("secondary", contact);

		Object[] fields = fieldExtractor.extract(org);
		Assert.assertEquals("name", fields[24]);
		Assert.assertEquals("title", fields[25]);
		Assert.assertEquals("addressLine1", fields[26]);
		Assert.assertEquals("addressLine2", fields[27]);
		Assert.assertEquals("city", fields[28]);
		Assert.assertEquals("state", fields[29]);
		Assert.assertEquals("zip", fields[30]);
		Assert.assertEquals("phone", fields[31]);
		Assert.assertEquals("phoneExtension", fields[32]);
		Assert.assertEquals("fax", fields[33]);
		Assert.assertEquals("email", fields[34]);
		Assert.assertEquals(35, fields.length);
	}
}
