<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="h" uri="http://techreadiness.net/helpers"%>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="dataViewContainer">
	<c:if test="${not empty title}">
		<h4><span class="heading"><c:out value="${title}"/></span></h4>
	</c:if>

	<ul>
		<c:if test="${not empty dataViewFilters}">
			<li class="header-control">
				<ui:dataViewControl displayed="true" code="filters" name="core.find" namespace="/filterList" side="top" contentAction="show"
					dataGridId="${dataGridTag.value}" viewDefType="${dataViewFilters.viewDefType}" fullTextShown="true" orgShown="true" number="1"
					dataViewFilters="${dataViewFilters.filters}">
					<ui:toolbar> 
						<s:a namespace="/filterList" action="clear" cssClass="configInline">
							<s:text name="core.clear" />
							<s:param name="ajax" value="true"/>
							<s:param name="fullTextShown" value="true"/>
							<s:param name="orgShown" value="true"/>
							<s:param name="dataGridId" value="dataGridTag.value"/>
							<s:param name="viewDefType" value="dataViewFilters.viewDefType"/>
							<s:param name="dataViewFilters" value="dataViewFilters.filters"/>
						</s:a>
						
						<s:a namespace="/filterList" action="manage" cssClass="configPopup">
							<s:text name="core.manageFilters" />
							<span class="caret"></span>
							<s:param name="ajax" value="true"/>
							<s:param name="fullTextShown" value="true"/>
							<s:param name="orgShown" value="true"/>
							<s:param name="dataGridId" value="dataGridTag.value"/>
							<s:param name="viewDefType" value="dataViewFilters.viewDefType"/>
							<s:param name="dataViewFilters" value="dataViewFilters.filters"/>
						</s:a>
					</ui:toolbar>
				</ui:dataViewControl>
			</li>
		</c:if>
		
		<c:if test="${not empty shoppingCart}">
			<li class="header select">
				<div class="dataViewControlHeader title">
					<h4>
						<span class="number"><s:text name="core.number.two" /></span>
						<c:out value="${shoppingCart.name}" />
                        (<span id="shoppingCartSize"><c:out value="${fn:length(selectedItems)}" /></span>)
					</h4>
					<s:a cssClass="updateShoppingCart">
						<s:param name="%{dataGridName}.clearSelected" value="true" />
						<s:text name="core.clear" />
					</s:a>
				</div>
				<div id="data-grid-selection" class="dataViewControlBody">
					<ul class="selection">
						<s:iterator value="selectedItems">
							<s:set scope="page" var="selectionLabel" value="text" />
							<c:set scope="page" var="selectionLabel" value="${fn:trim(selectionLabel)}" />
							<c:set scope="page" var="deleted" value="${fn:startsWith(selectionLabel,'|')}"/>
							<li id="shoppingCartItem_${representation}" title="${selectionLabel}">
								<s:a cssClass="updateShoppingCart">
									<span class="remove">&times;</span>
									<s:param
										name="%{dataGridName}.selectedRowId[%{representation}]"
										value="false" />
								</s:a>
								
								<c:choose>
								  <c:when test="${deleted}">
								    <c:set scope="page" var="selectionLabel" value="${fn:substring(selectionLabel,1, fn:length(selectionLabel))}" />
								    <c:out value="${ui:abbreviate(selectionLabel, 35)}" />
								    <span class="delete icons-16x16" style="display: inline-block;"></span>
								  </c:when>
								  <c:otherwise>
								    <c:out value="${ui:abbreviate(selectionLabel, 35)}" />
								  </c:otherwise>
								</c:choose>
								
							</li>
						</s:iterator>
					</ul>
				</div>
			</li>
		</c:if>
		<c:if test="${not empty taskFlow}">
			<li class="header tasks">
				<div class="dataViewControlHeader title">
					<h4>
						<span class="number"><s:text name="core.number.three" />
						</span>
						<s:text name="core.tasks" />
					</h4>
				</div>
				<div class="dataViewControlBody">
					<s:form namespace="%{#attr.tag.taskFlow.namespace}"
						action="%{#attr.tag.taskFlow.startAction}">
						<s:hidden name="dataGridId" />
						<div id="task-checkboxes">
						<ul class="task-list">
							<s:iterator value="availableTasks" var="task" status="taskStatus">
								<li class="${taskStatus.index % 2 == 0 ? 'col1' : 'col2'}">
									<s:checkbox id="task-%{#task.taskName}" name="selectedTasks[%{#taskStatus.index}]"
										fieldValue="%{#task.taskName}" />
									<s:label for="task-%{#task.taskName}" value="%{getText(#task.taskName)}" />
								</li>
							</s:iterator>
						</ul>
						</div>
						<s:submit id="start-tasks-button" cssClass="start-tasks-button btn btn-xs btn-success"
								type="button"><s:text name="core.startTasks" /></s:submit>
						<ul class="unstyled">
							<s:iterator value="availableExternalTasks" var="task" status="taskStatus">
								<li class="${taskStatus.index % 2 == 0 ? 'col1' : 'col2'}">
									<s:url namespace="%{#task.namespace}" action="%{#task.action}" var="taskUrl"/>									
									<a class="task-link" href="<s:property value="#taskUrl" />"><s:property value="%{getText(#task.taskName)}"/></a>
								</li>
							</s:iterator>
						</ul>
					</s:form>
				</div></li>
		</c:if>
		<li style="float:left;min-width:60em;">
			<h:evaluateTag tag="${tag.dataGridTag}" />
		</li>
	</ul>
</div>