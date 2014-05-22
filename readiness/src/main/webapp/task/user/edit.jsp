<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><s:text name="task.user.enable.edit.pageTitle" /></title>
<script type="text/javascript">  
$(function() {  
	var refreshControlsForEdit = function() {
	$("input.DATE").datepicker({
		changeMonth: true,  
		changeYear: true
	});
	};
	refreshControlsForEdit();
	$('#editForm').bind('formLoaded', refreshControlsForEdit);
});
</script>
</head>
<body>
	<s:form id="editForm" action="save">
		<ui:taskView taskFlow="${taskFlowData}" value="editUserGrid" itemProvider="${itemProvider}" detailMode="true" var="user" fieldName="users[%{user.userId}]"
			nameExpression="%{user.lastName}, %{user.firstName}" dataGridViewDef="${gridViewDef}" fieldSetViewDef="${detailsViewDef}"
			listHeader="core.selectedUsers" hideNameColumnInGrid="true">
			
			<ui:entityField code="userId">
            	<s:hidden name="users['%{user.userId}'].userId" value="%{userId}" />
            </ui:entityField>
            
            
            <ui:entityField code="username" name="Username" required="true"> 
                <s:textfield id="users%{user.userId}username" name="users[%{user.userId}].username" value="%{user.username}" label="Username" required="true" readonly="true"/>   
 				   <s:if test="user.deleteDate neq null">
				  	  	<span class="delete icons-16x16" style="display:inline-block;"></span>
				   </s:if>
			</ui:entityField>			
            
			

			
		</ui:taskView>
	</s:form>
</body>
</html>