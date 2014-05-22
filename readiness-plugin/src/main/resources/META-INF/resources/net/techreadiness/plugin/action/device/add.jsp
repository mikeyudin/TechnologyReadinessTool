<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.text-error {
	color: #FF4136;
}
</style>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/static/min-requirements.js'></script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/static/js/requirements_checker.js'></script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/static/js/matchingRules.js'></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/testnav.js"></script>

<title><ui:text name="ready.add.devices"/></title>
</head>
<body>
	<input type="hidden" name="remoteAddr" value="${request.remoteAddr}">
	<s:form action="create">
		<ui:taskView taskFlow="${taskFlowData}" fieldName="device"
			selection="${taskFlowData.devices}" detailMode="true"
			selectionTitle="ready.devices" selectionDisplayExpression="%{name}"
			fieldSetViewDef="${viewDef}">
			<ui:dataViewControl code="orgs" name="core.organization"
				configLinkName="core.change" namespace="/task/device"
				configAction="showOptions" contentAction="show" side="left"
				multiple="false" promptIfEmpty="true" dataGridId="" />
			<ui:taskNavigation saveButton="ready.create.device" />
			<ui:entityField code="compConf" name="Computer Configuration"
				displayInDetail="true">
				<!-- COMPUTER CONFIGURATION BEGINS -->

				<object id="detectPluginApplet"
					type="application/x-java-applet" height="0" width="0">
					<param name="code" value="net.techreadiness.applet.Detector">
					<param name="archive"
						value="${pageContext.request.contextPath}/static/applets/ReadinessAutoDetectApplet.jar">
				</object>

				<input type="hidden" id="pgContextPath"
					value="${pageContext.request.contextPath}" />
				<input type="hidden" id="dbFlag" value="1" />

				<div>
					<img
						src="${pageContext.request.contextPath}/static/images/autoPop_icon.png"
						id="CCStartIcon" alt="startIcon"
						onclick="javascript:CCStartIcon_click();">
				</div>
				<!-- COMPUTER CONFIGURATION ENDS -->
			</ui:entityField>
			<ui:entityField code="compConfStatus" name="Computer ConfigStatus"
				displayInDetail="true">
				<table style="width: 100%">
					<tr>
						<td><img
							src="${pageContext.request.contextPath}/static/images/sectionStatus_Icon.gif"
							id="CCSectionStatusIcon"></td>
						<td id="CCSectionStatusString"
							style="width: 400px; vertical-align: middle; text-align: right; font-size: 15px; font-weight: bold;">Not
							Started</td>
					</tr>
				</table>
			</ui:entityField>
		</ui:taskView>
	</s:form>
</body>
</html>