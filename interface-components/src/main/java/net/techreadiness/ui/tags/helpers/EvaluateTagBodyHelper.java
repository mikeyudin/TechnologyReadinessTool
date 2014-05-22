package net.techreadiness.ui.tags.helpers;

import net.techreadiness.ui.tags.BaseTag;

/**
 * Evaluate the body of a given jsp tag reference.
 */
public class EvaluateTagBodyHelper extends BaseTag {

	private BaseTag tag;

	@Override
	public String execute() throws Exception {
		tag.writeBody(getJspContext().getOut());
		return null;
	}

	public void setTag(BaseTag tag) {
		this.tag = tag;
	}

	public BaseTag getTag() {
		return tag;
	}
}
