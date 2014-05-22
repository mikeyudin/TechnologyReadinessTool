package net.techreadiness.ui.tags.form;

import java.util.Comparator;

import net.techreadiness.service.common.ViewComponent;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.ui.tags.BaseTag;
import net.techreadiness.ui.tags.ViewFieldDisplayOrderComparator;
import net.techreadiness.ui.tags.ViewFieldTag;

public class FieldSetRowTag extends BaseTag implements ViewFieldTag {
	private static final Comparator<ViewFieldTag> comparator = new ViewFieldDisplayOrderComparator();
	private String name;
	private String code;
	private String id;
	private ViewComponent viewComponent;
	private String fieldName;
	private String var;
	private boolean displayLabel;
	private boolean readOnly;
	private boolean showFieldErrors = true;
	private String displayOrder;
	private String description;
	private ViewDef viewDef;
	private Integer pageOrder;

	@Override
	public String execute() throws Exception {
		FieldSetTag fieldSet = getRequiredParentTag(FieldSetTag.class);
		viewDef = fieldSet.getViewDef();
		return "/form/fieldsetRow.jsp";
	}

	public String getInlineValidationError() {
		// TODO : fix me!
		// if(showFieldErrors && viewComponent != null && InputFieldHandler.hasFieldErrors(fieldName + "." +
		// viewComponent.getCode(),
		// getValueStack())) {
		// return "inline-validation-error";
		// }
		return "";
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setDisplayLabel(boolean displayLabel) {
		this.displayLabel = displayLabel;
	}

	public boolean isDisplayLabel() {
		return displayLabel;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setShowFieldErrors(boolean showFieldErrors) {
		this.showFieldErrors = showFieldErrors;
	}

	public boolean isShowFieldErrors() {
		return showFieldErrors;
	}

	public ViewComponent getViewComponent() {
		return viewComponent;
	}

	public void setViewComponent(ViewComponent viewComponent) {
		this.viewComponent = viewComponent;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public String getDisplayOrder() {
		return displayOrder;
	}

	@Override
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	@Override
	public int compareTo(ViewFieldTag o) {
		return comparator.compare(this, o);
	}

	@Override
	public Integer getPageOrder() {
		return pageOrder;
	}

	@Override
	public void setPageOrder(Integer pageOrder) {
		this.pageOrder = pageOrder;
	}

}
