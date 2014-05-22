package net.techreadiness.plugin.action.reports.tester;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.action.reports.ReportItemProviderImpl;
import net.techreadiness.plugin.service.reports.ReportsService;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Qualifier("TesterAssessmentItemProvider")
public class TesterAssessmentItemProviderImpl extends ReportItemProviderImpl {

	@Inject
	private ReportsService reportsService;

	@Override
	public Collection<Map<String, String>> export(ExportType exportType) {

		if (org == null) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		Collection<Map<String, String>> results = Lists.newArrayList();

		if (!org.getOrgTypeName().equals("School")) {

			results = reportsService.retrieveSummaryForChildOrgs(snapshotWindow.getSnapshotWindowId(), org.getOrgId(),
					minimumRecommendedFlag, true, 0, 0).getRows();

			for (Map<String, String> map : results) {
				if (exportType.equals(ExportType.pdf)) {
					formatTBD("devicePassingCount", "devicePassingCount", map);
					String percent = map.get("testTakerPercentStudentsTestable");
					if (percent == null) {
						percent = "";
					} else {
						if (!percent.equals("(missing)") && !percent.equals("(Not Applicable)")) {
							percent = percent.toUpperCase();
						}
					}
					if (NumberUtils.toInt(map.get("deviceTbdCount")) > 0) {
						percent += " <span style=\"color: #9a9a9a\">TBD</span>";
					}
					map.put("testTakerPercentStudentsTestable", percent);
				} else {
					String percent = map.get("testTakerPercentStudentsTestable");
					if (NumberUtils.toInt(map.get("deviceTbdCount")) > 0) {
						percent += " TBD";
					}
					map.put("testTakerPercentStudentsTestable", percent);
				}

				String testingWindowLength = map.get("testingWindowLength");
				String testingWindowLengthCalc = map.get("testingWindowLengthCalc");
				if (!(testingWindowLengthCalc.equals("(missing)") && !testingWindowLength.equals("(missing)"))) {
					int windowLength = NumberUtils.toInt(testingWindowLength);
					int windowLengthCalc = NumberUtils.toInt(testingWindowLengthCalc);
					if (windowLength > windowLengthCalc) {
						testingWindowLength = testingWindowLengthCalc + "(Consortium)";
						map.put("testingWindowLengthCalc", testingWindowLength);
					}
				}

				if (exportType.equals(ExportType.csv) && org.getOrgTypeName().equals("District")) {
					if (map.get("schoolType") != null) {
						map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
								: SCHOOL_TYPE.get(map.get("schoolType")));
					}
				}
			}
		} else {

			QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForOrg(
					snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag);
			results = result.getRows();

			if (result.getTotalRowCount() == 0) {
				return Lists.newArrayList();
			}

			for (Map<String, String> map : results) {
				if (exportType.equals(ExportType.pdf)) {
					formatTBD("devicePassingCount", "devicePassingCount", map);
					String percent = map.get("testTakerPercentStudentsTestable");
					if (percent == null) {
						percent = "";
					} else {
						if (!percent.equals("(missing)") && !percent.equals("(Not Applicable)")) {
							percent = percent.toUpperCase();
						}
					}
					if (NumberUtils.toInt(map.get("deviceTbdCount")) > 0) {
						percent += " <span style=\"color: #9a9a9a\">TBD</span>";
					}
					map.put("testTakerPercentStudentsTestable", percent);

				} else {
					String percent = map.get("testTakerPercentStudentsTestable");
					if (NumberUtils.toInt(map.get("deviceTbdCount")) > 0) {
						percent += " TBD";
					}
					map.put("testTakerPercentStudentsTestable", percent);
				}

				String testingWindowLength = map.get("testingWindowLength");
				String testingWindowLengthCalc = map.get("testingWindowLengthCalc");
				if (!(testingWindowLengthCalc.equals("(missing)") && !testingWindowLength.equals("(missing)"))) {
					int windowLength = NumberUtils.toInt(testingWindowLength);
					int windowLengthCalc = NumberUtils.toInt(testingWindowLengthCalc);
					if (windowLength > windowLengthCalc) {
						testingWindowLength = testingWindowLengthCalc + "(Consortium)";
						map.put("testingWindowLengthCalc", testingWindowLength);
					}
				}
			}
		}
		return results;
	}
}
