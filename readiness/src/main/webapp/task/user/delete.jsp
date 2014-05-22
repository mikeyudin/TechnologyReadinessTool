<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib prefix="comp" uri="http://swiftelan.com/components"%>

<html>
<head>
<title><s:text name="task.user.delete.page.title" />
</title>
</head>
<body>
	<s:form id="userForm" action="save">
		<ui:taskView taskFlow="${taskFlowData}">
			<ui:taskNavigation saveButton="core.delete.undelete"/>
			<ui:text name='core.delete.undelete.instructions' var="deleteInstructions" />
			<comp:table items="${users}" var="user" class="table table-condensed table-striped">
				<comp:column header="${deleteInstructions}">
					<c:choose>
						<c:when test="${empty user.deleteDate}">
							<label>
								<input type="checkbox" name="userStates[${user.userId}]" value="true">
								<c:out value="${user.username}" />
							</label>
						</c:when>
						<c:otherwise>
							<label>
								<input type="checkbox" name="userStates[${user.userId}]" checked="checked" value="true">
								<c:out value="${user.username}" />
							</label>
						</c:otherwise>
					</c:choose>
				</comp:column>
				<comp:column header="Name">
					<c:out value="${user.firstName} ${user.lastName}" />
					<c:if test="${not empty user.deleteDate}">
						<span class="icons-16x16 delete"></span>
					</c:if>
				</comp:column>
			</comp:table>
		</ui:taskView>
	</s:form>
</body>