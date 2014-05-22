package net.techreadiness.plugin.service;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import net.techreadiness.plugin.service.object.SnapshotOrg;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;

public interface SnapshotRestService {

	SnapshotOrg getSnapshotDataForOrg(SecurityContext context, Long snapshotWindowId, Long orgId, MinimumRecommendedFlag flag);

	Response exportNonCompliantDevices(SecurityContext context, Request request, Long snapshotWindow, Long orgId,
			MinimumRecommendedFlag flag);
}
