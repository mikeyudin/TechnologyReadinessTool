<!DOCTYPE HTML>
<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ui:dataView taskFlow="${taskFlow}" title="%{getText('ready.tab.device.title')}">
    <ui:dataViewFilters viewDefType="DEVICE_DATAGRID">
        <s:if test="serviceContext.userName.contains('admin')">
			<ui:dataViewFilter name="%{getText('core.organization')}" code="organization" beanName="orgSelectionFilterForDeviceList" valueKey="orgId" primary="true"/>
        </s:if>
    </ui:dataViewFilters>

    <ui:shoppingCart name="Devices">
		<s:property value="name" />
    </ui:shoppingCart>

    <ui:datagrid value="deviceSearchGrid" var="device" selectable="true" columnSelectable="true" rowValue="deviceId" viewDef="${viewDefinition}" itemProvider="${deviceItemProvider}">
            <ui:dataGridColumn code="organization" name="Organization">
            	<s:property value="device.org.name" />
            </ui:dataGridColumn>
    </ui:datagrid>
</ui:dataView>