package net.techreadiness.plugin.service.object;

import java.util.Date;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.plugin.persistence.domain.SnapshotWindowDO;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.Scope;

import com.google.common.base.Objects;

public class SnapshotWindow extends BaseObject<SnapshotWindowDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	private Long snapshotWindowId;
	@CoreField
	private String name;
	@CoreField
	private Boolean visible;
	@CoreField
	private String requestUser;
	@CoreField
	private Date requestDate;
	@CoreField
	private Date executeDate;
	@CoreField
	private Integer calcEnrollmentCountKMedian;
	@CoreField
	private Double calcEnrollmentCountKStddev;
	@CoreField
	private Integer calcEnrollmentCount1Median;
	@CoreField
	private Double calcEnrollmentCount1Stddev;
	@CoreField
	private Integer calcEnrollmentCount2Median;
	@CoreField
	private Double calcEnrollmentCount2Stddev;
	@CoreField
	private Integer calcEnrollmentCount3Median;
	@CoreField
	private Double calcEnrollmentCount3Stddev;
	@CoreField
	private Integer calcEnrollmentCount4Median;
	@CoreField
	private Double calcEnrollmentCount4Stddev;
	@CoreField
	private Integer calcEnrollmentCount5Median;
	@CoreField
	private Double calcEnrollmentCount5Stddev;
	@CoreField
	private Integer calcEnrollmentCount6Median;
	@CoreField
	private Double calcEnrollmentCount6Stddev;
	@CoreField
	private Integer calcEnrollmentCount7Median;
	@CoreField
	private Double calcEnrollmentCount7Stddev;
	@CoreField
	private Integer calcEnrollmentCount8Median;
	@CoreField
	private Double calcEnrollmentCount8Stddev;
	@CoreField
	private Integer calcEnrollmentCount9Median;
	@CoreField
	private Double calcEnrollmentCount9Stddev;
	@CoreField
	private Integer calcEnrollmentCount10Median;
	@CoreField
	private Double calcEnrollmentCount10Stddev;
	@CoreField
	private Integer calcEnrollmentCount11Median;
	@CoreField
	private Double calcEnrollmentCount11Stddev;
	@CoreField
	private Integer calcEnrollmentCount12Median;
	@CoreField
	private Double calcEnrollmentCount12Stddev;

	private Scope scope;

	public SnapshotWindow() { // required by JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("snapshotWindowId", snapshotWindowId).add("scope", scope.getCode())
				.add("name", name).add("visible", visible).add("requestUser", requestUser).add("requestDate", requestDate)
				.add("executeDate", executeDate).add("calcEnrollmentCountKMedian", calcEnrollmentCountKMedian)
				.add("calcEnrollmentCountKStddev", calcEnrollmentCountKStddev)
				.add("calcEnrollmentCount1Median", calcEnrollmentCount1Median)
				.add("calcEnrollmentCount1Stddev", calcEnrollmentCount1Stddev)
				.add("calcEnrollmentCount2Median", calcEnrollmentCount2Median)
				.add("calcEnrollmentCount2Stddev", calcEnrollmentCount2Stddev)
				.add("calcEnrollmentCount3Median", calcEnrollmentCount3Median)
				.add("calcEnrollmentCount3Stddev", calcEnrollmentCount3Stddev)
				.add("calcEnrollmentCount4Median", calcEnrollmentCount4Median)
				.add("calcEnrollmentCount4Stddev", calcEnrollmentCount4Stddev)
				.add("calcEnrollmentCount5Median", calcEnrollmentCount5Median)
				.add("calcEnrollmentCount5Stddev", calcEnrollmentCount5Stddev)
				.add("calcEnrollmentCount6Median", calcEnrollmentCount6Median)
				.add("calcEnrollmentCount6Stddev", calcEnrollmentCount6Stddev)
				.add("calcEnrollmentCount7Median", calcEnrollmentCount7Median)
				.add("calcEnrollmentCount7Stddev", calcEnrollmentCount7Stddev)
				.add("calcEnrollmentCount8Median", calcEnrollmentCount8Median)
				.add("calcEnrollmentCount8Stddev", calcEnrollmentCount8Stddev)
				.add("calcEnrollmentCount9Median", calcEnrollmentCount9Median)
				.add("calcEnrollmentCount9Stddev", calcEnrollmentCount9Stddev)
				.add("calcEnrollmentCount10Median", calcEnrollmentCount10Median)
				.add("calcEnrollmentCount10Stddev", calcEnrollmentCount10Stddev)
				.add("calcEnrollmentCount11Median", calcEnrollmentCount11Median)
				.add("calcEnrollmentCount11Stddev", calcEnrollmentCount11Stddev)
				.add("calcEnrollmentCount12Median", calcEnrollmentCount12Median)
				.add("calcEnrollmentCount12Stddev", calcEnrollmentCount12Stddev).toString();
	}

	public Long getSnapshotWindowId() {
		return snapshotWindowId;
	}

	public void setSnapshotWindowId(Long snapshotWindowId) {
		this.snapshotWindowId = snapshotWindowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getRequestUser() {
		return requestUser;
	}

	public void setRequestUser(String requestUser) {
		this.requestUser = requestUser;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getExecuteDate() {
		return executeDate;
	}

	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Class<SnapshotWindowDO> getBaseEntityType() {
		return SnapshotWindowDO.class;
	}

	@Override
	public Long getId() {
		return snapshotWindowId;
	}

	public Integer getCalcEnrollmentCountKMedian() {
		return calcEnrollmentCountKMedian;
	}

	public void setCalcEnrollmentCountKMedian(Integer calcEnrollmentCountKMedian) {
		this.calcEnrollmentCountKMedian = calcEnrollmentCountKMedian;
	}

	public void setCalcEnrollmentCountKStddev(Double calcEnrollmentCountKStddev) {
		this.calcEnrollmentCountKStddev = calcEnrollmentCountKStddev;
	}

	public Double getCalcEnrollmentCountKStddev() {
		return calcEnrollmentCountKStddev;
	}

	public Integer getCalcEnrollmentCount1Median() {
		return calcEnrollmentCount1Median;
	}

	public void setCalcEnrollmentCount1Median(Integer calcEnrollmentCount1Median) {
		this.calcEnrollmentCount1Median = calcEnrollmentCount1Median;
	}

	public void setCalcEnrollmentCount1Stddev(Double calcEnrollmentCount1Stddev) {
		this.calcEnrollmentCount1Stddev = calcEnrollmentCount1Stddev;
	}

	public Double getCalcEnrollmentCount1Stddev() {
		return calcEnrollmentCount1Stddev;
	}

	public Integer getCalcEnrollmentCount2Median() {
		return calcEnrollmentCount2Median;
	}

	public void setCalcEnrollmentCount2Median(Integer calcEnrollmentCount2Median) {
		this.calcEnrollmentCount2Median = calcEnrollmentCount2Median;
	}

	public void setCalcEnrollmentCount2Stddev(Double calcEnrollmentCount2Stddev) {
		this.calcEnrollmentCount2Stddev = calcEnrollmentCount2Stddev;
	}

	public Double getCalcEnrollmentCount2Stddev() {
		return calcEnrollmentCount2Stddev;
	}

	public Integer getCalcEnrollmentCount3Median() {
		return calcEnrollmentCount3Median;
	}

	public void setCalcEnrollmentCount3Median(Integer calcEnrollmentCount3Median) {
		this.calcEnrollmentCount3Median = calcEnrollmentCount3Median;
	}

	public void setCalcEnrollmentCount3Stddev(Double calcEnrollmentCount3Stddev) {
		this.calcEnrollmentCount3Stddev = calcEnrollmentCount3Stddev;
	}

	public Double getCalcEnrollmentCount3Stddev() {
		return calcEnrollmentCount3Stddev;
	}

	public Integer getCalcEnrollmentCount4Median() {
		return calcEnrollmentCount4Median;
	}

	public void setCalcEnrollmentCount4Median(Integer calcEnrollmentCount4Median) {
		this.calcEnrollmentCount4Median = calcEnrollmentCount4Median;
	}

	public void setCalcEnrollmentCount4Stddev(Double calcEnrollmentCount4Stddev) {
		this.calcEnrollmentCount4Stddev = calcEnrollmentCount4Stddev;
	}

	public Double getCalcEnrollmentCount4Stddev() {
		return calcEnrollmentCount4Stddev;
	}

	public Integer getCalcEnrollmentCount5Median() {
		return calcEnrollmentCount5Median;
	}

	public void setCalcEnrollmentCount5Median(Integer calcEnrollmentCount5Median) {
		this.calcEnrollmentCount5Median = calcEnrollmentCount5Median;
	}

	public void setCalcEnrollmentCount5Stddev(Double calcEnrollmentCount5Stddev) {
		this.calcEnrollmentCount5Stddev = calcEnrollmentCount5Stddev;
	}

	public Double getCalcEnrollmentCount5Stddev() {
		return calcEnrollmentCount5Stddev;
	}

	public Integer getCalcEnrollmentCount6Median() {
		return calcEnrollmentCount6Median;
	}

	public void setCalcEnrollmentCount6Median(Integer calcEnrollmentCount6Median) {
		this.calcEnrollmentCount6Median = calcEnrollmentCount6Median;
	}

	public void setCalcEnrollmentCount6Stddev(Double calcEnrollmentCount6Stddev) {
		this.calcEnrollmentCount6Stddev = calcEnrollmentCount6Stddev;
	}

	public Double getCalcEnrollmentCount6Stddev() {
		return calcEnrollmentCount6Stddev;
	}

	public Integer getCalcEnrollmentCount7Median() {
		return calcEnrollmentCount7Median;
	}

	public void setCalcEnrollmentCount7Median(Integer calcEnrollmentCount7Median) {
		this.calcEnrollmentCount7Median = calcEnrollmentCount7Median;
	}

	public void setCalcEnrollmentCount7Stddev(Double calcEnrollmentCount7Stddev) {
		this.calcEnrollmentCount7Stddev = calcEnrollmentCount7Stddev;
	}

	public Double getCalcEnrollmentCount7Stddev() {
		return calcEnrollmentCount7Stddev;
	}

	public Integer getCalcEnrollmentCount8Median() {
		return calcEnrollmentCount8Median;
	}

	public void setCalcEnrollmentCount8Median(Integer calcEnrollmentCount8Median) {
		this.calcEnrollmentCount8Median = calcEnrollmentCount8Median;
	}

	public void setCalcEnrollmentCount8Stddev(Double calcEnrollmentCount8Stddev) {
		this.calcEnrollmentCount8Stddev = calcEnrollmentCount8Stddev;
	}

	public Double getCalcEnrollmentCount8Stddev() {
		return calcEnrollmentCount8Stddev;
	}

	public Integer getCalcEnrollmentCount9Median() {
		return calcEnrollmentCount9Median;
	}

	public void setCalcEnrollmentCount9Median(Integer calcEnrollmentCount9Median) {
		this.calcEnrollmentCount9Median = calcEnrollmentCount9Median;
	}

	public void setCalcEnrollmentCount9Stddev(Double calcEnrollmentCount9Stddev) {
		this.calcEnrollmentCount9Stddev = calcEnrollmentCount9Stddev;
	}

	public Double getCalcEnrollmentCount9Stddev() {
		return calcEnrollmentCount9Stddev;
	}

	public Integer getCalcEnrollmentCount10Median() {
		return calcEnrollmentCount10Median;
	}

	public void setCalcEnrollmentCount10Median(Integer calcEnrollmentCount10Median) {
		this.calcEnrollmentCount10Median = calcEnrollmentCount10Median;
	}

	public void setCalcEnrollmentCount10Stddev(Double calcEnrollmentCount10Stddev) {
		this.calcEnrollmentCount10Stddev = calcEnrollmentCount10Stddev;
	}

	public Double getCalcEnrollmentCount10Stddev() {
		return calcEnrollmentCount10Stddev;
	}

	public Integer getCalcEnrollmentCount11Median() {
		return calcEnrollmentCount11Median;
	}

	public void setCalcEnrollmentCount11Median(Integer calcEnrollmentCount11Median) {
		this.calcEnrollmentCount11Median = calcEnrollmentCount11Median;
	}

	public void setCalcEnrollmentCount11Stddev(Double calcEnrollmentCount11Stddev) {
		this.calcEnrollmentCount11Stddev = calcEnrollmentCount11Stddev;
	}

	public Double getCalcEnrollmentCount11Stddev() {
		return calcEnrollmentCount11Stddev;
	}

	public Integer getCalcEnrollmentCount12Median() {
		return calcEnrollmentCount12Median;
	}

	public void setCalcEnrollmentCount12Median(Integer calcEnrollmentCount12Median) {
		this.calcEnrollmentCount12Median = calcEnrollmentCount12Median;
	}

	public void setCalcEnrollmentCount12Stddev(Double calcEnrollmentCount12Stddev) {
		this.calcEnrollmentCount12Stddev = calcEnrollmentCount12Stddev;
	}

	public Double getCalcEnrollmentCount12Stddev() {
		return calcEnrollmentCount12Stddev;
	}

}