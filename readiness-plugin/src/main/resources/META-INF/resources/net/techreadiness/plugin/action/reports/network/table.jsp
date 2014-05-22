<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
		
		$(function(){
			$('input[type="radio"]').on('change', function(event){
				$(event.target).closest('form').submit();
			});
		});
		
		function selectOrg(orgCode) {
			$('#selectOrg #orgCode').val(orgCode);
			$('#selectOrg').submit();
		}
	</script>
	<script type="text/javascript"><c:out value="${geoChart.chart}" escapeXml="false" /></script>

</head>
<body>
<jsp:include page="../breadcrumbs.jsp" />

<div class="report-layout-wrapper">
<table class="report-layout">
	<tr>
		<td class="left">
			<c:if test="${ dualConsortium }">
				<div>Consortium</div>
				<div class="report-toggle">
					<s:form action="networkAssessment" id="consortiumForm">
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
						<s:hidden name="snapshotName"/>
						<s:hidden name="snapshotScopeId"/>
					</s:form>
				</div>	
			</c:if>
			<div id='mapDiv' style="height: ${ geoChart.height }px; width: ${ geoChart.width }px; margin: auto"></div>	
		</td>
		<td class="center">
			<div class="report-header">Network Indicators</div>
			<div class="reports-updated-note">Reports are updated hourly</div> 
			<c:if test="${ orgSummary['createDate'] != null }">
				<div class="report-born-on">Data as of ${ orgSummary['createDate'] } </div>
					<c:if test="${ !empty snapshotName }">
						&nbsp;-&nbsp;${ snapshotName == 'default' ? 'Current Window' : snapshotName }
					</c:if>
			</c:if>
			<c:choose>
				<c:when test="${ orgSummary['networkPercentStudentsTestable'] == null or orgSummary['networkPercentStudentsTestable'] == 'TBD' }">
					<div class="report-org-text">
						<div class="tbd">TBD</div>
						<div class="report-label">${ consortium } - </div>
						<div class="report-label2">It <span class="text-underline">cannot</span> be determined what % of Students can be Tested in the Window, based on ${ requirements } Requirements</div><div style="clear: both;"></div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="report-org-text">
						<div class="color <s:property value="getPercentColor(orgSummary['networkPercentStudentsTestable'])"/>"></div>
						<div class="report-label">${ consortium } - </div>
						<div class="report-label2">${ orgSummary['networkPercentStudentsTestable'] } of Students can be Tested in the Window, based on ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="# Possible Test Starts / # Test Starts Needed"/></div><div style="clear: both;"></div>
					</div>
				</c:otherwise>
			</c:choose>
			
			<div class="disclaimer-text1"> <s:text name="ready.report.disclaimer.network.text1"/></div>
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
					<s:form action="networkAssessment">
						<s:select name="requirements" list="#{ 'Minimum':'Minimum', 'Recommended':'Recommended' }" onchange="this.form.submit()"/>
						<s:hidden name="viewBy"/>
						<s:hidden name="consortium"/>
						<s:hidden name="snapshotName"/>
						<s:hidden name="snapshotScopeId"/>
					</s:form>
				</div>				
			</div>
			<div style="clear: both;"></div>
			<div class="report-subheading">
				<ui:flyout text="Minimum / Recommended Requirements" title="Minimum / Recommended Requirements" namespace="/reports/device" action="minRequirements" reset="true" consortium="${ consortium }" snapshotName="${ snapshotName }" snapshotScopeId="${ snapshotScopeId }"  />	
			</div>
		</td>
		<td class="right">
		  <c:if test="${ orgSummary['createDate'] != null }">
			 <div class="report-buttons">
				<button type="submit" class="btn btn-xs btn-default print-button" onclick="$('#downloadPdf').submit();"><img src="${pageContext.request.contextPath}/static/images/icons/printer.png" class="report-button-image">Print to .pdf</button>
			 </div>
		  </c:if>	 
			<div style="clear: both;"></div>
			<div class="report-legend">
				<div class="header"><c:out value="${legendTitle}" /></div>
				
				<div class="legend-row">
					<div class="color" style="background-color:#e5e5e5"></div>
					<div class="report-label">Not a Member</div>
				</div>
				
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
<s:form action="networkAssessment" id="viewByForm">
	<c:choose>
		<c:when test="${viewBy eq 'map'}">
			<label class="radio inline">
				<input type="radio" name="viewBy" value="map" checked="checked">
				Map
			</label>
			<label class="radio inline">
				<input type="radio" name="viewBy" value="table">
				Table
			</label>
		</c:when>
		<c:otherwise>
			<label class="radio inline">
				<input type="radio" name="viewBy" value="map">
				Map
			</label>
			<label class="radio inline">
				<input type="radio" name="viewBy" value="table" checked="checked">
				Table
			</label>
		</c:otherwise>
	</c:choose>
	<s:hidden name="consortium"/>
	<s:hidden name="requirements"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
