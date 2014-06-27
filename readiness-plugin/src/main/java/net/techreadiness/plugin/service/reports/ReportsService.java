package net.techreadiness.plugin.service.reports;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.plugin.service.object.SnapshotOrg;
import net.techreadiness.service.BaseService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;

public interface ReportsService extends BaseService {

	String DEFAULT_SNAPSHOT_WINDOW = "default";
	String DISTRICT_ORG_TYPE = "district";
	String PERCENT_COMPLETE = "percentComplete";

	String READINESS_ORG = "readiness";

	String READINESS_SCOPE_PATH = "/readiness";

	/**
	 * Finds number of rows that will be returned for a given report if there were no pagination involved. This is done by
	 * determining the number of Child Organizations that belong to a parent organization. As all the reports are access thru
	 * a parent organization, this approach will will.
	 *
	 * @param org
	 *            The org parent org for which a count of ChildOrgs will be calculated
	 * @param scope
	 *            The scope for which the orgs must belong to
	 * @return The total number of records in the result set.
	 */
	Integer retrieveTotalCount(Org org, Scope scope);

	ScopeDO findScope(String scopePathName);

	OrgDO findRootOrg(ServiceContext context);

	/**
	 * This method will return a Map that contains the information for a given snapshot and organization regarding the
	 * percentage of devices that are deemed complaint to support online testing.
	 *
	 * <pre>
	 * <code>
	 *  The Map will contain the following key pair values:
	 *
	 *      orgId - This is the row identifier for the Consortium.
	 *      percenctPassingDevices - This is the percent of devices that are deemed
	 *                               to have been complaint
	 *      createDate - This is the date and time that the Device Indicators were calculated.
	 * </code>
	 * </pre>
	 * @param snapshotWindowId The snapshot to find data for.
	 * @param orgId The organization to find data for.
	 * @param minimumOrRecommendedFlag The type of snapshot information requested.
	 *
	 * @return The Map of data that contains the information about Consortium Level Device Assessment data.
	 */
	QueryResult<Map<String, String>> retrieveSummaryForOrg(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag);

	/**
	 * This method will return a List of Maps of Device Assessment information for the child organizations that belong to the
	 * given org. This list will support pagination based on the alphabetical ordering of the org's Name using firstRow thru
	 * lastRow parameters.
	 *
	 * @param snapshotWindowId
	 *            the ID of the snapshot to be displayed
	 * @param orgId
	 *            the ID of the ancestor org for which the descendants are being displayed
	 * @param minimumOrRecommendedFlag
	 *            is the user viewing the minimum report values or the recommended
	 * @param retrieveAll
	 *            Disables pagination
	 * @param startingRow
	 *            The starting row of the result set that is to be returned
	 * @param numberOfRows
	 *            The total number of rows to be returned.
	 * @return The Map of data that contains the information about State Level Device Assessment data.
	 */
	QueryResult<Map<String, String>> retrieveSummaryForChildOrgs(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows);

	/**
	 * This method will return a List of Maps of Device Assessment information for the descendant organizations that belong
	 * to the given org. This list will support pagination based on the alphabetical ordering of the org's Name using
	 * firstRow thru lastRow parameters.
	 *
	 * @param snapshotWindowId
	 *            the ID of the snapshot to be displayed
	 * @param orgId
	 *            the ID of the ancestor org for which the descendants are being displayed
	 * @param minimumOrRecommendedFlag
	 *            is the user viewing the minimum report values or the recommended
	 * @param retrieveAll
	 *            Disables pagination
	 * @param startingRow
	 *            The starting row of the result set that is to be returned
	 * @param numberOfRows
	 *            The total number of rows to be returned.
	 * @param orgTreeDistance
	 *            the distance down the org hierarchy of the organizations being displayed (if the selected org is a state
	 *            and the distance is 2 ... schools will be returned)
	 * @return The Map of data that contains the information about State Level Device Assessment data.
	 */
	QueryResult<Map<String, String>> retrieveSummaryForDescendantOrgs(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows,
			Integer orgTreeDistance);

