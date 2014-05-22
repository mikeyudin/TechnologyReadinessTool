package net.techreadiness.batch.device;

import java.util.List;

import net.techreadiness.service.common.ValidationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.google.common.collect.Lists;

public class OrgFilterProcessor extends AbstractDeviceStep implements ItemProcessor<DeviceData, DeviceData> {
	private final Logger log = LoggerFactory.getLogger(OrgFilterProcessor.class);

	@Override
	public DeviceData process(DeviceData item) throws Exception {
		if (userService.hasAccessToOrgByCode(getServiceContext(), getUserId(), item.getCombinedCode())) {
			log.debug("Granting delete access to org: {}", item.getCombinedCode());
			return item;
		}
		log.debug("Denying delete access to org: {}", item.getCombinedCode());
		List<ValidationError> errors = Lists.newArrayList();
		addNotAllowedError(errors, item.getCombinedCode(), "delete");
		throwIfErrors(errors);
		return null;
	}
}
