<!DOCTYPE HTML>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trt" uri="http://techreadiness.net" %>
<%@ taglib prefix="core" uri="http://techreadiness.net/components" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
<script type="text/javascript">
$(function(){
	$('input[name=requirements]').on('change', function(event){
		var radio = $(event.target);
		$('.minimum, .recommended').hide();
		$('.' + radio.val()).show();
	});

    $( "#dialog" ).dialog({
      autoOpen: false,
      height: 500,
	  width: 650
    });
 
    $( "#what_is_link" ).click(function() {
      $( "#dialog" ).dialog( "open" );
    });
	
});
</script>
<style type="text/css">
  table.ready-status {
    width:99%;
    margin:10px;
    border-spacing: 10px;
    border-collapse: separate;
    margin-bottom: 4 px;
  }
  table.ready-status td {
    width:50%;
    border: 1px solid lightgrey;
  }
  .core-section-header {
    background-color: #E8E8E8;
    border-top: 1px solid lightgrey;
    font-size: 16px;
    font-weight: bold;
    padding: 4px;
  }
  .core-section-body {
    padding: 5px 10px 5px 10px;
    line-height: 16px;
  }
  
  .core-subsection-bordered {
    border-bottom: 1px solid lightgrey;
  }
  .subsection {
   	margin-top: 10px;
  }
  .subsection p {
    margin-left: 20px;
    margin-top: 4px;
    margin-bottom: 4px;
  }
  .subsection-header {
    font-weight: bold;
    font-size: 15px;
    color:gray;
  }
  .subsection-header small {
    font-weight: normal;
  }
  .muted {
    color: #888888;
    font-size: 80%;
  }
  
  .notifications li {
  	list-style: disc;
  	margin-left: 18px;
  }
  
  .notifications h1 {
  font-size: 15px;
  line-height: 18px;
  color: red;
  }
</style>
</head>
<body>

