<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<s:form action="update" remote="true" class="filterList data-grid-filters" novalidate="novalidate">
	<s:hidden name="dataGridId"/>
	<s:hidden name="viewDefType"/>
	<s:hidden name="fullTextShown"/>
	<s:hidden name="dataViewFilters"/>
	
	<c:if test="${fullTextShown}">
		<div>
		<label class="control-label"><s:text name="core.fullText" /></label>
		<input type="text" name="search" value="${search}">
		<s:submit type="button" action="search" cssClass="btn btn-default btn-xs"><s:text name="core.search" /></s:submit>
		</div>
	</c:if>

	<c:forEach items="${dataViewFiltersAsObject}" var="filter">
		<div>
		<s:set var="filter" value="#attr.filter"/>
		<c:if test="${filter.primary || expanded }">
			<c:choose>
				<c:when test="${filter.type == 'autocomplete' }">
					<s:url var="filterOptionsUrl"  action="filterOptions">
						<s:param name="beanName" value="#filter.beanName"/>
						<s:param name="nameKey"  value="#filter.nameKey"/>
						<s:param name="valueKey" value="#filter.valueKey"/>
						<s:param name="ajax" value="true" />
					</s:url>
					<label>
						<c:out value="${filter.name}"/>
					</label>
					<s:textfield name="dataViewFilterValues.%{#filter.beanName}" cssClass="core-autocomplete" data-autocomplete-options="{source:'${filterOptionsUrl}'}"/>
					<s:submit type="button" name="updateDataViewFilter" value="%{#filter.beanName}" cssClass="btn btn-default btn-xs"><s:text name="core.add" /></s:submit>
					<s:iterator value="getSelectedItems(#filter.beanName)" var="item">
						<div style="margin-bottom: 4px;">
							<s:a action="removeDataViewFilter">
								<s:param name="dataGridId" value="dataGridId"/>
								<s:param name="viewDefType" value="viewDefType"/>
								<s:param name="fullTextShown" value="fullTextShown"/>
								<s:param name="dataViewFilters" value="dataViewFilters"/>
								<s:param name="ajax" value="true"/>
								
								<s:param name="beanName" value="#filter.beanName"/>
								<s:param name="filterValue" value="#item[#filter.valueKey]"/>
								&times;
							</s:a>
							<s:property value="#item[#filter.nameKey]"/>
						</div>
					</s:iterator>
				</c:when>


				<c:when test="${filter.type == 'select' }">
					<label class="control-label"><c:out value="${filter.name}" /></label>
					<s:select name="dataViewFilterValues.%{#filter.beanName}"  
								list="getDataViewFilterOptions(#filter.beanName)"
								listKey="%{(#filter.valueKey)(top)}" listValue="%{(#filter.nameKey)(top)}" emptyOption="true" >
					</s:select>
					<s:submit type="button" name="updateDataViewFilter" value="%{#filter.beanName}" cssClass="btn btn-default btn-xs"><s:text name="core.add" /></s:submit>
					<s:iterator value="getSelectedItems(#filter.beanName)" var="item">
						<div>
							<s:a action="removeDataViewFilter">
								<s:param name="dataGridId" value="dataGridId"/>
								<s:param name="viewDefType" value="viewDefType"/>
								<s:param name="fullTextShown" value="fullTextShown"/>
								<s:param name="dataViewFilters" value="dataViewFilters"/>
								<s:param name="ajax" value="true"/>
								
								<s:param name="beanName" value="#filter.beanName"/>
								<s:param name="filterValue" value="#item[#filter.valueKey]"/>
								&times;
							</s:a>
							<s:property value="#item[#filter.nameKey]"/>
						</div>
					</s:iterator>
				</c:when>
				
				<c:when test="${filter.type == 'checkbox'}">
					<div class="checkbox">
					<label>
						<s:checkbox name="dataViewFilterValues.%{#filter.beanName}" fieldValue="1" value="!getSelectedItems(#filter.beanName).isEmpty()" />
						<s:submit type="button" name="updateDataViewFilter" value="%{#filter.beanName}" cssStyle="display:none;"><s:text name="core.add" /></s:submit>
						<c:out value="${filter.name}" />
					</label>
					</div>
				</c:when>
			</c:choose>
		</c:if>
		</div>
	</c:forEach>
	
	<c:if test="${expanded}">
		<s:iterator value="activeFields" var="field">
			<s:if test="%{visible && !readOnly}">
				<h:viewFieldValue rowVar="filters" actionFieldName="filters" field="${field}" hideTooltip="true" />
				<s:submit type="button" name="updateField" value="%{code}" cssClass="btn btn-default btn-xs"><s:text name="core.add" /></s:submit>
				<s:iterator value="existingFilters[code]" var="filter">
					<div>
                        <s:a action="removeFilter">
                            <s:param name="dataGridId" value="dataGridId"/>
                            <s:param name="viewDefType" value="viewDefType"/>
                            <s:param name="fullTextShown" value="fullTextShown"/>
                            <s:param name="dataViewFilters" value="dataViewFilters"/>
                            <s:param name="ajax" value="true"/>

                            <s:param name="filterName" value="code"/>
                            <s:param name="filterValue" value="filter"/>
                            &times;
                        </s:a>
                        <s:if test="#field.options.isEmpty">
                            <s:property value="filter"/>
                        </s:if>
                        <s:else>
                            <s:property value="#field.options[#filter]"/>
                        </s:else>
                    </div>
				</s:iterator>
			</s:if>
		</s:iterator>
		
	</c:if>
	
	<div>
	<s:a action="show" id="filter-expand-%{dataGridId}" cssClass="filter-expand-contract-link %{expanded? 'filter-contract-link expanded' : 'filter-expand-link'}" >
		<s:param name="dataGridId" value="dataGridId" />
		<s:param name="viewDefType" value="viewDefType"/>
		<s:param name="fullTextShown" value="fullTextShown"/>
		<s:param name="dataViewFilters" value="dataViewFilters"/>
		<s:param name="ajax" value="true"/>
		<s:param name="expanded" value="!expanded"/>
		<s:property value="expanded? 'Hide': 'More'"/>
	</s:a>
	</div>

</s:form>
