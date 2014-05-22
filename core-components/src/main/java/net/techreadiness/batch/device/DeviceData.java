package net.techreadiness.batch.device;

import java.util.Map;

import net.techreadiness.batch.BaseData;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.Org;

import org.drools.core.util.StringUtils;

import com.google.common.collect.Maps;

public class DeviceData extends BaseData {
	protected static final String STATE_CODE_SEPARATOR = "-";
	private String stateCode;
	private Device device;
	private String combinedCode;

	public DeviceData() {
		device = new Device();
		device.setOrg(new Org());
		Map<String, String> attrs = Maps.newHashMap();
		device.setExtendedAttributes(attrs);
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getCombinedCode() {
		if (StringUtils.isEmpty(combinedCode)) {
			combinedCode = stateCode + STATE_CODE_SEPARATOR + device.getOrg().getLocalCode();
		}
		return combinedCode;
	}
}
