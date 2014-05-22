<!DOCTYPE HTML>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/reporting.js"></script>
<c:if test="${currentOrg.orgTypeCode.equalsIgnoreCase('readiness')}">
	<script type='text/javascript' src='https://www.google.com/jsapi'></script>
	<script type="text/javascript"><c:out value="${geoChart.chart}" escapeXml="false" /></script>
	</c:if>
</head>
<body>
	<s:a id="selectOrg" cssStyle="display: none;" aria-hidden="true">
		<s:param name="viewBy">
			<c:out value="${viewBy}" />
		</s:param>
		<s:param name="consortium">
			<c:out value="${consortium}" />
		</s:param>
	</s:a>
	<c:if test="${not currentOrg.orgTypeCode.equalsIgnoreCase('readiness')}">
		<jsp:include page="../breadcrumbs.jsp" />
	</c:if>
	<div class="report-layout-wrapper">
		<table class="report-layout">
			<tbody>
				<tr>
					<td class="left"><c:if test="${dualConsortium}">
							<div>Consortium</div>
							<div class="report-toggle">
								<s:form action="completionStatus" id="consortiumForm">
									<c:choose>
										<c:when test="${consortium eq 'SBAC'}">
											<label class="radio inline"> <input type="radio" name="consortium" value="SBAC" checked="checked">
												SBAC
											</label>
											<label class="radio inline"> <input type="radio" name="consortium" value="PARCC"> PARCC
											</label>
										</c:when>
										<c:otherwise>
											<label class="radio inline"> <input type="radio" name="consortium" value="SBAC"> SBAC
											</label>
											<label class="radio inline"> <input type="radio" name="consortium" value="PARCC" checked="checked">
												PARCC
											</label>
										</c:otherwise>
									</c:choose>
									<s:hidden name="viewBy" />
									<c:if test="${not viewBy.equalsIgnoreCase('map')}">
										<s:hidden name="orgCode" />
										<s:hidden name="stateCode" />
										<s:hidden name="stateName" />
									</c:if>
								</s:form>
							</div>
						</c:if> <c:choose>
							<c:when test="${viewBy.equalsIgnoreCase('table') and currentOrg.orgTypeCode.equalsIgnoreCase('readiness')}">
								<div id='mapDiv' style="height: ${ geoChart.height }px; width: ${ geoChart.width }px; margin: auto"></div>
							</c:when>
							<c:when test="${not currentOrg.orgTypeCode.equalsIgnoreCase('readiness')}">
								<div class="report-state-img">
									<img src="${pageContext.request.contextPath}/static/images/states/${stateCode}.png" />
								</div>
								<div class="report-state-label">
									<c:out value="${stateName}" />
								</div>
							</c:when>
						</c:choose></td>
					<td class="center">
						<div class="report-header">
							<s:text name="ready.report.completion.title" />
						</div>
						<div class="reports-updated-note">
							<s:text name="ready.report.updated" />
						</div> <c:if test="${not empty orgSummary}">
							<div class="report-born-on">
								<c:out value="${asOfDate}" />
							</div>
							<c:choose>
								<c:when test="${currentOrg.orgTypeCode.equalsIgnoreCase('school')}">
									<div class="report-org-text">
										<div class="color <s:property value="getColorBoolean(orgSummary.dataEntryComplete)"/>"></div>
										<div class="report-label">${orgSummary['orgName']}-</div>
										<div class="report-label2">
											<s:text name="ready.report.completion.status.school">
												<s:param>
													<c:out value="${orgSummary['dataEntryComplete'].equalsIgnoreCase('yes') ? '' : 'not ' }" />
												</s:param>
											</s:text>
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<div class="report-org-text">
										<div class="color <s:property value="getPercentColor(orgSummary['percentComplete'])"/>"></div>

										<div class="report-label">
											<c:choose>
												<c:when test="${currentOrg.orgTypeCode.equalsIgnoreCase('readiness')}">
													<c:out value="${consortium}" />
												</c:when>
												<c:otherwise>
													<c:out value="${orgSummary['orgName']}" />
												</c:otherwise>
											</c:choose>
											-
										</div>
										<div class="report-label2">
											<s:text name="ready.report.completion.status">
												<s:param>
													<c:out value="${orgSummary['percentComplete']}" />
												</s:param>
											</s:text>
											<img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png"
												title="Number of Schools indicating Data Submitted / Total Number of Schools" />
										</div>
									</div>
								</c:otherwise>
							</c:choose>
						</c:if>
					</td>
					<td class="right"><c:if test="${not empty orgSummary}">
							<div class="report-buttons">
								<s:a cssClass="btn btn-xs btn-default" action="download" download="completion-report.pdf">
									<img src="${pageContext.request.contextPath}/static/images/icons/printer.png" class="report-button-image">
									<s:text name="ready.report.print.pdf" />
									<s:param name="viewBy">
										<c:out value="${viewBy}" />
									</s:param>
									<s:param name="consortium">
										<c:out value="${consortium}" />
									</s:param>
									<s:param name="orgCode">
										<c:out value="${orgCode}" />
									</s:param>
									<s:param name="fileType" value="'pdf'" />
								</s:a>
							</div>
						</c:if>
						<div class="report-legend-tag" style="width: 145px;">
							<c:choose>
								<c:when
									test="${currentOrg.orgTypeCode.equalsIgnoreCase('district') or currentOrg.orgTypeCode.equalsIgnoreCase('school')}">
									<h3 class="report-legend-tag-title">
										<s:text name="ready.report.submitted" />
									</h3>
									<div class="color-group">
										<span class="color report-level4"></span>
										<s:text name="core.yes" />
									</div>

									<div class="color-group">
										<span class="color report-level1"></span>
										<s:text name="core.no" />
									</div>
								</c:when>
								<c:otherwise>
									<h3 class="report-legend-tag-title">
										<s:text name="ready.report.percent.complete" />
									</h3>
									<div class="color-group">
										<span class="color" style="background-color: #e5e5e5"></span>
										<s:text name="ready.report.not.member" />
									</div>

									<div class="color-group">
										<span class="color report-level1"></span>
										<s:text name="ready.report.level.one" />
									</div>

									<div class="color-group">
										<span class="color report-level2"></span>
										<s:text name="ready.report.level.two" />
									</div>

									<div class="color-group">
										<span class="color report-level3"></span>
										<s:text name="ready.report.level.three" />
									</div>

									<div class="color-group">
										<span class="color report-level4"></span>
										<s:text name="ready.report.level.four" />
									</div>
								</c:otherwise>
							</c:choose>
						</div></td>
				</tr>
			</tbody>
		</table>
	</div>
	<c:if test="${currentOrg.orgTypeCode.equalsIgnoreCase('readiness')}">
		<s:form action="completionStatus" id="viewByForm" cssStyle="margin-left: 10px;">
			<c:choose>
				<c:when test="${viewBy eq 'map'}">
					<label class="radio inline"> <input type="radio" name="viewBy" value="map" checked="checked"> Map
					</label>
					<label class="radio inline"> <input type="radio" name="viewBy" value="table"> Table
					</label>
				</c:when>
				<c:otherwise>
					<label class="radio inline"> <input type="radio" name="viewBy" value="map"> Map
					</label>
					<label class="radio inline"> <input type="radio" name="viewBy" value="table" checked="checked"> Table
					</label>
				</c:otherwise>
			</c:choose>
			<s:hidden name="consortium" />
		</s:form>
	</c:if>
	<c:choose>
		<c:when test="${viewBy.equalsIgnoreCase('map') and currentOrg.orgTypeCode.equalsIgnoreCase('readiness')}">
			<div id='mapDiv' style="height: ${ geoChart.height }px; width: ${ geoChart.width }px; margin: auto"></div>
		</c:when>
		<c:when
			test="${(viewBy.equalsIgnoreCase('table') and currentOrg.orgTypeCode.equalsIgnoreCase('readiness')) or not currentOrg.orgTypeCode.equalsIgnoreCase('readiness')}">
			<s:form>
				<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="status" selectable="false"
					rowValue="status.orgCode">
					<ui:dataGridHeader>
						<c:if test="${totalNumberOfItems > 0}">
							<s:a cssClass="btn btn-xs btn-default" action="download" download="completion-report.csv">
								<img src="${pageContext.request.contextPath}/static/images/icons/export.png" class="report-button-image">
								<s:text name="ready.report.export.csv" />
								<s:param name="viewBy">
									<c:out value="${viewBy}" />
								</s:param>
								<s:param name="consortium">
									<c:out value="${consortium}" />
								</s:param>
								<s:param name="orgCode">
									<c:out value="${orgCode}" />
								</s:param>
								<s:param name="fileType" value="'csv'" />
							</s:a>
						</c:if>
					</ui:dataGridHeader>
					<ui:dataGridColumn code="orgName" name="Organization">
						<c:choose>
							<c:when test="${not currentOrg.orgTypeCode.equalsIgnoreCase('school')}">
								<s:a>
									<s:param name="viewBy">
										<c:out value="${viewBy}" />
									</s:param>
									<s:param name="consortium">
										<c:out value="${consortium}" />
									</s:param>
									<s:param name="orgCode">
										<c:out value="${status.orgCode}" />
									</s:param>
									<c:out value="${status.orgName}" />
								</s:a>
							</c:when>
							<c:otherwise>
								<s:property value="status.orgName" />
							</c:otherwise>
						</c:choose>
					</ui:dataGridColumn>
					<ui:dataGridColumn code="orgCode" name="Org Code">
						<s:property value="status.orgCode" />
					</ui:dataGridColumn>
					<ui:dataGridColumn code="completionStatus" name="Completion Status">
						<c:choose>
							<c:when	test="${currentOrg.orgTypeCode.equalsIgnoreCase('district') or currentOrg.orgTypeCode.equalsIgnoreCase('school')}">
							  <c:choose>
								 <c:when test="${status.dataEntryComplete == '(missing)'}">
									<div class="report-label">
										<span class="below-req"> <c:out value="${status.dataEntryComplete}" /> </span>
									</div>	
								 </c:when>
								 <c:otherwise>
									<div class="report-datagrid-color-icon">
									<div class="color <s:property value="getColorBoolean(status.dataEntryComplete)"/>"></div>
										<div class="report-label">
											<s:property value="status.dataEntryComplete" />
										</div>
									</div>	
								 </c:otherwise>
							  </c:choose>		
							</c:when>
							<c:otherwise>
								<div class="report-datagrid-color-icon">
								<div class="color <s:property value="getPercentColor(status.percentComplete)"/>"></div>
								<div class="report-label">
									<s:property value="status.percentComplete" />
								</div>
							</div>	
							</c:otherwise>
						</c:choose>
					</ui:dataGridColumn>
				</ui:datagrid>
			</s:form>
		</c:when>
	</c:choose>

</body>
</html>