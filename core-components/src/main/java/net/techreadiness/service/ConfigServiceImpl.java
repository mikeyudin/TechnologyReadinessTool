package net.techreadiness.service;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.EntityFieldDAO;
import net.techreadiness.persistence.dao.OptionListDAO;
import net.techreadiness.persistence.dao.ScopeExtDAO;
import net.techreadiness.persistence.dao.ViewDefDAO;
import net.techreadiness.persistence.dao.ViewDefTypeDAO;
import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.persistence.domain.OptionListDO;
import net.techreadiness.persistence.domain.ScopeExtDO;
import net.techreadiness.persistence.domain.ViewDefDO;
import net.techreadiness.persistence.domain.ViewDefFieldDO;
import net.techreadiness.persistence.domain.ViewDefTextDO;
import net.techreadiness.persistence.domain.ViewDefTypeDO;
import net.techreadiness.service.common.ViewColumn;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.common.ViewField;
import net.techreadiness.service.common.ViewText;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.OptionList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfigServiceImpl extends BaseServiceImpl implements ConfigService {

	@Inject
	private RuleService ruleService;
	@Inject
	private EntityFieldDAO entityFieldDao;
	@Inject
	private ViewDefDAO viewDefDao;
	@Inject
	private ScopeExtDAO scopeExtDao;
	@Inject
	private ViewDefTypeDAO viewDefTypeDao;
	@Inject
	private OptionListDAO optionListDAO;

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "viewDef", key = "#scopeId + #viewTypeCode.toString()")
	public ViewDef getViewDefinition(ServiceContext context, Long scopeId, ViewDefTypeCode viewTypeCode)
			throws ServiceException {
		return getViewDefinition(scopeId, viewTypeCode);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "viewDef", key = "#context.scopeId + #viewTypeCode.toString()")
	public ViewDef getViewDefinition(ServiceContext context, ViewDef.ViewDefTypeCode viewTypeCode) throws ServiceException {
		return getViewDefinition(context.getScopeId(), viewTypeCode);
	}

	private ViewDef getViewDefinition(Long scopeId, ViewDef.ViewDefTypeCode viewTypeCode) throws ServiceException {
		ViewDef viewDef = new ViewDef();
		viewDef.setRuleService(ruleService);

		ViewDefTypeDO viewDefTypeDo = viewDefTypeDao.getByCode(viewTypeCode.toString());
		if (viewDefTypeDo == null || viewDefTypeDo.getEntityType() == null) {
			return null;
		}
		viewDef.setName(viewDefTypeDo.getName());
		viewDef.setViewTypeCategory(viewDefTypeDo.getCategory());

		EntityTypeCode entityTypeCode = EntityTypeCode.valueOf(viewDefTypeDo.getEntityType().getCode());
		viewDef.setEntityTypeCode(entityTypeCode);

		ViewDefDO viewDefDO = viewDefDao.getByViewTypeAndScopePath(viewTypeCode.toString(), scopeId);

		if (viewDefDO == null) {
			viewDefDO = viewDefDao.getByEntityTypeAndScopePath(entityTypeCode.name(), scopeId);
		}

		if (viewDefDO != null) {
			if (StringUtils.isNotBlank(viewDefDO.getName())) {
				viewDef.setName(viewDefDO.getName());
			}
			viewDef.setTypeCode(ViewDef.ViewDefTypeCode.valueOf(viewDefDO.getViewDefType().getCode().toUpperCase()));
			viewDef.setTypeName(viewDefDO.getViewDefType().getName());
			viewDef.setViewDefId(viewDefDO.getViewDefId());
			viewDef.setScopePath(viewDefDO.getScope().getPath());
			for (ViewDefFieldDO viewDefFieldDo : viewDefDO.getViewDefFields()) {
				ViewField field = new ViewField(viewDefFieldDo.getEntityField(), viewDefFieldDo);
				int colNum = 1;
				if (viewDefFieldDo.getColumnNumber() != null) {
					colNum = viewDefFieldDo.getColumnNumber();
				}
				if (colNum > 4) {
					colNum = 4;
				}
				if (viewDef.getColumns().size() < colNum) {
					for (int i = viewDef.getColumns().size(); i < colNum; i++) {
						viewDef.getColumns().add(new ViewColumn());
					}
				}
				ViewColumn column = viewDef.getColumns().get(colNum - 1);
				column.add(field);
			}
			for (ViewDefTextDO viewDefTextDo : viewDefDO.getViewDefTexts()) {
				ViewText text = new ViewText(viewDefTextDo);
				int colNum = viewDefTextDo.getColumnNumber();
				if (colNum > 4) {
					colNum = 4;
				}
				if (viewDef.getColumns().size() < colNum) {
					for (int i = viewDef.getColumns().size(); i < colNum; i++) {
						viewDef.getColumns().add(new ViewColumn());
					}
				}
				ViewColumn column = viewDef.getColumns().get(colNum - 1);
				column.add(text);
			}

			if (viewDef.getColumns().size() >= 3) {
				viewDef.getColumns().get(2).setWidth(viewDefDO.getColumn3Width());
				viewDef.getColumns().get(2).setLabelWidth(viewDefDO.getColumn3LabelWidth());
				Collections.sort(viewDef.getColumns().get(2).getComponents());

			}
			if (viewDef.getColumns().size() >= 2) {
				viewDef.getColumns().get(1).setWidth(viewDefDO.getColumn2Width());
				viewDef.getColumns().get(1).setLabelWidth(viewDefDO.getColumn2LabelWidth());
				Collections.sort(viewDef.getColumns().get(1).getComponents());
			}
			if (viewDef.getColumns().size() >= 1) {
				viewDef.getColumns().get(0).setWidth(viewDefDO.getColumn1Width());
				viewDef.getColumns().get(0).setLabelWidth(viewDefDO.getColumn1LabelWidth());
				Collections.sort(viewDef.getColumns().get(0).getComponents());
			}
		} else {
			List<EntityFieldDO> entityFields = entityFieldDao.findByScopePathAndType(scopeId, viewDef.getEntityTypeCode());
			for (EntityFieldDO entityFieldDo : entityFields) {
				ViewField field = new ViewField(entityFieldDo);

				ViewColumn column = viewDef.getColumns().size() == 0 ? null : viewDef.getColumns().get(0);
				if (column == null) {
					column = new ViewColumn();
					viewDef.getColumns().add(column);
				}
				column.add(field);
			}
		}

		return viewDef;
	}

	public void setScopeExtDao(ScopeExtDAO scopeExtDao) {
		this.scopeExtDao = scopeExtDao;
	}

	@Override
	public boolean isBooleanActive(ServiceContext context, Long scopeId, String key) {
		if (scopeId != null && key != null) {
			ScopeExtDO configItem = scopeExtDao.getLowestExistingConfigurationItem(scopeId, key);
			if (configItem != null && "true".equals(configItem.getValue())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public OptionList getOptionList(ServiceContext serviceContext, String optionListCode, Long scopeId) {
		OptionListDO optionListDO = optionListDAO.getOptionListByCode(optionListCode, scopeId);
		return getMappingService().getMapper().map(optionListDO, OptionList.class);
	}
}
