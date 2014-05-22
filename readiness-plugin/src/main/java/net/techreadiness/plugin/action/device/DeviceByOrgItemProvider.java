package net.techreadiness.plugin.action.device;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.dao.DeviceDAO;
import net.techreadiness.persistence.domain.DeviceDO;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.common.SelectableItemProvider;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.mapping.MappingService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

public interface DeviceByOrgItemProvider extends SelectableItemProvider<Device> {
	void setOrg(Org org);

	void setDoNotDisplayText(String text);
}

@Component
@Transactional
class DeviceByOrgItemProviderImpl extends DataGridItemProviderImpl<Device> implements DeviceByOrgItemProvider {

	private Org org;
	private String doNotDisplayText = "";
	private int numDevices;

	@Inject
	private DeviceDAO deviceDao;
	@Inject
	private MappingService mappingService;
	@Inject
	CriteriaQuery<DeviceDO> criteriaQuery;

	@Override
	public Collection<Device> getPage(DataGrid<Device> grid) {

		if (StringUtils.isNotBlank(doNotDisplayText)) {
			throw new IllegalStateException(doNotDisplayText);
		}

		Criteria criteria = createCriteria(grid, "blank");
		criteriaQuery.setFullTextSearchColumns(new String[] { "location", "name" });

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT d.* ");
		sb.append("FROM   device d ");
		sb.append("join org o on o.org_id = d.org_id ");
		sb.append("join org_tree tree on o.org_id = tree.org_id ");
		sb.append("where tree.ancestor_org_id = :orgId ");
		criteriaQuery.setBaseSubSelect(sb.toString());
		criteria.getParameters().put("orgId", org.getOrgId());
		QueryResult<DeviceDO> result = criteriaQuery.getData(criteria, DeviceDO.class);
		numDevices = result.getTotalRowCount();
		Collection<DeviceDO> devices = result.getRows();

		Collection<Device> deviceMaps = new ArrayList<>();
		for (DeviceDO deviceDO : devices) {
			deviceMaps.add(mappingService.getMapper().map(deviceDO, Device.class));
		}
		return deviceMaps;
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Device> grid) {
		return numDevices;
	}

	@Override
	public void setOrg(Org org) {
		this.org = org;
	}

	@Override
	public void setDoNotDisplayText(String text) {
		doNotDisplayText = text;
	}

	@Override
	public Device getObjectForKey(String rowKey) {
		Long deviceId = Long.valueOf(rowKey);
		DeviceDO device = deviceDao.getById(deviceId);
		return mappingService.getMapper().map(device, Device.class);
	}
}
