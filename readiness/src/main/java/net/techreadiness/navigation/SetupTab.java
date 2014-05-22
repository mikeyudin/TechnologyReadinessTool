package net.techreadiness.navigation;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_FILE_LOADING_ACCESS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORGANIZATION_ACCESS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ACCESS;

public class SetupTab extends DefaultTab {

	public SetupTab() {
		super("setup", "tab.setup.title", "", "", "tab.setup.description", Integer.valueOf(1), false);

		Group setup = new Group(this, "menu.group.title.admin", Integer.valueOf(0));
		setup.addSubTab(new DefaultSubTab("orgSubTab", "tab.organizations.title", "/organization", "list",
				"tab.organizations.description", Integer.valueOf(4), CORE_CUSTOMER_ORGANIZATION_ACCESS));
		setup.addSubTab(new DefaultSubTab("userSubTab", "tab.users.title", "/user", "list", "tab.users.description",
				Integer.valueOf(5), CORE_CUSTOMER_USER_ACCESS));

		Group data = new Group(this, "menu.group.title.data", Integer.valueOf(1));

		Group files = new Group(this, "menu.group.title.files", Integer.valueOf(2));
		files.addSubTab(new DefaultSubTab("fileBatch", "tab.fileBatch.title", "/filebatch", "list",
				"tab.fileBatch.description", Integer.valueOf(0), CORE_CUSTOMER_FILE_LOADING_ACCESS));

		getGroups().add(setup);
		getGroups().add(data);
		getGroups().add(files);

	}
}
