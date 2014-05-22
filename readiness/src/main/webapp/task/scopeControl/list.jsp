<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<ul>
	<s:iterator value="scopes">
		<li>
			<s:a action="change-scope">
				<s:param name="ajax">true</s:param>
				<s:param name="scopeId" value="scopeId"/>
				<s:property value="name"/>
			</s:a>
		</li>
	</s:iterator>
</ul>