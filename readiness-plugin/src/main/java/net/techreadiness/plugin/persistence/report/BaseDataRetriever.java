package net.techreadiness.plugin.persistence.report;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;

public interface BaseDataRetriever {

	String ZERO = "0";
	String ZERO_PERCENT = "0%";
	String TOTAL_NUMBER_DEVICES = "deviceCount";
	String PASSING_DEVICE_COUNT = "devicePassingCount";
	String PERCENT_PASSING_DEVICES = "devicePassingPercent";
	String PERCENT_TESTABLE_STUDENTS = "testTakerPercentStudentsTestable";
	String TIME_REQUIRED_TO_DOWNLAD_TEST = "networkTimeToDownloadTest";

	String COMPLETION_STATUS = "completionStatus";
	String PERCENT_COMPLETE = "percentComplete";

	/**
	 * Finds number of rows that will be returned for a given report if there were no pagination involved. This is done by
	 * determining the number of Child Organizations that belong to a parent organization. As all the reports are access thru
	 * a parent organization, this approach will will.
	 * 
	 * @param districtOrg
	 *            the district org
	 * @param scope
	 *            the scope
	 * @param snapshotWindow
	 *            the window for which the snapshot is to be completed
	 */
	Integer findTotalCount(OrgDO org, ScopeDO scope);

	String formatString(String propertyName, Object value);

	Object formatTime(Object value);

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
	 * @param rootOrg
	 *            The Root Org for Readiness.
	 * @param consortiumScope
	 *            The scope that is tied to the Consortium. This is used to retrieve the organizations that belong to a
	 *            consortium.
	 * @param window
	 *            The window for which the percent of passing devices is desired.
	 * @return The Map of data that contains the information about State Device Assessment data.
	 * @throws SQLException
	 *             the sQL exception
	 */
	QueryResult<Map<String, String>> getSnapshotReportDataForOrg(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag) throws SQLException;

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
	 * @param parentOrgDO
	 *            the parent org for which to get the child report data for.
	 * @param snapshotWindowDO
	 *            the snapshot window for which to retrieve the data from.
	 * @return The Map of data that contains the information about Device Assessment data.
	 * @throws SQLException
	 *             the SQL exception
	 */
	QueryResult<Map<String, String>> findSnapshotReportDataForChildOrgs(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows)
			throws SQLException;

	QueryResult<Map<String, String>> findSnapshotReportDataForDescendantOrgs(Long snapshotWindowId, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow, Integer numberOfRows,
			Integer orgTreeDistance) throws SQLException;

	QueryResult<Map<String, String>> findProgressSnapshotReportDataForChildOrgs(Collection<Long> snapshotWindowIds,
			Long orgId, MinimumRecommendedFlag minimumOrRecommendedFlag, boolean retrieveAll, Integer startingRow,
			Integer numberOfRows) throws SQLException;

	QueryResult<Map<String, String>> findProgressSnapshotReportDataForOrg(Collection<Long> snapshotWindowIds, Long orgId,
			MinimumRecommendedFlag minimumOrRecommendedFlag) throws SQLException;

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
	 * 
	 * @param consortiumScope
	 *            The scope that is tied to the Consortium. Minimum recommended values are stored by consortium scope. a
	 *            consortium.
	 * @param normalSandBoxMode
	 *            Indicates to see if this is for normal usage or sandbox usage.
	 * @return The Map of data that contains the information about District Level Device Assessment data.
	 * @throws SQLException
	 *             the sQL exception
	 */
	List<Map<String, String>> findMinimumRecommendedValues(Long snapshotWindowId) throws SQLException;

	/**
	 * Method to create the snapshot summary rollup information for a particular snapshot window id. This may be either the
	 * nightly rollup, or an adhoc request (they are treated the same).
	 * 
	 * @param snapshotWindowId
	 *            The id of the snapshot window to for which to create the rollup data.
	 */
	void createSnapshotRollup(Long snapshotWindowId, boolean fullRefresh);

	QueryResult<Map<String, String>> retrieveSchoolExceptions(Long snapshotWindowId, Long orgId, String exceptionTypeCode,
			boolean retrieveAllRows, boolean retrieveFullDetails, Integer startingRow, Integer numberOfRows)
			throws SQLException;
}
