package net.techreadiness.service;

import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.OptionList;

public interface ConfigService extends BaseService {

	/* The lookup keys to the core defined scope extension attributes */
	public static final String ORG_PART_DESCENDANT_CASCADE_ADD = "orgPartDescendantCascadeAdd";
	public static final String ORG_PART_DESCENDANT_CASCADE_DELETE = "orgPartDescendantCascadeDelete";

	/**
	 * Gets a view definition for a view type.
	 *
	 * @param context
	 *            The scope in the {@link ServiceContext} is used as the level to look for the view definition.
	 * @param viewTypeCode
	 *            The type code to find
	 *
	 * @return The view as configured for the provided scope.
	 */
	ViewDef getViewDefinition(ServiceContext context, ViewDefTypeCode viewTypeCode);

	/**
	 * To be used if you don't want to use the scope within the service context.
	 *
	 * @param context
	 *            The current context for the caller.
	 * @param scopeId
	 *            The id of the scope to find the view.
	 * @param viewTypeCode
	 *            The type of view to find.
	 * @return The view as configured for the provided scope.
	 */
	ViewDef getViewDefinition(ServiceContext context, Long scopeId, ViewDefTypeCode viewTypeCode);

	boolean isBooleanActive(ServiceContext context, Long scopeId, String key);

	/**
	 * Get a specific option list w/values by code
	 *
	 * @param serviceContext
	 *            The {@link ServiceContext} used for authorization and access control.
	 * @param optionListCode
	 *            code of option list
	 * @param scopeId
	 *            id of scope the option list is tied to
	 * @return and OptionList with OptionListValues populated
	 */
	OptionList getOptionList(ServiceContext serviceContext, String optionListCode, Long scopeId);
}
