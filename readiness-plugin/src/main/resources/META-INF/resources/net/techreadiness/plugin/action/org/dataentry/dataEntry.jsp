<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="comp" uri="http://swiftelan.com/components"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
<script type="text/javascript">
	function confirmAction() {
		window.onbeforeunload = null;
		var answer = confirm("Are you sure you want to Mark/Unmark Data Entry Complete for the selected Organizations?");

		if (answer == true) {
			t.form.submit();
		} else {
			return false;
		}
	}
	
	$(document).on('ready', function() {
		$('input[type="checkbox"][data-table="select-all"]').on('change', function(event) {
		     var $target = $(event.target);
		     if ($target.is(':checked')) {
	            $target.closest('table').find('input[type="checkbox"][data-grid="selection"]:not(:disabled)').each(function (index, element) {
	            	$(this).prop("checked", true);
	            });
		     } else {
		    	 $target.closest('table').find('input[type="checkbox"][data-grid="selection"]:not(:disabled)').each(function (index, element) { 
	        	   $(this).prop("checked", false);
	             });
		     }
		});
	});
</script>
<title><s:text name="task.org.dataentry.pageTitle" /></title>
</head>
<body>
	<s:form action="save" onSubmit="return confirmAction()">
		<ui:taskView taskFlow="${taskFlowData}">
			<ui:taskNavigation />
			<p>
				<ui:text name="ready.markdataentrycomplete.subheader" />
			</p>
			
			<comp:table items="${taskFlowData.orgs}" var="org" class="table table-condensed table-striped">
				<comp:column>
					<comp:columnHeader>
						<input type="checkbox" data-table="select-all">
					</comp:columnHeader>
					<comp:input type="checkbox" name="dataEntryComplete[${org.orgId}]" disabled="${not empty dataEntryErrors[org.orgId]}" checked="${org.dataEntryComplete}" data-grid="selection" value="true" />
				</comp:column>
				<comp:column>
					<comp:columnHeader>
						<ui:text name="core.code"/>
					</comp:columnHeader>
					<c:out value="${org.code}" />
				</comp:column>
				<comp:column>
					<comp:columnHeader>
						<ui:text name="core.name"/>
					</comp:columnHeader>
					<c:out value="${org.name}" />
				</comp:column>
				<comp:column>
					<comp:columnHeader>
						<ui:text name="errors"/>
					</comp:columnHeader>
					<c:if test="${not empty dataEntryErrors[org.orgId]}">
						<span class="text-error"><ui:text name="data.entry.fields.required"/></span>
						<ol class="list-data-entry">
							<c:forEach items="${dataEntryErrors[org.orgId]}" var="dataEntryError">
								<li class="text-error"><c:out value="${dataEntryError}" /></li>
							</c:forEach>
						</ol>
					</c:if>
				</comp:column>
				<comp:column>
					<comp:columnHeader>
						<ui:text name="atypical.data"/>
					</comp:columnHeader>
					<c:if test="${not empty abnormalMessages[org.orgId]}">
						<span class="text-error"><ui:text name="data.entry.atypical.data"/></span>
						<ol class="list-data-entry">
							<c:forEach items="${abnormalMessages[org.orgId]}" var="message">
								<li class="text-error"><c:out value="${message}" /></li>
							</c:forEach>
						</ol>
					</c:if>
				</comp:column>
				<comp:column>
					<comp:columnHeader>
						<ui:text name="organization.type"/>
					</comp:columnHeader>
					<c:out value="${org.orgTypeName}" />
				</comp:column>
				<comp:column>
					<comp:columnHeader>
						<ui:text name="parent.organization"/>
					</comp:columnHeader>
					<c:out value="${org.parentOrgName}" />
				</comp:column>
			</comp:table>
		</ui:taskView>
	</s:form>
</body>
</html>