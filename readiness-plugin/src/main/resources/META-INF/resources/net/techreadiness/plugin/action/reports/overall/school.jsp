<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="rdy" uri="http://techreadiness.net"%>

<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/what-if.js"></script>
	<script>
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
					<s:form action="overallAssessment" id="consortiumForm">
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
						<s:hidden name="orgCode" id="orgCode"/>
						<s:hidden name="stateCode"/>
						<s:hidden name="stateName"/>
						<s:hidden name="snapshotName"/>
						<s:hidden name="snapshotScopeId"/>
					</s:form>
				</div>	
			</c:if>
			<div class="report-state-img"><img src="${pageContext.request.contextPath}/static/images/states/${ stateCode }.png" /></div>
			<div class="report-state-label">${ stateName }</div>	
		</td>
		<td class="center">
			<div class="report-header">Overall Readiness Indicators</div>
			<div class="reports-updated-note">Reports are updated hourly</div> 
			<c:if test="${ orgSummary['createDate'] != null }">
				<div class="report-born-on">Data as of ${ orgSummary['createDate'] } </div>
					<c:if test="${ !empty snapshotName }">
						&nbsp;-&nbsp;${ snapshotName == 'default' ? 'Current Window' : snapshotName }
					</c:if>
			</c:if>
			
			
			<div class="disclaimer-text1"> <s:text name="ready.report.disclaimer.overall.text1"/></div>
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
					<s:form action="overallAssessment">
						<s:select name="requirements" list="#{ 'Minimum':'Minimum', 'Recommended':'Recommended' }" onchange="this.form.submit()"/>
						<s:hidden name="viewBy"/>
						<s:hidden name="consortium"/>
						<s:hidden name="orgCode" id="orgCode"/>
						<s:hidden name="stateCode"/>
						<s:hidden name="stateName"/>
						<s:hidden name="snapshotName"/>
						<s:hidden name="snapshotScopeId"/>
					</s:form>
				</div>				
			</div>
			<div style="clear: both;"></div>
			<div class="report-subheading">
				<ui:flyout text="Minimum / Recommended Requirements" title="Minimum / Recommended Requirements" namespace="/reports/overall" action="minRequirements" reset="true" consortium="${ consortium }" snapshotName="${ snapshotName }" snapshotScopeId="${ snapshotScopeId }"  />	
			</div>
		</td>
		<td class="right">
			<div class="report-buttons">
				<c:if test="${ orgSummary['createDate'] != null }">
					<button type="submit" class="btn btn-xs btn-default print-button" onclick="$('#downloadPdf').submit();"><img src="${pageContext.request.contextPath}/static/images/icons/printer.png" class="report-button-image">Print to .pdf</button>
				</c:if>
			</div>
			<div style="clear: both;"></div>
			<div class="report-legend">
				<div class="header"><c:out value="${legendTitle}" /></div>
				
				<div class="legend-row">
					<div class="report-label"><ui:flyout text="TBD" title="Why are some reports showing TBD as a value?" namespace="/reports/overall" action="TBD" reset="true" /></div>
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
<s:hidden name="orgId" />
<div style="clear: both;"></div>
<s:form action="overallAssessment" id="selectOrg">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="orgCode" id="orgCode"/>
	<s:hidden name="stateCode"/>
	<s:hidden name="stateName"/>
	<s:hidden name="requirements"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
<s:form action="download" id="downloadCsv">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="orgCode" id="orgCode"/>
	<s:hidden name="fileType" value="csv"/>
	<s:hidden name="requirements"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
<s:form action="download" id="downloadPdf">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="orgCode" id="orgCode"/>
	<s:hidden name="fileType" value="pdf"/>
	<s:hidden name="requirements"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
