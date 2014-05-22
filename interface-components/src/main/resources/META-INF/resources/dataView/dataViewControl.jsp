<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="h" uri="http://techreadiness.net/helpers"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="dataViewControl">
	<s:a namespace="%{namespace}" action="%{contentAction}" cssClass="refresh" cssStyle="display:none">
		<s:param name="dataGridId" value="%{#attr.tag.dataGridId}" />
		<s:param name="ajax" value="true" />
		<c:forEach items="${tag.dynamicAttributes}" var="entry">
			<s:param name="%{#attr.entry.key}" value="%{#attr.entry.value}" />
		</c:forEach>
		<s:text name="core.refresh" />
	</s:a>

	<div class="dataViewControlHeader title ${tag.configFullPopup? 'configFullPopup' : '' }">
		<h4>
			<c:if test="${!empty number && number != 0}">
				<span class="number">
					<s:property value="number" />
				</span>
			</c:if>
			<s:text name="%{#attr.name}" />
		</h4>
		<c:if test="${!empty configAction}">
		  <s:if test="%{#attr.tag.configFullPopup}"> 
		    <s:a namespace="%{namespace}" action="%{configAction}" class="entity-name" title="%{getText(#attr.tag.name)}">
				<s:param name="ajax" value="true" />
				<s:text name="%{#attr.configLinkName}" />
				<s:param name="dataGridId" value="%{#attr.tag.dataGridId}" />
				<c:forEach items="${tag.dynamicAttributes}" var="entry">
					<s:param name="%{#attr.entry.key}" value="%{#attr.entry.value}" />
				</c:forEach>
		    </s:a>
		  </s:if>
		  <s:else>
			<s:a namespace="%{namespace}" action="%{configAction}" cssClass=" %{#attr.tag.configPopup? 'configPopup' : 'configInline' }">
				<s:param name="ajax" value="true" />
				<s:text name="%{#attr.configLinkName}" />
				<span class="caret"></span>
				<s:param name="dataGridId" value="%{#attr.tag.dataGridId}" />
				<c:forEach items="${tag.dynamicAttributes}" var="entry">
					<s:param name="%{#attr.entry.key}" value="%{#attr.entry.value}" />
				</c:forEach>
			</s:a>
		  </s:else>
		</c:if>
		<c:if test="${!empty toolbar}">
			<h:evaluateTagBody tag="${toolbar}"/>
		</c:if>
		<div style="clear: both"></div>
	</div>
	
	<div class="dataViewControlBody">
		<s:action namespace="%{namespace}" name="%{contentAction}" executeResult="true" rethrowException="true" ignoreContextParams="true" >
			<s:param name="dataGridId" value="%{#attr.tag.dataGridId}" />
			<c:forEach items="${tag.dynamicAttributes}" var="entry">
				<s:param name="%{#attr.entry.key}" value="%{#attr.entry.value}" />
			</c:forEach>
		</s:action>
	</div>
</div>