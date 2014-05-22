package net.techreadiness.plugin.persistence.report;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedCaseInsensitiveMap;

@Repository(value = "BaseDataRetriever")
@Scope("prototype")
public class BaseDataRetrieverImpl implements BaseDataRetriever {

	Timestamp currentTime = new Timestamp(System.currentTimeMillis());

	@Inject
	private CriteriaQuery<Map<String, String>> criteriaQuery;
	@Inject
	private JdbcTemplate jdbcTemplate;

	@PersistenceContext
	protected EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	public QueryResult<Map<String, String>> getSnapshotReportDataForOrg(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag) throws SQLException {

		return getSnapshotData(orgId, snapshotWindowId, true, 0, 0, getSnapshotSql(minimumOrRecommendedFlag, 0));
	}

	@Override
	public QueryResult<Map<String, String>> findSnapshotReportDataForChildOrgs(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows)
					throws SQLException {

		return getSnapshotData(orgId, snapshotWindowId, retrieveAll, startingRow, numberOfRows,
				getSnapshotSql(minimumOrRecommendedFlag, 1));
	}

	@Override
	public QueryResult<Map<String, String>> findSnapshotReportDataForDescendantOrgs(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows,
			Integer orgTreeDistance) throws SQLException {

		return getSnapshotData(orgId, snapshotWindowId, retrieveAll, startingRow, numberOfRows,
				getSnapshotSql(minimumOrRecommendedFlag, orgTreeDistance));
	}

	@Override
	public QueryResult<Map<String, String>> findProgressSnapshotReportDataForOrg(Collection<Long> snapshotWindowIds,
			Long orgId, MinimumRecommendedFlag minimumOrRecommendedFlag) throws SQLException {
		return getProgressSnapshotData(orgId, true, 0, 0,
				getProgressSnapshotSql(minimumOrRecommendedFlag, snapshotWindowIds, true));
	}

	@Override
	public QueryResult<Map<String, String>> findProgressSnapshotReportDataForChildOrgs(Collection<Long> snapshotWindowIds,
			Long orgId, MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow,
			Integer numberOfRows) throws SQLException {

		return getProgressSnapshotData(orgId, retrieveAll, startingRow, numberOfRows,
				getProgressSnapshotSql(minimumOrRecommendedFlag, snapshotWindowIds, false));
	}

	@Override
	public QueryResult<Map<String, String>> retrieveSchoolExceptions(Long snapshotWindowId, Long orgId,
			String exceptionTypeCode, boolean retrieveAllRows, boolean retrieveFullDetails, Integer startingRow,
			Integer numberOfRows) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("   o.org_id orgId, ");
		sql.append("   o.name orgName, ");
		sql.append("   o.code orgCode, ");
		sql.append("   o.local_code localOrgCode, ");
		sql.append("   concat(po.name,' (',po.local_code,')') parentOrgName, ");
		sql.append("   po.code parentOrgCode, ");
		sql.append("   po.local_code parentLocalOrgCode, ");
		sql.append("   so.calc_survey_admin_count_display surveyAdminCount,");
		sql.append("   so.calc_survey_admin_understanding_display surveyAdminUnderstanding,");
		sql.append("   so.calc_survey_admin_training_display surveyAdminTraining,");
		sql.append("   so.calc_survey_techstaff_count_display surveyTechstaffCount,");
		sql.append("   so.calc_survey_techstaff_understanding_display surveyTechstaffUnderstanding,");
		sql.append("   so.calc_survey_techstaff_training_display surveyTechstaffTraining,");
		sql.append("   so.calc_internet_speed_display internetSpeed,");
		sql.append("   so.internet_utilization internetUtilization,");
		sql.append("   so.calc_network_speed_display networkSpeed,");
		sql.append("   so.network_utilization networkUtilization,");
		sql.append("   so.testing_window_length testingWindowLength,");
		sql.append("   so.sessions_per_day sessionsPerDay,");
		sql.append("   so.simultaneous_testers simultaneousTesters,");
		sql.append("   so.wireless_access_points wirelessAccessPoints,");
		sql.append("   so.enrollment_countk enrollmentCountK,");
		sql.append("   so.enrollment_count1 enrollmentCount1,");
		sql.append("   so.enrollment_count2 enrollmentCount2,");
		sql.append("   so.enrollment_count3 enrollmentCount3,");
		sql.append("   so.enrollment_count4 enrollmentCount4,");
		sql.append("   so.enrollment_count5 enrollmentCount5,");
		sql.append("   so.enrollment_count6 enrollmentCount6,");
		sql.append("   so.enrollment_count7 enrollmentCount7,");
		sql.append("   so.enrollment_count8 enrollmentCount8,");
		sql.append("   so.enrollment_count9 enrollmentCount9,");
		sql.append("   so.enrollment_count10 enrollmentCount10,");
		sql.append("   so.enrollment_count11 enrollmentCount11,");
		sql.append("   so.enrollment_count12 enrollmentCount12,");
		sql.append("   so.school_type schoolType,");
		sql.append("   ifnull(so.data_entry_complete,'(missing)') dataEntryComplete,");
		sql.append("   so.calc_device_count deviceCount,");
		sql.append("   so.calc_survey_answered_count surveyQuestionCount,");
		sql.append("   so.calc_survey_unanswered_count unansweredSurveyCount,");
		sql.append("   so.calc_testing_teststart_count testingTestStartCount,");
		sql.append("   round(ifnull(so.calc_device_count/so.calc_testing_teststart_count,0)) deviceTestTakerRatio");

