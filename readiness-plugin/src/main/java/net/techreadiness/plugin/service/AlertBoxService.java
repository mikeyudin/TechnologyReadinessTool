package net.techreadiness.plugin.service;

import net.techreadiness.service.ServiceContext;

public interface AlertBoxService {

	Long getDeviceCountMissingReadinessDeterminants(ServiceContext context, Long orgId);

	Long getDeviceCount(ServiceContext context, Long orgId);
}
