package net.techreadiness.plugin.action.reports.overall;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.action.reports.ReportItemProviderImpl;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.common.DataGrid;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Qualifier("OverallAssessmentItemProvider")
public class OverallAssessmentItemProviderImpl extends ReportItemProviderImpl {

	@Inject
	private ReportsService reportsService;

	@Override
	public Collection<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		if (org == null) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		if (consortium == null) {
			throw new IllegalStateException("No Data to Display.  Please ensure the appropriate consortium is selected.");
		}

		if (org.getOrgTypeName().equals("School")) {
			QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForOrg(
					snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag);

			setTotalNumberOfItems(result.getTotalRowCount());

			if (result.getTotalRowCount() == 0) {
				return Lists.newArrayList();
			}

			Collection<Map<String, String>> results = result.getRows();
			results = determineMoreConstrainedBy(results);
			return results;
		}

		QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForChildOrgs(
				snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag, false, grid.getFirstResult(),
				grid.getPageSize());
		setTotalNumberOfItems(result.getTotalRowCount());

		Collection<Map<String, String>> results = result.getRows();
		results = determineMoreConstrainedBy(results);

		return results;
	}

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
					String estinternetSpeed = map.get("internetSpeed");
					if (estinternetSpeed.startsWith("<")) {
						String internetSpeed = estinternetSpeed.replaceAll("<", "&lt;");
						map.put("internetSpeed", internetSpeed);
					}
				}
				if (exportType.equals(ExportType.csv) && org.getOrgTypeName().equals("District")) {
					if (map.get("schoolType") != null) {
						map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
								: SCHOOL_TYPE.get(map.get("schoolType")));
					}
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
			results = determineMoreConstrainedBy(results);
		} else {
			QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForOrg(
					snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag);
			results = result.getRows();

			for (Map<String, String> map : results) {
				if (exportType.equals(ExportType.pdf)) {
					String estinternetSpeed = map.get("internetSpeed");
					if (estinternetSpeed.startsWith("<")) {
						String internetSpeed = estinternetSpeed.replaceAll("<", "&lt;");
						map.put("internetSpeed", internetSpeed);
					}
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
			results = determineMoreConstrainedBy(results);
		}
		return results;
	}

	@Override
	public Collection<Map<String, String>> exportAllSchoolsDetail(ExportType exportType) {
		if (org == null) {
			return Lists.newArrayList();
		}

		QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForDescendantOrgs(
				snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag, true, 0, 0, 2);
		Collection<Map<String, String>> results = result.getRows();
		if (exportType.equals(ExportType.csv) && org.getOrgTypeName().equals("State")) {
			for (Map<String, String> map : results) {
				if (map.get("schoolType") != null) {
					map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
							: SCHOOL_TYPE.get(map.get("schoolType")));
				}
			}
		}

		for (Map<String, String> map : results) {
			if (exportType.equals(ExportType.pdf)) {
				String estinternetSpeed = map.get("internetSpeed");
				if (estinternetSpeed.startsWith("<")) {
					String internetSpeed = estinternetSpeed.replaceAll("<", "&lt;");
					map.put("internetSpeed", internetSpeed);
				}
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
		results = determineMoreConstrainedBy(results);

		return results;
	}

	public Collection<Map<String, String>> determineMoreConstrainedBy(Collection<Map<String, String>> results) {

		for (Map<String, String> map : results) {

			String deviceToTestTakerPercentStudentsTestable = map.get("testTakerPercentStudentsTestable");
			String networkPercentTestable = map.get("networkPercentStudentsTestable");
			if (deviceToTestTakerPercentStudentsTestable.equals(">100%")) {
				deviceToTestTakerPercentStudentsTestable = "100%";
			}
			if (networkPercentTestable.equals(">100%")) {
				networkPercentTestable = "100%";
			}

			if ((deviceToTestTakerPercentStudentsTestable.equals("(missing)")
					|| deviceToTestTakerPercentStudentsTestable.equals("TBD") || deviceToTestTakerPercentStudentsTestable
						.equals("(Not Applicable)"))
					&& (networkPercentTestable.equals("(missing)") || networkPercentTestable.equals("TBD") || networkPercentTestable
							.equals("(Not Applicable)"))) {
				map.put("moreConstrainedBy", "Both Equally");
			} else {
				if ((networkPercentTestable.equals("(missing)") || networkPercentTestable.equals("TBD") || networkPercentTestable
						.equals("(Not Applicable)")) && !deviceToTestTakerPercentStudentsTestable.equals("0%")) {
					map.put("moreConstrainedBy", "Network Bandwidth");
				} else {
					if ((deviceToTestTakerPercentStudentsTestable.equals("(missing)")
							|| deviceToTestTakerPercentStudentsTestable.equals("TBD") || deviceToTestTakerPercentStudentsTestable
								.equals("(Not Applicable)")) && !networkPercentTestable.equals("0%")) {
						map.put("moreConstrainedBy", "Compliant Devices");
					} else {
						String devicePctStr = "";
						String networkPctStr = "";
						if (deviceToTestTakerPercentStudentsTestable.equals("(missing)")
								|| deviceToTestTakerPercentStudentsTestable.equals("TBD")) {
							devicePctStr = "0";
						} else {
							devicePctStr = removePercentSign(deviceToTestTakerPercentStudentsTestable);
						}
						if (networkPercentTestable.equals("(missing)") || networkPercentTestable.equals("TBD")) {
							networkPctStr = "0";
						} else {
							networkPctStr = removePercentSign(networkPercentTestable);
						}

						if (Integer.parseInt(devicePctStr) == Integer.parseInt(networkPctStr)) {
							map.put("moreConstrainedBy", "Both Equally");
						} else {
							if (Integer.parseInt(devicePctStr) < Integer.parseInt(networkPctStr)) {
								map.put("moreConstrainedBy", "Compliant Devices");
							} else {
								map.put("moreConstrainedBy", "Network Bandwidth");
							}
						}
					}
				}
			}
		}
		return results;
	}

	public String removePercentSign(String str) {

		if (str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
}
