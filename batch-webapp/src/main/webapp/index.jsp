<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
	<title>Core Batch</title>
</head>

<body>
	<h1>Quartz Jobs</h1>
<table>
	<thead>
	<tr>
		<th>Group</th>
		<th>Name</th>
		<th></th>
	</tr>
	</thead>
	<tbody>
		<c:forEach items="${jobKeys}" var="jobKey">
			<tr>
				<td><c:out value="${jobKey.group}"/></td>
				<td><c:out value="${jobKey.name}"/></td>
				<td>
					<a href="${pageContext.request.contextPath}/run-job.action?jobGroup=${jobKey.group}&jobName=${jobKey.name}">Run Now</a>
				</td>
			</tr>	
		</c:forEach>
	</tbody>
</table>

	<h1>Spring Batch Jobs</h1>
	<a href="${pageContext.request.contextPath}/batch-admin/jobs">Click Here</a>	

 </body>
</html>
