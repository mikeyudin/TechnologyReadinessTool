package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.techreadiness.persistence.BaseEntity;

/**
 * The persistent class for the generic_hist database table.
 * 
 */
@Entity
@Table(name = "generic_hist")
public class GenericHistDO extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "generic_hist_id", unique = true, nullable = false)
	private String genericHistId;

	@Column(name = "change_date")
	private Timestamp changeDate;

	@Column(name = "change_type", length = 1)
	private String changeType;

	@Column(name = "change_user", length = 100)
	private String changeUser;

	@Column(name = "column_name", nullable = false, length = 64)
	private String columnName;

	@Column(name = "new_value", length = 30000)
	private String newValue;

	@Column(name = "old_value", length = 30000)
	private String oldValue;

	@Column(name = "primary_key", nullable = false)
	private int primaryKey;

	@Column(name = "table_name", nullable = false, length = 64)
	private String tableName;

	public GenericHistDO() {
	}

	public String getGenericHistId() {
		return genericHistId;
	}

	public void setGenericHistId(String genericHistId) {
		this.genericHistId = genericHistId;
	}

	public Timestamp getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Timestamp changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangeUser() {
		return changeUser;
	}

	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}