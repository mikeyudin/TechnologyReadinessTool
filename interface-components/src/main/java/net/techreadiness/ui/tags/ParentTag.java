package net.techreadiness.ui.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public abstract class ParentTag extends BaseTag {

	private boolean collectingChildren;

	private List<BaseTag> children = new ArrayList<>();

	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
	}

	public void collectChildren() throws JspException, IOException {
		collectingChildren = true;
		invokeBody();
		collectingChildren = false;
	}

	public boolean isCollectingChildren() {
		return collectingChildren;
	}

	public List<BaseTag> getChildren() {
		return children;
	}

	public <T> List<T> getChildren(Class<T> type) {
		return Lists.newArrayList(Iterables.filter(children, type));
	}
}