<table class="ready-status">
<tr>
  <td>
	<div class="core-section-header">
		<s:text name="ready.dashboard.title" /> <span class="muted">as of <fmt:formatDate value="${snapshot.executeDate}" pattern="MMMM dd, yyyy"/> at <fmt:formatDate value="${snapshot.executeDate}" pattern="hh:mm a"/> CT</span>
	</div>
	<div class="core-section-body">
		<div style="font-weight: bold;font-size: 15px;">
			<s:text name="ready.dashboard.requirements" />&nbsp;&nbsp;
			<label class="radio inline">
				<input type="radio" name="requirements" checked="checked" value="minimum">
				<s:text name="ready.dashboard.minimum" />
			</label>
			<label class="radio inline">
				<input type="radio" name="requirements" value="recommended">
				<s:text name="ready.dashboard.recommended" />
			</label>
		</div>
		<c:if test="${org.orgTypeCode.equalsIgnoreCase('school')}">
			<div class="subsection">
				<span class="subsection-header"><s:text name="ready.dashboard.data.entry.title" /></span>
				<a href="${pageContext.request.contextPath}/reports/completion/completionStatus"><s:text name="ready.dashboard.view.report" /></a><br>
				<c:choose>
					<c:when test="${org.dataEntryComplete}">
						<p class="text-success">
							<s:text name="ready.dashboard.data.entry.success" />
							<a href="${pageContext.request.contextPath}/data-entry-task.action"><s:text name="ready.dashboard.update" /></a>
						</p>
					</c:when>
					<c:otherwise>
						<p class="text-error">
							<s:text name="ready.dashboard.data.entry.error" />
							<a href="${pageContext.request.contextPath}/data-entry-task.action"><s:text name="ready.dashboard.update" /></a>
						</p>
					</c:otherwise>
				</c:choose>
			</div>				
			<div class="subsection">
				<span class="subsection-header"><s:text name="ready.dashboard.survey.title" /></span><br>
				<c:choose>
					<c:when test="${numberOfUnansweredSurveys > 0}">
						<p class="text-error">
							<s:text name="ready.dashboard.survey.error">
								<s:param><c:out value="${numberOfUnansweredSurveys}" /></s:param>
							</s:text>
							<a href="${pageContext.request.contextPath}/survey-task.action"><s:text name="ready.dashboard.update" /></a>
						</p>
					</c:when>
					<c:otherwise>
						<p class="text-success">
							<s:text name="ready.dashboard.survey.success" />
							<a href="${pageContext.request.contextPath}/survey-task.action"><s:text name="ready.dashboard.update" /></a>
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
		<div class="minimum">
			<div class="subsection">
				<span class="subsection-header"><s:text name="ready.dashboard.device.title" /></span>
				<a href="${pageContext.request.contextPath}/reports/device/deviceAssessment?requirements=Minimum"><s:text name="ready.dashboard.view.report" /></a><br>
				<p>
					<s:text name="ready.dashboard.device.count">
						<s:param> <fmt:formatNumber value="${minSummary.deviceCount}" pattern="#,###,###" /> </s:param>
					</s:text>   
					<c:if test="${!org.orgTypeCode.equalsIgnoreCase('readiness')}">
						<a href="${pageContext.request.contextPath}/device/list.action"><s:text name="ready.dashboard.update" /></a>
					</c:if>
				</p>
				<p>
					<trt:statusColor status="${minSummary.devicePassingPercent}" size="small" />
					<s:text name="ready.dashboard.device.status.min">
						<s:param><c:out value="${minSummary.devicePassingPercent}"  escapeXml="false"/></s:param>
					</s:text>
				</p>
			</div>
			<div class="subsection">
				<span class="subsection-header"><s:text name="ready.dashboard.test.taker.title" /></span>
				<a href="${pageContext.request.contextPath}/reports/tester/testerAssessment?requirements=Minimum"><s:text name="ready.dashboard.view.report" /></a><br>
				<p>
					<c:choose>
						<c:when test="${minSummary.testTakerPercentStudentsTestable.equalsIgnoreCase('(missing)')}">
							<span class="text-error"><s:text name="ready.dashboard.test.taker.missing" /></span>
						</c:when>
						<c:when test="${minSummary.testTakerPercentStudentsTestable.equalsIgnoreCase('TBD')}">
							<span class="text-error">
								<s:text name="ready.dashboard.test.taker.tbd">
									<s:param><s:text name="ready.dashboard.minimum" /></s:param>
								</s:text>
							</span>
						</c:when>
						<c:otherwise>
							<trt:statusColor status="${minSummary.testTakerPercentStudentsTestable}" size="small" />
							<s:text name="ready.dashboard.test.taker.status.min">
								<s:param><c:out value="${minSummary.testTakerPercentStudentsTestable}" escapeXml="false"/></s:param>
							</s:text>
						</c:otherwise>
					</c:choose>
				</p>
			</div>
			<div class="subsection">
				<span class="subsection-header"><s:text name="ready.dashboard.network.title" /></span>
				<a href="${pageContext.request.contextPath}/reports/network/networkAssessment?requirements=Minimum"><s:text name="ready.dashboard.view.report" /></a><br>
				<p>
					<c:choose>
						<c:when test="${minSummary.networkPercentStudentsTestable.equalsIgnoreCase('(missing)')}">
							<span class="text-error"><s:text name="ready.dashboard.network.missing" /></span>
						</c:when>
						<c:when test="${minSummary.networkPercentStudentsTestable.equalsIgnoreCase('TBD')}">
							<span class="text-error">
								<s:text name="ready.dashboard.network.tbd">
									<s:param><s:text name="ready.dashboard.minimum" /></s:param>
								</s:text>
							</span>
						</c:when>
						<c:otherwise>
							<trt:statusColor status="${minSummary.networkPercentStudentsTestable}" size="small" />
							<s:text name="ready.dashboard.network.status.min">
								<s:param><c:out value="${minSummary.networkPercentStudentsTestable}" escapeXml="false"/></s:param>
							</s:text>
						</c:otherwise>
					</c:choose>
				</p>
			</div>
		</div>
		<div class="recommended" style="display:none;">
			<div class="subsection">
				<span class="subsection-header"><s:text name="ready.dashboard.device.title" /></span>
				<a href="${pageContext.request.contextPath}/reports/device/deviceAssessment?requirements=Recommended"><s:text name="ready.dashboard.view.report" /></a><br>
				<p>
					<s:text name="ready.dashboard.device.count">
						<s:param> <fmt:formatNumber value="${recSummary.deviceCount}" pattern="#,###,###" /> </s:param>
					</s:text>
					<c:if test="${!org.orgTypeCode.equalsIgnoreCase('readiness')}">
						<a href="${pageContext.request.contextPath}/device/list.action"><s:text name="ready.dashboard.update" /></a>
					</c:if>
				</p>
				<p>
					<trt:statusColor status="${recSummary.devicePassingPercent}" size="small" />
					<s:text name="ready.dashboard.device.status.rec">
						<s:param><c:out value="${recSummary.devicePassingPercent}"  escapeXml="false"/></s:param>
					</s:text>
				</p>
			</div>
			<div class="subsection">
				<span class="subsection-header"><s:text name="ready.dashboard.test.taker.title" /></span>
				<a href="${pageContext.request.contextPath}/reports/tester/testerAssessment?requirements=Recommended"><s:text name="ready.dashboard.view.report" /></a><br>
				<p>
					<c:choose>
						<c:when test="${recSummary.testTakerPercentStudentsTestable.equalsIgnoreCase('(missing)')}">
							<span class="text-error"><s:text name="ready.dashboard.test.taker.missing" /></span>
						</c:when>
						<c:when test="${recSummary.testTakerPercentStudentsTestable.equalsIgnoreCase('TBD')}">
							<span class="text-error">
								<s:text name="ready.dashboard.test.taker.tbd">
									<s:param><s:text name="ready.dashboard.recommended" /></s:param>
								</s:text>
							</span>
						</c:when>
						<c:otherwise>
							<trt:statusColor status="${recSummary.testTakerPercentStudentsTestable}" size="small" />
							<s:text name="ready.dashboard.test.taker.status.rec">
								<s:param><c:out value="${recSummary.testTakerPercentStudentsTestable}" escapeXml="false"/></s:param>
							</s:text>
						</c:otherwise>
					</c:choose>
				</p>
			</div>
			<div class="subsection">
				<span class="subsection-header"><s:text name="ready.dashboard.network.title" /></span>
				<a href="${pageContext.request.contextPath}/reports/network/networkAssessment?requirements=Recommended"><s:text name="ready.dashboard.view.report" /></a><br>
				<p>
					<c:choose>
						<c:when test="${recSummary.networkPercentStudentsTestable.equalsIgnoreCase('(missing)')}">
							<span class="text-error"><s:text name="ready.dashboard.network.missing" /></span>
						</c:when>
						<c:when test="${recSummary.networkPercentStudentsTestable.equalsIgnoreCase('TBD')}">
							<span class="text-error">
								<s:text name="ready.dashboard.network.tbd">
									<s:param><s:text name="ready.dashboard.recommended" /></s:param>
								</s:text>
							</span>
						</c:when>
						<c:otherwise>
							<trt:statusColor status="${recSummary.networkPercentStudentsTestable}" size="small" />
							<s:text name="ready.dashboard.network.status.rec">
								<s:param><c:out value="${recSummary.networkPercentStudentsTestable}" escapeXml="false"/></s:param>
							</s:text>
						</c:otherwise>
					</c:choose>
				</p>
			</div>
		</div>
	</div>
   </td>
  <td>
	<div class="core-section-header" style="background-color: #FFFF99">
		<s:text name="ready.dashboard.resources.title" />
	</div>
	<div class="core-section-body">
		<core:text name="ready.dashboard.resources" escapeHtml="false"/>
	</div>
  </td>
</tr>
<tr>
  <td colspan="2">
	<div class="core-section-header" style="background-color: #99FF99">
		<s:text name="ready.dashboard.notifications.title" />
	</div>
	<div class="core-section-body notifications" >
		<core:text name="ready.dashboard.notifications" escapeHtml="false"/>
  	</div>
  </td>
</tr>
</table>
  <script>
  </script>
  <div id="dialog" title="What is the Technology Readiness Tool?">
	<core:text name="ready.dashboard.whatis" escapeHtml="false"/>
  </div>
</body>
</html>