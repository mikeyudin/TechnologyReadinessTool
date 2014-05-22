package net.techreadiness.ui.tags;

/**
 * The interface represents a tag that is used to display information in table or form format.
 * 
 */
public interface ViewFieldTag extends Comparable<ViewFieldTag> {

	String getCode();

	String getDescription();

	String getDisplayOrder();

	String getName();

	Integer getPageOrder();

	void setCode(String code);

	void setDescription(String description);

	void setDisplayOrder(String displayOrder);

	void setName(String name);

	void setPageOrder(Integer pageOrder);

}
