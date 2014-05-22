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
		function geoChartSelectHandler() {
			var selection = geoChart.getSelection();
			if(selection.length > 0) {
				$('#selectOrg #orgCode').val(geoData.getRowProperty(selection[0].row, 'stateCode'));
				$('#selectOrg').submit();
			}
		}
		
		function selectOrg(orgCode) {
			$('#selectOrg #orgCode').val(orgCode);
			$('#selectOrg').submit();
		}
		
		$(function(){
			$('input[type="radio"]').on('change', function(event){
				$(event.target).closest('form').submit();
			});
		});
		
	</script>
	<c:if test="${ currentOrg.orgTypeName == 'Readiness' }">
		<script type="text/javascript"><c:out value="${geoChart.chart}" escapeXml="false" /></script>
	</c:if>
	

</head>
<body>

<c:if test="${ currentOrg.orgTypeName != 'Readiness' }">
	<jsp:include page="../../breadcrumbs.jsp" />
</c:if>

<div class="report-layout-wrapper">
<table class="report-layout">
	<tr>
		<td class="left">
			<c:if test="${ dualConsortium }">
				<div>Consortium</div>
				<div class="report-toggle">
					<s:form action="deviceProgress" id="consortiumForm">
						<c:choose>
								<c:when test="${consortium eq 'SBAC'}">
									<label class="radio inline">
										<input type="radio" name="consortium" value="SBAC" checked="checked">
										SBAC
									</label>
									<label class="radio inline">
										<input type="radio" name="consortium" value="PARCC">
										PARCC
									</label>
								</c:when>
								<c:otherwise>
									<label class="radio inline">
										<input type="radio" name="consortium" value="SBAC">
										SBAC
									</label>
									<label class="radio inline">
										<input type="radio" name="consortium" value="PARCC" checked="checked">
										PARCC
									</label>
								</c:otherwise>
							</c:choose>
						<s:hidden name="viewBy"/>
						<s:hidden name="requirements"/>
						<s:hidden name="orgCode"/>
					</s:form>
				</div>	
			</c:if>
			
			<c:choose>
				<c:when test="${ currentOrg.orgTypeName == 'Readiness' }">
					<div id='mapDiv' style="height: ${ geoChart.height }px; width: ${ geoChart.width }px; margin: auto"></div><div style="clear: both;"></div>
					<div class="report-datagrid-color-icon">
						<div class="color report-levelmember"></div>
						<div class="report-label">Member</div><div style="clear: both;"></div>
						
						<div class="color report-levelnotmember"></div>
						<div class="report-label">Not a Member</div><div style="clear: both;"></div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="report-state-img"><img src="${pageContext.request.contextPath}/static/images/states/${ stateCode }.png" /></div>
					<div class="report-state-label">${ stateName }</div>
				</c:otherwise>
			</c:choose>
		</td>
		<td class="center">
			<div class="report-header">Progress Report - Device Indicators</div>
			<div class="reports-updated-note">Reports are updated hourly</div> 
			<div class="report-born-on"><c:out value="${ asOfDate }"/></div>
			<c:choose>
				<c:when test="${ currentOrg.orgTypeName == 'Readiness' }">
					<div class="report-org-text">
						<div class="report-label">% Devices Meet the ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="Number of Devices that Meet Requirements / Total number of devices"/></div><div style="clear: both;"></div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="report-org-text">
						<div class="report-label">${ currentOrgName } - % Devices Meet the ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="Number of Devices that Meet Requirements / Total number of devices"/></div><div style="clear: both;"></div>
					</div>
				</c:otherwise>
			</c:choose>
			
			<div class="disclaimer-text1"> <s:text name="ready.report.disclaimer.device.text1"/></div>
			<div class="disclaimer-text1"> <s:text name="ready.report.consortia.text">
			  <s:param value="consortium" /></s:text>
			  <c:choose>
				<c:when test="${consortium == 'SBAC'}"> 
	 					<a href='http://www.techreadiness.org/SBACrequirements' target="_blank"> <span class="report-link"> here</span>.</a>
	  			 </c:when>
	 			<c:when test="${consortium == 'PARCC'}"> 
	 					<a href='http://www.techreadiness.org/PARCCrequirements' target="_blank"> <span class="report-link"> here</span>.</a>
	  			</c:when>
	  			<c:otherwise>
	  			</c:otherwise>
			</c:choose>
			</div>
			
			<div class="report-requirements">
				<div class="report-label">Requirements</div>
				<div class="data">
					<s:form action="deviceProgress">
						<s:select name="requirements" list="#{ 'Minimum':'Minimum', 'Recommended':'Recommended' }" onchange="this.form.submit()"/>
						<s:hidden name="viewBy"/>
						<s:hidden name="consortium"/>
						<s:hidden name="orgCode"/>
					</s:form>
				</div>				
			</div>
			<div style="clear: both;"></div>
			<%-- <div class="label3"> <s:text name="ready.report.online.snapshot.summary.text"/> <a href="http://techreadiness.org/onlinedatasheets" target="_blank"> <span class="report-link"> here</span>.</a></div> --%>
		</td>
		<td class="right">
			<div class="report-buttons">
				<button type="submit" class="btn btn-xs btn-default print-button" onclick="$('#downloadPdf').submit();"><img src="${pageContext.request.contextPath}/static/images/icons/printer.png" class="report-button-image">Print to .pdf</button>
			</div>
			<div style="clear: both;"></div>
			<div class="report-legend">
				<div class="header"><c:out value="${legendTitle}" /></div>
				
				<div class="legend-row">
					<div class="report-label"><ui:flyout text="TBD" title="Why are some reports showing TBD as a value?" namespace="/reports/device" action="TBD" reset="true" /></div>
				</div>
				
				<div class="legend-row">
					<div class="color report-level1"></div>
					<div class="report-label">0%-25%</div>
				</div>
				
				<div class="legend-row">
					<div class="color report-level2"></div>
					<div class="report-label">26%-50%</div>
				</div>
				
				<div class="legend-row">
					<div class="color report-level3"></div>
					<div class="report-label">51%-75%</div>
				</div>
				
				<div class="legend-row">
					<div class="color report-level4"></div>
					<div class="report-label">76%-100%</div>
				</div>
			</div>
		</td>
	</tr>
