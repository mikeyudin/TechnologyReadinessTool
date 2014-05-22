<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>

<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title><s:text name="not.authorized.page.title" /></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<div class="row" style="margin-top: 50px; margin-bottom: 25px;">
			<img alt="Technology Readiness Tool Logo" src="${pageContext.request.contextPath}/static/images/program/readiness-logo.png" style="display: block; margin: 0 auto;">
		</div>
		<div class="row">
			<div class="col-md-offset-2 col-md-8 col-xs-offset-0 col-xs-12 col-sm-offset-0 col-sm-12">
				<div class="jumbotron">
					<h1><s:text name="not.authorized.header" /></h1>
					<p><s:text name="not.authorized.message" /></p>
				</div>
			</div>
		</div>
	</div>										
</body>
</html>