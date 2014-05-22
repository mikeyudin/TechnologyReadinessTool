<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib prefix="comp" uri="http://swiftelan.com/components" %>

<html>
<head>
<title><ui:text name="task.user.change.password.pageTitle" /></title>
<style type="text/css">
.form-group {
	margin-bottom: 15px;
}

label {
	margin-bottom: 5px;
	font-weight: 700;
	display: inline-block;
}

.form-control {
font-size: 14px;
}
</style>
</head>
<body>
	<form action="save-password.action" method="post">
		<ui:taskView>
			<c:if test="${not empty actionErrors}">
				<div class="alert alert-error">
					<c:forEach items="${actionErrors}" var="error">
						<div><c:out value="${error}" /></div>
					</c:forEach>
				</div>
			</c:if>
			<comp:table items="${users}" var="user" class="table table-condensed">
				<comp:column>
					<comp:columnHeader>
						<ui:text name="core.user"/>
					</comp:columnHeader>
					<c:out value="${user.firstName} ${user.lastName}" />
				</comp:column>
				<comp:column>
					<comp:columnHeader><ui:text name="core.password"/></comp:columnHeader>
					<s:password id="password" name="password" />
				</comp:column>
				<comp:column>
					<comp:columnHeader><ui:text name="confirm.password"/></comp:columnHeader>
					<s:password id="confirmPassword" name="confirmPassword" />
				</comp:column>
			</comp:table>
			<button type="submit" class="btn btn-primary btn-xs">
					<ui:text name="core.save" />
				</button>
				<a href="${pageContext.request.contextPath}" class="btn btn-default btn-xs"><ui:text name="core.cancel" /></a>
		</ui:taskView>
	</form>
</body>
</html>