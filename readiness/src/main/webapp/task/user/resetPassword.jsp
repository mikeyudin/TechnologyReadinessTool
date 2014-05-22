<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><s:text name="task.user.reset.password.pageTitle" /></title>
</head>
<body>
	<s:form action="save-reset-password">

		<ui:taskView taskFlow="${taskFlowData}" itemProvider="${itemProvider}" fieldName="users[%{user.userId}]" value="resetUsersDataGrid" var="user">
			<ui:taskNavigation saveButton="task.user.reset.password"/>
			<ui:entityField code="delete" displayOrder="first" nameKey="task.user.reset.password">
				<s:checkbox name="users[%{user.userId}]" value="false"/>
			</ui:entityField>
			
			<ui:entityField name="users[%{user.username}]" code="username" nameKey="core.username">
				<s:property value="%{user.username}" />
			</ui:entityField>

			<ui:entityField name="Name" code="name">
				<s:property value="%{user.firstName}" />
				<s:property value="%{user.lastName}" />
				<s:if test="user.deleteDate neq null">
				  <span class="delete icons-16x16" style="display:inline-block;"></span>
				</s:if>	
			</ui:entityField>

		</ui:taskView>
	</s:form>
</body>
</html>