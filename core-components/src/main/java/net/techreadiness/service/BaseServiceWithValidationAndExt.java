package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;
import net.techreadiness.persistence.AuditedBaseEntityWithExt;
import net.techreadiness.persistence.dao.EntityDAO;
import net.techreadiness.persistence.dao.ExtDAO;
import net.techreadiness.persistence.domain.EntityFieldDO;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class BaseServiceWithValidationAndExt<T extends AuditedBaseEntityWithExt, S extends AbstractAuditedBaseEntityWithExt<T>>
		extends BaseServiceWithValidationImpl {

	public BaseServiceWithValidationAndExt() {
		super();
	}

	/**
	 * Generic method for storing extended attributes for any of our entities which have them.
	 * 
	 * @param context
	 *            service context object with the corresponding scope
	 * @param entityDO
	 *            base entity object that we are saving the attributes for
	 * @param extDAO
	 *            the context specific DAO for saving the attributes
	 * @param type
	 *            the context specific enum type code
	 * @param scopeId
	 *            scopeId where the entityfields are found, should be from the authoritative parent
	 * @param newEntity
	 *            boolean for indicating whether this is a create or an update scenario
	 */
	protected void storeExtFields(ServiceContext context, T entityDO, ExtDAO<T, S> extDAO,
			EntityDAO.EntityTypeCode type, Long scopeId) {
		if (entityDO.getExtAttributes() == null) { // nothing to do, get out of here.
			return;
		}
		List<S> currentList = extDAO.getExtDOs(entityDO);
		
		if (currentList == null) {
			currentList = Lists.newArrayList();
		}
		
		// deletes first
		for (S extDO : currentList) {
			// delete if not exists in entityDO.getExtAttributes() OR the value is empty
			if (!entityDO.getExtAttributes().containsKey(extDO.getEntityField().getCode())
					|| StringUtils.isEmpty(entityDO.getExtAttributes().get(extDO.getEntityField().getCode()))) {
				extDAO.delete(extDO);
			}
		}
		// updates+adds
		for (Map.Entry<String, String> entry : entityDO.getExtAttributes().entrySet()) {
			String attCode = entry.getKey();
			String attValue = entry.getValue();

			boolean found = false;
			for (S extDO : currentList) {
				if (extDO.getEntityField().getCode().equals(attCode)) {
					// extDO exists and if the value isn't empty && equal to the previous value run an update
					found = true;
					if (!StringUtils.isEmpty(attValue) && !extDO.getValue().equals(attValue)) {
						extDO.setValue(attValue);
						extDAO.update(extDO);
					}
					break;
				}
			}
			if (!found) {
				// create
				if (!StringUtils.isEmpty(attValue)) {
					EntityFieldDO entityFieldDO = getEntityFieldByScopeAndTypeAndCode(scopeId, type, attCode);
					if (entityFieldDO != null) {
						S extDO = extDAO.getNew();
						extDO.setParent(entityDO);
						extDO.setEntityField(entityFieldDO);
						extDO.setValue(attValue);
						extDAO.create(extDO);
					}
				}
			}
		}
	}
}
