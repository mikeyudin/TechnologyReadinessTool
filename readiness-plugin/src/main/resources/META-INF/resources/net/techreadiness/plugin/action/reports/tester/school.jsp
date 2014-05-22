<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
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
					<s:form action="testerAssessment" id="consortiumForm">
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
			<div class="report-header">Device to Test-Taker Indicators</div>
			<div class="reports-updated-note">Reports are updated hourly</div>
			<c:if test="${ orgSummary['createDate'] != null }">
				<div class="report-born-on">Data as of ${ orgSummary['createDate'] } </div>
					<c:if test="${ !empty snapshotName }">
						&nbsp;-&nbsp;${ snapshotName == 'default' ? 'Current Window' : snapshotName }
					</c:if>
			</c:if>
			<c:if test="${ !empty orgSummary }">
				<c:choose>
					<c:when test="${ currentOrg.orgTypeName  == 'School' }">
						<c:choose>
							<c:when test="${ orgSummary['testTakerPercentStudentsTestable'] == null or orgSummary['testTakerPercentStudentsTestable'] == 'TBD' }">
								<div class="report-org-text">
								<div class="tbd">TBD</div>
								<div class="report-label">${  orgSummary['orgName'] } - </div>
								<div class="report-label2">It <span class="text-underline">cannot</span> be determined what % of Eligible Test-Takers can be <br> Tested on Existing Devices, based on <br> ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="# Possible Test Starts / # Test Starts Needed"/></div><div style="clear: both;"></div> 
								</div>
							</c:when>
							<c:when test="${ orgSummary['testTakerPercentStudentsTestable'] == '(missing)' }">
								<div class="report-org-text">
								<div class="color report-level1"></div>
								<div class="report-label">${  orgSummary['orgName'] } - </div>
								<div class="report-label2">It <span class="text-underline">cannot</span> be determined what % of Eligible Test-Takers can be Tested on Existing Devices, based on ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="# Possible Test Starts / # Test Starts Needed"/></div><div style="clear: both;"></div>
								</div>
							</c:when>
							<c:when test="${ orgSummary['testTakerPercentStudentsTestable'] == '(Not Applicable)'}">
					  				<div class="report-org-text">
									<div class="report-label">${ orgSummary['orgName'] } - </div>
									<div class="report-label2">There are no Eligible Test Takers </div><div style="clear: both;"></div>
								 	</div>
				   			</c:when>
							<c:otherwise>
								<div class="report-org-text">
								<div class="color <s:property value="getPercentColor(orgSummary['testTakerPercentStudentsTestable'])"/>"></div>
								<div class="report-label">${ orgSummary['orgName'] } - </div>
								<div class="report-label2">${ orgSummary['testTakerPercentStudentsTestable'] } of Eligible Test-Takers can be Tested on Existing Devices, based on ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="# Possible Test Starts / # Test Starts Needed"/></div><div style="clear: both;"></div>
								</div>
							</c:otherwise>
						</c:choose>	
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${ orgSummary['testTakerPercentStudentsTestable'] == null or orgSummary['testTakerPercentStudentsTestable'] == 'TBD' }">
								<div class="report-org-text">
								<div class="tbd">TBD</div>
								<div class="report-label">${  orgSummary['orgName'] } - </div>
								<div class="report-label2">It <span class="text-underline">cannot</span> be determined what % of Eligible Test-Takers can be Tested on Existing Devices, based on ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="# Possible Test Starts / # Test Starts Needed"/></div><div style="clear: both;"></div> 
								</div>
							</c:when>
							<c:when test="${ orgSummary['testTakerPercentStudentsTestable'] == '(missing)' }">
								<div class="report-org-text">
								<div class="color report-level1"></div>
								<div class="report-label">${  orgSummary['orgName'] } - </div>
								<div class="report-label2">It <span class="text-underline">cannot</span> be determined what % of of Eligible Test-Takers can be Tested on Existing Devices, based on ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="# Possible Test Starts / # Test Starts Needed"/></div><div style="clear: both;"></div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="report-org-text">
								<div class="color <s:property value="getPercentColor(orgSummary['testTakerPercentStudentsTestable'])"/>"></div>
								<div class="report-label">${ orgSummary['orgName'] } - </div>
								<div class="report-label2">${ orgSummary['testTakerPercentStudentsTestable'] } of Eligible Test-Takers can be Tested on Existing Devices, based on ${ requirements } Requirements <img src="${pageContext.request.contextPath}/static/images/moreinfo-reverse.png" title="# Possible Test Starts / # Test Starts Needed"/></div><div style="clear: both;"></div>
								</div>
							</c:otherwise>
						</c:choose>	
					</c:otherwise>		
				</c:choose>
			</c:if>
			
			<div class="disclaimer-text1"> <s:text name="ready.report.disclaimer.testtaker.text1"/></div>
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
					<s:form action="testerAssessment">
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
				<ui:flyout text="Minimum / Recommended Requirements" title="Minimum / Recommended Requirements" namespace="/reports/device" action="minRequirements" reset="true" consortium="${ consortium }" snapshotName="${ snapshotName }" snapshotScopeId="${ snapshotScopeId }"  />	
			</div>
		</td>
		<td class="right">
			<div class="report-buttons">
				<c:if test="${ !empty orgSummary }">
					<button type="submit" class="btn btn-xs btn-default print-button" onclick="$('#downloadPdf').submit();"><img src="${pageContext.request.contextPath}/static/images/icons/printer.png" class="report-button-image">Print to .pdf</button>
				</c:if>
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

