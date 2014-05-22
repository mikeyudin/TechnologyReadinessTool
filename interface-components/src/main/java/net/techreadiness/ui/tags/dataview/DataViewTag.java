package net.techreadiness.ui.tags.dataview;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspException;

import net.techreadiness.service.common.DataGridColumn;
import net.techreadiness.ui.tags.ParentTag;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.tags.datagrid.DataGridTag;
import net.techreadiness.ui.task.TaskFlowDefinition;
import net.techreadiness.util.NumericStringComparator;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DataViewTag extends ParentTag {
	private List<DataViewControlTag> dataViewControls;
	private DataGridState<?> dataGrid;
	private ShoppingCartTag shoppingCart;
	private DataGridTag<? super Object> dataGridTag;
	private String dataGridName;
	private String title;
	private TaskFlowDefinition taskFlow;
	private List<DataGridColumn> displayedFilters;
	private List<DataGridColumn> hiddenFilters;
	private boolean filterSelectable;
	private DataViewFiltersTag dataViewFilters;

	public DataViewTag() {
		displayedFilters = Lists.newArrayList();
		hiddenFilters = Lists.newArrayList();
	}

	public class SelectedItem implements Comparable<SelectedItem> {
		String representation;
		String text;

		public SelectedItem(String representation, String text) {
			this.representation = representation;
			this.text = text;
		}

		public String getRepresentation() {
			return representation;
		}

		public String getText() {
			return text;
		}

		@Override
		public int compareTo(SelectedItem o) {
			return NumericStringComparator.compareStrings(text, o.getText());
		}
	}

	@Override
	public String execute() throws Exception {
		dataViewControls = getChildren(DataViewControlTag.class);
		shoppingCart = Iterables.getFirst(getChildren(ShoppingCartTag.class), null);
		dataViewFilters = Iterables.getFirst(getChildren(DataViewFiltersTag.class), null);
		dataGridTag = Iterables.getOnlyElement(getChildren(DataGridTag.class));
		dataGrid = (DataGridState<?>) getValueStack().findValue(dataGridTag.getValue());
		dataGridName = dataGridTag.getValue();

		if (title != null) {
			title = evaluateOgnl(title);
		}

		/*
		 * Rows are not marked as selected or de-selected until the tag iterates over each row in the grid. Since the
		 * selected items comes before the grid in the JSP the grid must be executed so the correct state can be rendered.
		 */
		dataGridTag.execute();

		if (dataViewFilters != null) {
			dataViewFilters.execute();
		}
		for (DataGridColumn column : dataGridTag.getAllColumns()) {
			if (displayFilter(column)) {
				getDisplayedFilters().add(column);
			} else {
				getHiddenFilters().add(column);
			}

		}

		return "/dataView/dataView.jsp";
	}

	private boolean displayFilter(DataGridColumn column) {
		if (dataGrid == null || dataGrid.getDisplayedFilters() == null) {
			return true;
		}
		for (String filter : dataGrid.getDisplayedFilters()) {
			if (filter.equals(column.getCode())) {
				return true;
			}
		}
		return false;
	}

	public boolean isLeftSidebarDisplayed() {
		return dataGrid != null || dataViewControls != null;
	}

	public List<SelectedItem> getSelectedItems() throws JspException, IOException {
		List<SelectedItem> selectedItems = Lists.newArrayList();
		if (shoppingCart != null) {
			for (Object item : dataGrid.getSelectedItems()) {
				StringWriter writer = new StringWriter();
				try {
					getValueStack().push(item);
					shoppingCart.writeBody(writer);
				} finally {
					getValueStack().pop();
				}
				String repr = dataGridTag.getRowId(item);
				selectedItems.add(new SelectedItem(repr, StringEscapeUtils.unescapeHtml4(writer.toString())));
			}
			Collections.sort(selectedItems);
		}
		return selectedItems;
	}

	public boolean isRightSidebarDisplayed() {
		return shoppingCart != null;
	}

	public List<DataViewControlTag> getDataViewControls() {
		return dataViewControls;
	}

	public DataGridState<?> getDataGrid() {
		return dataGrid;
	}

	public ShoppingCartTag getShoppingCart() {
		return shoppingCart;
	}

	public DataGridTag<?> getDataGridTag() {
		return dataGridTag;
	}

	public String getDataGridName() {
		return dataGridName;
	}

	public TaskFlowDefinition getTaskFlow() {
		return taskFlow;
	}

	public void setTaskFlow(TaskFlowDefinition taskFlow) {
		this.taskFlow = taskFlow;
	}

	public List<DataGridColumn> getDisplayedFilters() {
		return displayedFilters;
	}

	public void setDisplayedFilters(List<DataGridColumn> displayedFilters) {
		this.displayedFilters = displayedFilters;
	}

	public List<DataGridColumn> getHiddenFilters() {
		return hiddenFilters;
	}

	public void setHiddenFilters(List<DataGridColumn> hiddenFilters) {
		this.hiddenFilters = hiddenFilters;
	}

	public boolean dataFiltered(String id) {
		return dataGrid.getFilters().asMap().containsKey(id);
	}

	public boolean getFilterSelectable() {
		return filterSelectable;
	}

	public void setFilterSelectable(boolean filterSelectable) {
		this.filterSelectable = filterSelectable;
	}

	public DataViewFiltersTag getDataViewFilters() {
		return dataViewFilters;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
