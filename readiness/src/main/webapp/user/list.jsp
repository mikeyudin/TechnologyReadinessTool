<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
	<title><s:text name="user.list.pageTitle" /></title>
</head>
<body>
	<ui:dataView taskFlow="${taskFlow}" filterSelectable="true" title="%{getText('tab.users.title')}">	
		<ui:dataViewFilters viewDefType="USER_DATAGRID">
		    <c:if test="${showDeletedUsersCheckboxDisplayed}">
				<ui:dataViewFilter name="%{getText('user.list.searchDeletedUsers')}" code="showAll" beanName="showDeletedUsersSelectionHandler" type="checkbox" primary="true"/>
			</c:if>
			<ui:dataViewFilter name="%{getText('core.organization')}" code="organization" beanName="orgFilterSelectionHandlerForUser" nameKey="name" valueKey="orgId"/>
			<ui:dataViewFilter name="%{getText('core.role')}" code="role" beanName="roleFilterSelectionHandlerForUser" nameKey="name" valueKey="roleId"/>
		</ui:dataViewFilters>
		
		<ui:shoppingCart name="%{getText('core.users')}"><s:if test="deleteDate neq null">|</s:if><s:property value="lastName"/>, <s:property value="firstName"/></ui:shoppingCart>
		<s:form action="save-in-line">
			<ui:datagrid itemProvider="${itemProvider}" value="userGrid" var="user" viewDef="${viewDef}" selectable="true" rowValue="userId"
				columnSelectable="true">
				<ui:dataGridColumn code="userId" hidden="true">
					<s:hidden name="user.userId" value="%{userId}"/>
					<s:hidden name="user.username" value="%{username}"/> 
				</ui:dataGridColumn>
				<ui:dataGridColumn name="Username" code="username" nameKey="core.username">
					<s:if test="editMode">
						<s:textfield name="user.username" cssErrorStyle="display:none;" />
					</s:if>
					<s:else>
						<ui:flyout text="${username}" title="User Details" namespace="/user" action="details" userId="${user.userId}" reset="true" showTaskLinks="true"/>
					</s:else>
					<s:if test="user.deleteDate neq null">
						<span class="delete icons-16x16 inline-icon-left"></span>
					</s:if>
				</ui:dataGridColumn>
			</ui:datagrid>
		</s:form>
	</ui:dataView>
</body>
</html>