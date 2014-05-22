<%@ taglib prefix="h" uri="http://techreadiness.net/helpers" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set var="taskViewTag" scope="page" value="#attr.tag" />

<div class="task-flow-header">
	<c:if test="${!empty tag.taskFlowData}">
	<ul class="task-menu">
		<c:forEach items="${tag.taskFlowData.taskFlowState.tasks}" var="task" varStatus="taskStatus">
			<li class="${tag.taskFlowData.taskFlowState.currentTask.taskName eq task.taskName ? 'progress-current' : ''}">
				<s:a cssClass="task-name" action="navigateToTask">
					<span><c:out value="${taskStatus.index + 1}" />.&nbsp;<s:text name="%{#attr.task.taskName}" /></span>
					<s:param name="taskName" value="%{taskFlowDefinition.getTaskCode(#attr.task)}" />
				</s:a>
			</li>
		</c:forEach>
	</ul>
	</c:if>
	
	<h:evaluateTag tag="${tag.navigationTag}" />
</div>

<div class="task-flow-content">
<s:if test="%{#action.hasActionErrors()}">
	<div class="message failed-message">
		<span class="icons-16x16 inline-icon-left warning"></span>
		<s:actionerror cssClass="message-list" />
	</div>
</s:if>
<s:elseif test="%{#action.hasActionMessages()}">
	<div class="message success-message">
		<span class="icons-16x16 inline-icon-left check"></span>
		<s:actionmessage cssClass="message-list" />
	</div>
</s:elseif>
<s:else>
</s:else>

<c:if test="${tag.filterBarDisplayed && not (not tag.detailMode && empty tag.itemProvider)}">
	<ul>
		<s:iterator value="viewControls" var="control">
			<li class="taskViewControl">
				<s:if test="#control.leftSide">
					<h:evaluateTag tag="${control}" displayed="true"/>
				</s:if>	
			</li>
		</s:iterator>
	</ul>
</c:if>
<c:if test="${tag.selectionDisplayed}">
	<div style="float: right; margin-right: 1.4em;" class="dataViewControl task-selection">
   		<div class="dataViewControlHeader">
       		<h4><s:text name="%{#attr.taskViewTag.selectionTitle}" /></h4>
       		<div style="clear: both"></div>
   		</div>

    		<div class="dataViewControlBody">
      		<ul>
	    		<s:iterator value="%{#attr.taskViewTag.selection}" var="selectedObject">
	    			<s:set scope="page" var="selectionDisplayLabel" value="@com.opensymphony.xwork2.util.TextParseUtil@translateVariables(#attr.taskViewTag.selectionDisplayExpression, valueStack)" />
	        		<li class="selected-item" title="${selectionDisplayLabel}"><c:out value="${ui:abbreviate(selectionDisplayLabel, 25)}" /></li>
	   	 		</s:iterator>
	  		</ul>
   		</div>
   	</div>
