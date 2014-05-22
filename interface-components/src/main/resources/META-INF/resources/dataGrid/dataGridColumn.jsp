<%@ taglib prefix="h" uri="http://techreadiness.net/helpers" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<s:if test="!field">
	<h:evaluateTagBody tag="${tag}" />
</s:if>
<s:elseif test="#attr.tag.rowInfo.editMode">
	<span class="dataGrid-edit-field">
		<h:viewFieldValue rowVar="${baseParent.var}" actionFieldName="${baseParent.fieldName}" field="${field}" valueOnly="${field.readOnly}" hideLabel="true" hideErrors="true"/>
		
	</span>
</s:elseif>
<s:else>
	<h:viewFieldValue rowVar="${baseParent.var}" actionFieldName="${baseParent.fieldName}" field="${field}" valueOnly="true" />
</s:else>				
<s:if test="lastColumn && #attr.tag.rowInfo.editMode && !multiEditable">
	<div class="inlineSaveCancel" style="float:right">
		<s:a class="inlineAction inlineSave" action="%{#attr.baseParent.inlineSaveAction}"></s:a>
		<s:a class="inlineAction inlineCancel"></s:a>
	</div>
</s:if>
