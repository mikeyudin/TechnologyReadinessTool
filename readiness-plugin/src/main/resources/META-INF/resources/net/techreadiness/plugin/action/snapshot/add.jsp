<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title><s:text name="ready.task.snapshot.add.title" /></title>
</head>
<body>
<s:form id="snapshotForm" action="save" name="updateForm" >
	<ui:taskView taskFlow="${taskFlowData}" fieldName="snapshotWindow" fieldSetViewDef="${viewDef}" detailMode="true">
		<ui:taskNavigation saveButton="core.create" />
	</ui:taskView>
</s:form>
</body>
</html>