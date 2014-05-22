package net.techreadiness.navigation;

import java.util.Set;

import net.techreadiness.security.PermissionCode;

import com.google.common.collect.Sets;

public class DefaultTab implements Tab, Cloneable {
	private String label;
	private String namespace;
	private String defaultAction;
	private Set<Group> groups;
	private String subtitle;
	private Integer sequence;
	private String code;
	private String descriptionText;
	private boolean displayIfEmpty;
	private PermissionCode[] permissions;

	protected DefaultTab(String code, String label, String namespace, String defaultAction, Integer sequence,
			String descriptionText, Set<Group> groups) {
		setCode(code);
		setLabel(label);
		setNamespace(namespace);
		setDefaultAction(defaultAction);
		setSequence(sequence);
		setDescriptionText(descriptionText);
		setGroups(groups);

	}

	protected DefaultTab(String code, String label, String namespace, String defaultAction, String descriptionText,
			Integer sequence, boolean displayIfEmpty) {
		setCode(code);
		setLabel(label);
		setNamespace(namespace);
		setDefaultAction(defaultAction);
		setSequence(sequence);
		setDescriptionText(descriptionText);
		setDisplayIfEmpty(displayIfEmpty);
		groups = Sets.newTreeSet();
	}

	protected DefaultTab(String code, String subtitle, String label, String namespace, String defaultAction,
			String descriptionText, Integer sequence, boolean displayIfEmpty) {
		setCode(code);
		setSubtitle(subtitle);
		setLabel(label);
		setNamespace(namespace);
		setDefaultAction(defaultAction);
		setSequence(sequence);
		setDescriptionText(descriptionText);
		setDisplayIfEmpty(displayIfEmpty);
		groups = Sets.newTreeSet();
	}

	@Override
	public Tab clone() {
		Tab tab = new DefaultTab(code, subtitle, label, namespace, defaultAction, descriptionText, sequence, displayIfEmpty);

		for (Group g : groups) {
			Group ng = new Group(tab, g.getName(), g.getSequence());
			for (SubTab st : g.getChildren()) {
				SubTab nst = new DefaultSubTab(st.getCode(), st.getLabel(), st.getNamespace(), st.getDefaultAction(),
						st.getDescriptionText(), st.getSequence(), st.getPermissionCodes());
				ng.addSubTab(nst);
			}
			tab.getGroups().add(ng);
		}

		return tab;

	}

	@Override
	public boolean isEmpty() {
		for (Group group : groups) {
			if (!group.getChildren().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getSubtitle() {
		return subtitle;
	}

	@Override
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Override
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	@Override
	public void addChild(SubTab subTab, String groupName) {
		if (groups == null) {
			groups = Sets.newTreeSet();
		}
		for (Group group : groups) {
			if (group.getName().equals(groupName)) {
				subTab.setGroup(group);
				group.getChildren().add(subTab);
			}
		}
	}

	@Override
	public Set<Group> getGroups() {
		return groups;
	}

	@Override
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public void setDefaultAction(String defaultAction) {
		this.defaultAction = defaultAction;
	}

	@Override
	public String getDefaultAction() {
		return defaultAction;
	}

	@Override
	public Integer getSequence() {
		return sequence;
	}

	@Override
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public int compareTo(Tab o) {
		return getSequence().compareTo(o.getSequence());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (code == null ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DefaultTab)) {
			return false;
		}
		DefaultTab other = (DefaultTab) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDescriptionText() {
		return descriptionText;
	}

	@Override
	public void setDescriptionText(String descriptionText) {
		this.descriptionText = descriptionText;
	}

	@Override
	public void setDisplayIfEmpty(boolean displayIfEmpty) {
		this.displayIfEmpty = displayIfEmpty;
	}

	@Override
	public boolean isDisplayIfEmpty() {
		return displayIfEmpty;
	}

	@Override
	public void setPermissionCodes(PermissionCode... permissions) {
		this.permissions = permissions;
	}

	@Override
	public PermissionCode[] getPermissionCodes() {
		return permissions;
	}

}
