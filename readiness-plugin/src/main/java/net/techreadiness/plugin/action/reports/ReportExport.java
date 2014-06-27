package net.techreadiness.plugin.action.reports;

public interface ReportExport {

	/**
	 * Returns a byte[] of the data to be streamed to the user. Most reports will have two implementations, one for PDF and
	 * one for CSV.
	 *
	 * @return Bytes containing the report data.
	 * @throws Exception When the report cannot be generated.
	 */
	byte[] getReport() throws Exception;

}
