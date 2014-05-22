<!DOCTYPE html>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components" %>

<html>
<head>
<title>File Loading</title>
</head>
<body>
	<ui:dataView taskFlow="${taskFlow}" filterSelectable="true" title="%{getText('tab.fileBatch.title')}">
		<ui:dataViewFilters>
			<ui:dataViewFilter name="%{getText('core.fileType')}" code="fileType" beanName="fileTypeSelectionHandler" valueKey="fileTypeId" nameKey="name"/>
			<ui:dataViewFilter name="%{getText('core.fileStatus')}" code="fileStatus" beanName="fileStatusSelectionHandler" valueKey="ordinal" nameKey="name"/>
		</ui:dataViewFilters>
		<ui:shoppingCart name="Files">
			<s:property value="displayFilename" />
		</ui:shoppingCart>

		<ui:datagrid title="Files" value="fileDataGrid" var="file"
			itemProvider="${itemProvider}" selectable="true" rowValue="id"
			columnSelectable="true">

			<ui:dataGridColumn code="id" hidden="true">
				<s:hidden name="file.id" />
			</ui:dataGridColumn>

			<ui:dataGridColumn code="name" name="Filename">

				<c:choose>
					<c:when test="${not empty file.fileId}">
						<s:a action="viewFileDetails">
                           	<s:param name="id" value="file.fileId"/>
							<c:out value="${file.displayFilename}" />
						</s:a>
					</c:when>
					<c:otherwise>
						<s:property value="file.displayFilename" />
					</c:otherwise>
				</c:choose>

			</ui:dataGridColumn>

			<ui:dataGridColumn code="type" name="Type">
				<s:text name="core.fileTypeName.%{file.fileTypeCode.toLowerCase()}" />
			</ui:dataGridColumn>

			<ui:dataGridColumn code="status" name="Status">
				<c:choose>
					<c:when
						test="${file.status eq 'errors' and
									not empty file.errorMessageFilename}">
						<s:a action="download">
							<s:param name="fileId" value="file.fileId" />
							<s:param name="fileType" value="'m'" />
							<c:out value="${file.status}" />
						</s:a>
					</c:when>
					<c:otherwise>
						<c:out value="${file.status}" />
					</c:otherwise>
				</c:choose>
			</ui:dataGridColumn>

			<ui:dataGridColumn code="message" name="Results">
				<c:choose>
					<c:when
						test="${file.status eq 'errors' and
									not empty file.errorDataFilename}">
						<s:a action="download">
							<s:param name="fileId" value="file.fileId" />
							<s:param name="fileType" value="'d'" />
							<c:out value="${file.statusMessage}" />
						</s:a>
					</c:when>
					<c:otherwise>
						<c:out value="${file.statusMessage}" />
					</c:otherwise>
				</c:choose>
			</ui:dataGridColumn>
			
			<ui:dataGridColumn code="recordCount" nameKey="core.recordCount">
				<c:out value="${file.totalRecordCount}" />
			</ui:dataGridColumn>

			<ui:dataGridColumn code="sentDate" name="Date">
            	<fmt:formatDate value="${file.requestDate}" type="both"/>
			</ui:dataGridColumn>

			<ui:dataGridColumn code="org" name="Organization">
				<c:out value="${file.org.name}" />
			</ui:dataGridColumn>


			<ui:dataGridColumn code="sentBy" name="User">
				<c:out value="${file.username}" />
			</ui:dataGridColumn>

		</ui:datagrid>
	</ui:dataView>
</body>
</html>
