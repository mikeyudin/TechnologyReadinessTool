package net.techreadiness.plugin.service;

import javax.inject.Inject;

import net.techreadiness.plugin.action.task.dataentry.dao.AlertBoxDAO;
import net.techreadiness.service.BaseServiceImpl;
import net.techreadiness.service.ServiceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlertBoxServiceImpl extends BaseServiceImpl implements AlertBoxService {

	@Inject
	AlertBoxDAO alertBoxDao;

	@Override
	public Long getDeviceCountMissingReadinessDeterminants(ServiceContext context, Long orgId) {
		return alertBoxDao.getDeviceCountMissingReadinessDeterminants(orgId);
	}

	@Override
	public Long getDeviceCount(ServiceContext context, Long orgId) {
		return alertBoxDao.getDeviceCount(orgId);
	}

}