<s:form>
	<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="overall" selectable="false" rowValue="overall.orgCode">
		<ui:dataGridHeader>
		    <c:if test="${ orgSummary['createDate'] != null }">
				<button type="button" class="btn btn-xs btn-default export-button" onclick="$('#downloadCsv').submit();">
					<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export to .csv
				</button>
			</c:if>  	
		</ui:dataGridHeader>
		
		<ui:dataGridColumn code="orgName" name="Organization">
			<c:choose>
				<c:when test="${ currentOrg.orgTypeName  == 'District' }">
					<a href="javascript:selectOrg('<s:property value="overall.orgCode"/>')"><s:property value="overall.orgName"/></a>
				</c:when>
				<c:otherwise>
					<ui:flyout text="${overall.orgName}" title="Current Info" namespace="/reports" action="current-info" orgId="${orgId}" consortium="${consortium}" snapshotName="${snapshotName}" snapshotScopeId="${snapshotScopeId}"  />
					<a class="what-if-open" data-what-if="open" data-org="${overall.orgId}" data-snapshot="${snapshotWindow.snapshotWindowId}">
						<img alt="Calculator" title="What If Scenario Calculator" src="${pageContext.request.contextPath}/static/images/calculator.png">
					</a>
				</c:otherwise>
			</c:choose>
		</ui:dataGridColumn>
		<c:if test="${ currentOrg.orgTypeName  == 'District' }">
			<ui:dataGridColumn code="DistrictFlyout">
				<ui:flyout title="Current Info" namespace="/reports" action="current-info" orgId="${orgId}" consortium="${consortium}" snapshotName="${snapshotName}" snapshotScopeId="${snapshotScopeId}"  />
				<a class="what-if-open" data-what-if="open" data-org="${overall.orgId}" data-snapshot="${snapshotWindow.snapshotWindowId}">
					<img alt="Calculator" title="What If Scenario Calculator" src="${pageContext.request.contextPath}/static/images/calculator.png">
				</a>
			</ui:dataGridColumn>
		</c:if>	
		<ui:dataGridColumn code="orgCode" name="Org Code">
			<s:property value="overall.orgCode"/>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="completionStatus" name="Completion Status">
			<c:choose>
				<c:when test="${overall.dataEntryComplete == '(missing)'}">
					<div class="report-label">
						<span class="below-req"> <c:out value="${overall.dataEntryComplete}" /> </span>
					</div>	
				</c:when>
				<c:otherwise>
					<div class="report-datagrid-color-icon">
						<div class="color <s:property value="getColorBoolean(overall.dataEntryComplete)"/>"></div>
						<div class="report-label">
							<s:property value="overall.dataEntryComplete" />
						</div>
					</div>	
				</c:otherwise>
			</c:choose>
	   </ui:dataGridColumn>		
		
			
		<ui:dataGridColumn code="percentDevices" name="% Devices Meeting<br>${ requirements } Requirements <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='# of Devices Meeting Requirements / Total Number of Devices '/>">
			<div class="report-datagrid-color-icon">
				<div class="color <s:property value="getPercentColor(overall.devicePassingPercent)"/>"></div>
				<div class="report-label">
					<span class="${ overall.devicePassingPercent == '(missing)' ? 'below-req' : '' }">${ overall.devicePassingPercent }</span>
				</div>
			</div>
			<c:if test="${ overall.deviceTbdCount > 0 }">
				<span class="tbd">TBD</span>
			</c:if>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="testableStudentsTester" name="% of Device to Test-Taker <br> Readiness of the School <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='# Possible Test Starts / # Test Starts Needed'>">
			<div class="report-datagrid-color-icon">
				<div class="color <s:property value="getPercentColor(overall.testTakerPercentStudentsTestable)"/>"></div>
				<div class="report-label">
					<span class="${ overall.testTakerPercentStudentsTestable == '(missing)' ? 'below-req' : '' }">${ overall.testTakerPercentStudentsTestable }</span>
				</div>
			</div>
			<c:if test="${ overall.deviceTbdCount > 0 }">
				<span class="tbd">TBD</span>
			</c:if>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="testableStudentsNetwork" name="% of Network <br> Readiness of the School <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='# Possible Test Starts / # Test Starts Needed'>">
			<div class="report-datagrid-color-icon">
				<div class="color <s:property value="getPercentColor(overall.networkPercentStudentsTestable)"/>"></div>
				<div class="report-label">
					<span class="${ overall.networkPercentStudentsTestable == '(missing)' ? 'below-req' : '' }">${ overall.networkPercentStudentsTestable }</span>
				</div>
			</div>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="MostContrained" name="More Constrained By">
			<div class="report-label"><s:property value="overall.moreConstrainedBy"/></div>
		</ui:dataGridColumn>
				
	</ui:datagrid>