</table>
</div>

<div class="datagrid-toggle">
<s:form action="deviceProgress" id="selectOrg">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="orgCode" id="orgCode"/>
	<s:hidden name="requirements"/>
</s:form>
<s:form action="download" id="downloadCsv">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="orgCode"/>
	<s:hidden name="fileType" value="csv"/>
	<s:hidden name="requirements"/>
</s:form>
<s:form action="download" id="downloadPdf">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="orgCode"/>
	<s:hidden name="fileType" value="pdf"/>
	<s:hidden name="requirements"/>
</s:form>
</div>
<div style="clear: both;"></div>
<s:form>
	<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="device" selectable="false" rowValue="device.orgCode" columnSelectable="true" >
		<ui:dataGridHeader>
			<button type="button" class="btn btn-xs btn-default export-button" onclick="$('#downloadCsv').submit();">
				<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export to .csv
			</button>
		</ui:dataGridHeader>
		<ui:dataGridColumn code="orgName" name="Organization" manageable="${false}" displayOrder="first">
			<c:choose>
				<c:when test="${ currentOrg.orgTypeName != 'School' }">
					<a href="javascript:selectOrg('<s:property value="device.orgCode"/>')"><s:property value="device.orgName"/></a>
				</c:when>
				<c:otherwise>
					<s:property value="device.orgName"/>
				</c:otherwise>
			</c:choose>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="orgCode" name="Org Code">
			<s:property value="device.orgCode"/>
		</ui:dataGridColumn>	
		<s:iterator value="snapshots" var="snapshot">
			<s:set var="snapshotId">devicePassingPercent_${ snapshot.snapshotWindowId }</s:set>
			<s:set var="deviceTBD">deviceTbdCount_${ snapshot.snapshotWindowId }</s:set>
			
			<fmt:formatDate value="${ snapshot.executeDate }" pattern="MM/dd/yyyy hh:mm:ss a" var="executeDate" timeZone="US/Central"/>
			<s:url var="requirementsUrl" namespace="/reports/device" action="minRequirements">
				<s:param name="reset" value="true"/>
				<s:param name="ajax" value="true"/>
				<s:param name="snapshotName" value="#snapshot.name"/>
				<s:param name="snapshotScopeId" value="#snapshot.scope.id"/>
				<s:param name="consortium">${consortium}</s:param>
			</s:url>
			<s:url var="deviceAssessmentUrl" namespace="/reports/device" action="deviceAssessment">
				<s:param name="snapshotName" value="#snapshot.name"/>
				<s:param name="snapshotScopeId" value="#snapshot.scope.id"/>
				<s:param name="viewBy">${viewBy}</s:param>
				<s:param name="orgCode">${orgCode}</s:param>
				<s:param name="stateCode">${stateCode}</s:param>
				<s:param name="stateName">${stateName}</s:param>
				<s:param name="requirements">${requirements}</s:param>
				<s:param name="consortium">${consortium}</s:param>
			</s:url>
			<ui:dataGridColumn code="${snapshotId}" manageColumnsText="${snapshot.name == 'default' ? 'Current Window' : snapshot.name}" name="${snapshot.name == 'default' ? 'Current Window' : snapshot.name}<a class='entity-name' title='Minimum / Recommended Requirements - ${ executeDate }' href='${ requirementsUrl }'><img style='margin-left: 4px' src='${pageContext.request.contextPath}/static/images/magnifying-glass-white.png'/></a><br>${ executeDate }<a class='#report-desc' title='View of Device Indicators Report as of ${ executeDate }' href='${ deviceAssessmentUrl }'><img style='margin-left: 3px' src='${pageContext.request.contextPath}/static/images/external-link-white.gif'/></a>">
					<div class="report-datagrid-color-icon">
						<div class="color <s:property value="getPercentColor(device[#snapshotId])"/>"></div>
						<div class="completion-label"><s:property value="device[#snapshotId]"/>
						<s:if test="%{device[#deviceTBD] > 0}">
					    	<span class="tbd">TBD  </span>
						</s:if>
						</div>
					</div>
			</ui:dataGridColumn>
		</s:iterator>
	</ui:datagrid>
</s:form>

</body>