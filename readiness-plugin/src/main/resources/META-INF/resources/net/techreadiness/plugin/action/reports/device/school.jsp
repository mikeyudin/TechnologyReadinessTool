<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/reporting.js"></script>
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
					<s:form action="deviceAssessment" id="consortiumForm" method="get">
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
						<s:hidden name="orgCode"/>
						<s:hidden name="stateCode"/>
						<s:hidden name="stateName"/>
						<s:hidden name="requirements"/>
						<s:hidden name="snapshotName"/>
						<s:hidden name="snapshotScopeId"/>
					</s:form>
				</div>		
			</c:if>
			<div class="report-state-img"><img src="${pageContext.request.contextPath}/static/images/states/${ stateCode }.png" /></div>
			<div class="report-state-label">${ stateName }</div>
		</td>
		<td class="center">
			<div class="report-header">Device Indicators</div>
			<div class="reports-updated-note">Reports are updated hourly</div> 
			<c:if test="${ orgSummary['createDate'] != null }">
				<div class="report-born-on">Data as of ${ orgSummary['createDate'] } </div>
					<c:if test="${ !empty snapshotName }">
						&nbsp;-&nbsp;${ snapshotName == 'default' ? 'Current Window' : snapshotName }
					</c:if>
				</c:if>
			<c:choose>
				<c:when test="${ orgSummary['devicePassingPercent'] == null or orgSummary['devicePassingPercent'] == 'TBD' }">
					<div class="report-org-text">
						<div class="tbd">TBD</div>
						<div class="report-label">${ orgSummary['orgName'] } - </div>
						<div class="report-label2">It <span class="text-underline">cannot</span> be determined what % of Devices Meet the ${ requirements } Requirements</div><div style="clear: both;"></div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="report-org-text">
						<div class="color <s:property value="getPercentColor(orgSummary['devicePassingPercent'])"/>"></div>
						<div class="report-label">${ orgSummary['orgName'] } - </div>
						<div class="report-label2">${ orgSummary['devicePassingPercent'] } of Devices Meet the ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="Number of devices that are compliant / Total number of devices"/></div><div style="clear: both;"></div>
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
					<s:form action="deviceAssessment">
						<s:select name="requirements" list="#{ 'Minimum':'Minimum', 'Recommended':'Recommended' }" onchange="this.form.submit()"/>
						<s:hidden name="viewBy"/>
						<s:hidden name="consortium"/>
						<s:hidden name="orgCode"/>
						<s:hidden name="stateCode"/>
						<s:hidden name="stateName"/>
						<s:hidden name="snapshotName"/>
						<s:hidden name="snapshotScopeId"/>
					</s:form>
				</div>				
			</div>
			<div style="clear: both;"></div>
			<div class="report-subheading">
				<ui:flyout text="Minimum / Recommended Requirements" title="Minimum / Recommended Requirements" namespace="/reports/device" action="minRequirements" reset="true" consortium="${ consortium }" snapshotName="${ snapshotName }" snapshotScopeId="${ snapshotScopeId }" />	
			</div>
		</td>
		<td class="right">
		  <c:if test="${ orgSummary['createDate'] != null }">
			 <div class="report-buttons">
				<button type="submit" class="btn btn-xs btn-default print-button" onclick="$('#downloadPdf').submit();"><img src="${pageContext.request.contextPath}/static/images/icons/printer.png" class="report-button-image">Print to .pdf</button>
			 </div><div style="clear: both;"></div>
		  </c:if>
			<div class="report-legend">
				
				<div class="legend-row">
					<div class="report-label"><ui:flyout text="TBD" title="Why are some reports showing TBD as a value?" namespace="/reports/device" action="TBD" reset="true" /></div>
				</div>
				
				<div class="legend-row">
					<div class="color" style="background-color: red"></div>
					<div class="report-label">Requirements Not Met</div>
				</div>
			</div>
		</td>
	</tr>
</table>
</div>

