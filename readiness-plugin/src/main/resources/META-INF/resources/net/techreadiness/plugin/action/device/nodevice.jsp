<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><s:text name="core.error" /></title>
</head>
<body>
	<s:form action="list" namespace="/device">
		<ui:taskView taskFlow="${taskFlowData}" suppressSave="true">
			<span class="icons-abnormal-sizes inline-icon-left warningBig"></span>
			NO DEVICES SELECTED
		</ui:taskView>
	</s:form>
</body>
</html>