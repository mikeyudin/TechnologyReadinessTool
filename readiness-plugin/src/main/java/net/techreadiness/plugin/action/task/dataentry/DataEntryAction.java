package net.techreadiness.plugin.action.task.dataentry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.plugin.service.object.SnapshotOrg;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.DeviceService;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.task.org.dataentry.DataEntryTaskFlowAction;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", location = "/net/techreadiness/plugin/action/org/dataentry/dataEntry.jsp"),
		@Result(name = "noorg", location = "/net/techreadiness/plugin/action/org/dataentry/noorg.jsp") })
public class DataEntryAction extends DataEntryTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;
	private Multimap<Long, String> dataEntryErrors;
	private Multimap<Long, String> abnormalMessages;
	private SnapshotWindow snapshot;
	@Key(Long.class)
	@Element(Boolean.class)
	private Map<Long, Boolean> dataEntryComplete;

	@Inject
	private SnapshotWindowService snapshotService;
	@Inject
	private DeviceService deviceService;
	@Inject
	private ReportsService reportsService;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_NETWORK_INFRASTRUCTURE })
	@Override
	public String execute() {
		snapshot = snapshotService.getByScopeIdAndName(getServiceContext(), getServiceContext().getScopeId(),
				ReportsService.DEFAULT_SNAPSHOT_WINDOW);
		for (Org org : getTaskFlowData().getOrgs()) {
			if (StringUtils.isBlank(org.getInternetSpeed())) {
				dataEntryErrors.put(org.getOrgId(), "Estimated Internet Bandwidth");
			}
			if (StringUtils.isBlank(org.getInternetUtilization())) {
				dataEntryErrors.put(org.getOrgId(), "Estimated Internet Bandwidth Utilization");
			} else {
				int internetBandwidth = Integer.parseInt(org.getInternetUtilization());
				if (internetBandwidth >= 90) {
					abnormalMessages.put(org.getOrgId(), "Internet Bandwidth utilization is greater than or equal to 90%");
				} else {
					if (internetBandwidth <= 5) {
						abnormalMessages.put(org.getOrgId(), "Internet Bandwidth utilization is less than or equal to 5%");
					}
				}
			}
			if (StringUtils.isBlank(org.getNetworkSpeed())) {
				dataEntryErrors.put(org.getOrgId(), "Estimated Internal Network Bandwidth");
			}
			if (StringUtils.isBlank(org.getNetworkUtilization())) {
				dataEntryErrors.put(org.getOrgId(), "Estimated Internal Network Bandwidth Utilization");
			} else {
				int networkBandwidth = Integer.parseInt(org.getNetworkUtilization());
				if (networkBandwidth >= 90) {
					abnormalMessages.put(org.getOrgId(),
							"Internal Network Bandwidth utilization is greater than or equal to 90%");
				} else {
					if (networkBandwidth <= 5) {
						abnormalMessages.put(org.getOrgId(),
								"Internal Network Bandwidth utilization is less than or equal to 5%");
					}
				}
			}
			if (StringUtils.isBlank(org.getTestingWindowLength())) {
				dataEntryErrors.put(org.getOrgId(), "Length of Testing Window in School Days");
			}

			if (StringUtils.isBlank(org.getSessionsPerDay())) {
				dataEntryErrors.put(org.getOrgId(), "SessionsPerDay");
			} else {
				int sessionsPerDay = Integer.parseInt(org.getSessionsPerDay());
				if (sessionsPerDay > 4) {
					abnormalMessages.put(org.getOrgId(), "School has 5 or more sessions per day");
				}
			}

			int enrollmentCountTotal = getAnsweredEnrollmentCount(org);
			if (enrollmentCountTotal == 0) {
				dataEntryErrors.put(org.getOrgId(), "At least one grade must have a non-zero enrollment count");
			}

			Collection<Device> devices = deviceService.findByOrgId(getServiceContext(), org.getOrgId());

			if (devices.size() == 0) {
				abnormalMessages.put(org.getOrgId(), "School has not provided device data");
			}

			for (Device device : devices) {
				if (device.getOperatingSystem() == null) {
					dataEntryErrors.put(org.getOrgId(), "Operating System is required");
				}
				if (device.getMemory() == null) {
					dataEntryErrors.put(org.getOrgId(), "Memory is required");
				}
				if (device.getScreenResolution() == null) {
					dataEntryErrors.put(org.getOrgId(), "Screen Resolution is required");
				}
				if (device.getMonitorDisplaySize() == null) {
					dataEntryErrors.put(org.getOrgId(), "Monitor / Display Size is required");
				}
				if (device.getEnvironment() == null) {
					dataEntryErrors.put(org.getOrgId(), "Assessment Environment is required");
				}
			}

			SnapshotOrg snapshotOrg = reportsService.getSnapshotDataForOrg(snapshot.getSnapshotWindowId(), org.getOrgId(),
					MinimumRecommendedFlag.MINIMUM);
			if (snapshotOrg != null) {
				String studentCount = snapshotOrg.getTestingStudentCount();
				String totalDeviceCount = snapshotOrg.getDeviceCount();
				if (studentCount != null
						&& !(studentCount.equals("(missing)") && totalDeviceCount != null && !totalDeviceCount
								.equals("(missing)"))) {
					int testStudentCountDoubled = NumberUtils.toInt(studentCount) * 2;
					if (NumberUtils.toInt(totalDeviceCount) > testStudentCountDoubled) {
						abnormalMessages.put(org.getOrgId(), "Device-to-test-taker ratio is greater than 2:1");
					}
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
				abnormalMessages.put(org.getOrgId(), sbd.toString());
			}
		}
		return SUCCESS;
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

	@Override
	public void prepare() throws Exception {
		dataEntryErrors = HashMultimap.create();
		abnormalMessages = HashMultimap.create();
		dataEntryComplete = new HashMap<>();
	}

	public Multimap<Long, String> getDataEntryErrors() {
		return dataEntryErrors;
	}

	public Multimap<Long, String> getAbnormalMessages() {
		return abnormalMessages;
	}

	public Map<Long, Boolean> getDataEntryComplete() {
		return dataEntryComplete;
	}
}
