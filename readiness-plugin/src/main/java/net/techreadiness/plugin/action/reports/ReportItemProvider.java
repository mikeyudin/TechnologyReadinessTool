package net.techreadiness.plugin.action.reports;

import java.util.Collection;
import java.util.Map;

import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;

public interface ReportItemProvider extends DataGridItemProvider<Map<String, String>> {

	public enum ExportType {
		csv, pdf;
	}

	public void setSnapshotWindow(SnapshotWindow snapshotWindow);

	public void setSnapshotWindows(Collection<SnapshotWindow> snapshotWindows);

	public void setOrg(Org org);

	public void setConsortium(Scope consortiumScope);

	public void setMinimumRecommendedFlag(MinimumRecommendedFlag flag);

	public void setQuestion(String questionKey);

	public Collection<Map<String, String>> export(ExportType exportType);

	public Collection<Map<String, String>> exportAllSchoolsDetail(ExportType exportType);
}
