package net.techreadiness.plugin.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;
import net.techreadiness.persistence.dao.OrgDAO;
import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.plugin.persistence.dao.SnapshotWindowDao;
import net.techreadiness.plugin.persistence.domain.SnapshotWindowDO;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.BaseServiceImpl;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SnapshotWindowServiceImpl extends BaseServiceImpl implements SnapshotWindowService {

	@Inject
	SnapshotWindowDao snapshotWindowDao;
	@Inject
	OrgDAO orgDAO;
	@Inject
	ScopeDAO scopeDAO;

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.UPDATE, entityClass = SnapshotWindowDO.class)
	public SnapshotWindow addOrUpdate(ServiceContext context, SnapshotWindow snapshotWindow) {
		SnapshotWindowDO snapshotWindowDO;

		if (StringUtils.isBlank(snapshotWindow.getName())) {
			FaultInfo faultInfo = new FaultInfo();
			ValidationError error = new ValidationError("name", "Name", "The name cannot be blank.");
			faultInfo.getAttributeErrors().add(error);
			throw new ValidationServiceException(faultInfo);
		}

		SnapshotWindowDO temp = snapshotWindowDao.findByScopeAndName(context.getScopeId(), snapshotWindow.getName());

		if (temp != null && snapshotWindow.getSnapshotWindowId() == null) {
			FaultInfo faultInfo = new FaultInfo();
			ValidationError error = new ValidationError("name", "Name", "A snapshot with this name already exists.");
			faultInfo.getAttributeErrors().add(error);
			throw new ValidationServiceException(faultInfo);
		}

		if (snapshotWindow.getSnapshotWindowId() == null) {
			snapshotWindowDO = getMappingService().map(snapshotWindow);

			ScopeDO scopeDO = scopeDAO.getById(context.getScopeId());

			snapshotWindowDO.setRequestDate(new Date(System.currentTimeMillis()));
			snapshotWindowDO.setRequestUser(context.getUserName());
			snapshotWindowDO.setScope(scopeDO);
		} else { // update
			snapshotWindowDO = snapshotWindowDao.getById(snapshotWindow.getSnapshotWindowId());

			if (snapshotWindowDO.getName().equals(ReportsService.DEFAULT_SNAPSHOT_WINDOW)) {
				FaultInfo faultInfo = new FaultInfo();
				ValidationError error = new ValidationError("name", "Name", "The default snapshot cannot be modified.");
				faultInfo.getAttributeErrors().add(error);
				throw new ValidationServiceException(faultInfo);
			}

			// the only thing modifiable is the name and visible
			snapshotWindowDO.setName(snapshotWindow.getName());
			snapshotWindowDO.setVisible(snapshotWindow.getVisible());
		}

		if (snapshotWindow.getSnapshotWindowId() == null) {

			snapshotWindowDO = snapshotWindowDao.create(snapshotWindowDO);

			return getMappingService().map(snapshotWindowDO);
		}

		return getMappingService().map(snapshotWindowDao.update(snapshotWindowDO));
	}

	@Override
	@CoreDataModificationStatus(modificationType = ModificationType.DELETE, entityClass = SnapshotWindowDO.class)
	public void delete(ServiceContext context, Long snapshotWindowId) {
		SnapshotWindowDO snapshotWindowDO = snapshotWindowDao.getById(snapshotWindowId);

		if (snapshotWindowDO.getName().equals(ReportsService.DEFAULT_SNAPSHOT_WINDOW)) {
			FaultInfo faultInfo = new FaultInfo();
			ValidationError error = new ValidationError("name", "Name", "The default snapshot cannot be deleted.");
			faultInfo.getAttributeErrors().add(error);
			throw new ValidationServiceException(faultInfo);
		}

		if (snapshotWindowDO.getVisible() == Boolean.TRUE) {
			FaultInfo faultInfo = new FaultInfo();
			ValidationError error = new ValidationError("visible", "Progress Report",
					"Snapshots marked for Progress Report display cannot be deleted.");
			faultInfo.getAttributeErrors().add(error);
			throw new ValidationServiceException(faultInfo);
		}

		snapshotWindowDao.delete(snapshotWindowDO.getSnapshotWindowId());
	}

	@Override
	public List<SnapshotWindow> findUnexecutedSnapshots(ServiceContext context) {
		return getMappingService().mapFromDOList(snapshotWindowDao.findUnexecutedSnapshots());
	}

	@Override
	public SnapshotWindow getByScopeIdAndName(ServiceContext context, Long scopeId, String snapshotWindowName) {
		return getMappingService().map(snapshotWindowDao.findByScopeAndName(scopeId, snapshotWindowName));
	}

	@Override
	public List<SnapshotWindow> findActiveSnapshots(ServiceContext context, Long scopeId) {
		return getMappingService().mapFromDOList(snapshotWindowDao.findActiveSnapshots(scopeId));
	}
}