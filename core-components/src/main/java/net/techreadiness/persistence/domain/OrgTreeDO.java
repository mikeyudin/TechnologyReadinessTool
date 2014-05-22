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

/**
 * The persistent class for the org_tree database table.
 * 
 */
@Entity
@Table(name = "org_tree")
public class OrgTreeDO extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "org_tree_id", unique = true, nullable = false)
	private Long orgTreeId;

	@Column(name = "ancestor_path", nullable = false, length = 2000)
	private String ancestorPath;

	@Column(nullable = false)
	private short depth;

	@Column(nullable = false)
	private short distance;

	@Column(nullable = false, length = 2000)
	private String path;

	// bi-directional many-to-one association to OrgDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = false)
	private OrgDO org;

	// bi-directional many-to-one association to OrgDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ancestor_org_id")
	private OrgDO ancestorOrg;

	public OrgTreeDO() {
	}

	public Long getOrgTreeId() {
		return orgTreeId;
	}

	public void setOrgTreeId(Long orgTreeId) {
		this.orgTreeId = orgTreeId;
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

	public OrgDO getOrg() {
		return org;
	}

	public void setOrg(OrgDO org) {
		this.org = org;
	}

	public OrgDO getAncestorOrg() {
		return ancestorOrg;
	}

	public void setAncestorOrg(OrgDO ancestorOrg) {
		this.ancestorOrg = ancestorOrg;
	}

}