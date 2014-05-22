package net.techreadiness.service.common;

import java.util.LinkedList;
import java.util.List;

import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.service.RuleService;

import org.apache.commons.lang3.StringUtils;

/**
 * This class represents the configured definition of a specific entity type for a specific context. It contains a list of
 * entity attributes which specify the detailed display and validation parameters particular to each field.
 * 
 * @see ViewField
 */
public class ViewDef extends ViewFieldGroup {
	private static final long serialVersionUID = 1L;

	public enum ViewDefTypeCode {
		ORG, ORG_DATAGRID, ORG_PART, ORG_PART_DATAGRID, SCOPE, CONTACT, CONTACT_DATAGRID, SCOPE_DATAGRID, ROLE_DATAGRID, USER, USER_DATAGRID, USER_CREATE, USER_DATAGRID_EDIT, USER_ENABLE, DEVICE, DEVICE_DATAGRID, SNAPSHOT, SNAPSHOT_DATAGRID, SCOPE_MINS, ORG_NETWORK_TASK, ORG_DATA_ENTRY_TASK;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	private ViewDefTypeCode typeCode;
	private String typeName;
	private Long viewDefId;
	private String scopePath;
	private String viewTypeCategory;
	private EntityTypeCode entityTypeCode;

	private transient RuleService ruleService;

	public ViewDef() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Entity: {");
		sb.append("type=" + typeCode + ",");
		sb.append("fields=[");
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}

	public String getScopePath() {
		return scopePath;
	}

	public void setScopePath(String scopePath) {
		this.scopePath = scopePath;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getViewDefId() {
		return viewDefId;
	}

	public void setViewDefId(Long viewDefId) {
		this.viewDefId = viewDefId;
	}

	public ViewDefTypeCode getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(ViewDefTypeCode typeCode) {
		this.typeCode = typeCode;
	}

	public String getViewTypeCategory() {
		return viewTypeCategory;
	}

	public void setViewTypeCategory(String viewTypeCategory) {
		this.viewTypeCategory = viewTypeCategory;
	}

	public EntityTypeCode getEntityTypeCode() {
		return entityTypeCode;
	}

	public void setEntityTypeCode(EntityTypeCode entityTypeCode) {
		this.entityTypeCode = entityTypeCode;
	}

	public List<ViewField> getFields() {
		List<ViewField> viewFields = new LinkedList<>();
		for (ViewColumn col : getColumns()) {
			for (ViewComponent component : col.getComponents()) {
				if (component instanceof ViewField) {
					viewFields.add((ViewField) component);
				}
			}
		}
		return viewFields;
	}

	public ViewField getField(String code) {
		for (ViewColumn col : getColumns()) {
			for (ViewComponent component : col.getComponents()) {
				if (component instanceof ViewField) {

					ViewField field = (ViewField) component;
					if (StringUtils.endsWithIgnoreCase(field.getCode(), code)) {
						return field;
					}
				}
			}
		}
		return null;
	}

	public List<ViewComponent> getComponents() {
		List<ViewComponent> viewComponents = new LinkedList<>();
		for (ViewColumn col : getColumns()) {
			for (ViewComponent component : col.getComponents()) {
				viewComponents.add(component);
			}
		}
		return viewComponents;
	}

	public RuleService getRuleService() {
		return ruleService;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

}
