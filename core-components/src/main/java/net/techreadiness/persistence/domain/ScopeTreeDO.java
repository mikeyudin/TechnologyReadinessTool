package net.techreadiness.persistence.domain;

import java.io.Serializable;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the scope_tree database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "scope_tree")
public class ScopeTreeDO extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "scope_tree_id", unique = true, nullable = false)
	private Long scopeTreeId;

	@Column(name = "ancestor_path", nullable = false, length = 2000)
	private String ancestorPath;

	@Column(name = "ancestor_depth", nullable = false)
	private short ancestorDepth;

	@Column(nullable = false)
	private short depth;

	@Column(nullable = false)
	private short distance;

	@Column(nullable = false, length = 2000)
	private String path;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO scope;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ancestor_scope_id")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO ancestorScope;

	public ScopeTreeDO() {
	}

	public Long getScopeTreeId() {
		return scopeTreeId;
	}

	public void setScopeTreeId(Long scopeTreeId) {
		this.scopeTreeId = scopeTreeId;
	}

	public String getAncestorPath() {
		return ancestorPath;
	}

	public void setAncestorPath(String ancestorPath) {
		this.ancestorPath = ancestorPath;
	}

	public short getDepth() {
		return depth;
	}

	public void setDepth(short depth) {
		this.depth = depth;
	}

	public short getDistance() {
		return distance;
	}

	public void setDistance(short distance) {
		this.distance = distance;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public ScopeDO getAncestorScope() {
		return ancestorScope;
	}

	public void setAncestorScope(ScopeDO ancestorScope) {
		this.ancestorScope = ancestorScope;
	}

	public short getAncestorDepth() {
		return ancestorDepth;
	}

	public void setAncestorDepth(short ancestorDepth) {
		this.ancestorDepth = ancestorDepth;
	}

}