<div style="clear: both;"></div>
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
	<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="device" selectable="false" rowValue="device.name">
		<ui:dataGridHeader>
		  <c:if test="${ orgSummary['createDate'] != null }">
			 <button type="button" class="btn btn-xs btn-default export-button" onclick="$('#downloadCsv').submit();">
				<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export to .csv
			 </button>
		   </c:if>	 
		</ui:dataGridHeader>
		<ui:dataGridColumn code="name" name="Device Name">
			<s:property value="device.name"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="totalDevices" name="# of Devices">
			<s:property value="device.count"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="os" name="Operating<br>System">
			<c:choose>
				<c:when test="${ device.operatingSystemCompliance == 'no' && device.operatingSystem != '(missing)' }">
					<span class="below-req">&nbsp;(<s:property value="device.operatingSystem"/>)&nbsp;</span>
				</c:when>	
				<c:when test="${ device.operatingSystem == '(missing)' }">
					<span class="below-req"><s:property value="device.operatingSystem"/></span>	
				</c:when>
				<c:when test="${ device.operatingSystemCompliance != null && device.operatingSystemCompliance == 'TBD' }">
					<span class="tbd-device">${device.operatingSystem} TBD</span>
				</c:when>	
				<c:otherwise>	
					<span class=" "><s:property value="device.operatingSystem"/></span>
				</c:otherwise>	
			</c:choose>	
		</ui:dataGridColumn>
		<ui:dataGridColumn code="memory" name="Memory">
			<c:choose>
				<c:when test="${ device.memoryCompliance == 'no' && device.memory != '(missing)'}">
					<span class="below-req">&nbsp;(<s:property value="device.memory"/>)&nbsp;</span>
				</c:when>
				<c:when test="${device.memory == '(missing)'}">
					<span class="below-req"><s:property value="device.memory"/></span>
				</c:when>
				<c:when test="${ device.memoryCompliance != null && device.memoryCompliance == 'TBD' }">
					<span class="tbd-device">${ empty device.memory ? '' : '&nbsp;' }TBD</span>
				</c:when>	
				<c:otherwise>	
					<span class=" "><s:property value="device.memory"/></span>
				</c:otherwise>	
			</c:choose>	
		</ui:dataGridColumn>
		<ui:dataGridColumn code="monitor" name="Monitor / Display<br>Size">
			<c:choose>
				<c:when test="${ device.monitorDisplaySizeCompliance == 'no' && device.monitorDisplaySize != '(missing)'}">
					<span class="below-req">&nbsp;(<s:property value="device.monitorDisplaySize"/>)&nbsp;</span>
				</c:when>
				<c:when test="${device.monitorDisplaySize == '(missing)'}">
					<span class="below-req"><s:property value="device.monitorDisplaySize"/></span>
				</c:when>
				<c:when test="${ device.monitorDisplaySizeCompliance != null && device.monitorDisplaySizeCompliance == 'TBD' }">
					<span class="tbd-device">${ empty device.monitorDisplaySize ? '' : '&nbsp;' }TBD</span>
				</c:when>	
				<c:otherwise>	
					<span class=" "><s:property value="device.monitorDisplaySize"/></span>
				</c:otherwise>	
			</c:choose>	
		</ui:dataGridColumn>
		<ui:dataGridColumn code="resolution" name="Screen<br>Resolution">
			<c:choose>
				<c:when test="${ device.screenResolutionCompliance == 'no' && device.screenResolution != '(missing)'}">
					<span class="below-req">&nbsp;(<s:property value="device.screenResolution"/>)&nbsp;</span>
				</c:when>
				<c:when test="${device.screenResolution == '(missing)'}">
					<span class="below-req"><s:property value="device.screenResolution"/></span>
				</c:when>
				<c:when test="${ device.screenResolutionCompliance != null && device.screenResolution == 'TBD' }">
					<span class="tbd-device">${ empty device.screenResolution ? '' : '&nbsp;' }TBD</span>
				</c:when>	
				<c:otherwise>	
					<span class=" "><s:property value="device.screenResolution"/></span>
				</c:otherwise>	
			</c:choose>	
		</ui:dataGridColumn>
		<ui:dataGridColumn code="environment" name="Environment">
			<c:choose>
				<c:when test="${ device.environmentCompliance == 'no' && device.environment != '(missing)'}">
					<span class="below-req">&nbsp;(<s:property value="device.environment"/>)&nbsp;</span>
				</c:when>
				<c:when test="${device.environment == '(missing)'}">
					<span class="below-req"><s:property value="device.environment"/></span>
				</c:when>
				<c:when test="${ device.environmentCompliance != null && device.environment == 'TBD' }">
					<span class="tbd-device">${ empty device.environment ? '' : '&nbsp;' }TBD</span>
				</c:when>	
				<c:otherwise>	
					<span class=" "><s:property value="device.environment"/></span>
				</c:otherwise>	
			</c:choose>	
		</ui:dataGridColumn>
	</ui:datagrid>
</s:form>

</body>