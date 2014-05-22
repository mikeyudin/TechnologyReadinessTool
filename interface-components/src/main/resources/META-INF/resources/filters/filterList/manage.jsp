<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:form action="updateDisplayed" data-filters="manage">
	<s:hidden name="viewDefType"/>
	<s:hidden name="dataGridId"/>
	<s:iterator value="#{'Displayed Filters ': activeFields, 'Hidden Filters': inactiveFields }">
		<s:set var="displayed" value="value==activeFields"/>
		<s:if test="displayed || !value.isEmpty()">
			<ul class="filters-manage-list">
				<li class="column-list-heading">
					<s:property value="key" /> 
				</li>
				
				<c:if test="${displayed && fullTextShown}">
					<li>
						<label>
							<s:checkbox disabled="true" value="true"/>
							Full Text
						</label>
					</li>
				</c:if>
				
				<c:if test="${displayed}">
					<c:forEach items="${dataViewFiltersAsObject}" var="filter">
					<li>
						<label>
							<s:checkbox disabled="true" value="true"/>
							<input type="hidden" name="displayedFilters" value="${filter.code}">
							<c:out value="${filter.name}"/>
						</label>
					</li>
					</c:forEach>
				</c:if>
				
				<c:if test="${displayed && orgShown}">
					<li>
						<label>
							<s:checkbox disabled="true" value="true"/>
							Organization
						</label>
					</li>
				</c:if>
				
				<s:iterator value="value">
					<s:if test="!name.isEmpty()">
						<li>
							<label>
								<s:checkbox id="%{code}" name="displayedFilters" value="#displayed" fieldValue="%{code}"/>						
								<s:property value="name"/>
								
								<c:if test="${!empty existingFilters[code] }">
									<s:set var="filtersApplied" value="true"/>
									<img src="${pageContext.request.contextPath}/static/images/icons/export.png" alt="Data Filtered">
								</c:if>
							</label>
						</li>
					</s:if>
				</s:iterator>
			</ul>
		</s:if>
	</s:iterator>
	<div>
		<s:submit class="manageButtons btn btn-xs btn-primary" type="button">Apply</s:submit>
		<button type="button" class="btn btn-xs btn-default manageButtons" onclick="$('#dataViewControlOverlay').click();"><s:text name="core.cancel" /></button>
		<c:if test="${filtersApplied}">
			<img src="${pageContext.request.contextPath}/static/images/icons/export.png" alt="Data Filtered">
			Data Filtered
		</c:if>
	</div>
</s:form>