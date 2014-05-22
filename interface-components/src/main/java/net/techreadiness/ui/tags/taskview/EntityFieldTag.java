package net.techreadiness.ui.tags.taskview;

import java.util.Comparator;

import net.techreadiness.ui.tags.BaseTag;
import net.techreadiness.ui.tags.ViewFieldDisplayOrderComparator;
import net.techreadiness.ui.tags.ViewFieldTag;

public class EntityFieldTag extends BaseTag implements ViewFieldTag {
	private static final Comparator<ViewFieldTag> comparator = new ViewFieldDisplayOrderComparator();
	private String name;
	private String description;
	private String nameKey;
	private String code;
	private String defaultSortDirection;
	private String displayOrder;
	private String sortColumns;
	private boolean sortable;
	private Integer pageOrder;
	private boolean displayInGrid = true;
	private boolean displayInDetail = true;
	private boolean grouped;
	private boolean required;

	@Override
	public String execute() {

		name = evaluateOgnl(name);
		code = evaluateOgnl(code);
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public String getDefaultSortDirection() {
		return defaultSortDirection;
	}

	public void setDefaultSortDirection(String defaultSortDirection) {
		this.defaultSortDirection = defaultSortDirection;
	}

	public String getSortColumns() {
		return sortColumns;
	}

	public void setSortColumns(String sortColumns) {
		this.sortColumns = sortColumns;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	@Override
	public String getDisplayOrder() {
		return displayOrder;
	}

	@Override
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int compareTo(ViewFieldTag o) {
		return comparator.compare(this, o);
	}

	@Override
	public Integer getPageOrder() {
		return pageOrder;
	}

	@Override
	public void setPageOrder(Integer pageOrder) {
		this.pageOrder = pageOrder;
	}

	public boolean isDisplayInGrid() {
		return displayInGrid;
	}

	public void setDisplayInGrid(boolean displayInGrid) {
		this.displayInGrid = displayInGrid;
	}

	public boolean isDisplayInDetail() {
		return displayInDetail;
	}

	public void setDisplayInDetail(boolean displayInDetail) {
		this.displayInDetail = displayInDetail;
	}

	public boolean isGrouped() {
		return grouped;
	}

	public void setGrouped(boolean grouped) {
		this.grouped = grouped;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