		sql.append("  from readiness.snapshot_org so");
		sql.append("   join core.org_tree otree on otree.org_id=so.org_id ");
		sql.append("   join core.org o on o.org_id = otree.org_id");
		sql.append("   join core.org_type ot on ot.org_type_id = o.org_type_id and ot.code='school'");
		sql.append("   join core.org po on po.org_id = o.parent_org_id");
		sql.append(" where ");
		sql.append("   so.snapshot_window_id = :snapshotWindowId ");
		sql.append("   and otree.ancestor_org_id = :parentOrgId ");
		if (StringUtils.equalsIgnoreCase(exceptionTypeCode, "notComplete")) {
			sql.append("   and ifnull(so.data_entry_complete,'no') = 'no'");
		} else if (StringUtils.equalsIgnoreCase(exceptionTypeCode, "noDeviceEntry")) {
			sql.append("   and ifnull(so.calc_device_count,0) = 0");
		} else if (StringUtils.equalsIgnoreCase(exceptionTypeCode, "noActivity")) {
			sql.append("   and ifnull(so.calc_device_count,0) = 0 and ifnull(so.calc_survey_answered_count,0) = 0");
		} else if (StringUtils.equalsIgnoreCase(exceptionTypeCode, "potentiallyMissed")) {
			sql.append("   and ifnull(so.calc_device_count,0) > 0 and ifnull(so.calc_survey_answered_count,0) = 0");
		} else if (StringUtils.equalsIgnoreCase(exceptionTypeCode, "potentiallyMissedDevice")) {
			sql.append("   and ifnull(so.calc_device_count,0) = 0 and ifnull(so.calc_survey_answered_count,0) > 0");
		} else if (StringUtils.equalsIgnoreCase(exceptionTypeCode, "potentiallyComplete")) {
			sql.append("   and ifnull(so.calc_device_count,0) > 0 and ifnull(so.calc_survey_unanswered_count,0) = 0 and ifnull(so.data_entry_complete,'no') = 'no'");
		} else if (StringUtils.equalsIgnoreCase(exceptionTypeCode, "potentiallyUnderReported")) {
			sql.append("   and ifnull(so.data_entry_complete,'no') = 'yes' and (ifnull(so.calc_device_count,0) = 0 or ifnull(so.calc_survey_unanswered_count,0) > 0)");
		}
		sql.append(" group by ");
		sql.append("   o.org_id ");
		sql.append(" order by ");
		sql.append("  o.name ");

		Criteria criteria = new Criteria();
		criteria.getParameters().put("snapshotWindowId", snapshotWindowId);
		criteria.getParameters().put("parentOrgId", orgId);

