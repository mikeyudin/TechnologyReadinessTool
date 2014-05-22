package net.techreadiness.service.common;

import java.util.Collection;

public interface DataGridItemProvider<T> {
	/**
	 * Get the list of items for a given DataGrid based on the current state of the DataGrid.
	 *
	 * This method will always be called before getting the total number of items.
	 *
	 * The list of items should be no larger than the size of the DataGrid#getPageSize method.
	 *
	 * @param grid
	 *            the current data grid
	 * @return the list of items for this page.
	 */
	Collection<T> getPage(DataGrid<T> grid);

	/**
	 * Get the total number of items in the result set for this DataGrid.
	 *
	 * This method will always be called AFTER the getPage method.
	 *
	 * @param grid
	 * @return The number of items that would be returned if no paging is applied
	 */
	int getTotalNumberOfItems(DataGrid<T> grid);

}
