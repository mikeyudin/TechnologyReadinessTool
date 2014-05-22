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

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the view_def_text database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "view_def_text")
public class ViewDefTextDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "view_def_text_id")
	private int viewDefTextId;

	@Column(name = "column_number")
	private int columnNumber;

	@Column(name = "display_order")
	private int displayOrder;

	private String text;

	// bi-directional many-to-one association to ViewDefDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "view_def_id")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ViewDefDO viewDef;

	public ViewDefTextDO() {
	}

	public int getViewDefTextId() {
		return viewDefTextId;
	}

	public void setViewDefTextId(int viewDefTextId) {
		this.viewDefTextId = viewDefTextId;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ViewDefDO getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDefDO viewDef) {
		this.viewDef = viewDef;
	}

}