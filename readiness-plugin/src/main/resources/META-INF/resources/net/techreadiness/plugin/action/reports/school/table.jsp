<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
	<script type='text/javascript' src='https://www.google.com/jsapi'></script>
	<script>
	function selectOrg(orgCode) {
		$('#selectOrg #orgCode').val(orgCode);
		$('#selectOrg').submit();
	}
	
	<%--$("body").delegate("ul#breadcrumbs a", "click", function(event){
		event.preventDefault();
		$("div.report-layout-wrapper").trigger("update-state", [{url: $(event.target).attr("href")}]);
	}); --%>

	</script>
</head>
<body>

<div class="report-layout-wrapper">

<c:if test="${ !empty breadcrumbs }">
	<ul id="breadcrumbs">
		<c:forEach var="crumb" items="${breadcrumbs}" varStatus="count">
			<c:choose>
				<c:when test="${fn:length(breadcrumbs) == count.count or !crumb.link}">
					<li>${crumb.label}</li>
					<c:if test="${!crumb.link}">
						<li>&gt;</li>
					</c:if>
				</c:when>
				<c:otherwise>
					<li>${crumb.label}</li>
					<li>&gt;</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
</c:if>
<table class="report-layout">
	<tr>
		<td class="left">
			 <c:if test="${ dualConsortium }">
				<div class="report-toggle">
				</div>		
			</c:if>
		</td>
		<td class="center">
			<div class="report-header">School Exception Report</div>
			<div class="reports-updated-note">Reports are updated hourly</div>
				<c:if test="${not empty asOfDate}"> 		
					<div class="report-born-on"><c:out value="${ asOfDate }"/></div>
					<div class="report-question">
					<div class="report-label">SELECT A REPORT:</div>
					<div style="clear: both;"></div>
					<div class="data">
						<s:form action="schoolExceptionReport">
							<s:select name="question" list="questions" onchange="this.form.submit()"/>
							<s:hidden name="viewBy"/>
							<s:hidden name="consortium"/>
						</s:form>
					</div>	
					</div>
				</c:if>				
			<br>
			<div class="report-subheading">
				<ui:flyout text="Exception Report Types" title="Exception Report Types" namespace="/reports/school" action="exceptionTypes" consortium="${ consortium }"  />	
			</div>
		</td>
	</tr>
</table>
</div>

<div class="datagrid-toggle">
 <s:form action="download" id="downloadCsv">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="fileType" value="csv"/>
	<s:hidden name="question"/>
</s:form>


<s:form action="download" id="downloadFullDetailCsv">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="fileType" value="csv"/>
	<s:hidden name="csvFullDetail" value="true"/>
	<s:hidden name="question"/>
</s:form>

 <s:form action="/task/batch/device/edit.action" id="requestDeviceExport">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="question"/>
</s:form>


<s:form action="download" id="downloadPdf">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="fileType" value="pdf"/>
	<s:hidden name="question"/>
</s:form>
</div>
<br><br><br>
<s:form>
	<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="school" selectable="false" rowValue="school.orgCode">
		<ui:dataGridHeader>
		   <c:if test="${not empty asOfDate}">  
			<c:choose>
				<c:when test="${orgCode == 'readiness'}">
					<button type="button" class="btn btn-xs btn-default export-button" disabled="disabled">
						<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export to .csv
					</button>
					<button type="button" class="btn btn-xs btn-default export-button" disabled="disabled">
						<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export with Survey Data to .csv
					</button>
					<button name="deviceButton" type="button" class="btn btn-xs btn-default export-button" onclick="javascript:location.href = '${pageContext.request.contextPath }/task/batch/device/devices';" disabled="disabled">
						<img name="deviceImage" src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export Device Data to .csv
					</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-xs btn-default export-button" onclick="$('#downloadCsv').submit();">
						<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export to .csv
					</button>
					<button type="button" class="btn btn-xs btn-default export-button" onclick="$('#downloadFullDetailCsv').submit();">
						<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export with Survey Data to .csv
					</button>
					<c:choose>
						<c:when test="${importDeviceDataAllowed}">
							<button name="deviceButton" type="button" class="btn btn-xs btn-default export-button" onclick="javascript:location.href = '${pageContext.request.contextPath }/task/batch/device/devices';">
							<img name="deviceImage" src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export Device Data to .csv
							</button>
						</c:when>
						<c:otherwise>	
							<button name="deviceButton" type="button" class="btn btn-xs btn-default export-button" onclick="javascript:location.href = '${pageContext.request.contextPath }/task/batch/device/devices';" disabled="disabled">
							<img name="deviceImage" src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export Device Data to .csv
							</button>
						</c:otherwise>
					</c:choose>
				</c:otherwise>	
			</c:choose>
		  </c:if>	
		</ui:dataGridHeader>
		
		<ui:dataGridColumn code="orgName" name="Organization">
			<s:property value="orgName"/>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="orgCode" name="Org Code">
			<s:property value="orgCode"/>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="parentOrgName" name="Parent Organization">
			<s:property value="parentOrgName"/>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="Completion Status" name="Completion Status">
		   <c:choose>
				<c:when test="${dataEntryComplete == '(missing)'}">
					<div class="report-label">
						<span class="${dataEntryComplete == '(missing)' ? 'below-req' : '' }"> <c:out value="${dataEntryComplete}" /> </span>
					</div>	
				</c:when>
				<c:otherwise>
					<div class="report-datagrid-color-icon">
						<div class="color <s:property value="getColorBoolean(dataEntryComplete)"/>"></div>
						<div class="report-label">
							<s:property value="dataEntryComplete" />
						</div>
					</div>	
				</c:otherwise>
			</c:choose>				
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="totalDeviceCount" name="Total Device Count">
			<s:property value="deviceCount"/>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="numberOfTestTakers" name="Total # of Test Starts <br> Needed Per School">
			<s:property value="testingTestStartCount"/>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="deviceToTestTakerRatio" name="Device to <br>Test Starts Ratio">
			<s:property value="deviceTestTakerRatio"/>
		</ui:dataGridColumn>

		<ui:dataGridColumn code="unansweredSurveyCount" name="Number of Unanswered<br>Survey Questions">
			<s:property value="unansweredSurveyCount"/>
		</ui:dataGridColumn>
		
	</ui:datagrid>
</s:form>

</body>
