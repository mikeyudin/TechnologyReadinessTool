<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="debugMode && viewDef != null">
   <s:property value="getViewDefDebugOutput(viewDef)" escapeHtml="false"/>
</s:if>

<div class="form-${tag.columnCount}column ${editAlertDisabled? 'editAlertDisabled':'' }">
	<c:if test="${!empty tag.toolbars }">
		<s:push value="#{'renderingToolbar':true}">
			<s:property value="asdf"/>
			<div class="toolbar">
				<div class="fieldset-title"><h3><c:out value="${tag.title}"/></h3></div>
				<div class="fieldset-actions">
					<c:forEach items="${tag.toolbars}" var="toolbar">
						<h:evaluateTagBody tag="${toolbar}"/>
					</c:forEach>
				</div>
				<div style="clear:both"></div>
			</div>
		</s:push>
	</c:if>
	
	<c:if test="${not empty tag.hiddenErrors}">
		<div style="margin: 1.4em;">
			<h4 class="error-heading">The following validation errors are for fields not currently being displayed:</h4>
			<ul style="margin-left: .4em;">
				<c:forEach items="${tag.hiddenErrors}" var="error">
					<li class="validation-error"><c:out value="${error}" /></li>
				</c:forEach>
			</ul>
		</div>
	</c:if>

	<c:forEach items="${tag.columns}" var="column">
		<h:evaluateTag tag="${column}"/>
	</c:forEach>

	<%-- duplicating because jsp:include does not work yet. --%>
	<c:if test="${tag.bottomToolbar and !empty tag.toolbars}">
		<s:push value="#{'toolbar':true}">
			<li class="toolbar">
				<div class="fieldset-title"></div>
				<div class="fieldset-actions">
					<c:forEach items="${tag.toolbars}" var="toolbar">
						<h:evaluateTagBody tag="${toolbar}"/>
					</c:forEach>
				</div>
				<div style="clear:both"></div>
			</li>
		</s:push>
	</c:if>

</div>

<div style="clear:both"></div>