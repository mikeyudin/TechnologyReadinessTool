package net.techreadiness.batch.org;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class OrgHeaderCallback implements FlatFileHeaderCallback {

	@Override
	public void writeHeader(Writer writer) throws IOException {
		String headers = StringUtils.join(OrgLineTokenizer.COL_NAMES, ',');
		writer.append(headers);
	}

}
