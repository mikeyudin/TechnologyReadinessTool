<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<title><s:text name="task.scope.limits.page.title" /></title>
</head>
<body>
<c:if test="${not empty actionErrors}">
	<div class="message failed-message">
		<span class="icons-16x16 inline-icon-left warning"></span>
		<s:actionerror cssClass="message-list" />
	</div>
</c:if>
<c:if test="${not empty actionMessages}">
	<div class="message success-message">
		<span class="icons-16x16 inline-icon-left check"></span>
		<s:actionmessage cssClass="message-list" />
	</div>
</c:if>
<br>
<h3>
	<s:if test="%{!firstTime}" >
    	<s:text name="ready.task.minRequirements.updateMessage" >
			<s:param value="getChangeDate()" />
        	<s:param value="getChangeUser()" />
		</s:text>
	</s:if>
	<s:else>
		<s:text name="ready.task.minRequirements.updateMessageNull" >
			<s:param value="getServiceContext().getScopeName()" />
		</s:text>
	</s:else>
	<s:property value="getScopeExtDO()" />
</h3>
<br>
<s:form action="save">
	<ui:fieldset title="Set Minimum Device Specs" viewDef="${viewDef}" field="scope">	
		<ui:toolbar>
			<s:a namespace="/" action="info" cssClass="btn btn-xs btn-default">Exit</s:a>
			<s:a action="limits" cssClass="btn btn-xs btn-default">Reset</s:a>
			<s:submit value="save" cssClass="btn btn-xs btn-default">Save</s:submit>
		</ui:toolbar>
		<ui:fieldsetRow code="id">
			<s:hidden name="scope.scopeId" value="%{scope.scopeId}" />
		</ui:fieldsetRow>
	</ui:fieldset>
</s:form>

</body>