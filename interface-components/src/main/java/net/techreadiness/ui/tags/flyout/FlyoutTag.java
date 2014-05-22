package net.techreadiness.ui.tags.flyout;

import java.util.Map;

import net.techreadiness.ui.tags.BaseTag;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextParseUtil;

public class FlyoutTag extends BaseTag {

	String action;
	String namespace;
	String text;
	String title;
	String value;

	Map<String, Object> dynamicAttributes;

	@Override
	public String execute() throws Exception {
		if (namespace == null) {
			ActionMapper mapper = ActionContext.getContext().getContainer().getInstance(ActionMapper.class);
			namespace = TagUtils.buildNamespace(mapper, getValueStack(), getRequest());
		}
		if (StringUtils.isNotBlank(title)) {
			title = TextParseUtil.translateVariables(title, getValueStack());
		}

		return "/flyout/flyout.jsp";
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
