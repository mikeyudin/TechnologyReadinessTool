<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><ui:text name="your.account" /></title>

<script type="text/javascript">
	$(function() {
		$(".datepicker").datepicker({
			changeMonth : true,
			changeYear : true
		});
	});
</script>
</head>
<body>
	<s:form action="save">
		<ui:taskView taskFlow="${taskFlowData}" value="editYourAcccountGrid" itemProvider="${itemProvider}" detailMode="true" var="user" fieldName="users['%{user.userId}']"
			nameExpression="%{user.lastName}, %{user.firstName}" dataGridViewDef="${gridViewDef}" fieldSetViewDef="${detailsViewDef}"
			listHeader="core.selectedUsers" hideNameColumnInGrid="true">
			<ui:toolbar>
				<s:submit action="save" cssClass="btn btn-default btn-xs"><ui:text name="core.save" /></s:submit>
			</ui:toolbar>
			<ui:entityField code="username">
				<s:textfield name="user.username" value="%{user.username}"
					required="true" label="Username" readonly="true"/>
			</ui:entityField>
			<ui:entityField code="firstName">
				<s:textfield name="user.firstName" label="First Name" required="true" />
			</ui:entityField>
			<ui:entityField code="lastName" >
				<s:textfield name="user.lastName" label="Last Name" required="true" />
			</ui:entityField>
			<ui:entityField code="email" >
				<s:textfield name="user.email" label="Email" required="true"/>
			</ui:entityField>
		</ui:taskView>
	</s:form>
</body>
</html>