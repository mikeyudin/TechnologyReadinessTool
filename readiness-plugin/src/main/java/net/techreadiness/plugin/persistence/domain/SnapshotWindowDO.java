package net.techreadiness.plugin.persistence.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.persistence.ServiceObjectMapped;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.object.BaseObject;

import com.google.common.base.Objects;

/**
 * The persistent class for the snapshot_window database table.
 * 
 */
@Entity
@Table(name = "readiness.snapshot_window")
public class SnapshotWindowDO extends BaseEntity implements Serializable, ServiceObjectMapped {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "snapshot_window_id")
	private Long snapshotWindowId;

	// bi-directional many-to-one association to OrgPart
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id")
	private ScopeDO scope;

	@Column(name = "name")
	private String name;

	@Column(name = "visible")
	private Boolean visible;

	@Column(name = "request_user")
	private String requestUser;

	@Column(name = "request_date")
	private Date requestDate;

	@Column(name = "execute_date")
	private Date executeDate;

	@Column(name = "calc_enrollment_countk_median")
	private Integer calcEnrollmentCountKMedian;

	@Column(name = "calc_enrollment_countk_stddev")
	private Double calcEnrollmentCountKStddev;

	@Column(name = "calc_enrollment_count1_median")
	private Integer calcEnrollmentCount1Median;

	@Column(name = "calc_enrollment_count1_stddev")
	private Double calcEnrollmentCount1Stddev;

	@Column(name = "calc_enrollment_count2_median")
	private Integer calcEnrollmentCount2Median;

	@Column(name = "calc_enrollment_count2_stddev")
	private Double calcEnrollmentCount2Stddev;

	@Column(name = "calc_enrollment_count3_median")
	private Integer calcEnrollmentCount3Median;

	@Column(name = "calc_enrollment_count3_stddev")
	private Double calcEnrollmentCount3Stddev;

	@Column(name = "calc_enrollment_count4_median")
	private Integer calcEnrollmentCount4Median;

	@Column(name = "calc_enrollment_count4_stddev")
	private Double calcEnrollmentCount4Stddev;

	@Column(name = "calc_enrollment_count5_median")
	private Integer calcEnrollmentCount5Median;

	@Column(name = "calc_enrollment_count5_stddev")
	private Double calcEnrollmentCount5Stddev;

	@Column(name = "calc_enrollment_count6_median")
	private Integer calcEnrollmentCount6Median;

	@Column(name = "calc_enrollment_count6_stddev")
	private Double calcEnrollmentCount6Stddev;

	@Column(name = "calc_enrollment_count7_median")
	private Integer calcEnrollmentCount7Median;

	@Column(name = "calc_enrollment_count7_stddev")
	private Double calcEnrollmentCount7Stddev;

	@Column(name = "calc_enrollment_count8_median")
	private Integer calcEnrollmentCount8Median;

	@Column(name = "calc_enrollment_count8_stddev")
	private Double calcEnrollmentCount8Stddev;

	@Column(name = "calc_enrollment_count9_median")
	private Integer calcEnrollmentCount9Median;

	@Column(name = "calc_enrollment_count9_stddev")
	private Double calcEnrollmentCount9Stddev;

	@Column(name = "calc_enrollment_count10_median")
	private Integer calcEnrollmentCount10Median;

	@Column(name = "calc_enrollment_count10_stddev")
	private Double calcEnrollmentCount10Stddev;

	@Column(name = "calc_enrollment_count11_median")
	private Integer calcEnrollmentCount11Median;

	@Column(name = "calc_enrollment_count11_stddev")
	private Double calcEnrollmentCount11Stddev;

	@Column(name = "calc_enrollment_count12_median")
	private Integer calcEnrollmentCount12Median;

	@Column(name = "calc_enrollment_count12_stddev")
	private Double calcEnrollmentCount12Stddev;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", name).add("snapshotWindowId", snapshotWindowId)
				.add("visible", visible).add("requestUser", requestUser).add("requestDate", requestDate)
				.add("executeDate", executeDate).add("scope", scope).toString();
	}

	public Long getSnapshotWindowId() {
		return snapshotWindowId;
	}

	public void setSnapshotWindowId(Long snapshotWindowId) {
		this.snapshotWindowId = snapshotWindowId;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (scope == null ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SnapshotWindowDO other = (SnapshotWindowDO) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (scope == null) {
			if (other.scope != null) {
				return false;
			}
		} else if (!scope.equals(other.scope)) {
			return false;
		}
		return true;
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return SnapshotWindow.class;
	}
}