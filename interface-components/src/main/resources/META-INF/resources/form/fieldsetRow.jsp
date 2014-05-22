<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="h" uri="http://techreadiness.net/helpers" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components" %>

<li id="${tag.id}" class="fieldset-row ${inlineValidationError}">
	<c:choose>
		<c:when test="${empty tag.viewComponent}">
			<h:evaluateTagBody tag="${tag}"/>
		</c:when>
		
		<c:otherwise>
			<ui:viewRule ruleId="${tag.viewComponent.editRuleId}" ruleService="${tag.viewDef.ruleService}" row="${var}" var="viewRuleAllowEdit" />
			<h:viewFieldValue readOnly="${tag.readOnly || !viewRuleAllowEdit}" rowVar="${tag.var}" actionFieldName="${tag.fieldName}" field="${tag.viewComponent}" />
		</c:otherwise>
	</c:choose>
</li>