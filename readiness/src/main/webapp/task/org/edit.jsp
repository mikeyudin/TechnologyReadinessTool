<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><s:text name="organization.edit.pageTitle" /></title>
</head>
<body>
	<s:form id="editOrgsForm" action="save">
		<s:a id="updateForm" style="display:none" action="updateForm"></s:a>
				
		<ui:taskView taskFlow="${taskFlowData}" value="editOrgsDataGrid" itemProvider="${orgsByIdItemProvider}" var="org" detailMode="true" fieldSetViewDef="${detailsViewDef}"
			fieldName="orgs[%{orgId}]" listHeader="core.name" hideNameColumnInGrid="true" nameExpression="%{org.name}">
			
			<ui:entityField code="org">
				
		    	<ui:autocompleteField labelKey="task.org.new.parentOrganization" name="org" namespace="/multiOrganizationControl"
		    	 loadAction="ajaxOrgLoad" showAction="show" required="false" instanceId="${orgId}" multiple="false"/>
			</ui:entityField>
		
			<ui:entityField code="orgType" name="Org Type" >
				<div class="control-group" >
					<label for="orgType" class="control-label">
						Organization Type
					</label>
					<div class="controls">
						<input type="hidden" name="orgs[${org.orgId}]" value="${org.orgId}">
						<input type="text" disabled value="${org.orgTypeName}" >
					</div>
				</div>
			</ui:entityField>
		</ui:taskView>
	</s:form>
</body>
</html>