		QueryResult<Map<String, String>> results = getResults(retrieveAllRows, startingRow, numberOfRows, sql.toString(),
				criteria);
		return results;
	}

	private static String getSnapshotSql(MinimumRecommendedFlag minimumOrRecommendedFlag, Integer distance) {
		StringBuilder sql = new StringBuilder();

		sql.append("select ");
		sql.append("  o.org_id orgId,  ");
		sql.append("  o.name orgName,  ");
		sql.append("  o.code orgCode,  ");
		sql.append("  o.local_code localOrgCode,  ");
		sql.append("  concat(po.name,' (',po.local_code,')') parentOrgName,  ");
		sql.append("  po.code parentOrgCode,  ");
		sql.append("  po.local_code parentLocalOrgCode,  ");
		sql.append("  date_format(sw.execute_date,'%M %d, %Y at %l:%i %p CT') createDate, ");

		sql.append("  so.school_type schoolType, ");
		sql.append("  so.calc_device_count deviceCount, ");
		sql.append("  case");
		sql.append("    when so.calc_percent_complete is null then '0%'");
		sql.append("    when so.calc_percent_complete > 100 then '>100%'");
		sql.append("    else concat(round(so.calc_percent_complete),'%')");
		sql.append("  end percentComplete,");
		sql.append("  case ");
		sql.append("   when so.calc_not_applicable = 1 then '(Not Applicable)'");
		sql.append("   else ifnull(so.calc_testing_teststart_count,'(missing)')");
		sql.append("  end testingTestStartCount,");
		sql.append("  ifnull(so.calc_testing_student_count,'(missing)') testingStudentCount,");
		sql.append("  ifnull(so.data_entry_complete,'(missing)') dataEntryComplete,");
		sql.append("  ifnull(so.calc_network_speed_display,'(missing)') networkSpeed,");
		sql.append("  ifnull(so.calc_internet_speed_display,'(missing)') internetSpeed,");
		sql.append("  ifnull(so.internet_utilization,'(missing)') internetUtilization,");
		sql.append("  ifnull(so.network_utilization,'(missing)') networkUtilization,");
		sql.append("  ifnull(so.sessions_per_day,'(missing)') sessionsPerDay,");
		sql.append("  ifnull(so.simultaneous_testers,'(missing)') simultaneousTesters,");
		sql.append("  ifnull(so.testing_window_length,'(missing)') testingWindowLength,");

		if (minimumOrRecommendedFlag.equals(MinimumRecommendedFlag.MINIMUM)) {
			// Minimum ================================================================
			sql.append("  case");
			sql.append("    WHEN so.testing_window_length is NULL then '(missing)'");
			sql.append("    WHEN so.min_testing_window_length IS NULL THEN '0'");
			sql.append("    ELSE so.min_testing_window_length");
			sql.append("  end testingWindowLengthCalc, ");
			sql.append("  cast(so.min_device_tbd_count as char) deviceTbdCount, ");
			sql.append("  cast(so.min_device_passing_count  as char) devicePassingCount, ");
			sql.append("  case");
			sql.append("    when so.min_device_passing_percent is null then '0%'");
			sql.append("    when so.min_device_passing_percent > 100 then '>100%'");
			sql.append("    else concat(round(so.min_device_passing_percent),'%')");
			sql.append("  end devicePassingPercent,");

			sql.append("  so.min_testtaker_0to25 testTaker0To25,");
			sql.append("  so.min_testtaker_26to50 testTaker26To50,");
			sql.append("  so.min_testtaker_51to75 testTaker51To75,");
			sql.append("  so.min_testtaker_76to100 testTaker76To100,");
			sql.append("  case ");
			sql.append("    when so.calc_not_applicable = 1 then '(Not Applicable)'");
			sql.append("   else cast(so.min_testtaker_possible_test_count as char)");
			sql.append("  end testTakerPossibleTestCount,");
			sql.append("  case");
			sql.append("    when so.calc_not_applicable = 1 then '(Not Applicable)'");
			sql.append("    when so.min_testtaker_percent_students_testable is null then '(missing)'");
			sql.append("    when so.min_testtaker_percent_students_testable > 100 then '>100%'");
			sql.append("    else concat(round(so.min_testtaker_percent_students_testable),'%')");
			sql.append("  end testTakerPercentStudentsTestable,");

			sql.append("  case ");
			sql.append("   when sw.min_network_tbd then 'TBD'");
			sql.append("   when so.min_network_possible_test_count is null then '(missing)'");
			sql.append("   else cast(so.min_network_possible_test_count as char)");
			sql.append("  end networkPossibleTestCount,");
			sql.append("  case ");
			sql.append("   when sw.min_network_tbd then 'TBD'");
			sql.append("   when so.calc_not_applicable = 1 then '(Not Applicable)'");
			sql.append("   when so.min_network_percent_students_testable is null then '(missing)'");
			sql.append("   when so.min_network_percent_students_testable > 100 then '>100%'");
			sql.append("   else concat(round(so.min_network_percent_students_testable),'%')");
			sql.append("  end networkPercentStudentsTestable,");
			sql.append("  if(round(so.min_network_percent_students_testable)>=75,'true','false') networkSufficient,");

			sql.append("  if(sw.min_network_tbd,'TBD',so.min_network_0to25) network0to25,");
			sql.append("  if(sw.min_network_tbd,'TBD',min_network_26to50) network26to50,");
			sql.append("  if(sw.min_network_tbd,'TBD',min_network_51to75) network51to75,");
			sql.append("  if(sw.min_network_tbd,'TBD',min_network_76to100) network76to100");
		} else {
			// Recommended ================================================================
			sql.append("  case");
			sql.append("    WHEN so.testing_window_length is NULL then '(missing)'");
			sql.append("    WHEN so.rec_testing_window_length IS NULL THEN '0'");
			sql.append("    ELSE so.rec_testing_window_length");
			sql.append("  end testingWindowLengthCalc, ");
			sql.append("  so.rec_device_tbd_count deviceTbdCount, ");
			sql.append("  so.rec_device_passing_count devicePassingCount, ");
			sql.append("  case");
			sql.append("    when so.rec_device_passing_percent is null then '0%'");
			sql.append("    when so.rec_device_passing_percent > 100 then '>100%'");
			sql.append("    else concat(round(so.rec_device_passing_percent),'%')");
			sql.append("  end devicePassingPercent,");

			sql.append("  so.rec_testtaker_0to25 testTaker0To25,");
			sql.append("  so.rec_testtaker_26to50 testTaker26To50,");
			sql.append("  so.rec_testtaker_51to75 testTaker51To75,");
			sql.append("  so.rec_testtaker_76to100 testTaker76To100,");
			sql.append("  case ");
			sql.append("    when so.calc_not_applicable = 1 then '(Not Applicable)'");
			sql.append("   else cast(so.rec_testtaker_possible_test_count as char)");
			sql.append("  end testTakerPossibleTestCount,");
			sql.append("  case");
			sql.append("    when so.calc_not_applicable = 1 then '(Not Applicable)'");
			sql.append("    when so.rec_testtaker_percent_students_testable is null then '(missing)'");
			sql.append("    when so.rec_testtaker_percent_students_testable > 100 then '>100%'");
			sql.append("    else concat(round(so.rec_testtaker_percent_students_testable),'%')");
			sql.append("  end testTakerPercentStudentsTestable,");

			sql.append("  case ");
			sql.append("   when sw.rec_network_tbd then 'TBD'");
			sql.append("   when so.rec_network_possible_test_count is null then 0");
			sql.append("   else cast(so.rec_network_possible_test_count as char)");
			sql.append("  end networkPossibleTestCount,");
			sql.append("  case ");
			sql.append("   when sw.rec_network_tbd then 'TBD'");
			sql.append("   when so.calc_not_applicable = 1 then '(Not Applicable)'");
			sql.append("   when so.rec_network_percent_students_testable is null then '(missing)'");
			sql.append("   when so.rec_network_percent_students_testable > 100 then '>100%'");
			sql.append("   else concat(round(so.rec_network_percent_students_testable),'%')");
			sql.append("  end networkPercentStudentsTestable,");
			sql.append("  if(round(so.rec_network_percent_students_testable)>=75,'true','false') networkSufficient,");

			sql.append("  if(sw.rec_network_tbd,'TBD',so.rec_network_0to25) network0to25,");
			sql.append("  if(sw.rec_network_tbd,'TBD',rec_network_26to50) network26to50,");
			sql.append("  if(sw.rec_network_tbd,'TBD',rec_network_51to75) network51to75,");
			sql.append("  if(sw.rec_network_tbd,'TBD',rec_network_76to100) network76to100");
		}
		sql.append(" from readiness.snapshot_org so");
		sql.append("  join readiness.snapshot_window sw on sw.snapshot_window_id = so.snapshot_window_id");
		sql.append("  join core.org o on o.org_id = so.org_id");
		sql.append("  join core.org_tree otree on otree.org_id=so.org_id");
		sql.append("  left join core.org po on po.org_id = o.parent_org_id");
		sql.append(" where ");
		sql.append("  so.snapshot_window_id = :snapshotWindowId ");
		sql.append("  and otree.ancestor_org_id = :parentOrgId ");
		if (distance != null) {
			sql.append("  and otree.distance=" + distance);
		}
		sql.append(" group by ");
		sql.append("  o.org_id ");
		sql.append(" order by ");
		sql.append("  o.name, o.code");

		return sql.toString();
	}

	private static String getProgressSnapshotSql(MinimumRecommendedFlag minimumOrRecommendedFlag,
			Collection<Long> snapshotWindowIds, boolean singleOrg) {
		StringBuilder sql = new StringBuilder();

		sql.append("select ");
		sql.append("o.org_id orgId, ");
		sql.append("o.name orgName, ");
		sql.append("o.code orgCode, ");
		sql.append("o.local_code localOrgCode, ");
		sql.append("concat(po.name,' (',po.local_code,')') parentOrgName, ");
		sql.append("po.code parentOrgCode, ");
		sql.append("po.local_code parentLocalOrgCode");
		if (!snapshotWindowIds.isEmpty()) {
			sql.append(",CASE ");
			for (Long id : snapshotWindowIds) {
				sql.append("WHEN sw");
				sql.append(id);
				sql.append(".name='default' THEN so");
				sql.append(id);
				sql.append(".school_type ");
			}
			sql.append("END schoolType, ");

			// Add the minimum/recommended values.
			String type = "rec";
			if (minimumOrRecommendedFlag.equals(MinimumRecommendedFlag.MINIMUM)) {
				type = "min";
			}
			Iterator<Long> i = snapshotWindowIds.iterator();
			while (i.hasNext()) {
				Long id = i.next();
				sql.append("ifnull(so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_device_tbd_count,0) deviceTbdCount_");
				sql.append(id);
				sql.append(",");

				sql.append("case ");
				sql.append("when so");
				sql.append(id);
				sql.append(".snapshot_org_id is null then '(not present)' ");
				sql.append("when so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_device_passing_percent is null then '0%' ");
				sql.append("when so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_device_passing_percent > 100 then '>100%' ");
				sql.append("else concat(round(so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_device_passing_percent),'%') ");
				sql.append("end devicePassingPercent_");
				sql.append(id);
				sql.append(",");

				sql.append("case ");
				sql.append("when so");
				sql.append(id);
				sql.append(".snapshot_org_id is null then '(not present)' ");
				sql.append("when so");
				sql.append(id);
				sql.append(".calc_not_applicable = 1 then '(Not Applicable)' ");
				sql.append("when so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_testtaker_percent_students_testable is null then '(missing)' ");
				sql.append("when so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_testtaker_percent_students_testable > 100 then '>100%' ");
				sql.append("else concat(round(so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_testtaker_percent_students_testable),'%') ");
				sql.append("end testTakerPercentStudentsTestable_");
				sql.append(id);
				sql.append(",");

				sql.append("case ");
				sql.append("when so");
				sql.append(id);
				sql.append(".snapshot_org_id is null then '(not present)' ");
				sql.append("when sw");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_network_tbd then 'TBD'");
				sql.append("when so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_network_percent_students_testable is null then '0%' ");
				sql.append("when so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_network_percent_students_testable > 100 then '>100%' ");
				sql.append("else concat(round(so");
				sql.append(id);
				sql.append(".");
				sql.append(type);
				sql.append("_network_percent_students_testable),'%') ");
				sql.append("end networkPercentStudentsTestable_");
				sql.append(id);

				if (i.hasNext()) {
					sql.append(",");
				}
			}
		}

		sql.append(" from core.org o ");
		sql.append("join core.org_tree otree on otree.org_id=o.org_id ");
		sql.append("left join core.org po on po.org_id = o.parent_org_id ");
		for (Long id : snapshotWindowIds) {
			sql.append("left join readiness.snapshot_window sw");
			sql.append(id);
			sql.append(" on sw");
			sql.append(id);
			sql.append(".snapshot_window_id and sw");
			sql.append(id);
			sql.append(".snapshot_window_id=");
			sql.append(id);
			sql.append(" ");
			sql.append("left join readiness.snapshot_org so");
			sql.append(id);
			sql.append(" on so");
			sql.append(id);
			sql.append(".org_id = o.org_id and so");
			sql.append(id);
			sql.append(".snapshot_window_id=");
			sql.append(id);
			sql.append(" ");
		}
		sql.append("where ");
		sql.append("otree.ancestor_org_id = :parentOrgId ");
		if (singleOrg) {
			sql.append("and otree.distance=0 ");
		} else {
			sql.append("and otree.distance=1 ");
		}
		if (!snapshotWindowIds.isEmpty()) {
			sql.append("and (");
			Iterator<Long> i = snapshotWindowIds.iterator();
			while (i.hasNext()) {
				Long id = i.next();
				sql.append("so");
				sql.append(id);
				sql.append(".snapshot_org_id is not null");
				if (i.hasNext()) {
					sql.append(" or ");
				}
			}
			sql.append(") ");
		}
		sql.append("order by o.name");

		return sql.toString();
	}

	private QueryResult<Map<String, String>> getSnapshotData(Long orgId, Long snapshotWindowId, boolean retrieveAll,
			Integer startingRow, Integer numberOfRows, String sql) {
		Criteria criteria = new Criteria();

		criteria.getParameters().put("snapshotWindowId", snapshotWindowId);
		criteria.getParameters().put("parentOrgId", orgId);

		return getResults(retrieveAll, startingRow, numberOfRows, sql, criteria);
	}

	private QueryResult<Map<String, String>> getProgressSnapshotData(Long orgId, boolean retrieveAll, Integer startingRow,
			Integer numberOfRows, String sql) {
		Criteria criteria = new Criteria();

		criteria.getParameters().put("parentOrgId", orgId);

		return getResults(retrieveAll, startingRow, numberOfRows, sql, criteria);
	}

	private QueryResult<Map<String, String>> getResults(boolean retrieveAll, Integer startingRow, Integer numberOfRows,
			String sql, Criteria criteria) {

		criteriaQuery.setBaseSubSelect(sql);
		if (!retrieveAll) {
			criteria.setFirstResults(startingRow);
			criteria.setPageSize(numberOfRows);
		}

		QueryResult<Map<String, String>> result = criteriaQuery.getData(criteria, Map.class);
		return result;
	}

	/**
	 * Spring DI for the JdbcTemplate.
	 *
	 * @param jdbcTemplate
	 *            The template that should be used to retrieve report data
	 */
	public void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public Integer findTotalCount(final OrgDO org, final ScopeDO consortiumScope) {
		final int totalCount = jdbcTemplate.queryForObject("select count(org.org_id) from core.org "
				+ "  inner join core.org_part on (org.org_id = org_part.org_id and org_part.scope_id = ?) "
				+ " where org.parent_org_id = ?", new Object[] { consortiumScope.getScopeId(), org.getOrgId() },
				Integer.class);
		return totalCount;
	}

	@Override
	public String formatString(String propertyName, Object value) {

		String returnValue;
		if (TOTAL_NUMBER_DEVICES.equals(propertyName)) {
			if (value == null) {
				returnValue = ZERO;
			} else if (value.toString().contains(",")) {
				returnValue = (String) value;
			} else {
				NumberFormat numberFormat = new DecimalFormat("##,###,###,###");
				numberFormat.setMaximumFractionDigits(0);
				Integer integerValue = new Integer(value.toString());
				returnValue = numberFormat.format(integerValue);
			}
		} else if (PASSING_DEVICE_COUNT.equals(propertyName) || "testTaker0To25".equals(propertyName)
				|| "testTaker26To50".equals(propertyName) || "testTaker51To75".equals(propertyName)
				|| "testTaker76To100".equals(propertyName) || "0To24NetworkComplianceCount".equals(propertyName)
				|| "25To49NetworkComplianceCount".equals(propertyName)
				|| "50To74NetworkComplianceCount".equals(propertyName)
				|| "75To100NetworkComplianceCount".equals(propertyName) || "maxNumberTestable".equals(propertyName)
				|| "testingTestStartCount".equals(propertyName) || "count".equals(propertyName)) {
			if (value == null) {
				returnValue = ZERO;
			} else if ("TBD".equals(value.toString())) {
				returnValue = (String) value;
			} else {
				if (value.toString().contains(",")) {
					returnValue = (String) value;
				} else {
					if (StringUtils.isEmpty((String) value)) {
						returnValue = "0";
					} else {
						NumberFormat numberFormat = new DecimalFormat("##,###,###,###");
						numberFormat.setMaximumFractionDigits(0);
						Integer integerValue = new Integer(value.toString());
						returnValue = numberFormat.format(integerValue);
					}
				}
			}

		} else if (PERCENT_PASSING_DEVICES.equals(propertyName)) {
			if (value == null) {
				returnValue = ZERO_PERCENT;
			} else if ("TBD".equals(value.toString())) {
				returnValue = (String) value;
			} else if (value.toString().contains("%")) {
				returnValue = (String) value;
			} else {
				NumberFormat decimalFormatter = NumberFormat.getInstance();
				decimalFormatter.setMinimumFractionDigits(0);
				decimalFormatter.setMaximumFractionDigits(0);
				decimalFormatter.setRoundingMode(RoundingMode.HALF_UP);
				double decimalValue = new Double(value.toString()) * 100;
				decimalFormatter.format(decimalValue);
				returnValue = decimalFormatter.format(decimalValue) + "%";
			}
		} else if (PERCENT_TESTABLE_STUDENTS.equals(propertyName)) {
			if (value == null) {
				returnValue = ZERO_PERCENT;
			} else if ("TBD".equals(value.toString())) {
				returnValue = (String) value;
			} else if (value.toString().contains("%")) {
				returnValue = (String) value;
			} else {
				returnValue = (String) value + "%";
			}
		} else if (TIME_REQUIRED_TO_DOWNLAD_TEST.equals(propertyName)) {
			if (value == null) {
				returnValue = ZERO;
			} else if ("TBD".equals(value.toString())) {
				returnValue = (String) value;
			} else {
				returnValue = millisToShortDHMS(new Long(value.toString()));
			}
		} else if (PERCENT_COMPLETE.equals(propertyName)) {
			if (value == null) {
				returnValue = ZERO_PERCENT;
			} else if ("TBD".equals(value.toString())) {
				returnValue = (String) value;
			} else if (value.toString().contains("%")) {
				returnValue = (String) value;
			} else {
				returnValue = (String) value + "%";
			}
		} else if (COMPLETION_STATUS.equals(propertyName)) {
			if (value == null) {
				returnValue = ZERO;
			} else if ("TBD".equals(value.toString())) {
				returnValue = (String) value;
			} else {
				returnValue = (String) value;
			}
		} else {
			if (value == null) {
				returnValue = "";
			} else {
				returnValue = value.toString();
			}
		}

		return returnValue;
	}

	@Override
	public Object formatTime(Object value) {

		if (value instanceof Timestamp) {
			Timestamp timeStampValue = (Timestamp) value;
			DateFormat dateFormatter = new SimpleDateFormat("MMMMMM dd, yyyy 'at' h:mm a z");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("CST"));

			value = dateFormatter.format(new Date(timeStampValue.getTime())).replace("CDT", "CT");
		}
		return value;
	}

	private static String millisToShortDHMS(long durationInMillis) {

		String hoursMinutesSeconds = "";

		long hours = durationInMillis / (1000 * 60 * 60);
		long minutes = durationInMillis % (1000 * 60 * 60) / (1000 * 60);
		long seconds = durationInMillis % (1000 * 60 * 60) % (1000 * 60) / 1000;
		if (hours == 0 && minutes == 0) {
			hoursMinutesSeconds = String.format("%2d sec", seconds);
		} else if (hours == 0) {
			hoursMinutesSeconds = String.format("%2d min %2d sec", minutes, seconds);
		} else {
			hoursMinutesSeconds = String.format("%2d hours %02d min %2d sec", hours, minutes, seconds);
		}
		return hoursMinutesSeconds;
	}

	public class ReadinessRowMapper implements RowMapper<Map<String, String>> {

		@Override
		public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			Map<String, String> mapOfColValues = new LinkedCaseInsensitiveMap<>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
				String obj = formatString(key, getColumnValue(rs, i));

				mapOfColValues.put(key, obj);
			}
			return mapOfColValues;
		}

		protected String getColumnKey(String columnName) {
			return columnName;
		}

		protected String getColumnValue(ResultSet rs, int index) throws SQLException {
			Object value = JdbcUtils.getResultSetValue(rs, index);

			value = formatTime(value);
			if (value == null) {
				return "";
			}

			return value.toString();
		}

	}

	@Override
	public List<Map<String, String>> findMinimumRecommendedValues(Long snapshotWindowId) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		sb.append("  *");
		sb.append("FROM ");
		sb.append("  (");
		sb.append("    SELECT");
		sb.append("     IFNULL(MAX(IF(sc.code = 'minimumMonitorDisplaySize', sc.name, NULL)), 'TBD') minimumMonitorDisplaySize,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'minimumScreenResolution', sc.name, NULL)), 'TBD') minimumScreenResolution,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'minimumTestingWindowLength', sc.name, NULL)), 'TBD') minimumTestingWindowLength,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'minimumThroughputRequiredPerStudent', sc.name, NULL)), 'TBD') minimumThroughputRequiredPerStudent,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'recommendedMonitorDisplaySize', sc.name, NULL)), 'TBD') recommendedMonitorDisplaySize,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'recommendedScreenResolution', sc.name, NULL)), 'TBD') recommendedScreenResolution,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'recommendedTestingWindowLength', sc.name, NULL)), 'TBD') recommendedTestingWindowLength,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'recommendedThroughputRequiredPerStudent', sc.name, NULL)), 'TBD') recommendedThroughputRequiredPerStudent,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGradeK', sc.name, NULL)), 'Not Testing') includeGradeK,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade1', sc.name, NULL)), 'Not Testing') includeGrade1,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade2', sc.name, NULL)), 'Not Testing') includeGrade2,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade3', sc.name, NULL)), 'Not Testing') includeGrade3,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade4', sc.name, NULL)), 'Not Testing') includeGrade4,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade5', sc.name, NULL)), 'Not Testing') includeGrade5,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade6', sc.name, NULL)), 'Not Testing') includeGrade6,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade7', sc.name, NULL)), 'Not Testing') includeGrade7,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade8', sc.name, NULL)), 'Not Testing') includeGrade8,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade9', sc.name, NULL)), 'Not Testing') includeGrade9,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade10', sc.name, NULL)), 'Not Testing') includeGrade10,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade11', sc.name, NULL)), 'Not Testing') includeGrade11,");
		sb.append("     IFNULL(MAX(IF(sc.code = 'includeGrade12', sc.name, NULL)), 'Not Testing') includeGrade12");
		sb.append("    FROM readiness.snapshot_config sc");
		sb.append("    WHERE sc.type = 'scope_ext' AND sc.snapshot_window_id = ?");
		sb.append("  ) os_list,");
		sb.append("  (");
		sb.append("    SELECT");
		sb.append("      os.value os_type,");
		sb.append("      os.name operatingSystem,");
		sb.append("      minMemory.name minimumMemory,");
		sb.append("      recMemory.name recommendedMemory");
		sb.append("    FROM readiness.snapshot_config os");
		sb.append("      LEFT JOIN readiness.snapshot_config minMemory ON minMemory.snapshot_window_id = os.snapshot_window_id AND minMemory.type = 'scope_ext' AND minMemory.code = CONCAT('minMemory_', os.value)");
		sb.append("      LEFT JOIN readiness.snapshot_config recMemory ON recMemory.snapshot_window_id = os.snapshot_window_id AND recMemory.type = 'scope_ext' AND recMemory.code = CONCAT('recommendedMemory_', os.value)");
		sb.append("    WHERE os.code = 'operatingSystems' and os.snapshot_window_id = ?");
		sb.append("  ) global_list");
		sb.append(" ORDER BY os_type");

		Object[] params = new Object[] { snapshotWindowId, snapshotWindowId };

		return getJdbcTemplate().query(sb.toString(), params, new ReadinessRowMapper());
	}

	@Override
	public void createSnapshotRollup(Long snapshotWindowId, boolean fullRefresh) {
		getJdbcTemplate().execute("call readiness.snapshot(" + snapshotWindowId + "," + fullRefresh + ")");
	}
}
