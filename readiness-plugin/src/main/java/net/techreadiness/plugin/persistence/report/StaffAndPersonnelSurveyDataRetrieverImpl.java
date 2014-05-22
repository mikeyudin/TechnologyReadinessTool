package net.techreadiness.plugin.persistence.report;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class StaffAndPersonnelSurveyDataRetrieverImpl extends BaseDataRetrieverImpl implements
StaffAndPersonnelSurveyDataRetriever {

	@Inject
	private CriteriaQuery<Map<String, String>> criteriaQuery;

	@Override
	public QueryResult<Map<String, String>> getSnapshotSurveyReportDataForOrg(Long snapshotWindowId, Long orgId,
			String surveyQuestionTypeCode) throws SQLException {

		QueryResult<Map<String, String>> result = getSurveyResults(snapshotWindowId, orgId, true, 0, 0,
				getSql(surveyQuestionTypeCode, true));

		return result;
	}

	@Override
	public QueryResult<Map<String, String>> findSnapshotSurveyReportDataForChildOrgs(Long snapshotWindowId, Long orgId,
			String surveyQuestionTypeCode, boolean retrieveAll, Integer startingRow, Integer numberOfRows)
					throws SQLException {

		return getSurveyResults(snapshotWindowId, orgId, retrieveAll, startingRow, numberOfRows,
				getSql(surveyQuestionTypeCode, false));
	}

	private static String getSql(String surveyQuestionTypeCode, boolean singleOrg) {
		StringBuilder sql = new StringBuilder();

		sql.append("select ");
		sql.append("  o.org_id orgId,  ");
		sql.append("  o.name orgName,  ");
		sql.append("  o.code orgCode,  ");
		sql.append("  o.local_code localOrgCode,  ");
		sql.append("  concat(po.name,' (',po.local_code,')') parentOrgName,  ");
		sql.append("  po.code parentOrgCode,  ");
		sql.append("  po.local_code parentLocalOrgCode,  ");
		sql.append("  date_format(sw.execute_date,'%M %d, %Y at %l:%i%p CT') createDate, ");
		sql.append("  ifnull(so.data_entry_complete,'(missing)') dataEntryComplete,");

		sql.append("  ifnull(so.school_type,'') schoolType,");
		sql.append("  so.calc_survey_" + surveyQuestionTypeCode + "_display levelOfConcern,");
		sql.append("  cast(so.calc_survey_" + surveyQuestionTypeCode + "_0to3 as char) levelOfConcernCount0to3,");
		sql.append("  cast(so.calc_survey_" + surveyQuestionTypeCode + "_4to5 as char) levelOfConcernCount4to5,");
		sql.append("  cast(so.calc_survey_" + surveyQuestionTypeCode + "_6to7 as char) levelOfConcernCount6to7,");
		sql.append("  cast(so.calc_survey_" + surveyQuestionTypeCode + "_8to10 as char) levelOfConcernCount8to10,");
		sql.append("  cast(round(ifnull(so.calc_survey_" + surveyQuestionTypeCode
				+ "_average,0),2) as char) levelOfConcernAverageResponse");

		sql.append(" from readiness.snapshot_org so");
		sql.append("  join readiness.snapshot_window sw on sw.snapshot_window_id = so.snapshot_window_id");
		sql.append("  join core.org_tree otree on otree.org_id=so.org_id");
		sql.append("  join core.org o on o.org_id = otree.org_id");
		sql.append("  left join core.org po on po.org_id = o.parent_org_id");
		sql.append(" where ");
		sql.append("  so.snapshot_window_id = :snapshotWindowId ");
		sql.append("  and otree.ancestor_org_id = :parentOrgId ");
		if (singleOrg) {
			sql.append("  and otree.distance=0 ");
		} else {
			sql.append("  and otree.distance=1 ");
		}

		sql.append(" group by ");
		sql.append("  o.org_id ");
		sql.append(" order by ");
		sql.append("  o.name");

		return sql.toString();
	}

	private QueryResult<Map<String, String>> getSurveyResults(Long snapshotWindowId, Long orgId, boolean retrieveAll,
			Integer startingRow, Integer numberOfRows, String sql) {
		Criteria criteria = new Criteria();

		criteria.getParameters().put("snapshotWindowId", snapshotWindowId);
		criteria.getParameters().put("parentOrgId", orgId);

		criteriaQuery.setBaseSubSelect(sql);
		if (!retrieveAll) {
			criteria.setFirstResults(startingRow);
			criteria.setPageSize(numberOfRows);
		}

		QueryResult<Map<String, String>> result = criteriaQuery.getData(criteria, Map.class);

		return result;
	}

	@Override
	public List<Map<String, String>> retrieveSurveyQuestions() {

		String sql = "select entity_field.code surveyQuestionCode  " + " from core.option_list "
				+ " INNER JOIN core.entity_field " + "  USING (option_list_id) "
				+ " where option_list.code = 'surveyRating'";

		return getJdbcTemplate().query(sql, new Object[] {}, new ReadinessRowMapper());

	}
}
