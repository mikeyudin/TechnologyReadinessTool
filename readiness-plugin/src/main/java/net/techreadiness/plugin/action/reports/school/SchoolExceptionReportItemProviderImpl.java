package net.techreadiness.plugin.action.reports.school;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.action.reports.ReportItemProviderImpl;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.common.DataGrid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Qualifier("SchoolReportItemProvider")
public class SchoolExceptionReportItemProviderImpl extends ReportItemProviderImpl {

	@Inject
	private ReportsService dao;

	@Override
	public Collection<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {

		if (org == null) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		if (consortium == null) {
			throw new IllegalStateException("No Data to Display. Please ensure the appropriate consortium is selected");
		}

		if (org.getName().equals("Readiness")) {
			throw new IllegalStateException("This report is not available at the current organizational level selected");
		}

		QueryResult<Map<String, String>> result = dao.retrieveSchoolExceptions(snapshotWindow.getSnapshotWindowId(),
				org.getOrgId(), question, false, false, grid.getFirstResult(), grid.getPageSize());
		setTotalNumberOfItems(result.getTotalRowCount());
		return result.getRows();
	}

	@Override
	public Collection<Map<String, String>> export(ExportType exportType) {
		if (org == null) {
			return Lists.newArrayList();
		}

		QueryResult<Map<String, String>> result = dao.retrieveSchoolExceptions(snapshotWindow.getSnapshotWindowId(),
				org.getOrgId(), question, true, true, null, null);

		return result.getRows();
	}
}
