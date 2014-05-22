<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>

<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Technology Readiness Tool: Error</title>
	<style>
	.bs-callout {
    margin: 20px 0;
    padding: 20px;
    border-left: 3px solid #eee;
}

.bs-callout h4 {
    margin-top: 0;
    margin-bottom: 5px;
}

.bs-callout p:last-child {
    margin-bottom: 0;
}

/* Variations */
.bs-callout-danger {
    background-color: #fdf7f7;
    border-color: #eed3d7;
}

.bs-callout-danger h4 {
    color: #b94a48;
}

.bs-callout-warning {
    background-color: #faf8f0;
    border-color: #faebcc;
}

.bs-callout-warning h4 {
    color: #8a6d3b;
}

.bs-callout-info {
    background-color: #f4f8fa;
    border-color: #bce8f1;
}

.bs-callout-info h4 {
    color: #34789a;
}
p {
	margin: 0 0 10px;
}

.control-label {
	display: block;
	font-weight: bold;
	margin-bottom: 5px;
}

h1, h4 {
	font-weight: 500;
	line-height: 1.1;
	margin-top: 10px;
	margin-bottom: 10px;
}

.bs-callout h4 {
	
	font-size: 18px;
}
	</style>
</head>
<body>
<div style="margin: 0 25px;">
	<h1>Error</h1>
	<div class="bs-callout bs-callout-danger">
		<h4>Unable to process your request</h4>
		<p>
			<c:out value="${throwable.message}" />
		</p>
		<a href="${pageContext.request.contextPath}">Home</a>
		<label for="correlationId" class="control-label">Correlation ID</label>
		<input id="correlationId" type="text" value="${faultInfo.correlationId}" readonly>
	</div>
</div>
</body>
</html>