<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title><s:text name="task.device.importdevices.pageTitle" /></title>
</head>
<body>
<h3> <s:property value="message" /></h3> 
<s:form action="importDevices" enctype="multipart/form-data" method="POST">
   <ui:taskView taskFlow="${taskFlowData}" fieldName="device" detailMode="true" >
        <ui:taskNavigation saveButton="Import File" resetButton="Reset" />
        <ui:entityField code="import" name="Import" displayInDetail="true">
            <s:file name="importedFile" label="Select File"/>
            <s:hidden name="submitted" value="true" />
        </ui:entityField>
    </ui:taskView>
</s:form>

</body>
</html>