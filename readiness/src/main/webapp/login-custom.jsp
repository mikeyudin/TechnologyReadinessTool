<!DOCTYPE html>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title><s:text name="login.page.title" /></title>
<%-- 	<h:staticResources /> --%>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<div class="row" style="margin-top: 50px; margin-bottom: 25px;">
			<img alt="Technology Readiness Tool Logo" src="${pageContext.request.contextPath}/static/images/program/readiness-logo.png" style="display: block; margin: 0 auto;">
		</div>
		<div class="row">
			<div class="col-md-offset-4 col-md-4 col-xs-offset-0 col-xs-12 col-sm-offset-0 col-sm-12">
				<form action="j_spring_security_check" method="post">
					<c:if test="${not empty param.login_error}">
						<p class="text-danger"><s:text name="core.authenticationFailureMessage" /></p>
					</c:if>
					<div class="form-group ${empty param.login_error ? '' : 'has-error'}">
						<label for="login" class="control-label"><s:text name="core.username" /></label>
						<input type="text" name="j_username" tabindex="1" class="form-control" autofocus="autofocus">
					</div>
					<div class="form-group ${empty param.login_error ? '' : 'has-error'}">
						<label for="password" class="control-label"><s:text name="core.password" /></label>
						<input type="password" name="j_password" tabindex="2" class="form-control" />
					</div>
					<div class="form-group">
						<button type="submit" class="btn btn-primary btn-block">
							<s:text name="core.signin" />
						</button>
					</div>
				</form>
				<ul class="list-unstyled">
					<li><a href="${pageContext.request.contextPath}/forgot-password.action">Forgot password</a></li>
					<li><a href="${pageContext.request.contextPath}/forgot-username.action">Forgot username</a></li>
					<li><a href="${pageContext.request.contextPath}/change-password.action">Change password</a></li>
				</ul>
			</div>
		</div>
	</div>										
</body>
</html>