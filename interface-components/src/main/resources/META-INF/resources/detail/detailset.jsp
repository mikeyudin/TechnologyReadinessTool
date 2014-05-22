<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="detail-set-container">
	<c:forEach items="${tag.rows}" var="row" varStatus="rowStatus">
		<h3 class="detailset-section-title" data-id="${rowStatus.index}"><a href="#"><span>[-]</span><s:text name="%{#attr.row.title}" /></a></h3>
		<div class="detail-set-section" data-id="${rowStatus.index}">
			<h:evaluateTagBody tag="${row}"/>
		</div>
	</c:forEach>
	<s:a id="detail-set-update" cssStyle="display: hidden;" includeParams="all">
		<s:param name="reset" value="false" />
	</s:a>
</div>