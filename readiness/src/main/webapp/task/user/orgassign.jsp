<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib prefix="comp" uri="http://swiftelan.com/components"%>

<html>
<head>
<title><ui:text name="task.user.orgassign.edit.pageTitle" /></title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/component-tag-library/table.js"></script>
<script type="text/javascript">
	$(function(){
		$('.dataViewControl').on('dataViewControlUpdated componentLoaded', function(event){
			$('.table').table('update');
		});
	});
</script>
</head>
<body>
	<s:form action="save">
		<ui:taskView taskFlow="${taskFlowData}" value="assignOrgsGrid" itemProvider="${itemProvider}" var="assignment" fieldName="assignments['%{assignment['user'].userId}']"
			listHeader="core.username" nameExpression="%{assignment['user'].username}">
				<ul>
				<li class="taskViewControl">
			<ui:dataViewControl code="organization" name="core.organizations" configLinkName="core.add" 
					namespace="/organizationControl"
					side="left"
					configAction="showOptions"
					contentAction="show" dataGridId="assignOrgsGrid" multiple="true" />
</li>
</ul>
			<comp:table items="${users}" var="user" class="table table-condensed table-striped">
				<comp:column>
					<comp:columnHeader>
						<ui:text name="core.username"/>
					</comp:columnHeader>
					<c:out value="${user.username}" />
				</comp:column>
				<comp:column>
					<comp:columnHeader>
						<ui:text name="core.name"/>
					</comp:columnHeader>
					<ui:flyout text="${user.firstName} ${user.lastName}" title="User Details" namespace="/user" action="details" userId="${user.userId}" showTaskLinks="false"/>
			    	<c:if test="${not empty user.deleteDate}">
						<span class="delete icons-16x16" style="display:inline-block;"></span>
					</c:if>
					<c:set value="assignments[${user.userId}]" var="errorKey" />
					<c:forEach items="${fieldErrors[errorKey]}" var="error">
						<div style="color: #FF4136;"><c:out value="${error}" /></div>
					</c:forEach>
				</comp:column>
				<c:forEach items="${orgs}" var="org">
					<comp:column header="${org.name}">
						<c:choose>
							<c:when test="${assignments[user.userId].contains(org.orgId)}">
								<input type="checkbox" checked="checked" name="assignments[${user.userId}]" value="${org.orgId}">
							</c:when>
							<c:otherwise>
								<input type="checkbox" name="assignments[${user.userId}]" value="${org.orgId}">
							</c:otherwise>
						</c:choose>
					</comp:column>
				</c:forEach>
			</comp:table>
		</ui:taskView>
	</s:form>
</body>
</html>