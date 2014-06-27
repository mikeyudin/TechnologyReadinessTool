package net.techreadiness.plugin.persistence.report;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.techreadiness.persistence.criteriaquery.QueryResult;

/**
 * This defines the API for the retrieval of data that is to be used to support the Summary Reporting capabilities for tje
 * Readiness Project. This interface does not extend BaseDAO as this is not really a Data Access Object, as any class that
 * implements this interface would not implement the insert, update, delete or findAll methods that are part of the BaseDAO
 * interface.
 */
public interface StaffAndPersonnelSurveyDataRetriever extends BaseDataRetriever {

	/**
	 * This method will return a Map that contains a list of survey questions. The surveyQuestionTypeCode will be the key,
	 * with the actual question being the value
	 *
	 * @return Map of SurveyQuestions
	 */
	List<Map<String, String>> retrieveSurveyQuestions();

	/**
	 * This method will return a Map of snapshot report data for the single org that is passed in. *
	 *
	 * <pre>
	 * <code>
	 * The returned Map will contain the following key pair values:
	 *
	 *     orgId - This is the row identifier for the State Level Organization.
	 *     orgName - The name of the State Level Organization.
	 *     orgCode - The code of the State Level Organization
	 * </code>
	 * </pre>
	 *
	 * @param orgId
	 *            The Root Org for Readiness.
	 * @param snapshotWindowId
	 *            The window for which the percent of passing devices is desired.
	 * @param surveyQuestionTypeCode The question to return data for
	 * @return The Map of data that contains the information about State Device Assessment data.
	 * @throws SQLException
	 *             the sQL exception
	 */
	QueryResult<Map<String, String>> getSnapshotSurveyReportDataForOrg(Long snapshotWindowId, Long orgId,
			String surveyQuestionTypeCode) throws SQLException;

	/**
	 * This method will return a List of Maps of Device Assessment information for the child orgs of the given parent org.
	 *
	 * <pre>
	 * <code>
	 * The returned Maps will contain the following key pair values:
	 *
	 *     orgId - This is the row identifier for the State Level Organization.
	 *     orgName - The name of the State Level Organization.
	 *     orgCode - The code of the State Level Organization
	 * </code>
	 * </pre>
	 *
	 * @param orgId
	 *            the parent organization for which to get the child report data for.
	 * @param snapshotWindowId
	 *            the snapshot window for which to retrieve the data from.
	 * @param surveyQuestionTypeCode Survey question to retrieve data for.
	 * @param retrieveAll If true paging parameters are ignored
	 * @param startingRow Index of the first result to be returned
	 * @param numberOfRows Maximum number of records to return
	 * @return The Map of data that contains the information about Device Assessment data.
	 * @throws SQLException
	 *             the sQL exception
	 */
	QueryResult<Map<String, String>> findSnapshotSurveyReportDataForChildOrgs(Long snapshotWindowId, Long orgId,
			String surveyQuestionTypeCode, boolean retrieveAll, Integer startingRow, Integer numberOfRows)
					throws SQLException;
}
