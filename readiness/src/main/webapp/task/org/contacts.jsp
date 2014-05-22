<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<title><s:text name="task.org.contacts.pageTitle" /></title>
</head>
<body>
<s:form action="save">
	<ui:taskView taskFlow="${taskFlowData}" detailMode="false">
		<s:hidden name="orgSelection" value="%{orgSelection}" />
		<table class="org-contacts" style="width: 100%;">
			<tr>
				<td style="width:1%">
					<div id="vertical-tabs">
						<ul class="task-detail-options">
							<c:forEach items="${taskFlowData.orgs}" var="org" varStatus="orgStatus">
								<li class="${orgStatus.index == orgSelection ? 'selected' : ''}" data-index="${orgStatus.index}">
									<s:a action="edit" title="%{#attr['org'].name}">
										<c:out value="${ui:abbreviate(org.name, 25)}" />
										<s:param name="orgSelection" value="#attr.orgStatus.index" />
									</s:a>
								</li>
							</c:forEach>
						</ul>
					</div>
				</td>
				<td class="task-detail">
					<ui:datagrid value="contactsDataGrid" itemProvider="${orgContactTaskItemProvider}" showRequired="true"
			             viewDef="${viewDef}" paging="false" var="contact" multiEditable="true" columnSelectable="true"
				         fieldName="contacts['%{contact['contactTypeId']}']" rowValue="contact['contactTypeId']">
	
						<ui:dataGridColumn code="contactTypeName" nameKey="core.type">
							<s:property value="contact['contactTypeName']" />
							<s:hidden name="contacts['%{contact['contactTypeId']}'].contactTypeName" value="%{contact['contactTypeName']}" />
							<s:hidden name="contacts['%{contact['contactTypeId']}'].contactTypeId" value="%{contact['contactTypeId']}" />
							<s:hidden name="contacts['%{contact['contactTypeId']}'].contactId" value="%{contact['contactId']}" />
							<s:hidden name="contacts['%{contact['contactTypeId']}'].orgId" value="%{contact['orgId']}" />
						</ui:dataGridColumn>
					</ui:datagrid>
				</td>
			</tr>
		</table>
	</ui:taskView>
</s:form>
</body>
</html>