<div style="clear: both;"></div>

<s:form action="testerAssessment" id="selectOrg">
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
	<s:hidden name="requirements"/>
	<s:hidden name="orgCode" id="orgCode"/>
	<s:hidden name="fileType" value="csv"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
<s:form action="download" id="downloadPdf">
	<s:hidden name="viewBy"/>
	<s:hidden name="consortium"/>
	<s:hidden name="requirements"/>
	<s:hidden name="orgCode" id="orgCode"/>
	<s:hidden name="fileType" value="pdf"/>
	<s:hidden name="snapshotName"/>
	<s:hidden name="snapshotScopeId"/>
</s:form>
<s:form>
	<ui:datagrid itemProvider="${reportItemProvider}" value="reportGrid" var="tester" selectable="false" rowValue="tester.orgCode">
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
					<a href="javascript:selectOrg('<s:property value="tester.orgCode"/>')"><s:property value="tester.orgName"/></a>
				</c:when>
				<c:otherwise>
					<s:property value="tester.orgName"/>	
				</c:otherwise>
			</c:choose>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="orgCode" name="Org Code">
			<s:property value="tester.orgCode"/>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="completionStatus" name="Completion Status">
			<c:choose>
				<c:when test="${tester.dataEntryComplete == '(missing)'}">
					<div class="report-label">
						<span class="below-req"> <c:out value="${tester.dataEntryComplete}" /> </span>
					</div>	
				</c:when>
				<c:otherwise>
					<div class="report-datagrid-color-icon">
						<div class="color <s:property value="getColorBoolean(tester.dataEntryComplete)"/>"></div>
						<div class="report-label">
							<s:property value="tester.dataEntryComplete" />
						</div>
					</div>	
				</c:otherwise>
			</c:choose>		
		</ui:dataGridColumn>		
				
		<ui:dataGridColumn code="numCompliantDevices" name="# Devices Meeting<br>${ requirements } Requirements">
			<s:property value="tester.devicePassingCount"/>
		</ui:dataGridColumn>
			
		<ui:dataGridColumn code="testingStudentCount" name="Enrollment Count<br> of Tested Grades">
			<span class="${ tester.testingStudentCount == '(missing)' ? 'below-req':''}">
			<s:property value="tester.testingStudentCount"/>
			</span>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="testingTestStartCount" name="Total # Test Starts<br>Needed per School <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='Sum of grade-specific enrollment * grade-specific test-start information from the min/rec requirements settings'>">
			<span class="${ tester.testingTestStartCount == '(missing)' ? 'below-req':''}">
				<s:property value="tester.testingTestStartCount"/>
			</span>
		</ui:dataGridColumn>

		<ui:dataGridColumn code="testingWindow" name="Testing Window (Days)">
			<span class="${ tester.testingWindowLengthCalc == '(missing)' ? 'below-req' : '' }"><s:property value="tester.testingWindowLengthCalc"/></span>
			<c:if test="${tester.testingWindowLengthCalc !='(missing)' && tester.testingWindowLengthCalc != tester.testingWindowLength }">
				(Consortium)
			</c:if>
		</ui:dataGridColumn>
		<ui:dataGridColumn code="sessionsPerDay" name="# Sessions per Day">
			<span class="${ tester.sessionsPerDay == '(missing)' ? 'below-req' : '' }"><s:property value="tester.sessionsPerDay"/></span>
		</ui:dataGridColumn>
		
		<ui:dataGridColumn code="testTakerPossibleTestCount" name="# Possible Test Starts <br> With Existing Devices <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='Compliant Devices * Testing Window Days * # Sessions Per Day'>">
			<c:choose>
				<c:when test="${ tester.testTakerPossibleTestCount == 'TBD' }">
					<span class="tbd">TBD</span>
				</c:when>
				<c:when test="${ tester.testTakerPossibleTestCount == '(Not Applicable)'}">
					<span>(Not Applicable)</span>
				</c:when>
				<c:when test="${ tester.testTakerPossibleTestCount == '(missing)' }">
					<span class="below-req">(missing)</span>
				</c:when>
				<c:otherwise>
					<s:property value="tester.testTakerPossibleTestCount"/>
				</c:otherwise>
			</c:choose>

		</ui:dataGridColumn>
		
		
		<ui:dataGridColumn code="testableStudents" name="% of Students<br> that can be Tested <img src='${pageContext.request.contextPath}/static/images/moreinfo-reverse.png' title='(# Compliant Devices * Testing Window Days * # Sessions Per Day) / # of Test Starts'>">
			<c:choose>
				<c:when test="${ tester.testTakerPercentStudentsTestable == '(Not Applicable)'}">
					<span>(Not Applicable)</span>
				</c:when>
				<c:when test="${ tester.testTakerPercentStudentsTestable == '(missing)'}">
					<span class="below-req">(missing)</span>
				</c:when>
				<c:otherwise>
					<div class="report-datagrid-color-icon">
						<div class="color <s:property value="getPercentColor(tester.testTakerPercentStudentsTestable)"/>"></div>
						<div class="report-label"><s:property value="tester.testTakerPercentStudentsTestable"/></div>
					</div>
					<c:if test="${ tester.deviceTbdCount > 0 }">
						<span class="tbd">TBD</span>
					</c:if>
				</c:otherwise>
			</c:choose>
		</ui:dataGridColumn>
	</ui:datagrid>
</s:form>

</body>
