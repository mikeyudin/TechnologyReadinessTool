<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<title><s:text name="task.org.network.pageTitle" /></title>
</head>
<body>
<s:form action="save">
    <ui:taskView taskFlow="${taskFlowData}" fieldName="orgs[%{org.orgId}]" itemProvider="${orgNetworkItemProvider}" value="orgNetworkGrid"
    selection="${taskFlowData.orgs}" var="org" fieldSetViewDef="${viewDef}" detailMode="true"
    listHeader="core.org" nameExpression="%{org.name}" >
      <ui:taskNavigation />
         
         <ui:entityField code="autoButton" name="">
       		<s:hidden name="orgs[%{org.orgId}]" value="%{org.orgId}" />
        </ui:entityField>
      </ui:taskView>
</s:form>
</body>
</html>