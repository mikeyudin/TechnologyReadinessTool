package net.techreadiness.batch.user;

import java.util.List;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import com.google.common.collect.Lists;

public class UserLineTokenizer extends DelimitedLineTokenizer {
	public static final String[] COL_NAMES;

	static {
		List<String> columnNames = Lists.newArrayList();
		columnNames.add("action");
		columnNames.add("username");
		columnNames.add("stateCode");
		columnNames.add("firstName");
		columnNames.add("lastName");
		columnNames.add("authorizedOrganizations");
		columnNames.add("roles");
		columnNames.add("activeBeginDate");
		columnNames.add("activeEndDate");
		columnNames.add("disabled");
		columnNames.add("disabledReason");

		COL_NAMES = columnNames.toArray(new String[columnNames.size()]);
	}

	public UserLineTokenizer() {
		setNames(COL_NAMES);
	}
}
