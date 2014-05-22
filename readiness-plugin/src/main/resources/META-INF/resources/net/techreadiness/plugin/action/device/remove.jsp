<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title><s:text name="task.device.remove.pageTitle" /></title>
</head>
<body>
<s:form id="deviceForm" action="save">
    <ui:taskView taskFlow="${taskFlowData}" itemProvider="${deviceItemProvider}" value="deviceRemoveGrid" 
     var="device">
        <ui:taskNavigation saveButton="core.delete" />
        <ui:entityField code="delete" displayOrder="first">
        	<s:checkbox name="devices[%{device.deviceId}]" />
        </ui:entityField>
        <ui:entityField code="name" name="Device Name">
        	<s:property value="device.name"/>
        </ui:entityField>
        <ui:entityField code="org" name="%{getText('core.organization')}">
        	<s:property value="device.orgName"/>
        </ui:entityField>
        <ui:entityField code="count" name="%{getText('core.count')}">
        	<s:property value="device.count"/>
        </ui:entityField>
    </ui:taskView>
</s:form>
</body>
</html>