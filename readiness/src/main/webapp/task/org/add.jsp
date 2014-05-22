<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><s:text name="organization.edit.pageTitle" /></title>
<script type="text/javascript">
	jQuery.fn.organizationReloadOrgTypes = function() {
		defaultAjax = {
			type : 'POST',
			url : $('form#organizationForm').attr('name'),
			data : {
				ajax: true
			},
			success : function(response) {
				$('#orgTypes').replaceWith($(response).find('#orgTypes'));
			},
			error : function(response) {
				$('body').html(response.responseText);
			}
		};
		$.ajax(defaultAjax);
	};
	$(function() {	
		$('body').bind('refreshControls',jQuery.fn.organizationReloadOrgTypes);
	});
</script>
</head>
<body>
<s:form id="organizationForm" action="save" name="updateForm" >
	<ui:taskView taskFlow="${taskFlowData}" fieldName="organization" fieldSetViewDef="${viewDef}" selection="${taskFlowData.orgs}"
		selectionDisplayExpression="%{name}" selectionTitle="core.selectedOrganizations" detailMode="true">
		<ui:taskNavigation saveButton="core.create" />
		
		<ui:entityField code="org">
		    <ui:autocompleteField labelKey="task.org.new.parentOrganization" name="org" namespace="/organizationControl"
		     loadAction="ajaxOrgLoad" showAction="show" required="false" multiple="false"/>
		</ui:entityField>
		
		<ui:entityField code="orgTypeId">
			<div class="control-group">
				<label for="orgTypes" class="control-label">Organization Type <span class="required">*</span></label>
				<div class="controls">
					<s:select id="orgTypes" name="organization.orgTypeId" list="orgTypes" listKey="key" listValue="value" emptyOption="false" />
				</div>
			</div>
		</ui:entityField>
	</ui:taskView>
</s:form>
</body>
</html>