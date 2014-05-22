<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="h" uri="http://techreadiness.net/helpers"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<c:if test="${not empty tag.text}">
	<c:out value="${tag.text}" />
</c:if>
<c:choose>
	<c:when test="${not empty tag.value}">
		<s:a cssClass="entity-name magnifying-glass icons-16x16 inline-icon-left" value="%{#attr.tag.value}" title="%{title}">
			<s:param name="ajax">true</s:param>
			<c:forEach items="${tag.dynamicAttributes}" var="entry">
				<s:param name="%{#attr.entry.key}" value="%{#attr.entry.value}" />
			</c:forEach>
		</s:a>
	</c:when>
	<c:otherwise>
		<s:a cssClass="entity-name magnifying-glass icons-16x16 inline-icon-left" namespace="%{#attr.tag.namespace}" action="%{#attr.tag.action}" title="%{title}">
			<s:param name="ajax">true</s:param>
			<c:forEach items="${tag.dynamicAttributes}" var="entry">
				<s:param name="%{#attr.entry.key}" value="%{#attr.entry.value}" />
			</c:forEach>
		</s:a>
	</c:otherwise>
</c:choose>