<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<head>
<title><s:text name="task.org.participations.pageTitle" />
</title>
</head>

<s:form action="save">
	<ui:taskView taskFlow="${taskFlowData}" value="participationsDataGrid" itemProvider="${orgPartTaskItemProvider}" dataGridViewDef="${viewDef}"
		fieldSetViewDef="${detailsViewDef}" var="part" fieldName="participations['%{part['orgId']}']" listHeader="core.organization"
		nameExpression="%{part['orgName']} (%{part['orgCode']})">
		<ui:entityField name="Participating" code="assigned" nameKey="core.participating">
			<c:choose>
				<c:when test="${part.assignmentAllowed}">
					<s:select value="%{part.assigned}" list="#{'true':'Yes', 'false':'No'}" name="participations['%{part.orgId}'].participation" label="%{getText('task.org.participations.participatingHeader')}" />
				</c:when>
				<c:otherwise>
					<c:out value="Not allowed" />
				</c:otherwise>
			</c:choose>
		</ui:entityField>
	</ui:taskView>
</s:form>
