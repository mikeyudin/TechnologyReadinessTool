<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title><s:text name="task.device.exportdevices.pageTitle" /></title>
</head>
<body>
<h3>If there were code that exported devices this is where we'd put it!</h3>
<s:form action="exportDevices">
   <ui:taskView taskFlow="${taskFlowData}" fieldName="device" detailMode="true" >
        <ui:taskNavigation saveButton="Export File" resetButton="Reset" />
    </ui:taskView>
</s:form>
</body>
</html>