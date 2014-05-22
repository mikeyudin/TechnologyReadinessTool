package net.techreadiness.plugin.action.reports;

import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ReportExportCsv implements ReportExport {

	protected List<String> columnLabels;
	protected List<String> columnKeys;
	protected Collection<Map<String, String>> data;

	public List<String> getColumnLabels() {
		return columnLabels;
	}

	public void setColumnLabels(List<String> columnLabels) {
		this.columnLabels = columnLabels;
	}

	public List<String> getColumnKeys() {
		return columnKeys;
	}

	public void setColumnKeys(List<String> columnKeys) {
		this.columnKeys = columnKeys;
	}

	public Collection<Map<String, String>> getData() {
		return data;
	}

	public void setData(Collection<Map<String, String>> data) {
		this.data = data;
	}

	@Override
	public byte[] getReport() throws Exception {
		StringWriter writer = new StringWriter(1024);
		CSVWriter csv = new CSVWriter(writer);
		// create header
		csv.writeLine(columnLabels);

		// write data
		for (Map<String, String> data : this.data) {
			List<String> values = Lists.newArrayList();
			for (Object key : columnKeys) {
				values.add(data.get(key) == null ? "" : data.get(key));
			}
			csv.writeLine(values);
		}
		return writer.toString().getBytes();
	}

}
