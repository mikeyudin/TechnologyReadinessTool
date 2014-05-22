<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head>
</head>
<body>
	<ui:dataView taskFlow="${taskFlow}" title="%{getText('ready.tab.snapshots.title')}">
    	<ui:dataViewFilters viewDefType="SNAPSHOT_DATAGRID">
        
    	</ui:dataViewFilters>

    	<ui:shoppingCart name="Snapshots">
			<s:property value="name" />
    	</ui:shoppingCart>

    	<ui:datagrid value="snapshotSearchGrid" viewDef="${dataGridView}" var="snapshot" selectable="true" columnSelectable="true" rowValue="snapshotWindowId" itemProvider="${snapshotItemProvider}">
    	</ui:datagrid>
	</ui:dataView>
</body>