<!DOCTYPE HTML>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>

<html>
<head>
<title><s:text name="core.technologyReadinessTool" />: <decorator:title /></title>
	
	<h:staticResources />
	<decorator:head />
	<script type="text/javascript">contextPath='${pageContext.request.contextPath}';</script>

	<link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/program/readiness.ico">
</head>
<body>
	<div class="navigation-object-alpha ui-helper-clearfix header-links" style="float: left; padding-left: 1.5em;">
		<ul>
			<li>
				<c:out value="${serviceContext.scopeName}" />
			</li>
			<li>
				<c:out value="${serviceContext.orgName}" />
		    </li>
		</ul>
	</div>
	<div class="navigation-object-alpha ui-helper-clearfix header-links" style="float: right;">
		<ul>

			<li>
				<s:text name="header.welcomeUser">
					<s:param value="serviceContext.user.firstName" />
					<s:param value="serviceContext.user.lastName" />
				</s:text>
			</li>
			<li>
				<s:url namespace="/" action="help" var="helpUrl">
					<s:param name="ajax" value="true"/>
					<s:param name="helpUrl" value="request.requestURI"/>
				</s:url>
				<a target="help_window" href="${helpUrl}" >
					<img src="${pageContext.request.contextPath}/static/images/icons/help.png" alt="Help" />
				</a>
			</li>
			<li>
				<s:text name="ready.link.answercenter.url" var="answerCenterUrl"/>
				<a href="${answerCenterUrl}" target="answer_center">
					<ui:text name="ready.link.answercenter.text"/>
				</a>
			</li>
		</ul>
	</div>
	<div id="header-beta" class="ui-helper-clearfix" style="clear: both; position: relative; margin: 0 0 0 20px;">
		<div id="header-column-alpha">
			<s:a href="%{request.contextPath}/info.action" id="logo-link-beta" title="Go to Home Page" accesskey="1">
				<img src="${pageContext.request.contextPath}/static/images/program/readiness-logo.png" alt="Go to Home Page" />
			</s:a>
		</div>
	</div>

	<div class="task-container ui-helper-clearfix">
		<decorator:body />
	</div>
	<div id="footer-alpha" class="footer ui-helper-clearfix">
                <div class="text-center readiness-logos">
                    <img src="${pageContext.request.contextPath}/static/images/nysed.jpg" /> 
		</div>
		<%--div id="container-content-beta">
			<a href="http://www.k12.wa.us/smarter" target="_blank" class="logo logo-sbac"></a>
			<a href="http://www.parcconline.org" target="_blank" class="logo logo-parcc"></a>
			<a href="http://www.setda.org" target="_blank" class="logo logo-setda"></a>
		</div--%>
	</div>

</body>
</html>
