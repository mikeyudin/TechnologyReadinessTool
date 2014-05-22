package net.techreadiness.navigation;

import java.util.Set;

import net.techreadiness.security.PermissionCode;

public interface Tab extends Comparable<Tab> {
	void setLabel(String label);

	String getLabel();

	void setGroups(Set<Group> groups);

	Set<Group> getGroups();

	boolean isEmpty();

	void addChild(SubTab subTab, String groupName);

	void setNamespace(String namespace);

	String getNamespace();

	void setDefaultAction(String defaultAction);

	String getDefaultAction();

	void setSequence(Integer sequence);

	Integer getSequence();

	void setSubtitle(String subtitle);

	String getSubtitle();

	void setCode(String code);

	String getCode();

	void setDescriptionText(String descriptionText);

	String getDescriptionText();

	void setDisplayIfEmpty(boolean displayIfEmpty);

	boolean isDisplayIfEmpty();

	void setPermissionCodes(PermissionCode... permissions);

	PermissionCode[] getPermissionCodes();

	Tab clone();
}
