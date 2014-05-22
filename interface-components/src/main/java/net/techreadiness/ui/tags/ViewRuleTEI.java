package net.techreadiness.ui.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class ViewRuleTEI extends TagExtraInfo {

	@Override
	public VariableInfo[] getVariableInfo(TagData data) {
		Object varAttr = data.getAttribute("var");
		if (varAttr != null && varAttr instanceof String) {
			VariableInfo var = new VariableInfo((String) varAttr, "java.lang.Boolean", true, VariableInfo.AT_BEGIN);
			return new VariableInfo[] { var };
		}

		return super.getVariableInfo(data);
	}
}
