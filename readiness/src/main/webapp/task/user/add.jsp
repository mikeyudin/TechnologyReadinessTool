<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<head>
<title><s:text name="task.user.add.pageTitle" /></title>
<script type="text/javascript">

	$(function() {	
		$(".datepicker").datepicker({
			changeMonth : true,
			changeYear : true
		});
		
		$('input[name="userMap.username"]').attr('disabled', 'disabled');
		$('input[name="userMap.username"]').after('<li class="fieldset-row"><label/><span class="field-label"><s:text name="task.user.add.enteredEmail" /></span></li>');
		$('input[name="userMap.username"]').after(' <a href="#"><img id="unlockUsername" src="${pageContext.request.contextPath}/static/images/icons/resetpassword.png"/></a>');
		var copyEmailFunction = function() {
		    var $this= $(this);
		    window.setTimeout(function() {
		    	 if ($('input[name="userMap.username"]').is(':disabled')) { 
		    	  $('input[name="userMap.username"]').val($this.val());
		    	 }
		    }, 0);
		};
		var unlockUsernameFunction = function() {
			$('input[name="userMap.username"]').removeAttr('disabled');
			$('#unlockUsername').attr("src","${pageContext.request.contextPath}/static/images/icons/unlock.png");
			$('#usernameLocked').val('true');
		}
		$('input[name="userMap.email"]').keydown(copyEmailFunction);
		$('input[name="userMap.email"]').click(copyEmailFunction);
		$('input[name="userMap.email"]').change(copyEmailFunction);
		$('input[name="userMap.email"]').bind('paste', copyEmailFunction);
		$('#unlockUsername').click(unlockUsernameFunction);
		$(document).ready(function() {
			if($('#usernameLocked').val() == 'true') {
				unlockUsernameFunction();
				return;
			}
			var email = $('input[name="userMap.email"]').val();
			if ($('input[name="userMap.username"]').is(':disabled')) { 
		    	  $('input[name="userMap.username"]').val(email);
		    	 }
			});
	});	
</script>
</head>
<s:form id="saveForm" action="save">
	<ui:taskView taskFlow="${taskFlowData}" detailMode="true"
		fieldName="userMap" fieldSetViewDef="${viewDef}"
		selection="${taskFlowData.users}" selectionTitle="core.selectedUsers"
		selectionDisplayExpression="%{lastName}, %{firstName}">
		<ui:taskNavigation saveButton="core.create" />

	    <ui:entityField code="role">
		    <ui:autocompleteField labelKey="task.user.new.selectRoles" name="role" namespace="/roleControl"
		     loadAction="ajaxRoleLoad" showAction="show" required="true" multiple="true"/>
		     <s:hidden id="usernameLocked" name="usernameLocked"/>	
		</ui:entityField>			
		<ui:entityField code="org">
		    <ui:autocompleteField labelKey="task.user.new.selectOrgs" name="org" namespace="/organizationControl"
		     loadAction="ajaxOrgLoad" showAction="show" required="true" multiple="true"/>
		</ui:entityField>
	
	</ui:taskView>

</s:form>