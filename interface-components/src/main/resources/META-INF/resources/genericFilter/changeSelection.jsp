<%@ taglib prefix="s" uri="/struts-tags"%>
<ul>
	<s:iterator value="values" var="selectedItem">
		<li>
			<s:a action="add" includeParams="get">
				<s:param name="ajax">true</s:param>
				<s:param name="id" value="%{(#attr.valueKey)(#selectedItem)}"/>
				<s:property value="%{(#attr.nameKey)(#selectedItem)}"/>
			</s:a>
		</li>
	</s:iterator>
</ul>