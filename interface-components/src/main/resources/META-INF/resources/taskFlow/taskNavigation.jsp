<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="h" uri="http://techreadiness.net/helpers"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="taskflow-actions">
	<c:if test="${not empty tag.taskFlow}">
	<c:choose>
		<c:when test="${tag.previousAvailable}">
			<s:a cssClass="btn btn-xs btn-default" action="prev">
				&#171; <s:text name="core.previousTask" />
			</s:a>
		</c:when>
		<c:otherwise>
			<s:submit disabled="true" cssClass="btn btn-xs btn-default" type="button">&#171; <s:text name="core.previousTask" /></s:submit>
		</c:otherwise>
	</c:choose>
	
	
	<!-- Reset Button -->
	<a class="btn btn-xs btn-default allowDirty"
		href="${pageContext.request.contextPath}${tag.taskFlow.currentTask.namespace}/${tag.taskFlow.currentTask.action}.action">
		<s:text name="%{#attr.tag.resetButton}" />
	</a>
	
	
	<!-- Save Button -->
	<c:if test="${not tag.suppressSave}">
	  <s:submit type="button" cssClass="btn btn-xs btn-success" value="save">
		  <s:text name="%{#attr.tag.saveButton}" />
	  </s:submit>
	</c:if>
	
	
	<!-- Next Task Button -->
	<c:choose>
		<c:when test="${tag.nextAvailable}">
			<s:a cssClass="btn btn-xs btn-default" action="next" >
				<s:text name="core.nextTask" /> &#187;
			</s:a>
		</c:when>
		<c:otherwise>
			<s:submit disabled="true" type="button" cssClass="btn btn-xs btn-default" ><s:text name="core.nextTask" /> &#187;</s:submit>
		</c:otherwise>
	</c:choose>
	<c:if test="${tag.allowModeSwitch}">
		<s:a>
			<c:if test="${!tag.detailMode}">
				<img class="mode-icon" title="Switch to Details Mode" alt="Details Mode" src="${pageContext.request.contextPath}/static/images/icons/form.png">
			</c:if>
			<c:if test="${tag.detailMode}">
				<img class="mode-icon" title="Switch to Grid Mode" alt="Grid Mode" src="${pageContext.request.contextPath}/static/images/icons/grid.png">
			</c:if>
			<s:param name="%{#attr.tag.value}.detailMode" value="%{!#attr.tag.detailMode}" />
		</s:a>
		
		
	</c:if>
	<s:a cssClass="btn btn-xs btn-default" action="end"><s:text name="core.exit" /></s:a>
	</c:if>
	
</div>