	/**
	 * Gets a single org's snapshot data to display for the progress reports. This method is for all three progress reports
	 * (data for all 3 is always returned). The returned maps contain the following org data:
	 *
	 * orgId - This is the row identifier for the Organization. orgName - The name of the Organization. orgCode - The code of
	 * the State Level Organization. localOrgCode parentOrgName parentOrgCode parentLocalOrgCode
	 *
	 * The map also contains the date the snapshot data was created:
	 *
	 * createDate
	 *
	 *
	 * @param snapshotWindowIds
	 *            The list of snapshot ids to get the data for.
	 * @param orgId
	 *            The org to get the child org data for.
	 * @param minimumOrRecommendedFlag The type of snapshot information to return.
	 *
	 * @return Snapshot information for the organization and snapshots specified.
	 */
	QueryResult<Map<String, String>> retrieveProgressDataForOrg(Collection<Long> snapshotWindowIds, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag);

	/**
	 * Gets the snapshot data of child orgs to display for the progress reports. This method is for all three progress
	 * reports (data for all 3 is always returned). The returned maps contain the following org data:
	 *
	 * orgId - This is the row identifier for the Organization. orgName - The name of the Organization. orgCode - The code of
	 * the State Level Organization. localOrgCode parentOrgName parentOrgCode parentLocalOrgCode
	 *
	 * The map also contains the date the snapshot data was created:
	 *
	 * createDate
	 *
	 * @param snapshotWindowIds
	 *            The list of snapshot ids to get the data for.
	 * @param orgId
	 *            - the org to get the child org data for.
	 * @param minimumOrRecommendedFlag The type of snapshot information to return.
	 * @param retrieveAll
	 *            - gets all rows (ie. for exports)
	 * @param startingRow
	 *            - if not getting all rows, the row to start with
	 * @param numberOfRows
	 *            - if not getting all rows, the number of rows to get.
	 *
	 * @return Snapshot information for child organizations of the organization and snapshots specified.
	 */
	QueryResult<Map<String, String>> retrieveProgressDataForChildOrgs(Collection<Long> snapshotWindowIds, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows);

	/**
	 * This method will return a List of Maps that contains the minimum and recommended values for the memory size, monitor
	 * display size, screen resolution, connection speed, size of test, max time to download a test and if the operating
	 * system is support.
	 *
	 * <pre>
	 * <code>
	 * The returned Maps will contain the following key pair values:
	 *
	 *     operatingSystem - This is the operating system values.
	 *     memoryMinimum - The minimum memory for an operating system, will contain a numeric value or tbd
	 *     recommendedMemory - The recommended memory for an operating system, will contain a numeric value or tbd
	 *     minimumMonitorDisplaySize - Specifies the minimum monitor display size, will contain a numeric value or tbd
	 *     recommendedMonitorDisplaySize - Specifies the recommended monitor display size, will contain a numeric value or tbd
	 *     minimumScreenResolution - Specifies the minimum screen resolution, will contain a numeric value or tbd
	 *     recommendedScreenResolution - Specifies the recommended screen resolution, will contain a numeric value or tbd
	 *     minimumConnectionSpeed - Specifies the minimum connection speed, will contain a numeric value or tbd
	 *     recommendedConnectionSpeed - Specifies the recommended connection speed, will contain a numeric value or tbd
	 *     minimumSizeOfTest - Specifies the minimum size of test, will contain a numeric value or tbd
	 *     recommendedSizeOfTest - Specifies the recommended size of test, will contain a numeric value or tbd
	 *     minimumTimeToDownloadTest - Specifies the minimum time to down load a test, will contain a numeric value or tbd
	 *     recommendedTimeToDownloadTest - Specifies the recommended time to down load a test, will contain a numeric value or tbd
	 *
	 * </code>
	 * </pre>
	 * @param snapshotWindowId The snapshot to get values for. 
	 * @return The Map of data that contains the information about District Level Device Assessment data.
	 */

