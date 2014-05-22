<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title><s:text name="ready.task.snapshot.edit.title" />
</title>
</head>
<body>
<s:form action="save">
    <ui:taskView taskFlow="${taskFlowData}" fieldName="snapshots[%{snapshotWindow.snapshotWindowId}]" itemProvider="${snapshotWindowsByIdItemProvider}" value="editSnapshotWindowDataGrid" 
    selection="${taskFlowData.snapshots}" detailMode="false" var="snapshotWindow" dataGridViewDef="${dataGridViewDef}" fieldSetViewDef="${detailsViewDef}" 
    listHeader="ready.tab.snapshots.title" nameExpression="%{snapshotWindow.name}" hideNameColumnInGrid="true">
    </ui:taskView>
</s:form>
</body>
</html>