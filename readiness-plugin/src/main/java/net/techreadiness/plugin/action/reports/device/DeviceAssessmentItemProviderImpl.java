package net.techreadiness.plugin.action.reports.device;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.action.reports.ReportItemProviderImpl;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.common.DataGrid;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Qualifier("DeviceAssessmentItemProvider")
public class DeviceAssessmentItemProviderImpl extends ReportItemProviderImpl {

	@Autowired
	CriteriaQuery<Map<String, String>> criteriaQuery;

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
			QueryResult<Map<String, String>> results = retrieveSchoolDevices(snapshotWindow.getSnapshotWindowId(),
					grid.getFirstResult(), grid.getPageSize());
			setTotalNumberOfItems(results.getTotalRowCount());
			return results.getRows();
		}
		QueryResult<Map<String, String>> results = reportsService.retrieveSummaryForChildOrgs(
				snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag, false, grid.getFirstResult(),
				grid.getPageSize());
		setTotalNumberOfItems(results.getTotalRowCount());
		return results.getRows();
	}

	private QueryResult<Map<String, String>> retrieveSchoolDevices(Long snapshotWindowId, Integer firstResult,
			Integer pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("  o.name orgName, ");
		sb.append("  o.code orgCode, ");
		sb.append("  o.local_code localOrgCode,");
		sb.append("  concat(po.name,' (',po.local_code,')') parentOrgName,  ");
		sb.append("  po.code parentOrgCode,  ");
		sb.append("  po.local_code parentLocalOrgCode,  ");
		sb.append("  sd.name,");
		sb.append("  CAST(sd.count as CHAR(100)) count,");
		sb.append("  ifnull(sc_operating_system.name,'(missing)') operatingSystem,");
		sb.append("  ifnull(sc_memory.name,'(missing)') memory,");
		sb.append("  ifnull(sc_display_size.name,'(missing)') monitorDisplaySize,");
		sb.append("  ifnull(sc_screen_resolution.name,'(missing)') screenResolution,");
		sb.append("  ifnull(sc_environment.name,'(missing)') environment,");
		sb.append("  sd.environment_compliant environmentCompliance,");
		if (minimumRecommendedFlag.equals(MinimumRecommendedFlag.MINIMUM)) {
			sb.append("  sd.min_compliant_operating_system operatingSystemCompliance,");
			sb.append("  sd.min_compliant_memory memoryCompliance,");
			sb.append("  sd.min_compliant_screen_resolution screenResolutionCompliance,");
			sb.append("  sd.min_compliant_display_size monitorDisplaySizeCompliance");
		} else {
			sb.append("  sd.rec_compliant_operating_system operatingSystemCompliance,");
			sb.append("  sd.rec_compliant_memory memoryCompliance,");
			sb.append("  sd.rec_compliant_screen_resolution screenResolutionCompliance,");
			sb.append("  sd.rec_compliant_display_size monitorDisplaySizeCompliance");
		}
		sb.append(" from readiness.snapshot_device sd");
		sb.append("  join core.org o on o.org_id = sd.org_id");
		sb.append("  join core.org po on po.org_id = o.parent_org_id");
		sb.append("  left join readiness.snapshot_config sc_operating_system on sc_operating_system.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_operating_system.code ='operatingSystems' ");
		sb.append("                                                         and sc_operating_system.value = sd.operating_system");
		sb.append("  left join readiness.snapshot_config sc_memory on sc_memory.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_memory.code ='memory' ");
		sb.append("                                                         and sc_memory.value = sd.memory");
		sb.append("  left join readiness.snapshot_config sc_display_size on sc_display_size.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_display_size.code ='displaySize' ");
		sb.append("                                                         and sc_display_size.value = sd.display_size");
		sb.append("  left join readiness.snapshot_config sc_screen_resolution on sc_screen_resolution.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_screen_resolution.code ='screenResolutions' ");
		sb.append("                                                         and sc_screen_resolution.value = sd.screen_resolution");
		sb.append("  left join readiness.snapshot_config sc_environment on sc_environment.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_environment.code ='environment' ");
		sb.append("                                                         and sc_environment.value = sd.environment");
		sb.append(" where ");
		sb.append("  sd.snapshot_window_id = :snapshotWindowId ");
		sb.append("  and sd.org_id = :orgId");
		criteriaQuery.setBaseSubSelect(sb.toString());

		Criteria criteria = new Criteria(firstResult, pageSize);
		criteria.getParameters().put("snapshotWindowId", snapshotWindowId);
		criteria.getParameters().put("orgId", org.getOrgId());

		return criteriaQuery.getData(criteria, Map.class);
	}

	@Override
	public Collection<Map<String, String>> export(ExportType exportType) {
		if (org == null) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}
		Collection<Map<String, String>> results;

		if (org.getOrgTypeName().equals("School")) {
			QueryResult<Map<String, String>> queryResults = retrieveSchoolDevices(snapshotWindow.getSnapshotWindowId(),
					null, null);
			results = queryResults.getRows();

			if (exportType.equals(ExportType.csv)) {
				for (Map<String, String> map : results) {
					formatTBDandMissing("operatingSystemCompliance", "operatingSystem", map);
					formatTBDandMissing("memoryCompliance", "memory", map);
					formatTBDandMissing("monitorDisplaySizeCompliance", "monitorDisplaySize", map);
					formatTBDandMissing("screenResolutionCompliance", "screenResolution", map);
					formatTBDandMissing("environmentCompliance", "environment", map);
				}
				return results;
			}

			for (Map<String, String> map : results) {
				specialFormatPdf("operatingSystemCompliance", "operatingSystem", map);
				specialFormatPdf("memoryCompliance", "memory", map);
				specialFormatPdf("monitorDisplaySizeCompliance", "monitorDisplaySize", map);
				specialFormatPdf("screenResolutionCompliance", "screenResolution", map);
				specialFormatPdf("environmentCompliance", "environment", map);
			}
		} else {
			results = reportsService.retrieveSummaryForChildOrgs(snapshotWindow.getSnapshotWindowId(), org.getOrgId(),
					minimumRecommendedFlag, true, 0, 0).getRows();

			for (Map<String, String> map : results) {
				if (exportType.equals(ExportType.pdf)) {
					formatTBD("devicePassingCount", "devicePassingCount", map);
					String percent = map.get("devicePassingPercent") == null ? "" : map.get("devicePassingPercent")
							.toUpperCase();
					if (NumberUtils.toInt(map.get("deviceTbdCount")) > 0) {
						percent += " <span style=\"color: #9a9a9a\">TBD</span>";
					}
					map.put("devicePassingPercent", percent);
				} else if (exportType.equals(ExportType.csv)) {
					String percent = map.get("devicePassingPercent") == null ? "" : map.get("devicePassingPercent")
							.toUpperCase();
					if (NumberUtils.toInt(map.get("deviceTbdCount")) > 0) {
						percent += " TBD";
					}
					map.put("devicePassingPercent", percent);
				}
				if (exportType.equals(ExportType.csv) && org.getOrgTypeName().equals("District")) {
					if (map.get("schoolType") != null) {
						map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
								: SCHOOL_TYPE.get(map.get("schoolType")));
					}
				}
			}
		}
		return results;
	}

	protected void specialFormatPdf(String complianceKey, String dataKey, Map<String, String> data) {
		StringBuilder sb = new StringBuilder();

		if (data.get(complianceKey) != null && data.get(dataKey).startsWith("<")) {
			String originalDataKey = data.get(dataKey);
			String newDataKey = originalDataKey.replaceAll("<", "&lt;");
			data.put(dataKey, newDataKey);
		}

		if (data.get(dataKey) != null && data.get(complianceKey) != null) {
			if (data.get(complianceKey).equals("no") && !data.get(dataKey).equals("(missing)")) {
				sb.append("<span style=\"color: red\">");
				sb.append("(");
				sb.append(data.get(dataKey));
				sb.append(")");
				sb.append("</span>");
				data.put(dataKey, sb.toString());
			} else {
				if (data.get(dataKey).equals("(missing)")) {
					sb.append("<span style=\"color: red\">");
					sb.append(data.get(dataKey));
					sb.append("</span>");
					data.put(dataKey, sb.toString());
				}
			}
		}

		if (data.get(complianceKey) != null && data.get(complianceKey).equals("TBD")) {
			sb.append(data.get(dataKey));
			sb.append("<span style=\"color: #9a9a9a\">").append("&nbsp;").append("TBD</span>");
			data.put(dataKey, sb.toString());
		}

	}
}
