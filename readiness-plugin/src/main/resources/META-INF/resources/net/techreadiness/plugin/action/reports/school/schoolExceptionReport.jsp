<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
	<script type='text/javascript' src='https://www.google.com/jsapi'></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery.plugin.geoChart.js"></script>
	<script type="text/javascript">
		google.load('visualization', '1', {'packages': ['geochart']});
		
		$(document).ready(function() {
			$("#usMap").geoChart($("#mapData").html());
			$("#dcMap").geoChart($("#mapData").html(), {region: 'US-DC'});
			
			$("body").delegate("form input[type='radio']", 'click', function(event) {
				$("div.report-layout-wrapper").trigger("update-state", [{data: $(event.target).closest("form").serializeArray()}]);
			});

			$("body").delegate("form.req-select select", 'change', function(event) {
				$("div.report-layout-wrapper").trigger("update-state", [{data: $(event.target).closest("form").serializeArray()}]);
			});

			$("body").delegate("div.page-content a.drill-down", "click", function(event){
				event.preventDefault();
				$("div.report-layout-wrapper").trigger("update-state", [{url: $(event.target).attr("href")}]);
			});

			$("body").delegate("div.report-layout-wrapper", "update-state", function(event, options) {
				var defaults = {
						url: $("#pageRefresh").html(),
						type: 'GET',
						success: function(response) {
							var $response = $(response);
							$("div.report-layout-wrapper").replaceWith($response.find("div.report-layout-wrapper"));
							$("div.page-content").replaceWith($response.find("div.page-content"));
							$('div.page-content').trigger('componentLoaded');
							var mapDataUrl = $("#mapData").html();
							$("#usMap").geoChart(mapDataUrl);
							$("#dcMap").geoChart(mapDataUrl, {region: 'US-DC'});
						}
					};
				$.extend(defaults, options);
				$.ajax(defaults);
			});

			$("body").delegate("#usMap, #dcMap", "regionSelected", function(event, orgId){
				$("div.report-layout-wrapper").trigger("update-state", [{data: {'orgId': orgId}}]);
			});
		});
	</script>
</head>
<body>

<div id="mapData" style="display: none;"><s:url action="map-data" includeParams="none" /></div>
<div id="pageRefresh" style="display: none;"><s:url includeParams="none" /></div>



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
					<li>${crumb.label} </li>
					<li>&gt;</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
</c:if>
<table class="report-layout">
	<tr>
		<td class="left">
			<div class="report-toggle">
				<c:if test="${dualConsortium}">
					<div>Consortium</div>
					<s:form>
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
					</s:form>
				</c:if>
			</div>
			<c:choose>
				<c:when test="${viewBy == 'Table' and orgTypeReadiness}">
					<div id="usMap" class="us-map-small" data-hide-tooltip="true">
					</div>
					<div class="dc-map-small-wrapper">
						<div id="dcMap" class="dc-map-small" data-hide-tooltip="true">
						</div>
					</div>
					<span class="dc-map-small-label">Washington DC</span>
				</c:when>
				<c:when test="${viewBy == 'Table' and !orgTypeReadiness}">
					<div class="report-state-img"><img src="${pageContext.request.contextPath}/static/images/states/${state.localCode}.png" /></div>
					<div class="report-state-label"><c:out value="${state.name}" /></div>
				</c:when>
			</c:choose>	
		</td>
		<td class="center">
			<c:if test="${not empty asOfDate}">
				<div class="report-born-on"><c:out value="${ asOfDate }"/></div>
			</c:if>
			<div class="report-header"><s:text name="ready.report.school.title" /></div>
			<div class="reports-updated-note">Reports are updated hourly</div>
			<div class="report-question">
				<div class="report-label">SELECT A QUESTION:</div>
				<div style="clear: both;"></div>
				<div class="data">
					<s:form cssClass="req-select">
						<s:select name="question" list="questions" />
					</s:form>
				</div>				
			</div>
		</td>
		<td class="right">
			<div class="report-buttons">
				<s:a cssClass="btn btn-xs btn-default print-button" action="download-report">
					<img src="${pageContext.request.contextPath}/static/images/icons/printer.png" class="report-button-image">
					<s:text name="ready.report.export.pdf" />
					<s:param name="fileType">pdf</s:param>
				</s:a>
			</div>
			<div style="clear: both;"></div>
			<ul class="report-caption">
				<li class="header">
					<span class="report-label"><s:text name="ready.report.staff.legend.title" /></span>
				</li>
				<c:if test="${orgTypeReadiness}">
					<li>
						<div class="color report-level0"></div>
						<span class="report-label"><s:text name="ready.report.not.member" /></span>
					</li>
				</c:if>
				<li>
					<div class="color report-level1"></div>
					<span class="report-label">8-10</span>
				</li>
				
				<li>
					<div class="color report-level2"></div>
					<span class="report-label">6-7</span>
				</li>
				
				<li>
					<div class="color report-level3"></div>
					<span class="report-label">4-5</span>
				</li>
				<li>
					<div class="color report-level4"></div>
					<span class="report-label">0-3</span>
				</li>
			</ul>
		</td>
	</tr>