<s:form action="networkAssessment" id="selectOrg">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="orgCode" id="orgCode"/>
	<s:hidden name="requirements"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
<s:form action="download" id="downloadCsv">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="fileType" value="csv"/>
	<s:hidden name="requirements"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
<s:form action="download" id="downloadPdf">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="fileType" value="pdf"/>
	<s:hidden name="requirements"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
</div>
<s:form>
	<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="network" selectable="false" rowValue="network.orgCode">
		<ui:dataGridHeader>
		   <c:if test="${ orgSummary['createDate'] != null }">
			 <button type="button" class="btn btn-xs btn-default export-button" onclick="$('#downloadCsv').submit();">
				<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export to .csv
			 </button>
		   </c:if>	
		</ui:dataGridHeader>
		<ui:dataGridColumn code="orgName" name="Organization">
			<a href="javascript:selectOrg('<s:property value="network.orgCode"/>')"><s:property value="network.orgName"/></a>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="orgCode" name="Org Code">
			<s:property value="network.orgCode"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="numSchoolsLvl1" name="# of Schools 0%-25%">
			<s:property value="network['network0to25']"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="numSchoolsLvl2" name="# of Schools 26%-50%">
			<s:property value="network['network26to50']"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="numSchoolsLvl3" name="# of Schools 51%-75%">
			<s:property value="network['network51to75']"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="numSchoolsLvl4" name="# of Schools 76%-100%">
			<s:property value="network['network76to100']"/>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="testingTestStartCount" name="Total # Test Starts Needed <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='Sum of grade-specific enrollment * grade-specific test-start information from the min/rec requirements settings'>">
			<span class="${ network.testingTestStartCount == '(missing)' ? 'below-req':''}">
				<s:property value="network.testingTestStartCount"/>
			</span>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="networkPossibleTestCount" name="# Possible Test Starts <br> With Existing Infrastructure <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='(available bandwidth (in Mbps) * 1000 (Kbps/Mbps) / throughput requirement (in Kbps)) * (# of days in test window * sessions per day)'>">
			<c:choose>
				<c:when test="${ network.networkPossibleTestCount == 'TBD' }">
					<span class="tbd">TBD</span>
				</c:when>
				<c:when test="${ network.networkPossibleTestCount == '(Not Applicable)'}">
					<span>(Not Applicable)</span>
				</c:when>
				<c:when test="${ network.networkPossibleTestCount == '(missing)' }">
					<span class="below-req">(missing)</span>
				</c:when>
				<c:otherwise>
					<s:property value="network.networkPossibleTestCount"/>
				</c:otherwise>
			</c:choose>

		</ui:dataGridColumn>
		<ui:dataGridColumn  code="networkPercentStudentsTestable" name="% of Students that can <br> be Tested in the Window <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='# Possible Test Starts / # Test Starts Needed'>">
			<c:choose>
				<c:when test="${ network.networkPercentStudentsTestable == 'TBD' }">
					<span class="tbd">TBD</span>
				</c:when>
				<c:when test="${ network.networkPercentStudentsTestable == '(Not Applicable)'}">
					<span>(Not Applicable)</span>
				</c:when>
				<c:when test="${ network.networkPercentStudentsTestable == '(missing)' }">
					<span class="below-req">(missing)</span>
				</c:when>
				<c:otherwise>
					<div class="report-datagrid-color-icon">
						<div class="color <s:property value="getPercentColor(network.networkPercentStudentsTestable)"/>"></div>
						<div class="report-label"><s:property value="network.networkPercentStudentsTestable"/></div>
					</div>
				</c:otherwise>
			</c:choose>
		</ui:dataGridColumn>
		
	</ui:datagrid>
</s:form>


</body>