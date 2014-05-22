package net.techreadiness.plugin.action.reports;

public interface ReportExport {

	/**
	 * Returns a byte[] of the data to be streamed to the user. Most reports will have two implementations, one for pdf and
	 * one for xls.
	 *
	 * @return Bytes containing the report data.
	 */
	byte[] getReport() throws Exception;

}
