package net.techreadiness.ui.tags.helpers;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.Tag;

import net.techreadiness.ui.tags.BaseTag;

public class EvaluateTagHelper extends BaseTag {

	private JspTag tag;
	private JspFragment tagBody;

	@Override
	public String execute() throws Exception {
		if (tag != null) {
			try {
				getValueStack().push(tag);
				for (Entry<String, Object> dynamicAttribute : getDynamicAttributes().entrySet()) {
					getValueStack().setValue(dynamicAttribute.getKey(), dynamicAttribute.getValue());
				}
			} finally {
				getValueStack().pop();
			}
		}
		if (tagBody == null) {
			tagBody = getJspBody();
		}
		if (tag instanceof SimpleTag) {
			processTag((SimpleTag) tag);
		} else if (tag instanceof Tag) {
			processTag((Tag) tag);
		}
		return null;
	}

	void processTag(SimpleTag tag) throws JspException, IOException {
		tag.setJspContext(getJspContext());
		tag.doTag();
	}

	void processTag(Tag tag) throws JspException, IOException {
		tag.setPageContext(getPageContext());
		int result = tag.doStartTag();
		if (result == Tag.EVAL_BODY_INCLUDE && getTagBody() != null) {
			getTagBody().invoke(null);
		}
		tag.doEndTag();
	}

	public void setTag(JspTag tag) {
		this.tag = tag;
	}

	public JspTag getTag() {
		return tag;
	}

	public void setTagBody(JspFragment tagBody) {
		this.tagBody = tagBody;
	}

	public JspFragment getTagBody() {
		return tagBody;
	}
}
