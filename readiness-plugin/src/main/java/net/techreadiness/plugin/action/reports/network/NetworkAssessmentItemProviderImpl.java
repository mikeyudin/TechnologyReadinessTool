package net.techreadiness.plugin.action.reports.network;

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
@Qualifier("NetworkAssessmentItemProvider")
public class NetworkAssessmentItemProviderImpl extends ReportItemProviderImpl {

	@Inject
	private ReportsService reportsService;

	@Override
	public Collection<Map<String, String>> export(ExportType exportType) {

		if (org == null) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		Collection<Map<String, String>> results;

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
					if (estinternetSpeed.contains("e.g.")) {
						String internetSpeed = map.get("internetSpeed");
						internetSpeed = internetSpeed.replaceAll("e.g.,", "e.g., <br>");
						map.put("internetSpeed", internetSpeed);
					}
					String estInternalNetworkBandwidth = map.get("networkSpeed");
					if (estInternalNetworkBandwidth.contains("e.g.")) {
						String internalNetworkBandwidth = estInternalNetworkBandwidth.replaceAll("e.g.,", "e.g., <br>");
						map.put("networkSpeed", internalNetworkBandwidth);
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
					if (estinternetSpeed.contains("e.g.")) {
						String internetSpeed = map.get("internetSpeed");
						internetSpeed = internetSpeed.replaceAll("e.g.,", "e.g., <br>");
						map.put("internetSpeed", internetSpeed);
					}
					String estInternalNetworkBandwidth = map.get("networkSpeed");
					if (estInternalNetworkBandwidth.contains("e.g.")) {
						String internalNetworkBandwidth = estInternalNetworkBandwidth.replaceAll("e.g.,", "e.g., <br>");
						map.put("networkSpeed", internalNetworkBandwidth);
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
		}

		return results;
	}
}
