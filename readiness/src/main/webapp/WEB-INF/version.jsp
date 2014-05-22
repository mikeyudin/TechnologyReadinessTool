<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="comp" uri="http://swiftelan.com/components"%>
<html>
<body>
	<c:forEach items="${actionErrors}" var="message">
		<div class="text-error"><c:out value="${message}" /></div>	
	</c:forEach>
	<ul>
		<li>Version: <c:out value="${application.version}" /></li>
		<li>Build Date: <fmt:formatDate value="${buildDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
	</ul>
	<h1>TRT Components</h1>
	<comp:table items="${trtVersions}" var="resource" class="table table-condensed table-striped">
		<comp:column header="Group">
			<c:out value="${resource.groupId}" />
		</comp:column>
		<comp:column header="Artifact">
			<c:out value="${resource.artifactId}" />
		</comp:column>
		<comp:column header="Version">
			<c:out value="${resource.version}" />
		</comp:column>
	</comp:table>
	<h1>Package Components</h1>
	<comp:table items="${versions}" var="resource" class="table table-condensed table-striped">
		<comp:column header="Group">
			<c:out value="${resource.groupId}" />
		</comp:column>
		<comp:column header="Artifact">
			<c:out value="${resource.artifactId}" />
		</comp:column>
		<comp:column header="Version">
			<c:out value="${resource.version}" />
		</comp:column>
	</comp:table>
</body>
</html>