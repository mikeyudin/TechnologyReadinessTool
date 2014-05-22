<!DOCTYPE html>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title>Exception Report Types</title>
</head>
<body>
	<ui:detailset>
	<ui:detailsetRow title="Exception Report Types">
	<table class="exceptionTypes">
		<tr>
			<th style="width: 250px"><b>Exception Name</b></th>
			<th style="width: 375px"><b>Description</b></th>
		</tr>
		<tr><td><br/></td></tr>
		<tr>
			<td align="right">Not Complete</td>
			<td>Data entry complete indicator is not marked Yes</td>
		</tr>
		<tr><td><br/></td></tr>
		<tr>
			<td align="right">No Device Entry</td>
			<td>No device data have been entered</td>
		</tr>
		<tr><td><br/></td></tr>
		<tr>
			<td align="right">No Activity</td>
			<td>No device or readiness survey question data have been entered</td>
		</tr>
		<tr><td><br/></td></tr>
		<tr>
			<td align="right">Potentially Missed Survey Questions</td>
			<td>Some device data have been entered, but no readiness survey question data have been entered</td> 
		</tr>
		<tr><td><br/></td></tr>
		<tr>
			<td align="right">Potentially Missed Device Entry</td>
			<td>Some readiness survey question data have been entered, but no device data have been entered</td>
		</tr>
		<tr><td><br/></td></tr>
		<tr>
			<td align="right">Potentially Complete</td>
			<td>Device and readiness survey question data have been entered, but data entry complete indicator is not marked Yes</td>
		</tr>
		<tr><td><br/></td></tr>
		<tr>
			<td align="right">Potentially Not Complete</td>
			<td>Data entry complete indicator is marked Yes, but some readiness survey question and device data have not been entered</td>
		</tr>
		<tr><td><br/></td></tr>
	</table>
	</ui:detailsetRow>
	</ui:detailset>
</body>
</html>