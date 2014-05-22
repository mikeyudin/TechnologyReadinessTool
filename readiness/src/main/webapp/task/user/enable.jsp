<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><ui:text name="task.user.enable.edit.pageTitle" /></title>

<script type="text/javascript">  
$(function() {  
	var refreshControlsForEnable = function() {
	$("input.DATE").datepicker({
		changeMonth: true,  
		changeYear: true
	});
	
	$("select.user-status").change(function(event){
		var row = $(this).closest("ul");
		var datepicker = row.find("input.hasDatepicker");
		if (event.target.value == "Disabled") {
			datepicker.datepicker("setDate", new Date());
		} else {
			datepicker.datepicker("setDate", "");
			row.find('input[name$=".disableReason"]').removeAttr("disabled").val("");
		}
	});
	};
	refreshControlsForEnable();
	$('#enableForm').bind('formLoaded', refreshControlsForEnable);
});
</script>
</head>
<body>
	<s:form id="enableForm" action="save">
		<ui:taskView taskFlow="${taskFlowData}" value="enableUserGrid" itemProvider="${itemProvider}" var="user" fieldName="users[%{user.userId}]"
			listHeader="core.selectedUsers" nameExpression="%{user.lastName}, %{user.firstName}" fieldSetViewDef="${viewDef}" detailMode="true">
			<ui:entityField code="user">
				<div class="control-group">
					<label for="" class="control-label"><ui:text name="core.name"/></label>
					<div class="controls">
						<input type="text" value="${user.firstName} ${user.lastName}" disabled="disabled">
					</div>
				</div>			
				<s:if test="user.deleteDate neq null">
				   <span class="delete icons-16x16" style="display:inline-block;"></span>
				 </s:if>
			</ui:entityField>
			<ui:entityField code="username">
				<div class="control-group">
					<label for="" class="control-label"><ui:text name="core.username"/></label>
					<div class="controls">
						<input type="text" value="${user.username}" disabled="disabled">
					</div>
				</div>
			</ui:entityField>
			<ui:entityField code="status">
				<div class="control-group">
					<label for="" class="control-label"><ui:text name="core.status"/></label>
					<div class="controls" data-user-id="${user.userId}">
						<s:select class="user-status" list="{'Enabled', 'Disabled'}" name="users[%{user.userId}].enabled" value="%{user.disableDate ? 'Disabled' : 'Enabled'}" />
					</div>
				</div>
			</ui:entityField>
		</ui:taskView>
	</s:form>
</body>
</html>