<!DOCTYPE html>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title>Minimum Requirements</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
</head>
<body>
					
	

	<div class="tbd-text"> <s:text name="ready.report.consortia.text.popup"/></div>
	   <c:choose>
			<c:when test="${consortium == 'SBAC'}">
				<div class="report-label2">${consortium} <s:text name="ready.report.consortia.link.text"/> 
	 				<a href='http://www.techreadiness.org/SBACrequirements' target="_blank"> <span class="report-link"> here</span>.</a>
	 			</div>	
	  		</c:when>
	 		<c:when test="${consortium == 'PARCC'}">
	 			<div class="report-label2">${consortium} <s:text name="ready.report.consortia.link.text"/> 
	 				<a href='http://www.techreadiness.org/PARCCrequirements' target="_blank"> <span class="report-link"> here</span>.</a>
	 			</div>	
	  		</c:when>
	  		<c:otherwise>
	  		</c:otherwise>
	  </c:choose>
	
	
	<ui:detailset>
	 	<ui:detailsetRow title="Operating System & Memory">
			<table class="minRecReq">
				<tr>
					<th style="width: 250px">&nbsp;</th>
					<th style="width: 150px">Minimum<br>Memory</th>
					<th style="width: 150px">Recommended<br>Memory</th>
				</tr>
				<c:forEach items="${ minRecReq }" var="os">
					<tr>
						<td align="right">${os.operatingSystem}</td>
						<td>
							${os.minimumMemory}
						</td>
						<td>
							${os.recommendedMemory}
						</td>
					</tr>
				</c:forEach>
			</table>		
		</ui:detailsetRow>
		
		<ui:detailsetRow title="Network & Display">
			<table class="minRecReq">
				<tr>
					<th style="width: 250px">&nbsp;</th>
					<th style="width: 75px">&nbsp;</th>
					<th style="width: 150px">Minimum</th>
					<th style="width: 150px">Recommended</th>
				</tr>
				<c:if test="${ !empty minRecReq }">
					<c:set var="req" value="${ minRecReq[0] }"/>
					<tr>
						<td align="right">Monitor Size</td>
						<td>&nbsp;</td>
						<td>
							<c:choose>
								<c:when test="${ req.minimumMonitorDisplaySize == 'TBD' }">
									TBD
								</c:when>
								<c:otherwise>${ req.minimumMonitorDisplaySize }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${ req.recommendedMonitorDisplaySize == 'TBD' }">
									TBD
								</c:when>
								<c:otherwise>${ req.recommendedMonitorDisplaySize }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">Screen Resolution</td>
						<td>&nbsp;</td>
						<td>
							<c:choose>
								<c:when test="${ req.minimumScreenResolution == 'TBD' }">
									TBD
								</c:when>
								<c:otherwise>${ req.minimumScreenResolution }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${ req.recommendedScreenResolution == 'TBD' }">
									TBD
								</c:when>
								<c:otherwise>${ req.recommendedScreenResolution }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">Max Testing Window Length(days)</td>
						<td>&nbsp;</td>
						<td>
							<c:choose>
								<c:when test="${ req.minimumTestingWindowLength == 'TBD' }">
									TBD
								</c:when>
								<c:otherwise>${ req.minimumTestingWindowLength }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>
						 	<c:choose>
								<c:when test="${ req.recommendedTestingWindowLength == 'TBD' }">
									TBD
								</c:when>
								<c:otherwise>${ req.recommendedTestingWindowLength }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">Throughput Required per Student</td>
						<td>&nbsp;</td>
						<td>
							<c:choose>
								<c:when test="${ req.minimumThroughputRequiredPerStudent == 'TBD' }">
									TBD
								</c:when>
								<c:otherwise>${ req.minimumThroughputRequiredPerStudent }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>
						 	<c:choose>
								<c:when test="${ req.recommendedThroughputRequiredPerStudent == 'TBD' }">
									TBD
								</c:when>
								<c:otherwise>${ req.recommendedThroughputRequiredPerStudent }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">Tested Grades<br>(# of test starts required per grade)</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td>&nbsp;</td>
						<td>K&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							 <c:choose>
								<c:when test="${ req.includeGradeK == 'NotTesting' || req.includeGradeK == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGradeK }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;7&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 	 <c:choose>
								<c:when test="${ req.includeGrade7 == 'NotTesting' || req.includeGrade7 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade7 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td>&nbsp;</td>
						<td>1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							 <c:choose>
								<c:when test="${ req.includeGrade1 == 'NotTesting' || req.includeGrade1 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade1 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;8&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 	 <c:choose>
								<c:when test="${ req.includeGrade8 == 'NotTesting' || req.includeGrade8 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade8 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td>&nbsp;</td>
						<td>2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							 <c:choose>
								<c:when test="${ req.includeGrade2 == 'NotTesting' || req.includeGrade2 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade2 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;9&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 	 <c:choose>
								<c:when test="${req.includeGrade9 == 'NotTesting' || req.includeGrade9 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${req.includeGrade9 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td>&nbsp;</td>
						<td>3&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							 <c:choose>
								<c:when test="${ req.includeGrade3 == 'NotTesting' || req.includeGrade3 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade3 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;10&nbsp;&nbsp;&nbsp;&nbsp;
						 	 <c:choose>
								<c:when test="${req.includeGrade10 == 'NotTesting' || req.includeGrade10 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade10 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td>&nbsp;</td>
						<td>4&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							 <c:choose>
								<c:when test="${ req.includeGrade4 == 'NotTesting' || req.includeGrade4 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade4 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;11&nbsp;&nbsp;&nbsp;&nbsp;
						 	 <c:choose>
								<c:when test="${ req.includeGrade11 == 'NotTesting' || req.includeGrade11 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade11 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td>&nbsp;</td>
						<td>5&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							 <c:choose>
								<c:when test="${ req.includeGrade5 == 'NotTesting' || req.includeGrade5 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade5 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;12&nbsp;&nbsp;&nbsp;&nbsp;
						 	 <c:choose>
								<c:when test="${ req.includeGrade12 == 'NotTesting' || req.includeGrade12 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade12 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td>&nbsp;</td>
						<td>6&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							 <c:choose>
								<c:when test="${ req.includeGrade6 == 'NotTesting' || req.includeGrade6 == '0'}">
									Not Testing
								</c:when>
								<c:otherwise>${ req.includeGrade6 }&nbsp;</c:otherwise>
							</c:choose>
						</td>
						<td>&nbsp;</td>
					</tr>					
				</c:if>
			</table>		
		</ui:detailsetRow>
	</ui:detailset>
</body>
</html>
