package net.techreadiness.plugin.datagrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.dao.DeviceDAO;
import net.techreadiness.persistence.domain.DeviceDO;
import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.BaseService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Org;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Component
@Scope("prototype")
@Transactional(readOnly = true)
public class DataEntryCompleteItemProviderImpl implements DataEntryCompleteItemProvider, BaseService {

	private Collection<Org> orgs;

	@Inject
	private DeviceDAO deviceDAO;
	@Inject
	private ReportsService reportsService;
	@Inject
	private SnapshotWindowService snapshotWindowService;

	private SnapshotWindow snapshot;
	protected MinimumRecommendedFlag minimumRecommendedFlag;

	private ServiceContext serviceContext;

	@Override
	public List<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		List<Map<String, String>> orgMaps = Lists.newArrayList();
		for (Org org : orgs) {
			Map<String, String> map = new HashMap<>();
			map.putAll(org.getAsMap());
			map.putAll(org.getExtendedAttributes());
			if (org.getParentOrgId() != null) {
				map.put("parentOrgId", org.getParentOrgId().toString());
				map.put("parentOrgName", org.getParentOrgName());
			}
			Collection<String> errors = new HashSet<>();
			Collection<String> abnormalMessages = new ArrayList<>();

			// add the error messages and the atypical data messages for displaying on the Mark Data Entry Complete page //

			if (StringUtils.isBlank(org.getInternetSpeed())) {
				errors.add("Estimated Internet Bandwidth");
			}
			if (StringUtils.isBlank(org.getInternetUtilization())) {
				errors.add("Estimated Internet Bandwidth Utilization");
			} else {
				int internetBandwidth = 0;
				internetBandwidth = Integer.parseInt(org.getInternetUtilization());
				if (internetBandwidth >= 90) {
					abnormalMessages.add("Internet Bandwidth utilization is greater than or equal to 90%");
				} else {
					if (internetBandwidth <= 5) {
						abnormalMessages.add("Internet Bandwidth utilization is less than or equal to 5%");
					}
				}
			}
			if (StringUtils.isBlank(org.getNetworkSpeed())) {
				errors.add("Estimated Internal Network Bandwidth");
			}
			if (StringUtils.isBlank(org.getNetworkUtilization())) {
				errors.add("Estimated Internal Network Bandwidth Utilization");
			} else {
				int networkBandwidth = 0;
				networkBandwidth = Integer.parseInt(org.getNetworkUtilization());
				if (networkBandwidth >= 90) {
					abnormalMessages.add("Internal Network Bandwidth utilization is greater than or equal to 90%");
				} else {
					if (networkBandwidth <= 5) {
						abnormalMessages.add("Internal Network Bandwidth utilization is less than or equal to 5%");
					}
				}
			}
			if (StringUtils.isBlank(org.getTestingWindowLength())) {
				errors.add("Length of Testing Window in School Days");
			}

			if (StringUtils.isBlank(org.getSessionsPerDay())) {
				errors.add("SessionsPerDay");
			} else {
				int sessionsPerDay = Integer.parseInt(org.getSessionsPerDay());
				if (sessionsPerDay > 4) {
					abnormalMessages.add("School has 5 or more sessions per day");
				}
			}

			int enrollmentCountTotal = getAnsweredEnrollmentCount(org);
			if (enrollmentCountTotal == 0) {
				errors.add("At least one grade must have a non-zero enrollment count");
			}

			List<DeviceDO> deviceDOs = deviceDAO.findByOrgId(org.getOrgId());

			if (deviceDOs.size() == 0) {
				abnormalMessages.add("School has not provided device data");
			}

			for (DeviceDO deviceDO : deviceDOs) {
				if (deviceDO.getOperatingSystem() == null) {
					errors.add("Operating System is required");
				}
				if (deviceDO.getMemory() == null) {
					errors.add("Memory is required");
				}
				if (deviceDO.getScreenResolution() == null) {
					errors.add("Screen Resolution is required");
				}
				if (deviceDO.getMonitorDisplaySize() == null) {
					errors.add("Monitor / Display Size is required");
				}
				if (deviceDO.getEnvironment() == null) {
					errors.add("Assessment Environment is required");
				}
			}

			// we must retrieve the current snapshot in order to determine Device-to-Test-Taker Ratio > 2:1 and Enrollment
			// Count per Grade > 4 standard deviations from the median enrollment//

			snapshot = snapshotWindowService.getByScopeIdAndName(serviceContext, serviceContext.getScopeId(),
					ReportsService.DEFAULT_SNAPSHOT_WINDOW);
			QueryResult<Map<String, String>> minResult = reportsService.retrieveSummaryForOrg(
					snapshot.getSnapshotWindowId(), org.getOrgId(), MinimumRecommendedFlag.MINIMUM);
			Collection<Map<String, String>> results = minResult.getRows();

			for (Map<String, String> snapshotMap : results) {
				String studentCount = snapshotMap.get("testingStudentCount");
				String totalDeviceCount = snapshotMap.get("deviceCount");
				if (!(studentCount == null)
						&& !(studentCount.equals("(missing)") && !(totalDeviceCount == null) && !totalDeviceCount
								.equals("(missing)"))) {
					int testStudentCountDoubled = NumberUtils.toInt(studentCount) * 2;
					if (NumberUtils.toInt(totalDeviceCount) > testStudentCountDoubled) {
						abnormalMessages.add("Device-to-test-taker ratio is greater than 2:1");
					}
				}

				StringBuilder sbd = new StringBuilder();
				sbd.append("High grade level enrollment in the following grades:");
				Collection<String> highEnrollment = new HashSet<>();

				if (hasHighEnrollment(org.getEnrollmentCountK(), snapshot.getCalcEnrollmentCountKMedian(),
						snapshot.getCalcEnrollmentCountKStddev())) {
					highEnrollment.add("K");
				}
				if (hasHighEnrollment(org.getEnrollmentCount1(), snapshot.getCalcEnrollmentCount1Median(),
						snapshot.getCalcEnrollmentCount1Stddev())) {
					highEnrollment.add("1");
				}
				if (hasHighEnrollment(org.getEnrollmentCount2(), snapshot.getCalcEnrollmentCount2Median(),
						snapshot.getCalcEnrollmentCount2Stddev())) {
					highEnrollment.add("2");
				}
				if (hasHighEnrollment(org.getEnrollmentCount3(), snapshot.getCalcEnrollmentCount3Median(),
						snapshot.getCalcEnrollmentCount3Stddev())) {
					highEnrollment.add("3");
				}
				if (hasHighEnrollment(org.getEnrollmentCount4(), snapshot.getCalcEnrollmentCount4Median(),
						snapshot.getCalcEnrollmentCount4Stddev())) {
					highEnrollment.add("4");
				}
				if (hasHighEnrollment(org.getEnrollmentCount5(), snapshot.getCalcEnrollmentCount5Median(),
						snapshot.getCalcEnrollmentCount5Stddev())) {
					highEnrollment.add("5");
				}
				if (hasHighEnrollment(org.getEnrollmentCount6(), snapshot.getCalcEnrollmentCount6Median(),
						snapshot.getCalcEnrollmentCount6Stddev())) {
					highEnrollment.add("6");
				}
				if (hasHighEnrollment(org.getEnrollmentCount7(), snapshot.getCalcEnrollmentCount7Median(),
						snapshot.getCalcEnrollmentCount7Stddev())) {
					highEnrollment.add("7");
				}
				if (hasHighEnrollment(org.getEnrollmentCount8(), snapshot.getCalcEnrollmentCount8Median(),
						snapshot.getCalcEnrollmentCount8Stddev())) {
					highEnrollment.add("8");
				}
				if (hasHighEnrollment(org.getEnrollmentCount9(), snapshot.getCalcEnrollmentCount9Median(),
						snapshot.getCalcEnrollmentCount9Stddev())) {
					highEnrollment.add("9");
				}
				if (hasHighEnrollment(org.getEnrollmentCount10(), snapshot.getCalcEnrollmentCount10Median(),
						snapshot.getCalcEnrollmentCount10Stddev())) {
					highEnrollment.add("10");
				}
				if (hasHighEnrollment(org.getEnrollmentCount11(), snapshot.getCalcEnrollmentCount11Median(),
						snapshot.getCalcEnrollmentCount11Stddev())) {
					highEnrollment.add("11");
				}
				if (hasHighEnrollment(org.getEnrollmentCount12(), snapshot.getCalcEnrollmentCount12Median(),
						snapshot.getCalcEnrollmentCount12Stddev())) {
					highEnrollment.add("12");
				}

				if (!highEnrollment.isEmpty()) {
					for (String grade : highEnrollment) {
						sbd.append(" ");
						sbd.append(grade);
					}
					abnormalMessages.add(sbd.toString());
				}
			}

			int count = 1;
			for (String error : errors) {
				map.put("error" + count, error);
			}
			map.put("hasErrors", String.valueOf(!errors.isEmpty()));

			count = 1;
			for (String abnormalMessage : abnormalMessages) {
				map.put("Atypical" + count, abnormalMessage);
			}
			map.put("hasAtypicalData", String.valueOf(!abnormalMessages.isEmpty()));

			map.put("orgTypeId", org.getOrgTypeId().toString());
			map.put("orgTypeName", org.getOrgTypeName());
			map.put("hasDevices", String.valueOf(org.isOrgTypeAllowDevice()));
			orgMaps.add(map);
		}
		return orgMaps;
	}

	private static boolean hasHighEnrollment(String enrollment, Integer median, Double stdDev) {
		if (enrollment != null && !"(missing)".equals(enrollment) && median != null && stdDev != null) {
			int count = NumberUtils.toInt(enrollment);
			if (count > median.intValue() + stdDev.doubleValue() * 4) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Map<String, String>> grid) {
		return orgs.size();
	}

	@Override
	public void setOrgs(Collection<Org> orgs) {
		this.orgs = orgs;
	}

	private static int getAnsweredEnrollmentCount(Org org) {
		int count = (StringUtils.isBlank(org.getEnrollmentCountK()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount1()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount2()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount3()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount4()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount5()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount6()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount7()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount8()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount9()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount10()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount11()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount12()) ? 0 : 1);
		return count;
	}

	public void setSnapshotWindow(SnapshotWindow snapshotWindow) {
		snapshot = snapshotWindow;
	}

}
