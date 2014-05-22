package net.techreadiness.plugin.service.reports;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.plugin.persistence.dao.SnapshotWindowDao;
import net.techreadiness.plugin.persistence.report.BaseDataRetriever;
import net.techreadiness.plugin.persistence.report.StaffAndPersonnelSurveyDataRetriever;
import net.techreadiness.plugin.service.object.SnapshotOrg;
import net.techreadiness.service.BaseServiceImpl;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

@Service
@Transactional(readOnly = true)
public class ReportsServiceImpl extends BaseServiceImpl implements ReportsService {

	@Inject
	@Qualifier("BaseDataRetriever")
	private BaseDataRetriever baseDataRetriever;

	@Inject
	ScopeService scopeService;
	@Inject
	OrganizationService organizationService;

	@Inject
	SnapshotWindowDao snapshotWindowDao;
	@Inject
	OrgDAO orgDAO;

	@Inject
	StaffAndPersonnelSurveyDataRetriever staffAndPersonnelSurveyDataRetriever;

	@Override
	public ScopeDO findScope(final String scopePathName) {

		final Scope scope = scopeService.getByScopePath(scopePathName);
		return getMappingService().map(scope);

	}

	@Override
	public OrgDO findRootOrg(ServiceContext context) {
		final Org organization = organizationService.getByCode(context, "readiness");
		return getMappingService().map(organization);
	}

	@Override
	public Integer retrieveTotalCount(final Org org, final Scope scope) {

		final OrgDO orgDO = getMappingService().map(org);
		final ScopeDO scopeDO = getMappingService().map(scope);
		return baseDataRetriever.findTotalCount(orgDO, scopeDO);
	}

	@Override
	public QueryResult<Map<String, String>> retrieveSummaryForOrg(Long snapshowWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag) {

		try {
			return baseDataRetriever.getSnapshotReportDataForOrg(snapshowWindowId, orgId, minimumOrRecommendedFlag);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public QueryResult<Map<String, String>> retrieveSummaryForChildOrgs(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows) {
		try {
			return baseDataRetriever.findSnapshotReportDataForChildOrgs(snapshotWindowId, orgId, minimumOrRecommendedFlag,
					retrieveAll, startingRow, numberOfRows);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public QueryResult<Map<String, String>> retrieveSummaryForDescendantOrgs(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows,
			Integer orgTreeDistance) {
		try {
			return baseDataRetriever.findSnapshotReportDataForDescendantOrgs(snapshotWindowId, orgId,
					minimumOrRecommendedFlag, retrieveAll, startingRow, numberOfRows, orgTreeDistance);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Map<String, String>> retrieveMinimumRecommendedValues(Long snapshotWindowId) {

		try {

			return baseDataRetriever.findMinimumRecommendedValues(snapshotWindowId);

		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Map<String, String>> retrieveSurveyQuestions() {
		return staffAndPersonnelSurveyDataRetriever.retrieveSurveyQuestions();
	}

	@Override
	public QueryResult<Map<String, String>> retrieveSurveySummaryForOrg(Long snapshotWindowId, Long orgId,
			String surveyQuestionTypeCode) {

		try {
			return staffAndPersonnelSurveyDataRetriever.getSnapshotSurveyReportDataForOrg(snapshotWindowId, orgId,
					surveyQuestionTypeCode);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public QueryResult<Map<String, String>> retrieveSchoolExceptions(Long snapshotWindowId, Long orgId,
			String exceptionTypeCode, boolean retrieveAllRows, boolean retrieveFullDetails, Integer startingRow,
			Integer numberOfRows) {
		try {
			return baseDataRetriever.retrieveSchoolExceptions(snapshotWindowId, orgId, exceptionTypeCode, retrieveAllRows,
					retrieveFullDetails, startingRow, numberOfRows);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public QueryResult<Map<String, String>> retrieveSurveySummaryForChildOrgs(Long snapshotWindowId, Long orgId,
			String surveyQuestionTypeCode, boolean retrieveAll, Integer startingRow, Integer numberOfRows) {
		try {
			return staffAndPersonnelSurveyDataRetriever.findSnapshotSurveyReportDataForChildOrgs(snapshotWindowId, orgId,
					surveyQuestionTypeCode, retrieveAll, startingRow, numberOfRows);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void createSnapshotRollup(Long snapshotWindowId, boolean fullRefresh) {
		baseDataRetriever.createSnapshotRollup(snapshotWindowId, fullRefresh);
	}

	@Override
	public QueryResult<Map<String, String>> retrieveProgressDataForChildOrgs(Collection<Long> snapshotWindowIds, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows) {
		try {
			return baseDataRetriever.findProgressSnapshotReportDataForChildOrgs(snapshotWindowIds, orgId,
					minimumOrRecommendedFlag, retrieveAll, startingRow, numberOfRows);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public QueryResult<Map<String, String>> retrieveProgressDataForOrg(Collection<Long> snapshotWindowIds, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag) {
		try {
			return baseDataRetriever
					.findProgressSnapshotReportDataForOrg(snapshotWindowIds, orgId, minimumOrRecommendedFlag);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public SnapshotOrg getSnapshotDataForOrg(Long snapshotWindowId, Long orgId, MinimumRecommendedFlag flag) {
		try {
			QueryResult<Map<String, String>> reportData = baseDataRetriever.getSnapshotReportDataForOrg(snapshotWindowId,
					orgId, flag);
			Collection<Map<String, String>> rows = reportData.getRows();
			if (CollectionUtils.isEmpty(rows)) {
				return null;
			}
			Map<String, String> first = Iterables.getFirst(rows, Collections.<String, String> emptyMap());
			SnapshotOrg org = new SnapshotOrg();
			for (Entry<String, String> entry : first.entrySet()) {
				BeanUtils.setProperty(org, entry.getKey(), entry.getValue());
			}
			return org;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
