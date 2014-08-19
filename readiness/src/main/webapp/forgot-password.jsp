<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setBundle basename="readiness-messages" var="msg" />
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><fmt:message bundle="${msg}" key="technology.readiness.tool" /> - <fmt:message bundle="${msg}"
		key="password.reset"></fmt:message></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/readiness.css">
</head>
<body>
	<div class="center-block readiness-container">
		<div class="text-center">
			<img src="${pageContext.request.contextPath}/static/images/readiness-logo.png">
		</div>

		<form class="form-login" method="post" action="forgot-password-accept.action">
			<div class="row">
				<div class="col-md-12">
					<h3>
						<fmt:message bundle="${msg}" key="forgot.password.header" />
					</h3>
				</div>
			</div>
			<c:forEach items="${actionErrors}" var="error">
				<div class="text-danger"><c:out value="${error}" /></div>
			</c:forEach>
			<c:if test="${not empty param.success}">
				<div class="alert alert-success"><fmt:message bundle="${msg}" key="password.reset.success" /></div>
			</c:if>

			<div class="form-group ${empty fieldErrors['username'] ? '' : 'has-error'}">
				<label class="control-label" for="username"><fmt:message bundle="${msg}" key="username" /></label> <input type="text"
					id="username" class="form-control" tabindex="1" name="username">
				<c:forEach items="${fieldErrors['username']}" var="error">
					<span class="help-block"><c:out value="${error}"/></span>
				</c:forEach>
			</div>
			<div class="form-group ${empty fieldErrors['email'] ? '' : 'has-error'}">
				<label class="control-label" for="email"><fmt:message bundle="${msg}" key="email" /></label> <input type="email"
					id="email" class="form-control" tabindex="2" name="email">
				<c:forEach items="${fieldErrors['email']}" var="error">
					<span class="help-block"><c:out value="${error}"/></span>
				</c:forEach>
			</div>

			<button type="submit" class="btn btn-default btn-xs">
				<fmt:message bundle="${msg}" key="request.password.reset" />
			</button>
			<a href="${pageContext.request.contextPath}/" class="btn btn-default btn-xs"><fmt:message bundle="${msg}" key="cancel" /></a>
		</form>

		<div class="text-center readiness-logos">
                    <img src="${pageContext.request.contextPath}/static/images/nysed.jpg" /> 
		</div>
	</div>
</body>
</html>