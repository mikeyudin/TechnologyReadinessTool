<!DOCTYPE html>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title><s:text name="org.details.pageTitle" /></title>
</head>
<body>
	<s:hidden name="orgId" />
	<s:if test="showTaskLinks && hasPermission('core_customer_org_update')">
		<s:a action="detailsEditOrg" style="float:right;">
			<s:param name="orgId" value="orgId" />
			<s:text name="core.edit" />
		</s:a>
	</s:if>
	<ui:fieldset title="Org Details" field="org" viewDef="${viewDef}" readOnly="true" />
	<br>
	<ui:detailset>
		<ui:detailsetRow title="org.details.parentOrgLabel">
			<s:if test="org.parentOrgName.isEmpty">
				<i><s:text name="org.details.noParentMessage" /></i>
			</s:if>
			<s:else>
				<s:property value="org.parentOrgName" />
			</s:else>
		</ui:detailsetRow>
	
		<ui:detailsetRow title="core.participations">
			<s:if test="%{orgPartItemProvider && orgPartScope != null}">
				<ui:datagrid itemProvider="${orgPartItemProvider}" viewDef="${orgPartViewDef}" value="orgPartDataGridState"
					var="orgPart" paging="false" >
					
					<ui:dataGridColumn code="editColumn">
						<s:if test="showTaskLinks && hasPermission('core_customer_org_part_update')">
							<s:a action="detailsEditParticipation" >
								<s:param name="orgId" value="orgId" />
								<s:text name="core.edit" />
							</s:a>
						</s:if>
					</ui:dataGridColumn>
					
					<ui:dataGridColumn code="testAdmin" name="%{orgPartScope.scopeTypeName}" >
						<s:property value="orgPart.scopeName" />
					</ui:dataGridColumn>
					<ui:dataGridColumn code="participating" nameKey="core.participating" >
						<s:property value="orgPart.participating" />
					</ui:dataGridColumn>
				</ui:datagrid>
			</s:if>
			<s:if test="showTaskLinks && hasPermission('core_customer_org_part_update')">
				<s:a action="detailsAddParticipation" class="btn btn-xs btn-success ">
					<s:param name="orgId" value="orgId" />
					<s:text name="core.addParticipation" />
				</s:a>
			</s:if>
			<s:a cssClass="show-all" namespace="/filterList" action="update">
				<s:param name="dataGridId" value="'orgPartDataGridState'" />
				<s:param name="updateField" value="'currentScope'" />
				<s:param name="viewDefType" value="'ORG_PART_DATAGRID'"/>
				<s:param name="filters.currentScope" value="false" />
				<s:text name="core.showAll" />
			</s:a>
			<br>
		</ui:detailsetRow>
		
		<ui:detailsetRow title="core.contacts">
			<s:if test="%{contactItemProvider}">
				<ui:datagrid itemProvider="${contactItemProvider}" viewDef="${contactsViewDef}" value="contactDataGridState"
					var="contact" paging="false" rowValue="contactId" columnSelectable="true">
				</ui:datagrid>
			</s:if>
			<s:else>
				<i><s:text name="core.noContactsForOrg" /></i>
			</s:else>
			<br>
			<s:if test="showTaskLinks && hasPermission('core_customer_org_contact_update')">
				<s:a action="detailsAddContact" class="btn btn-xs btn-success">
					<s:param name="orgId" value="orgId" />
					<s:text name="core.manageContacts" />
				</s:a>
			</s:if>
		</ui:detailsetRow>
	</ui:detailset>
</body>
</html>
