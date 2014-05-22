<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
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
		
	</script>
	<script type="text/javascript"><c:out value="${geoChart.chart}" escapeXml="false" /></script>
</head>
<body>

<div class="report-layout-wrapper">
<table class="report-layout">
	<tr>
		<td class="left">
			<c:choose>
				<c:when test="${ dualConsortium }">
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
				</c:when>
				<c:otherwise>
					<div style="height: 175px; width: 285px;">
						&nbsp;
					</div>
				</c:otherwise>
			</c:choose>
		</td>
		<td class="center">
			<div class="report-header">Staff &amp; Personnel Indicator Report</div>
			<div class="reports-updated-note">Reports are updated hourly</div>
			<c:if test="${not empty asOfDate}"> 
				<div class="report-born-on"><c:out value="${ asOfDate }"/></div>
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
			</c:if>	
		</td>
		<td class="right">
		  <c:if test="${not empty asOfDate}"> 
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
<s:form action="download" id="downloadPdf">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="fileType" value="pdf"/>
	<s:hidden name="question"/>
</s:form>
</div>

<div id='mapDiv' style="height: ${ geoChart.height }px; width: ${ geoChart.width }px; margin: auto"></div>

</body>
</html>