</table>
</div>
<div class="page-content">
	<c:if test="${orgTypeReadiness}">
		<div class="datagrid-toggle">
			<s:form action="testerAssessment" id="viewByForm">
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
			</s:form>
		</div>
	</c:if>
	<c:choose>
		<c:when test="${viewBy == 'Map'}">
			<div id="usMap" class="us-map-large">
			</div>
			<div class="dc-map-large-wrapper">
				<div id="dcMap" class="dc-map-large" >
				</div>
				<span class="dc-map-large-label">Washington DC</span>
			</div>
		</c:when>
		<c:otherwise>
			<s:form>
				<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="status" selectable="false" rowValue="status.orgCode">
					<ui:dataGridHeader>
						<s:a cssClass="btn btn-xs btn-default print-button" action="download-report">
							<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">
							<s:text name="ready.report.export.csv" />
							<s:param name="fileType">csv</s:param>
						</s:a>
					</ui:dataGridHeader>
					<ui:dataGridColumn code="orgName" name="Organization">
						<c:choose>
							<c:when test="${!orgTypeSchool}">
								<s:a cssClass="drill-down"><s:param name="orgId" value="status.orgId" /><s:property value="status.orgName" /></s:a>
							</c:when>
							<c:otherwise>
								<s:property value="status.orgName"/>
							</c:otherwise>
						</c:choose>
					</ui:dataGridColumn>
					<ui:dataGridColumn code="orgCode" name="Org Code">
						<s:property value="status.orgCode"/>
					</ui:dataGridColumn>
					<ui:dataGridColumn code="level1" name="Level of Concern<br><div class='report-datagrid-column-color-icon'><div class='color report-level4'></div></div>0-3">
						<span class="${ status['levelOfConcernCount0to3'] == '(missing)' ? 'below-req' : '' }"><s:property value="status['levelOfConcernCount0to3']"/></span>
					</ui:dataGridColumn>
					<ui:dataGridColumn code="level2" name="Level of Concern<br><div class='report-datagrid-column-color-icon'><div class='color report-level3'></div></div>4-5">
						<span class="${ status['levelOfConcernCount4to5'] == '(missing)' ? 'below-req' : '' }"><s:property value="status['levelOfConcernCount4to5']"/></span>
					</ui:dataGridColumn>
					<ui:dataGridColumn code="level3" name="Level of Concern<br><div class='report-datagrid-column-color-icon'><div class='color report-level2'></div></div> 6-7">
						<span class="${ status['levelOfConcernCount6to7'] == '(missing)' ? 'below-req' : '' }"><s:property value="status['levelOfConcernCount6to7']"/></span>
					</ui:dataGridColumn>
					<ui:dataGridColumn code="level4" name="Level of Concern<br><div class='report-datagrid-column-color-icon'><div class='color report-level1'></div></div> 8-10">
						<span class="${ status['levelOfConcernCount8to10'] == '(missing)' ? 'below-req' : '' }"><s:property value="status['levelOfConcernCount8to10']"/></span>
					</ui:dataGridColumn>
					<c:choose>
						<c:when test="${orgTypeReadiness || orgTypeState}">
							<ui:dataGridColumn code="average" name="Aggregated Average<br>Response <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='Sum of Level of Concerns / # of Organizations Responding Score Range 0-10'/>">
								<div class="report-datagrid-color-icon">
									<div class="color <s:property value="getColorStaff(status.levelOfConcernAverageResponse)"/>"></div>
									<div class="report-label"><s:property value="status.levelOfConcernAverageResponse"/></div>
								</div>
							</ui:dataGridColumn>
						</c:when>
						<c:otherwise>
							<ui:dataGridColumn code="other" name="Other">
								<span class="${ status.other == '(missing)' ? 'below-req' : '' }"><s:property value="status.other"/></span>
							</ui:dataGridColumn>
						</c:otherwise>
					</c:choose>
				</ui:datagrid>
			</s:form>
		</c:otherwise>
	</c:choose>
</div>
</body>