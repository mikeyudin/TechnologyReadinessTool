<!DOCTYPE html>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><s:text name="user.details.pageTitle" /></title>
<script type="text/javascript">
function expandEmailText() {
  $(".email-info").slideToggle();
}
</script>
</head>
<body>
	<s:hidden name="userId" />
	<s:if test="showUserEditLink">
		<s:a action="detailsUserEdit" style="float:right;">
			<s:param name="userId" value="%{userId}" />
			<s:text name="core.editUser" />
		</s:a>
	</s:if>
	<s:if test="user.deleteDate neq null">
	  <br/><br/>
	  <span class="delete icons-16x16 inline-icon-right"></span>
	</s:if>
	<ui:fieldset title="User Details" field="user" viewDef="${viewDef}" readOnly="true">
	 
	  <ui:secured hasPermission="${coreCustomerUserPasswordTokenAccess}">
	    <ui:fieldsetRow id="passwordToken" name="PasswordToken" code="passwordToken" displayOrder="100">
	  		<s:label value="Password Token"/>
	  		<a href="mailto:${user.email}?subject=Password Reset&body=${emailInfo}">Send Email</a>
	  		|
	  		<a href="#" onClick="javascript:expandEmailText();" class="filter-expand-link">More</a>
	  		<div class="email-info expand-email-link" style="display:none;"><hr><a href="#" onClick="javascript:expandEmailText();">Close</a><br />${emailInfoHTML}<br/><hr></div>
	    </ui:fieldsetRow>
	  </ui:secured>
	  
	</ui:fieldset>
   
	<ui:detailset>
		<ui:detailsetRow title="user.details.org.header">
			<ui:datagrid itemProvider="${orgGridItemProvider}" viewDef="${orgViewDef}" value="orgDataGridState" var="userOrg"
				paging="false">
				
				<ui:dataGridColumn code="editColumn">
					<s:if test="showOrgTaskLinks">
						<s:a action="detailsOrgAssignments" >
						<s:param name="userId" value="userId" />
						<s:param name="orgId" value="userOrg.orgId" />
							<s:text name="core.edit" />
						</s:a>
					</s:if>
				</ui:dataGridColumn>
				
				<ui:dataGridColumn code="orgName" nameKey="core.organization">
					<s:property value="userOrg.name" />
				</ui:dataGridColumn>
				<ui:dataGridColumn code="orgCode" nameKey="core.code">
					<s:property value="userOrg.code" />
				</ui:dataGridColumn>
			</ui:datagrid>
			<s:a action="detailsAddOrgAssignments" class="start-tasks-button btn btn-xs btn-success">
				<s:param name="userId" value="userId" />
				<s:text name="core.addAssignment" />
			</s:a>
			<s:a cssClass="show-all" namespace="/filterList" action="update">
				<s:param name="dataGridId" value="'orgDataGridState'" />
				<s:param name="updateField" value="'currentScope'" />
				<s:param name="viewDefType" value="'ORG_DATAGRID'"/>
				<s:param name="filters.currentScope" value="false" />
				<s:text name="core.showAll" />
			</s:a>
		</ui:detailsetRow>
		
		<ui:detailsetRow title="user.details.role.header">
			<ui:datagrid itemProvider="${roleGridItemProvider}" viewDef="${roleViewDef}" value="roleDataGridState" var="userRole"
				paging="false" >
				
				<ui:dataGridColumn code="editColumn">
					<s:if test="showRoleTaskLinks">
						<s:a action="detailsRoleAssignments" >
						<s:param name="userId" value="userId" />
						<s:param name="roleId" value="userRole.roleId" />
							<s:text name="core.edit" />
						</s:a>
					</s:if>
				</ui:dataGridColumn>
				
				<ui:dataGridColumn code="role" nameKey="core.role">
					<s:property value="userRole.name" />
				</ui:dataGridColumn>
			</ui:datagrid>
			<s:a action="detailsAddRoleAssignments" class="start-tasks-button btn btn-xs btn-success">
				<s:param name="userId" value="userId" />
				<s:text name="core.addRole" />
			</s:a>
			<s:a cssClass="show-all" namespace="/filterList" action="update">
				<s:param name="dataGridId" value="'roleDataGridState'" />
				<s:param name="updateField" value="'currentScope'" />
				<s:param name="viewDefType" value="'ROLE_DATAGRID'"/>
				<s:param name="filters.currentScope" value="false" />
				<s:text name="core.showAll" />
			</s:a>
		</ui:detailsetRow>
	</ui:detailset>
</body>
</html>