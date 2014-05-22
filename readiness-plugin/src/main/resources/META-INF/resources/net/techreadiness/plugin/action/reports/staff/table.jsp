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
	<script type="text/javascript"><c:out value="${geoChart.chart}" escapeXml="false" /></script>

</head>
<body>

<div class="report-layout-wrapper">
<table class="report-layout">
	<tr>
		<td class="left">
			<c:if test="${ dualConsortium }">
				<div>Consortium</div>
				<div class="report-toggle">
					<s:form action="staffReport" id="consortiumForm">
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
						<s:hidden name="question"/>
					</s:form>
				</div>		
			</c:if>
			<div id='mapDiv' style="height: ${ geoChart.height }px; width: ${ geoChart.width }px; margin: auto"></div>
		</td>
		<td class="center">
			<div class="report-header">Staff &amp; Personnel Indicator Report</div>
			<div class="reports-updated-note">Reports are updated hourly</div>
			<c:if test="${ asOfDate != null }">
				<div class="report-born-on"><c:out value="${ asOfDate }"/></div>
			</c:if>
			<div class="report-question">
				<div class="report-label">SELECT A QUESTION:</div>
				<div style="clear: both;"></div>
				<div class="data">
					<s:form action="staffReport">
						<s:select name="question" list="questions" onchange="this.form.submit()"/>
						<s:hidden name="viewBy"/>
						<s:hidden name="consortium"/>
					</s:form>
				</div>				
			</div>
		</td>
		<td class="right">
			<div class="report-buttons">
				<button type="submit" class="btn btn-xs btn-default print-button" onclick="$('#downloadPdf').submit();"><img src="${pageContext.request.contextPath}/static/images/icons/printer.png" class="report-button-image">Print to .pdf</button>
			</div>
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
					<div class="report-label">8-10</div>
				</div>
				
				<div class="legend-row">
					<div class="color report-level2"></div>
					<div class="report-label">6-7</div>
				</div>
				
				<div class="legend-row">
					<div class="color report-level3"></div>
					<div class="report-label">4-5</div>
				</div>
				
				<div class="legend-row">
					<div class="color report-level4"></div>
					<div class="report-label">0-3</div>
				</div>
			</div>
		</td>
	</tr>
</table>
</div>
<div class="datagrid-toggle">
<s:form action="staffReport" id="viewByForm">
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
	<s:hidden name="question"/>
</s:form>
<s:form action="staffReport" id="selectOrg">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="orgCode" id="orgCode"/>
	<s:hidden name="question"/>
</s:form>
<s:form action="download" id="downloadCsv">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="fileType" value="csv"/>
	<s:hidden name="question"/>
</s:form>
<s:form action="download" id="downloadPdf">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="fileType" value="pdf"/>
	<s:hidden name="question"/>
</s:form>
</div>

<s:form>
	<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="status" selectable="false" rowValue="status.orgCode">
		<ui:dataGridHeader>
			<button type="button" class="btn btn-xs btn-default export-button" onclick="$('#downloadCsv').submit();">
				<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">Export to .csv
			</button>
		</ui:dataGridHeader>
		<ui:dataGridColumn code="orgName" name="Organization">
			<a href="javascript:selectOrg('<s:property value="status.orgCode"/>')"><s:property value="status.orgName"/></a>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="orgCode" name="Org Code">
			<s:property value="status.orgCode"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="level1" name="Level of Concern<br><div class='report-datagrid-column-color-icon'><div class='color report-level4'></div></div>0-3">
			<s:property value="status['levelOfConcernCount0to3']"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="level2" name="Level of Concern<br><div class='report-datagrid-column-color-icon'><div class='color report-level3'></div></div>4-5">
			<s:property value="status['levelOfConcernCount4to5']"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="level3" name="Level of Concern<br><div class='report-datagrid-column-color-icon'><div class='color report-level2'></div></div> 6-7">
			<s:property value="status['levelOfConcernCount6to7']"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="level4" name="Level of Concern<br><div class='report-datagrid-column-color-icon'><div class='color report-level1'></div></div> 8-10">
			<s:property value="status['levelOfConcernCount8to10']"/>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="average" name="Aggregated Average<br>Response <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='Sum of Level of Concerns / # of Organizations Responding Score Range 0-10'/>">
			<div class="report-datagrid-color-icon">
				<div class="color <s:property value="getColorStaff(status.levelOfConcernAverageResponse)"/>"></div>
				<div class="report-label"><s:property value="status.levelOfConcernAverageResponse"/></div>
			</div>
		</ui:dataGridColumn>
	</ui:datagrid>
</s:form>

</body>