	List<Map<String, String>> retrieveMinimumRecommendedValues(Long snapshotWindowId);

	/**
	 * This method will return a Map that contains a list of survey questions. The surveyQuestionTypeCode will be the key,
	 * with the actual question being the value
	 *
	 * @return Map of SurveyQuestions
	 */
	List<Map<String, String>> retrieveSurveyQuestions();

	/**
	 * This method will return a List of Maps of Staff and Personnel Survey results for all the Schools that belong to a
	 * given District. This list will support pagination based on the alphabetical ordering of the School's Name using
	 * firstRow thru lastRow parameters. For example if the following Schools belonged to a District: A Street Elementary,
	 * Boeing Middle School, Delaware Ave Middle High School and West Side High School and the method was passes a starting
	 * row of 1 and a number of rows of 2, Street Elementary and Boeing Middle School would be returned.
	 *
	 * <pre>
	 * <code>
	 * The Map will contain the following key pair
	 * values:
	 *     orgId - This is the row identifier for the School.
	 *     orgCode - This is the Code for the School
	 *     localOrgCode - This is the Local Code for the School
	 *     orgName - This is the Name for the School
	 *     levelOfConcernCount0to3 - This is the level of concern if it is 0 thru 3 for the school for the type of
	 *     levelOfConcernCount4to5 - This is the count of Schools that have between 25% & 49% Network Compliance
	 *     levelOfConcernCount6to7 - This is the count of Schools that have between 50% & 74% Network Compliance
	 *     levelOfConcernCount8to10 - This is the count of Schools that have between 74% & 100% Network Compliance
	 *
	 * </code>
	 * </pre>
	 * @param snapshotWindowId The snapshot to get data for
	 * @param orgId The parent organization

	 * @param surveyQuestionTypeCode
	 *            Code for the type of Survey Questions.
	 * @param retrieveAll 
	 * @param startingRow
	 *            - The starting row of the result set that is to be returned
	 * @param numberOfRows
	 *            - The total number of rows to be returned.
	 * @return Summary data for child organizations of {@code orgId}
	 */
	QueryResult<Map<String, String>> retrieveSurveySummaryForChildOrgs(Long snapshotWindowId, Long orgId,
			String surveyQuestionTypeCode, boolean retrieveAll, Integer startingRow, Integer numberOfRows);

	/**
	 * This method will return a Maps of Staff and Personnel Survey results for the Schools that is passed into the method.
	 *
	 * <pre>
	 * <code>
	 * The Map will contain the following key pair
	 * values:
	 *     orgId - This is the row identifier for the School.
	 *     orgCode - This is the Code for the School
	 *     localOrgCode - This is the Local Code for the School
	 *     orgName - This is the Name for the School
	 *     levelOfConcernCount0to3 - This is the level of concern if it is 0 thru 3 for the school for the type of
	 *     levelOfConcernCount4to5 - This is the count of Schools that have between 25% & 49% Network Compliance
	 *     levelOfConcernCount6to7 - This is the count of Schools that have between 50% & 74% Network Compliance
	 *     levelOfConcernCount8to10 - This is the count of Schools that have between 74% & 100% Network Compliance
	 *
	 * </code>
	 * </pre>
	 * @param snapshotWindowId The snapshot to get data for
	 * @param orgId Organization to get data for
	 * @param surveyQuestionTypeCode
	 *            Code for the type of Survey Questions.
	 * @return the list
	 */
	QueryResult<Map<String, String>> retrieveSurveySummaryForOrg(Long snapshotWindowId, Long orgId,
			String surveyQuestionTypeCode);

	QueryResult<Map<String, String>> retrieveSchoolExceptions(Long snapshotWindowId, Long orgId, String exceptionTypeCode,
			boolean retrieveAllRows, boolean retreiveFullDetails, Integer startingRow, Integer numberOfRows);

	void createSnapshotRollup(Long snapshotWindowId, boolean fullRefresh);

	SnapshotOrg getSnapshotDataForOrg(Long snapshotWindowId, Long orgId, MinimumRecommendedFlag flag);
}