</c:if>
<div>
	<c:choose>
		<c:when test="${tag.detailMode && not empty tag.itemProvider}">
			<div class="task-detail-header">
				<div style="width: 200px; float: left;"><s:text name="%{#attr.tag.listHeader}" /></div>
				<span><s:text name="core.details" /></span>
			</div>
			<div class="task-detail-content">
				<table style="width:100%">
				<tr>
				<td style="width:1%">
					<ul class="task-detail-options">
						<c:forEach items="${tag.items}" var="rowInfo" varStatus="itemStatus">
							<s:push value="#attr.rowInfo.row">
								<s:push value="#{#attr.tag.var : top}">
									<li data-index="${itemStatus.count}" class="${tag.rowInError ? 'error' : ''}">
										<s:set scope="page" var="dataViewListName" value="@com.opensymphony.xwork2.util.TextParseUtil@translateVariables(#attr.tag.nameExpression, valueStack)" />
										
										<a href="#" title="${dataViewListName}" ><c:out value="${ui:abbreviate(dataViewListName, 25)}" /></a>
									</li>
								</s:push>
							</s:push>
						</c:forEach>
					</ul>
				</td>
				<td class="task-detail">
					<c:forEach items="${tag.items}" var="rowInfo" varStatus="itemStatus">
						<div class="entity-detail" data-index="${itemStatus.count}">
							<s:push value="#attr.rowInfo.row">
								<s:push value="#{#attr.tag.var: top}">
									<s:push value="#attr.rowInfo.errorRow">
										<s:push value="#{#attr.tag.var: top}">
											<ui:fieldset field="${tag.fieldName}" var="${tag.var}" viewDef="${tag.fieldSetViewDef}">
												<s:iterator value="%{#attr.tag.columns}" var="aVariable">
													<c:if test="${aVariable.displayInDetail}">
														<ui:fieldsetRow name="${aVariable.name}" code="${aVariable.code}" displayOrder="${aVariable.displayOrder}">
															<h:evaluateTagBody tag="${aVariable}"/>
														</ui:fieldsetRow>
													</c:if>
												</s:iterator>
											</ui:fieldset>
										</s:push>
									</s:push>
								</s:push>
							</s:push>
						</div>
					</c:forEach>
				</td>
				</tr>
				</table>
 			<s:a id="task-detail-content-update-link" style="display:none" includeParams="all" />
			</div>
		</c:when>
		<c:when test="${tag.detailMode && empty tag.itemProvider}">
			<ui:fieldset field="${tag.fieldName}" viewDef="${tag.fieldSetViewDef}">
				<s:iterator value="%{#attr.tag.columns}" var="aVariable">
					<ui:fieldsetRow name="${aVariable.name}" code="${aVariable.code}">
						<h:evaluateTagBody tag="${aVariable}"/>
					</ui:fieldsetRow>
				</s:iterator>
			</ui:fieldset>
		</c:when>
		<c:when test="${not tag.detailMode && empty tag.itemProvider}">
			<h:evaluateTagBody tag="${tag}"/>
		</c:when>
		<c:otherwise>
			<ui:datagrid value="${tag.value}" itemProvider="${tag.itemProvider}" paging="${tag.paging}" viewDef="${tag.dataGridViewDef}" multiEditable="true"
				fieldName="${tag.fieldName}" var="${tag.var}" rowValue="${rowIdentifier}" selectable="${tag.selectable}" columnSelectable="${tag.columnSelectable}"
				showRequired="true" groupExpression="${tag.groupExpression}" selectAllRows="${tag.selectAllRows}">
				<c:if test="${not empty taskViewTag.toolBar}">
					<ui:toolbar>
						<h:evaluateTagBody tag="${taskViewTag.toolBar}"/>
					</ui:toolbar>
				</c:if>   		
				<%-- since there may or may not be column groups, we have to copy and paste the first
				 column in case it needs to be in a group --%>
				<c:if test="${!tag.hideNameColumnInGrid}">
					<c:choose>
						<c:when test="${!empty tag.entityGroups }">
							<ui:dataGridColumnGroup>
								<ui:dataGridColumn code="nameExpression" nameKey="${tag.listHeader}" name="%{getText('${tag.listHeader}')}" displayOrder="first">
									<s:property value="@com.opensymphony.xwork2.util.TextParseUtil@translateVariables(#attr.taskViewTag.nameExpression, valueStack)" />
								</ui:dataGridColumn>
							</ui:dataGridColumnGroup>
						</c:when>
						<c:otherwise>
							<ui:dataGridColumn code="nameExpression" nameKey="${tag.listHeader}" name="%{getText('${tag.listHeader}')}" displayOrder="first">
								<s:property value="@com.opensymphony.xwork2.util.TextParseUtil@translateVariables(#attr.taskViewTag.nameExpression, valueStack)" />
							</ui:dataGridColumn>
						</c:otherwise>
					</c:choose>
				</c:if>
				<s:iterator value="%{#attr.tag.columns}" var="aVariable" >
					<c:if test="${aVariable.displayInGrid}">
						<ui:dataGridColumn code="${aVariable.code}" name="${aVariable.name}" nameKey="${aVariable.nameKey}"
							displayOrder="${aVariable.displayOrder}" grouped="${aVariable.grouped}" required="${aVariable.required}">
							<h:evaluateTagBody tag="${aVariable}"/>
						</ui:dataGridColumn>
					</c:if>
				</s:iterator>
				<s:iterator value="%{#attr.tag.entityGroups}" var="entityGroup">
					<ui:dataGridColumnGroup name="%{#entityGroup.name}">
						<s:iterator value="%{#entityGroup.fields}" var="aVariable">
							<c:if test="${aVariable.displayInGrid}">
								<ui:dataGridColumn code="${aVariable.code}" name="${aVariable.name}" nameKey="${aVariable.nameKey}" displayOrder="${aVariable.displayOrder}"
									grouped="${aVariable.grouped}" required="${aVariable.required}">
									<h:evaluateTagBody tag="${aVariable}"/>
								</ui:dataGridColumn>
							</c:if>
						</s:iterator>
					</ui:dataGridColumnGroup>
				</s:iterator>
			 </ui:datagrid>
		</c:otherwise>
	</c:choose>
</div>

</div>
<c:if test="${!empty tag.taskFlowData}">
	<div class="task-flow-footer">
		<h:evaluateTag tag="${tag.navigationTag}" />
	</div>
</c:if>