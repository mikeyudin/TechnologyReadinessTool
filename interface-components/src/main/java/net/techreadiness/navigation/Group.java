package net.techreadiness.navigation;

import java.util.Set;

import com.google.common.collect.Sets;

public class Group implements Comparable<Group> {

	private Tab parent;
	private Set<SubTab> children;
	private String name;
	private Integer sequence;

	public Group(Tab parent, String name, Integer sequence) {
		setParent(parent);
		setChildren(Sets.<SubTab> newTreeSet());
		setName(name);
		setSequence(sequence);
	}

	public void addSubTab(SubTab subTab) {
		if (!this.equals(subTab.getGroup())) {
			subTab.setGroup(this);
		}
		children.add(subTab);
	}

	public Tab getParent() {
		return parent;
	}

	public void setParent(Tab parent) {
		this.parent = parent;
	}

	public Set<SubTab> getChildren() {
		return children;
	}

	public void setChildren(Set<SubTab> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getSequence() {
		return sequence;
	}

	@Override
	public int compareTo(Group o) {
		return sequence.compareTo(o.sequence);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (parent == null ? 0 : parent.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		Group other = (Group) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}
}
