package net.techreadiness.batch.device;

import java.util.List;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import com.google.common.collect.Lists;

public class DeviceLineTokenizer extends DelimitedLineTokenizer {
	public static final String[] COL_NAMES;

	static {
		List<String> columnNames = Lists.newArrayList();
		columnNames.add("orgCode");
		columnNames.add("stateCode");
		columnNames.add("deviceCount");
		columnNames.add("name");
		columnNames.add("location");
		columnNames.add("operatingSystem");
		columnNames.add("processor");
		columnNames.add("memory");
		columnNames.add("screenResolution");
		columnNames.add("displaySize");
		columnNames.add("browser");
		columnNames.add("wireless");
		columnNames.add("deviceType");
		columnNames.add("environment");
		columnNames.add("owner");

		COL_NAMES = columnNames.toArray(new String[columnNames.size()]);
	}

	public DeviceLineTokenizer() {
		setNames(COL_NAMES);
	}
}
