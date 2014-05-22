<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<head>
<title><ui:text name="task.org.delete.pageTitle" /></title>
</head>
<body>
	<s:form id="orgForm" action="save">

		<ui:taskView taskFlow="${taskFlowData}" value="deleteOrgsDataGrid" itemProvider="${orgsByIdItemProvider}" var="org">
			
			<ui:taskNavigation saveButton="core.delete"/>
			
			<ui:entityField code="delete" displayOrder="first">
        		<s:checkbox name="orgs[%{org.orgId}]" />
        	</ui:entityField>
			<ui:entityField code="name" nameKey="core.name">
				<c:out value="${org.name}" />
			</ui:entityField>
			<ui:entityField code="code" nameKey="core.code">
				<c:out value="${org.code}" />
			</ui:entityField>	
			<ui:entityField code="inactive" nameKey="core.inactive">
				<c:out value="${org.inactive}" />
			</ui:entityField>	
			<ui:entityField code="participations" nameKey="core.participations">
				<c:out value="${org.participations}" />
			</ui:entityField>
			<ui:entityField code="contacts" nameKey="core.contacts">
				<c:out value="${org.contacts}" />
			</ui:entityField>
			
		</ui:taskView>
	</s:form>
</body>