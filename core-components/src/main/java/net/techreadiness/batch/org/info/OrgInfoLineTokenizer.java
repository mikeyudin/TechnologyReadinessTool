package net.techreadiness.batch.org.info;

import java.util.List;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import com.google.common.collect.Lists;

public class OrgInfoLineTokenizer extends DelimitedLineTokenizer {
	public static final String[] COL_NAMES;

	static {
		List<String> columns = Lists.newArrayList();
		columns.add("code");
		columns.add("state");
		columns.add("internetSpeed");
		columns.add("networkSpeed");
		columns.add("internetUtilization");
		columns.add("networkUtilization");
		columns.add("wirelessAccessPoints");
		columns.add("simultaneousTesters");
		columns.add("studentCount");
		columns.add("testingWindowLength");
		columns.add("sessionsPerDay");
		columns.add("dataEntryComplete");
		columns.add("surveyAdminCount");
		columns.add("surveyAdminUnderstanding");
		columns.add("surveyAdminTraining");
		columns.add("surveyTechstaffCount");
		columns.add("surveyTechstaffUnderstanding");
		columns.add("surveyTechstaffTraining");
		columns.add("enrollmentCountK");
		columns.add("enrollmentCount1");
		columns.add("enrollmentCount2");
		columns.add("enrollmentCount3");
		columns.add("enrollmentCount4");
		columns.add("enrollmentCount5");
		columns.add("enrollmentCount6");
		columns.add("enrollmentCount7");
		columns.add("enrollmentCount8");
		columns.add("enrollmentCount9");
		columns.add("enrollmentCount10");
		columns.add("enrollmentCount11");
		columns.add("enrollmentCount12");
		columns.add("schoolType");

		COL_NAMES = columns.toArray(new String[columns.size()]);
	}

	public OrgInfoLineTokenizer() {
		setNames(COL_NAMES);
	}
}
