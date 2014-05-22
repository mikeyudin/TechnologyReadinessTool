<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@ taglib uri="/struts-tags" prefix="s"%>

<c:if test="${!empty tag.width }">
	<c:set var="cssStyle" value="width:${tag.width}"/>
</c:if>

<ul id="${tag.id}" class="fieldset" style="${cssStyle}">
	<c:forEach items="${tag.rows}" var="row">
		<ui:viewRule ruleId="${row.viewComponent.displayRuleId}" ruleService="${tag.parent.viewDef.ruleService}" row="${var}">
			<h:evaluateTag tag="${row}"/>
		</ui:viewRule>
	</c:forEach>
</ul>

<c:if test="${!empty tag.labelWidth }">
	<!-- This is ugly, but the only way I can think of to support the requirement of
		 controlling the label width without completely reworking the form style. -->
<%-- 	<script type="text/javascript">alert('tag.labelWidth=${tag.labelWidth}');$('#${tag.id} label').width('${tag.labelWidth}');</script> --%>
</c:if>
	