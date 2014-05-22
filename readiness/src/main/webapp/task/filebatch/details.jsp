<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib prefix="comp" uri="http://swiftelan.com/components" %>

<html>
<head>
<title><s:text name="task.filebatch.details.pageTitle" /></title>
<style type="text/css">
.table-file-error {
	display: inline-block;
	width: 50%;
}

.table-file-error > thead > tr > th {
	background: lightgray;
	color: #111;
}

.table-file-error > thead > tr > th,.table-file-error > tbody > tr > td {
	padding: 3px;
}
</style>
</head>
<body>
	<ui:taskView taskFlow="${taskFlowData}" 
		itemProvider="${itemProvider}" detailMode="true" var="file"
		fieldName="files[%{file.fileId}]"
		nameExpression="%{file.displayFilename}"
		listHeader="task.filebatch.details.column.header"
		hideNameColumnInGrid="true" suppressSave="true" >

		<ui:entityField code="filename">
			<s:label cssStyle="font-weight: bold"
				key="task.filebatch.col.filename" />
			<c:if test="${not empty file.filename}">
				<c:choose>
					<c:when
						test="${file.fileTypeName eq 'device_import' or
          				  file.fileTypeName eq 'org_import' or
          				  file.fileTypeName eq 'user_import'}">

						<s:property value="file.displayFilename" />
						<c:if test="${not empty file.filename}">
							<br />
							<s:a action="download" namespace="/filebatch">
								<s:param name="fileId" value="file.fileId" />
								<s:param name="fileType" value="'f'" />
								<s:property value="file.filename" />
							</s:a>
						</c:if>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${file.status eq 'complete' or file.status eq 'stopped' or file.status eq 'errors'}">
								<s:a action="download" namespace="/filebatch">
									<s:param name="fileId" value="file.fileId" />
									<s:param name="fileType" value="'f'" />
									<s:property value="file.filename" />
								</s:a>
							</c:when>
							<c:otherwise>
								<s:property value="file.filename" />
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</c:if>
		</ui:entityField>
		<ui:entityField code="type">
			<s:label cssStyle="font-weight: bold" key="task.filebatch.col.type" />
			<s:property value="file.fileTypeName" />
		</ui:entityField>
		<ui:entityField code="description">
			<s:label cssStyle="font-weight: bold"
				key="task.filebatch.col.description" />
			<s:property value="file.description" />
		</ui:entityField>
		<ui:entityField code="status">
			<s:label cssStyle="font-weight: bold" key="task.filebatch.col.status" />
			<c:choose>
				<c:when
					test="${file.status eq 'errors' and
									not empty file.errorMessageFilename}">
					<s:a action="download" namespace="/filebatch">
						<s:param name="fileId" value="file.fileId" />
						<s:param name="fileType" value="'m'" />
						<s:property value="file.status" />
					</s:a>
				</c:when>
				<c:otherwise>
					<s:property value="file.status" />
				</c:otherwise>
			</c:choose>
		</ui:entityField>
		<ui:entityField code="org">
			<s:label cssStyle="font-weight: bold" key="task.filebatch.col.org" />
			<s:property value="file.org.name" />
		</ui:entityField>
		<ui:entityField code="mode">
			<c:if test="${file.fileTypeCode.equalsIgnoreCase('device_import')}">
				<s:label cssStyle="font-weight: bold" key="task.filebatch.col.mode"  />
				<c:choose>
			  		<c:when test="${ file.mode eq 'append' }">
			    		<s:text name="task.filebatch.details.append.description"/> 
			  		</c:when> 
			 		 <c:when test="${ file.mode eq 'replace' }">   
						<s:text name="task.filebatch.details.replace.description"/>
			  		</c:when>	
			 	 	<c:otherwise>
			 	 	</c:otherwise>
				</c:choose>
			</c:if>
		</ui:entityField>
		<ui:entityField code="sentBy">
			<s:label cssStyle="font-weight: bold" key="task.filebatch.col.sentBy" />
			<s:property value="file.user.username" />
		</ui:entityField>
		<ui:entityField code="size">
			<s:label cssStyle="font-weight: bold"
				key="task.filebatch.col.fileSize" />
			<s:property value="file.kilobytes" /> kb
			</ui:entityField>
		<ui:entityField code="sentDate">
			<s:label cssStyle="font-weight: bold"
				key="task.filebatch.col.sentDate" />
			  ${file.requestDate}
		</ui:entityField>
		<ui:entityField code="results">
			<s:label cssStyle="font-weight: bold"
				key="task.filebatch.col.results" />
			<c:choose>
				<c:when
					test="${file.status eq 'errors' and
									not empty file.errorDataFilename}">
					<s:a action="download" namespace="/filebatch">
						<s:param name="fileId" value="file.fileId" />
						<s:param name="fileType" value="'d'" />
						<s:property value="file.statusMessage" />
					</s:a>
				</c:when>
				<c:otherwise>
					<s:property value="file.statusMessage" />
				</c:otherwise>
			</c:choose>
		</ui:entityField>

		<ui:entityField code="errors">
			<s:if test="!file.fileErrors.isEmpty()">
				<s:label cssStyle="font-weight: bold"
					key="task.filebatch.col.errors" />
				
				<comp:table items="${file.fileErrors}" var="error" class="table table-condensed table-striped table-file-error">
					<comp:column>
						<comp:columnHeader>
							<ui:text name="task.filebatch.rownumber"/>
						</comp:columnHeader>
						<c:out value="${error.recordNumber}" />
					</comp:column>
					<comp:column>
						<comp:columnHeader>
							<ui:text name="task.filebatch.processing.errors"/>
						</comp:columnHeader>
						<c:out value="${error.message}" />
					</comp:column>
				</comp:table>
				
			</s:if>
		</ui:entityField>
	</ui:taskView>
</body>
</html>