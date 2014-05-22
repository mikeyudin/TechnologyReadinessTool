<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><s:text name="user.youraccount.pageTitle" /></title>
</head>
<body>
	<a href="${pageContext.request.contextPath}/user/edit-account.action" class="btn btn-xs btn-primary">Edit Account</a>
	<a href="${pageContext.request.contextPath}/user/change-password.action" class="btn btn-xs btn-default">Change Password</a>
	<ui:detailset>
		<ui:detailsetRow title="core.user">
			<ui:fieldset title="Your Account" field="user" viewDef="${viewDef}"
				readOnly="true" />
		</ui:detailsetRow>

		<ui:detailsetRow title="user.details.org.header">
			<ui:datagrid itemProvider="${orgByUserItemProvider}"
				viewDef="${orgViewDef}" value="orgDataGridState" var="userOrg"
				paging="false" columnSelectable="false">
				<s:if test="showOrgTaskLinks">
					<ui:dataGridColumn code="editColumn">
						<s:a action="detailsOrgAssignments">
							<s:param name="userId" value="userId" />
							<s:param name="orgId" value="userOrg.orgId" />
							<s:text name="core.edit" />
						</s:a>
					</ui:dataGridColumn>
				</s:if>
				<ui:dataGridColumn code="orgName" nameKey="core.organization">
					<s:property value="userOrg.name" />
				</ui:dataGridColumn>
				<ui:dataGridColumn code="orgCode" nameKey="core.code">
					<s:property value="userOrg.code" />
				</ui:dataGridColumn>
			</ui:datagrid>
			<s:a cssClass="show-all btn btn-xs btn-default"
				namespace="/filterList" action="update">
				<s:param name="dataGridId" value="'orgDataGridState'" />
				<s:param name="updateField" value="'currentScope'" />
				<s:param name="viewDefType" value="'ORG_DATAGRID'" />
				<s:param name="filters.currentScope" value="false" />
				<s:text name="core.showAll" />
			</s:a>
			<br>
		</ui:detailsetRow>

		<ui:detailsetRow title="user.details.role.header">
			<ui:datagrid itemProvider="${roleByUserItemProvider}"
				viewDef="${roleViewDef}" value="roleDataGridState" var="userRole"
				paging="false" columnSelectable="false">
				<s:if test="showRoleTaskLinks">
					<ui:dataGridColumn code="editColumn">
						<s:a action="detailsRoleAssignments">
							<s:param name="userId" value="userId" />
							<s:param name="roleId" value="userRole.roleId" />
							<s:text name="core.edit" />
						</s:a>
					</ui:dataGridColumn>
				</s:if>
				<ui:dataGridColumn code="role" nameKey="core.role">
					<s:property value="userRole.name" />
				</ui:dataGridColumn>
			</ui:datagrid>
			<s:a cssClass="show-all btn btn-xs btn-default"
				namespace="/filterList" action="update">
				<s:param name="dataGridId" value="'roleDataGridState'" />
				<s:param name="updateField" value="'currentScope'" />
				<s:param name="viewDefType" value="'ROLE_DATAGRID'" />
				<s:param name="filters.currentScope" value="false" />
				<s:text name="core.showAll" />
			</s:a>
		</ui:detailsetRow>
	</ui:detailset>
</body>
</html>