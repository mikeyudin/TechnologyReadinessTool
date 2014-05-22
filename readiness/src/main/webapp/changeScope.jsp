<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
</head>
<body>
	<c:choose>
		<c:when test="${scopes.isEmpty()}">
			<s:text name="no.consortium.for.org" />
		</c:when>
		<c:otherwise>
			<s:form namespace="/" action="update-scope">
			    <s:hidden name="returnUrl"/>
			    <div>
			    	<div style="width: 90%; margin: 0px auto;">
			    		<s:select name="scopeId" list="scopes" listKey="scopeId" listValue="name" label="%{scopeTypeName}" required="true" value="%{scopeId}" />
			    	</div>
			    </div>
			    <br>
			    <div>
					<s:submit value="Apply" action="update-scope" cssClass="btn btn-xs btn-primary"></s:submit>
					<div style="float:right;"><b>Currently applied: <c:out value="${serviceContext.scopeName}" /></b></div>
			    </div>
			</s:form>
		</c:otherwise>
	</c:choose>
</body>
</html>