package net.techreadiness.plugin.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.plugin.persistence.dao.SnapshotWindowDao;
import net.techreadiness.plugin.persistence.domain.SnapshotWindowDO;
import net.techreadiness.plugin.persistence.report.BaseDataRetriever;
import net.techreadiness.plugin.service.object.SnapshotDevice;
import net.techreadiness.plugin.service.object.SnapshotOrg;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

@Named
@Path("snapshots")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class SnapshotRestServiceImpl implements SnapshotRestService {
	@Inject
	private UserDAO userDAO;
	@Inject
	private OrgDAO orgDAO;
	@Inject
	private SnapshotWindowDao snapshotDAO;
	@Inject
	@Named("BaseDataRetriever")
	private BaseDataRetriever dataRetriever;
	@Inject
	private JdbcTemplate template;
	@Inject
	private MessageSource messageSource;

	@GET
	@Path("{snapshotWindowId}/{orgId}")
	@Override
	@Produces(MediaType.APPLICATION_JSON)
	public SnapshotOrg getSnapshotDataForOrg(@Context SecurityContext context,
			@PathParam("snapshotWindowId") Long snapshotWindowId, @PathParam("orgId") Long orgId,
			@QueryParam("flag") @DefaultValue("MINIMUM") MinimumRecommendedFlag flag) {
		UserDO user = userDAO.findByUsername(context.getUserPrincipal().getName(), false);
		if (!userDAO.hasAccessToOrg(user.getUserId(), orgId)) {
			String message = messageSource.getMessage("validation.org.access.denied", new Object[] { user.getUsername(),
					orgId }, java.util.Locale.getDefault());
			throw new ClientErrorException(Response.status(Status.FORBIDDEN).entity(message).build());
		}

		try {
			QueryResult<Map<String, String>> reportData = dataRetriever.getSnapshotReportDataForOrg(snapshotWindowId, orgId,
					flag);
			Collection<Map<String, String>> rows = reportData.getRows();
			if (CollectionUtils.isEmpty(rows)) {
				String message = messageSource.getMessage("ready.no.snapshot.data.for.org", null, Locale.getDefault());
				throw new NotFoundException(Response.status(Status.NOT_FOUND).entity(message).build());
			}
			Map<String, String> first = Iterables.getFirst(rows, Collections.<String, String> emptyMap());
			SnapshotOrg org = new SnapshotOrg();
			for (Entry<String, String> entry : first.entrySet()) {
				try {
					BeanUtils.setProperty(org, entry.getKey(), entry.getValue());
				} catch (Exception e) {
					throw new InternalServerErrorException(e);
				}
			}
			return org;
		} catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	@Override
	@GET
	@Produces("text/csv")
	@Path("{snapshotId}/{orgId}/non-compliant-devices")
	public Response exportNonCompliantDevices(@Context SecurityContext context, @Context Request request,
			@PathParam("snapshotId") Long snapshotId, @PathParam("orgId") Long orgId,
			@QueryParam("flag") @DefaultValue("MINIMUM") MinimumRecommendedFlag flag) {
		UserDO user = userDAO.findByUsername(context.getUserPrincipal().getName(), false);
		if (!userDAO.hasAccessToOrg(user.getUserId(), orgId)) {
			String message = messageSource.getMessage("validation.org.access.denied", new Object[] { user.getUsername(),
					orgId }, java.util.Locale.getDefault());
			throw new ClientErrorException(Response.status(Status.FORBIDDEN).entity(message).build());
		}

		OrgDO org = orgDAO.getById(orgId);
		if (!org.getOrgType().isAllowDevice()) {
			String message = messageSource.getMessage("ready.no.snapshot.data.for.org", new Object[] { user.getUsername(),
					orgId }, java.util.Locale.getDefault());
			throw new InternalServerErrorException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).build());
		}

		SnapshotWindowDO snapshot = snapshotDAO.getById(snapshotId);
		ResponseBuilder response = request.evaluatePreconditions(snapshot.getExecuteDate());
		if (response != null) {
			return response.build();
		}
		List<SnapshotDevice> devices = template.query(buildNonCompliantQuery(flag), new Object[] { snapshotId, orgId },
				new SnapshotDeviceRowMapper());
		String header = "Organization,Device Name,# of Devices,Operating System,Memory,Monitor / Display Size,Screen Resolution,Environment";
		StringBuilder sb = new StringBuilder(header.length() + devices.size() * 150);
		sb.append(header);
		sb.append("\n");
		for (SnapshotDevice device : devices) {
			sb.append(device.getOrgName());
			sb.append(" (");
			sb.append(device.getLocalOrgCode());
			sb.append("),");
			sb.append(device.getName());
			sb.append(",");
			sb.append(device.getCount());
			sb.append(",");
			if (device.getOperatingSystem() != null) {
				sb.append(buildExportValue(device.getOperatingSystem()));
			}
			sb.append(",");
			if (device.getMemory() != null) {
				sb.append(buildExportValue(device.getMemory()));
			}
			sb.append(",");
			if (device.getMonitorDisplaySize() != null) {
				sb.append(buildExportValue(device.getMonitorDisplaySize()));
			}
			sb.append(",");
			if (device.getScreenResolution() != null) {
				sb.append(buildExportValue(device.getScreenResolution()));
			}
			sb.append(",");
			if (device.getEnvironment() != null) {
				sb.append(buildExportValue(device.getEnvironment()));
			}
			sb.append("\n");
		}
		CacheControl cache = new CacheControl();
		cache.setMaxAge(3600);
		cache.setMustRevalidate(true);
		cache.setNoCache(false);
		return Response.ok().type("text/csv").entity(sb.toString()).lastModified(snapshot.getExecuteDate())
				.cacheControl(cache).build();
	}

	private static String buildExportValue(String value) {
		if (value.contains(",")) {
			String newValue = value.replaceAll(",", "");
			value = newValue;
		}
		return StringUtils.defaultIfBlank(value, "(missing)");
	}

	private static String buildNonCompliantQuery(MinimumRecommendedFlag flag) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("  o.name orgName, ");
		sb.append("  o.code orgCode, ");
		sb.append("  o.local_code localOrgCode,");
		sb.append("  concat(po.name,' (',po.local_code,')') parentOrgName,  ");
		sb.append("  po.code parentOrgCode,  ");
		sb.append("  po.local_code parentLocalOrgCode,  ");
		sb.append("  sd.name,");
		sb.append("  sd.count count,");
		sb.append("  sc_operating_system.name operatingSystem,");
		sb.append("  sc_memory.name memory,");
		sb.append("  sc_display_size.name monitorDisplaySize,");
		sb.append("  sc_screen_resolution.name screenResolution,");
		sb.append("  sc_environment.name environment,");
		sb.append("  sd.environment_compliant environmentCompliance,");
		if (flag.equals(MinimumRecommendedFlag.MINIMUM)) {
			sb.append("  sd.min_compliant_operating_system operatingSystemCompliance,");
			sb.append("  sd.min_compliant_memory memoryCompliance,");
			sb.append("  sd.min_compliant_screen_resolution screenResolutionCompliance,");
			sb.append("  sd.min_compliant_display_size monitorDisplaySizeCompliance");
		} else {
			sb.append("  sd.rec_compliant_operating_system operatingSystemCompliance,");
			sb.append("  sd.rec_compliant_memory memoryCompliance,");
			sb.append("  sd.rec_compliant_screen_resolution screenResolutionCompliance,");
			sb.append("  sd.rec_compliant_display_size monitorDisplaySizeCompliance");
		}
		sb.append(" from readiness.snapshot_device sd");
		sb.append("  join org o on o.org_id = sd.org_id");
		sb.append("  join org po on po.org_id = o.parent_org_id");
		sb.append("  left join readiness.snapshot_config sc_operating_system on sc_operating_system.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_operating_system.code ='operatingSystems' ");
		sb.append("                                                         and sc_operating_system.value = sd.operating_system");
		sb.append("  left join readiness.snapshot_config sc_memory on sc_memory.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_memory.code ='memory' ");
		sb.append("                                                         and sc_memory.value = sd.memory");
		sb.append("  left join readiness.snapshot_config sc_display_size on sc_display_size.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_display_size.code ='displaySize' ");
		sb.append("                                                         and sc_display_size.value = sd.display_size");
		sb.append("  left join readiness.snapshot_config sc_screen_resolution on sc_screen_resolution.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_screen_resolution.code ='screenResolutions' ");
		sb.append("                                                         and sc_screen_resolution.value = sd.screen_resolution");
		sb.append("  left join readiness.snapshot_config sc_environment on sc_environment.snapshot_window_id = sd.snapshot_window_id ");
		sb.append("                                                         and sc_environment.code ='environment' ");
		sb.append("                                                         and sc_environment.value = sd.environment");
		sb.append(" where ");
		sb.append("  sd.snapshot_window_id = ? ");
		sb.append("  and sd.org_id = ? ");
		if (flag.equals(MinimumRecommendedFlag.MINIMUM)) {
			sb.append(" and min_compliant = 'no'");
		} else {
			sb.append(" and rec_compliant = 'no'");
		}

		return sb.toString();
	}

	private static final class SnapshotDeviceRowMapper implements RowMapper<SnapshotDevice> {

		@Override
		public SnapshotDevice mapRow(ResultSet rs, int rowNum) throws SQLException {
			String orgName = rs.getString("orgName");
			String orgCode = rs.getString("orgCode");
			String localOrgCode = rs.getString("localOrgCode");
			String parentOrgName = rs.getString("parentOrgName");
			String parentOrgCode = rs.getString("parentOrgCode");
			String parentLocalOrgCode = rs.getString("parentLocalOrgCode");
			String name = rs.getString("name");
			Long count = rs.getLong("count");
			String operatingSystem = rs.getString("operatingSystem");
			String memory = rs.getString("memory");
			String monitorDisplaySize = rs.getString("monitorDisplaySize");
			String screenResolution = rs.getString("screenResolution");
			String environment = rs.getString("environment");

			SnapshotDevice device = new SnapshotDevice();
			device.setOrgName(orgName);
			device.setOrgCode(orgCode);
			device.setLocalOrgCode(localOrgCode);
			device.setParentOrgName(parentOrgName);
			device.setParentOrgCode(parentOrgCode);
			device.setParentLocalOrgCode(parentLocalOrgCode);
			device.setName(name);
			device.setCount(count);
			device.setOperatingSystem(operatingSystem);
			device.setMemory(memory);
			device.setMonitorDisplaySize(monitorDisplaySize);
			device.setScreenResolution(screenResolution);
			device.setEnvironment(environment);
			return device;
		}

	}
}
