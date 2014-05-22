package net.techreadiness.navigation;

import net.techreadiness.security.PermissionCode;

public class DefaultSubTab extends DefaultTab implements SubTab {
	private Group group;

	public DefaultSubTab(String code, String label, String namespace, String defaultAction, String descriptionText,
			Integer sequence, Group parent) {
		super(code, label, namespace, defaultAction, descriptionText, sequence, false);
		setGroup(parent);
	}

	public DefaultSubTab(String code, String label, String namespace, String defaultAction, String descriptionText,
			Integer sequence, PermissionCode... codes) {
		super(code, label, namespace, defaultAction, descriptionText, sequence, false);
		setPermissionCodes(codes);
	}

	@Override
	public Group getGroup() {
		return group;
	}

	@Override
	public void setGroup(Group parent) {
		group = parent;
	}

}
