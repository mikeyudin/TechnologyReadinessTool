package net.techreadiness.batch.org;

import java.util.List;

import javax.inject.Named;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import com.google.common.collect.Lists;

@Named
public class OrgLineTokenizer extends DelimitedLineTokenizer {
	public static final String[] COL_NAMES;
	static {
		List<String> columnNames = Lists.newArrayList();
		columnNames.add("typeCode");
		columnNames.add("parentOrgCode");
		columnNames.add("code");
		columnNames.add("name");
		columnNames.add("inactive");
		columnNames.add("addressLine1");
		columnNames.add("addressLine2");
		columnNames.add("city");
		columnNames.add("state");
		columnNames.add("zipCode");
		columnNames.add("phoneNumber");
		columnNames.add("phoneExtension");
		columnNames.add("faxNumber");

		columnNames.add("contact1Name");
		columnNames.add("contact1Title");
		columnNames.add("contact1AddressLine1");
		columnNames.add("contact1AddressLine2");
		columnNames.add("contact1City");
		columnNames.add("contact1State");
		columnNames.add("contact1ZipCode");
		columnNames.add("contact1PhoneNumber");
		columnNames.add("contact1PhoneExtension");
		columnNames.add("contact1FaxNumber");
		columnNames.add("contact1EmailAddress");

		columnNames.add("contact2Name");
		columnNames.add("contact2Title");
		columnNames.add("contact2AddressLine1");
		columnNames.add("contact2AddressLine2");
		columnNames.add("contact2City");
		columnNames.add("contact2State");
		columnNames.add("contact2ZipCode");
		columnNames.add("contact2PhoneNumber");
		columnNames.add("contact2PhoneExtension");
		columnNames.add("contact2FaxNumber");
		columnNames.add("contact2EmailAddress");

		COL_NAMES = columnNames.toArray(new String[columnNames.size()]);
	}

	public OrgLineTokenizer() {
		setNames(COL_NAMES);
	}
}
