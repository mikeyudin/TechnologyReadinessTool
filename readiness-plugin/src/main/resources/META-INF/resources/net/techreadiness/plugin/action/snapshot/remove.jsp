<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title><s:text name="ready.task.snapshot.delete.title" /></title>
</head>
<body>
<s:form id="snapshotForm" action="save">
    <ui:taskView taskFlow="${taskFlowData}" dataGridViewDef="${dataGridViewDef}" itemProvider="${snapshotWindowsByIdItemProvider}" detailMode="false" value="snapshotRemoveGrid" var="snapshotWindow" fieldName="snapshots['%{snapshotWindow.snapshotWindowId}']">
        <ui:taskNavigation saveButton="core.delete" />
        <ui:entityField code="delete" displayOrder="first">
            <c:choose>
            	<c:when test="${snapshotWindow.visible}">
            		<c:out value="Cannot delete (marked for Progress Report)"/>
            	</c:when>
            	<c:otherwise>
            		<s:checkbox name="snapshots[%{snapshotWindow.snapshotWindowId}]" />
            	</c:otherwise>
            </c:choose>
        </ui:entityField>
        <ui:entityField code="name">
        	<s:property value="snapshotWindow.name"/>
        </ui:entityField>
        <ui:entityField code="visible">
        	<s:checkbox name="visible" disabled="true" value="%{snapshotWindow.visible}" />
        </ui:entityField>
    </ui:taskView>
</s:form>
</body>
</html>