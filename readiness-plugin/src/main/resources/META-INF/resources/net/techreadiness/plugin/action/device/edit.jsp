<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
<title><s:text name="task.device.edit.pageTitle" /></title>
</head>
<body>
<s:form action="save">
    <ui:taskView taskFlow="${taskFlowData}" fieldName="devices[%{device.deviceId}]" itemProvider="${deviceItemProvider}" value="deviceEditGrid" 
    selection="${taskFlowData.devices}" detailMode="false" var="device" dataGridViewDef="${viewDef}" fieldSetViewDef="${detailsViewDef}" 
    listHeader="core.device" nameExpression="%{device.name}" hideNameColumnInGrid="true">
        <ui:taskNavigation />
        <ui:entityField code="org" name="%{getText('core.organization')}" >
        	<div class="control-group">
        		<c:if test="${deviceEditGrid.detailMode}">
        			<label for="${device.deviceId}-org" class="control-label"><s:text name="core.organization" /></label>
        		</c:if>
				<div class="controls">
					<s:textfield id="%{device.deviceId}-org" name="devices[%{device.deviceId}].org.name" readonly="true" value="%{device.org.name}" />
				</div>
			</div>
        	
        	<s:hidden name="devices[%{device.deviceId}].org.orgId" value="%{device.org.orgId}" />
        	<s:hidden name="devices[%{device.deviceId}].deviceId" value="%{device.deviceId}" />
        </ui:entityField>
    </ui:taskView>
</s:form>
</body>
</html>