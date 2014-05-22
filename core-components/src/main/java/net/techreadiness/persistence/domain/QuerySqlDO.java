package net.techreadiness.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

/**
 * The persistent class for the query_sql database table.
 * 
 */
@Entity
@Table(name = "query_sql")
public class QuerySqlDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "query_sql_id", unique = true, nullable = false)
	private Long querySqlId;

	@Column(nullable = false, length = 100)
	private String code;

	@Column(nullable = false, length = 1000)
	private String description;

	@Column(length = 1000)
	private String keywords;

	@Column(nullable = false, length = 200)
	private String name;

	@Lob()
	@Column(name = "sql_text", nullable = false)
	private String sqlText;

	// bi-directional many-to-one association to EntityTypeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_type_id", nullable = false)
	private EntityTypeDO entityType;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id")
	private ScopeDO scope;

	public QuerySqlDO() {
	}

	public Long getQuerySqlId() {
		return querySqlId;
	}

	public void setQuerySqlId(Long querySqlId) {
		this.querySqlId = querySqlId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSqlText() {
		return sqlText;
	}

	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

	public EntityTypeDO getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityTypeDO entityType) {
		this.entityType = entityType;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

}