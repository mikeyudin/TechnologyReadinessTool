<!DOCTYPE html>
<%@taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${ !empty breadcrumbs }">
	<ul id="breadcrumbs">
		<c:forEach var="crumb" items="${ breadcrumbs }" varStatus="count">
			<c:choose>
				<c:when test="${ crumb.progressReportLink == 'deviceProgress' }">
					<li>
						<s:form namespace="/reports/device/progress" action="deviceProgress" id="rpt_breadcrumb_deviceProgress">
							<a href="javascript:{}" onclick="document.getElementById('rpt_breadcrumb_deviceProgress').submit(); return false;">${ crumb.label }</a>
							<s:hidden name="consortium"/>
							<s:hidden name="requirements"/>
						</s:form>
					</li>
					<c:if test="${ fn:length(breadcrumbs) != count.count }">
						<li>&gt;</li>
					</c:if>
				</c:when>
				<c:when test="${ crumb.progressReportLink == 'networkProgress' }">
					<li>
						<s:form namespace="/reports/network/progress" action="networkProgress" id="rpt_breadcrumb_networkProgress">
							<a href="javascript:{}" onclick="document.getElementById('rpt_breadcrumb_networkProgress').submit(); return false;">${ crumb.label }</a>
							<s:hidden name="consortium"/>
							<s:hidden name="requirements"/>
						</s:form>
					</li>
					<c:if test="${ fn:length(breadcrumbs) != count.count }">
						<li>&gt;</li>
					</c:if>
				</c:when>
				<c:when test="${ crumb.progressReportLink == 'testTakerProgress' }">
					<li>
						<s:form namespace="/reports/tester/progress" action="testerProgress" id="rpt_breadcrumb_testerProgress">
							<a href="javascript:{}" onclick="document.getElementById('rpt_breadcrumb_testerProgress').submit(); return false;">${ crumb.label }</a>
							<s:hidden name="consortium"/>
							<s:hidden name="requirements"/>
						</s:form>
					</li>
					<c:if test="${ fn:length(breadcrumbs) != count.count }">
						<li>&gt;</li>
					</c:if>
				</c:when>
				<c:when test="${ fn:length(breadcrumbs) == count.count }">
					<li>${ crumb.label }</li>
				</c:when>
				<c:when test="${ !crumb.link }">
					<li>${ crumb.label }</li><li>&gt;</li>
				</c:when>
				<c:otherwise>
					<li>
						<form action="${ crumb.action }" id="rpt_breadcrumb${ count.count }" method="post">
							<a href="javascript:{}" onclick="document.getElementById('rpt_breadcrumb${ count.count }').submit(); return false;">${ crumb.label }</a>
							<input type="hidden" name="orgCode" value="${ crumb.orgCode }"/>
							<s:hidden name="consortium"/>
							<s:hidden name="requirements"/>
							<s:hidden name="viewBy"/>
							<s:hidden name="snapshotName"/>
							<s:hidden name="snapshotScopeId"/>
						</form>
					</li>
					<li>&gt;</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
</c:if>