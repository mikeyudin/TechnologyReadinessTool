package net.techreadiness.ui.tags.taskview;

import java.util.List;

import net.techreadiness.ui.tags.ParentTag;

public class EntityFieldGroupTag extends ParentTag {
	private String name;
	private List<EntityFieldTag> fields;

	@Override
	public String execute() throws Exception {
		fields = getChildren(EntityFieldTag.class);
		name = evaluateOgnl(name);
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EntityFieldTag> getFields() {
		return fields;
	}
}