</s:form>

<div id="what-if-dialog" class="hide">
	<h1 class="what-if-title"></h1>
	<div style="color: #b94a48"><s:text name="ready.report.disclaimer.overall.calculator"/></div>
	<div class="what-if-errors"></div>
	<form action="" class="what-if-requirements">
		<label for="whatIfRequirements"><s:text name="ready.report.overall.what.if.requirements" /></label>
		<select id="whatIfRequirements" name="flag">
			<c:choose>
				<c:when test="${requirements.equalsIgnoreCase('minimum')}">
					<option value="MINIMUM" selected="selected"><s:text name="ready.report.overall.what.if.minimum" /></option>
					<option value="RECOMMENDED"><s:text name="ready.report.overall.what.if.recommended" /></option>
				</c:when>
				<c:otherwise>
					<option value="MINIMUM"><s:text name="ready.report.overall.what.if.minimum" /></option>
					<option value="RECOMMENDED" selected="selected"><s:text name="ready.report.overall.what.if.recommended" /></option>
				</c:otherwise>
			</c:choose>
		</select>
	</form>
	
	<input name="orgId" type="hidden">
	<input name="snapshotId" type="hidden">
	<input name="minThroughput" value="${consortiumScope.minimumThroughputRequiredPerStudent}" type="hidden">
	<input name="recThroughput" value="${consortiumScope.recommendedThroughputRequiredPerStudent}" type="hidden">
	<input name="minTestingWindow" value="${consortiumScope.minimumTestingWindowLength}" type="hidden">
	<input name="recTestingWindow" value="${consortiumScope.recommendedTestingWindowLength}" type="hidden">
	<input name="testingTestStartCount" type="hidden">
	
	<rdy:reportLegend class="what-if" title="Meeting Requirements" />
	<form class="form-horizontal what-if">
		
		<div class="control-group what-if-header">
		<label class="control-label"></label>
			<div class="controls">
				<span class="what-if-column-header"><s:text name="ready.report.overall.what.if.current" /></span>
				<span class="what-if-column-header"><s:text name="ready.report.overall.what.if" /></span>
			</div>
		</div>
		
		<div class="control-group">
			<label for="deviceCount" class="control-label"><s:text name="ready.report.overall.what.if.devices.meeting.requirements" /></label>
			<div class="controls">
				<input type="text" class="input-medium" id="deviceCount" disabled="disabled" readonly="readonly" name="devicePassingCount" data-snapshot="current">
				<input type="number" class="input-medium" name="devicePassingCount" min="0" data-snapshot="what-if">
			</div>
		</div>
		<div class="control-group">
			<label for="testingWindow" class="control-label"><s:text name="ready.report.overall.what.if.testing.days" /></label>
			<div class="controls">
				<input type="text" class="input-medium" id="testingWindowLength" disabled="disabled" readonly="readonly" name="testingWindowLength" data-snapshot="current">
				<input type="number" class="input-medium" name="testingWindowLength" min="0" data-snapshot="what-if">
			</div>
		</div>
		<div class="control-group">
			<label for="sessionsPerDay" class="control-label"><s:text name="ready.report.overall.what.if.sessions.per.day" /></label>
			<div class="controls">
				<input type="text" class="input-medium" id="sessionsPerDay" disabled="disabled" readonly="readonly" name="sessionsPerDay" data-snapshot="current">
				<input type="number" class="input-medium" name="sessionsPerDay" min="0" data-snapshot="what-if">
			</div>
		</div>
		<div class="control-group">
			<label for="internetBandwidth" class="control-label"><s:text name="ready.report.overall.what.if.internet.bandwidth" /></label>
			<div class="controls">
				<select class="input-medium" id="internetSpeed" disabled="disabled" name="internetSpeed" data-snapshot="current">
					<c:forEach items="${internetSpeed.optionListValues}" var="optionListValue">
						<option value="${optionListValue.value}"><c:out value="${optionListValue.name}" /></option>
					</c:forEach>
				</select>
				<select class="input-medium" name="internetSpeed" data-snapshot="what-if">
					<c:forEach items="${internetSpeed.optionListValues}" var="optionListValue">
						<option value="${optionListValue.value}"><c:out value="${optionListValue.name}" /></option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label for="internetBandwidthUtilization" class="control-label"><s:text name="ready.report.overall.what.if.internet.bandwidth.utilization" /></label>
			<div class="controls">
				<input type="text" class="input-medium" id="internetUtilization" disabled="disabled" readonly="readonly" name="internetUtilization" data-snapshot="current">
				<input type="number" class="input-medium" name="internetUtilization" min="0" max="100" data-snapshot="what-if">
			</div>
		</div>
		<div class="control-group">
			<label for="internalBandwidth" class="control-label"><s:text name="ready.report.overall.what.if.network.bandwidth" /></label>
			<div class="controls">
				<select id="networkSpeed" disabled="disabled" class="input-medium" name="networkSpeed" data-snapshot="current">
					<c:forEach items="${networkSpeed.optionListValues}" var="optionListValue">
						<option value="${optionListValue.value}"><c:out value="${optionListValue.name}" /></option>
					</c:forEach>
				</select>
				<select class="input-medium" name="networkSpeed" data-snapshot="what-if">
					<c:forEach items="${networkSpeed.optionListValues}" var="optionListValue">
						<option value="${optionListValue.value}"><c:out value="${optionListValue.name}" /></option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label for="internalBandwidthUtilization" class="control-label"><s:text name="ready.report.overall.what.if.network.bandwidth.utilization" /></label>
			<div class="controls">
				<input type="text" class="input-medium" id="networkUtilization" disabled="disabled" readonly="readonly" name="networkUtilization" data-snapshot="current">
				<input type="number" class="input-medium" name="networkUtilization" min="0" max="100" data-snapshot="what-if">
			</div>
		</div>
		<div class="form-actions">
			<button type="button" name="calculate" class="btn btn-xs btn-primary what-if-button"><s:text name="ready.report.overall.what.if.calculate" /></button>
			<input type="reset" value="Reset" class="btn btn-xs btn-default what-if-button">
		</div>
		<div class="control-group">
			<p class="control-label">
				<s:text name="ready.report.overall.what.if.device.to.taker" />
				<span class="what-if-info" title="# Possible Test Starts / # Test Starts Needed">
					<img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png">
				</span>
			</p>
			<div class="controls" data-percent-ready="testTakerPercentStudentsTestable">
				<div style="float: left; width: 154px;">
					<div class="progress" data-percent="current">
						<div class="bar"></div>
					</div>
				</div>
				<div style="float: left; width: 154px; margin-left: 4px;">
					<div class="progress" data-percent="what-if">
						<div class="bar"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="control-group">
			<p class="control-label">
				<s:text name="ready.report.overall.what.if.network" />
				<span class="what-if-info" title="# Possible Test Starts / # Test Starts Needed">
					<img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png">
				</span>
			</p>
			<div class="controls" data-percent-ready="networkPercentStudentsTestable">
				<div style="float: left; width: 154px;">
					<div class="progress" data-percent="current">
						<div class="bar"></div>
					</div>
				</div>
				<div style="float: left; width: 154px; margin-left: 4px;">
					<div class="progress" data-percent="what-if">
						<div class="bar"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="control-group">
			<p class="control-label">
				<s:text name="ready.report.overall.what.if.most.constrained" />
			</p>
			<div class="controls" data-most-constrained>
				<div style="float: left; width: 154px;">
					<span data-constrained="current"></span>
				</div>
				<div style="float: left; width: 154px; margin-left: 4px;">
					<span data-constrained="what-if"></span>
				</div>
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<a target="_blank" class="gray-button" download="non-compliant-devices.csv" type="text/csv" data-link="export" data-href="${pageContext.request.contextPath}/services/rest/snapshots/">
					<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">
					<s:text name="ready.report.overall.what.if.export.devices" />
				</a>
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<s:text name="ready.report.overall.what.if.device.indicators" />
				<a data-link="device" data-href="${pageContext.request.contextPath}/reports/device/deviceAssessment.action?">
					here
				</a>
			</div>
		</div>
	</form>
	
</div>
</body>
