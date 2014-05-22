package net.techreadiness.batch.org.info;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class OrgInfoHeaderCallback implements FlatFileHeaderCallback {

	@Override
	public void writeHeader(Writer writer) throws IOException {
		String headers = StringUtils.join(OrgInfoLineTokenizer.COL_NAMES, ',');
		writer.append(headers);
	}

}
