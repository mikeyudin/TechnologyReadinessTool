<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="h" uri="http://techreadiness.net/helpers" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components" %>


<s:if test="debugMode && viewDef != null">
   <s:property value="getViewDefDebugOutput(viewDef)" escapeHtml="false"/>
</s:if>

<div class="data-grid-container ${editAlertDisabled? 'editAlertDisabled' : '' }">

    <s:url var="updateLink" includeParams="all" encode="true">
        <s:param name="ajax" value="true"/>
    </s:url>
	<table id="${tag.id}" class="data-grid-object-alpha gray" data-update-link="${updateLink}">
		<thead class="results">
			<s:if test="toolbarUsed && !illegalState">
				<tr class="data-grid-task-bar">
					<th>
						<s:push value="#{'renderingToolbar':true}">
							<h:evaluateTagBody tag="${tag.toolbar}"/>
						</s:push>
					</th>
				</tr>
			</s:if>
			
			<s:if test="resultsHeaderShown">
				<tr class="header-results">
					<c:if test="${not empty tag.title}">
						<td class="data-grid-title">
							<c:out value="${tag.title}" />
						</td>
					</c:if>
					<c:if test="${not empty tag.headerBody}">
						<td>
							<s:push value="#{'renderingToolbar':true}">
								<h:evaluateTagBody tag="${tag.headerBody}" />
							</s:push>
						</td>
					</c:if>
				  <td>
					<div class="results">
					  	<s:if test="paging">
							<s:if test="noResults">
								<strong><s:text name="core.noResults" /></strong>
							</s:if>
							<s:else>
								<s:text name="core.resultExpression">
									<s:param value="pageStart" />
									<s:param value="pageEnd" />
									<s:param value="totalNumberOfItems" />
								</s:text>
								|
								<s:text name="core.display" />
								<s:select list="#{10:10,25:25,50:50,100:100}" name="%{id}.pageSize" cssClass="dataGrid-update-state" cssStyle="margin:0px"/>
								<s:a cssClass="dataGrid-update-state pageSize"></s:a>
							</s:else>
						</s:if>
						
						<s:if test="columnSelectable">
							<s:if test="paging">
			 			    	<span class="results-separator">|</span>
						    </s:if>
						    
							<a href="#" class="show-selectable-columns"><s:text name="core.manageColumns" /><span class="caret"></span></a>
							<div style="display:none" class="column-select-dropdown shadow">
								<s:iterator value="#{getText('core.displayedColumns'): columns, getText('core.hiddenColumns'): hiddenColumns }">
									<s:if test="!value.isEmpty()">
										<ul>
											<li class="column-list-heading">
												<s:property value="key"/>
											</li>
											<s:iterator value="value">
											    <s:if test="!manageColumnsText.isEmpty() && manageable">
												<li>
													<s:label>
														<s:set var="id" value="#attr.tag.id + '-displayedColumns-' +  id" />
														<s:checkbox id="%{id}" name="%{#attr.tag.id}.displayedColumns" value="displayed" fieldValue="%{id}"/>
														<s:label for="%{id}" value="%{manageColumnsText}"/>
													</s:label>
													<s:push value="%{dataFiltered(id)}">
									  				  <s:set var="dataFiltered" value="top"/>
													</s:push>
													<s:if test="#dataFiltered == true">	
													  <s:set var="filtersApplied" value="true"/>
									  				  <img src="${pageContext.request.contextPath}/static/images/icons/export.png"
														alt="Data Filtered" />
													</s:if>
												</li>
												</s:if>
											</s:iterator>
										</ul>
									</s:if>
								</s:iterator>
								<div style="white-space:nowrap;">
									<s:a cssClass="select-columns btn btn-xs btn-primary">
										<s:text name="core.apply" />
									</s:a>
									<c:if test="${filtersApplied }">
										<img src="${pageContext.request.contextPath}/static/images/icons/export.png" alt="Data Filtered" />
										<span >
											<s:text name="core.dataFiltered" />
										</span>
									</c:if>
								</div>
							</div>
						</s:if>
					</div>
				  </td>
				</tr>
			</s:if>
	    </thead>
	    <s:if test="!paging && (totalNumberOfItems > currentPageSize)">
	    	<tfoot><tr class="footer-results">
	    		<td colspan="${tag.numberOfColumns}">
	    			<s:text name="core.onlyShow" >
						<s:param value="currentPageSize" />
					</s:text>
   				</td>
   			</tr></tfoot>
   		</s:if>
	    
	    <s:if test="paging">
		    <tfoot>
			    <tr class="footer-results">
			    	<td colspan="${tag.numberOfOuterColumns}">
			    		<div class="data-grid-foot-beta"></div>
			    		<div class="data-grid-foot-gamma"></div>
			    		<div class="data-grid-foot-alpha">
			    			<ul>
		    					<s:if test="onFirstPage">
				    				<li title="No Previous Pages" class="pagination-previous pagination-disabled">
				    					&laquo; <s:text name="core.previous" />
										<span>|</span>
				    				</li>
		    					</s:if>
		    					<s:else>
		    						<li class="pagination-previous">
										<s:a cssClass="dataGrid-update-state" title="Go to Previous Page">
											<s:param name="%{id}.page" value="page -1"/>
				    						&laquo; <s:text name="core.previous" />
										</s:a>
										<span>|</span>
									</li>
		    					</s:else>
			   					<s:if test="fewerPages">
									<li class="pagination-page-1">
										<s:a cssClass="dataGrid-update-state" data-paramKey="page" title="Go to Page 1">
											<s:param name="%{id}.page" value="1"/>
											<c:out value="1"/>
										</s:a> 
										<s:if test="firstPage > 2">...</s:if>
									</li>
			   					</s:if>
			   					<s:iterator begin="firstPage" end="lastPage" var="pageIter">
		   							<s:if test="page == #pageIter">
										<li class="pagination-current-page" title="You are on Page ${pageIter}">
											<s:property value="#pageIter" />
										</li>
									</s:if>
									<s:else>
										<li class="pagination-page-${pageIter}">
											<s:a cssClass="dataGrid-update-state" data-paramKey="page" title="Go to Page %{#pageIter}">
												<s:param name="%{id}.page" value="#pageIter"/>
												<s:property value="#pageIter" />
											</s:a>
										</li>
									</s:else>
			   					</s:iterator>
								<s:if test="morePages">
									<s:if test="lastPage < (totalNumberOfPages - 1)">...</s:if>
									<li class="pagination-page-${totalNumberOfPages}">
										<s:a cssClass="dataGrid-update-state" data-paramKey="page" title="Go to Page %{totalNumberOfPages}">
											<s:param name="%{id}.page" value="totalNumberOfPages"/>
											<s:property value="totalNumberOfPages"/>
										</s:a>
									</li>
								</s:if>
								
								<s:if test="onLastPage">
				    				<li title="No More Pages" class="pagination-next pagination-disabled">
										<span>|</span>
				    					<s:text name="core.next" /> &raquo;
			    					</li>
		    					</s:if>
		    					<s:else>
		    						<li class="pagination-next">
										<span>|</span>
										<s:a cssClass="dataGrid-update-state scroll append" title="Go to Next Page">
											<s:param name="%{id}.page" value="page + 1"/>
				    						<s:text name="core.next" /> &raquo;
										</s:a>
									</li>
		    					</s:else>
							</ul>
						</div>
			    	</td>
			   	</tr>
		   	</tfoot>
	   	</s:if>
	   	
	    <tbody>
	    	<tr>
	    		<td colspan="${tag.numberOfOuterColumns}">
	    			<div class="dataGrid-overflow" style="width:100px;overflow-y:auto">
				    	<table style="width: 100%">
				    		<thead class="headers">
				    		<s:if test="!columnGroups.isEmpty()">
				    			<tr class="headers top">
							        <s:if test="selectable">
							        	<th width="1%"></th>
							        </s:if>
							        <s:if test="inlineEditable">
							        	<th width="1%" class="inline-indicator-header">
							        		
							        	</th>
							        </s:if>
							        <s:iterator value="columnGroups">
						          		<th class="column-group" colspan="${fn:length(columns)}">
						          			<s:property value="name"/>
						          		</th>
							        </s:iterator>
				    			</tr>
				    			
				    		</s:if>
						    <tr class="headers bottom">
						        <c:if test="${tag.inlineEditable}">
						        	<th width="1%" class="inline-indicator-header">
						        		
						        	</th>
						        </c:if>
						        <c:choose>
						        	<c:when test="${tag.selectable and tag.selectAllRows}">
						        		<th class="rowselect">
						        			<input type="checkbox" name="checkAll" data-grid="select-all">
										</th>
						        	</c:when>
						        	<c:when test="${tag.selectable}">
						        		<th width="1%"></th>
						        	</c:when>
						        	<c:otherwise>
						        	</c:otherwise>
						        </c:choose>
						        <s:iterator value="columns">
						          <th id="${code}" style="${headerStyle}" width="${width}">
					             	<span class="filter-text">
						             	<s:if test="nameKey != null && nameKey != ''" >
						             		<s:text name="%{nameKey}" />
						             		<s:if test="required && showRequired">
						             			<span class="required-header"><c:out value="*" /></span>
						             		</s:if>
						             	</s:if>
						             	<s:else>
						             		<s:property value="name"  escapeHtml="false"/>
						             		<s:if test="required && showRequired">
						             			<span class="required-header"><c:out value="*" /></span>
						             		</s:if>
						             	</s:else>
					             	</span>
						          </th>
						        </s:iterator>
						    </tr>
						    </thead>
						    <tbody>
						    <s:if test="#attr.tag.illegalState">
						    	<tr>
						    		<td colspan="${tag.numberOfColumns}" style="padding: 1.4em;" class="failed-message">
						    			<span class="icons-abnormal-sizes inline-icon-left warningBig"></span>
						    			<s:property value="#attr.tag.errorMessage" />
						    		</td>
						    	</tr>
						    </s:if>
						    <s:else>
						    <c:forEach items="${tag.currentPage}" var="rowInf" varStatus="status">
								<s:push value="#attr.rowInf.row">
									<s:push value="#{#attr.tag.var: top }">
										<s:push value="#attr.rowInf.errorRow">
											<s:push value="#{#attr.tag.var: top }">
												<c:if test="${empty evenOddClass}">
													<c:set value="odd" var="evenOddClass" scope="page" />
												</c:if>
												<c:set scope="page" var="hasGroupValueChanged" value="${rowInf.groupChanged}" />
												<c:choose>
													<c:when test="${hasGroupValueChanged && 'odd' eq evenOddClass}">
														<c:set var="evenOddClass" value="even" />
													</c:when>
													<c:when test="${hasGroupValueChanged && 'odd' ne evenOddClass}">
														<c:set var="evenOddClass" value="odd" />
													</c:when>
												</c:choose>
												<tr class="${rowInf.rowClass} ${evenOddClass}" data-rowId="${rowInf.rowId}">
													<c:if test="${tag.selectable}">
														<td class="rowselect">
															<s:if test="#attr.rowInf.selectionDisabled">
																<img src="${pageContext.request.contextPath}/static/images/icons/disabled.png" />
															</s:if>
															<s:else>
																<c:choose>
																	<c:when test="${rowInf.rowSelected}">
																		<input type="checkbox" value="${rowInf.rowSelected}" name="${tag.id}.selectedRowId['${rowInf.rowId}']" class="row-select dataGrid-update-state" data-grid="selection" checked>
																	</c:when>
																	<c:otherwise>
																		<input type="checkbox" value="${rowInf.rowSelected}" name="${tag.id}.selectedRowId['${rowInf.rowId}']" class="row-select dataGrid-update-state" data-grid="selection">
																	</c:otherwise>
																</c:choose>
															</s:else>
														</td>
													</c:if>
													<s:if test="#attr.tag.inlineEditable">
														<td class="inline-indicator"></td>
													</s:if>

													<s:iterator value="#attr.tag.columns" var="column" status="columnStatus">
														
														<td style="${column.style}" width="${column.width}" class="${hidden ? 'hidden' : rowInf.columnEditClass}"
															data-ajax-params="'${tag.id}.editRowId':'${rowInf.rowId}'">
															
															<c:if test="${(hasGroupValueChanged && column.grouped) || !column.grouped}">
																<ui:viewRule ruleId="${column.field.displayRuleId}" ruleService="${tag.viewDef.ruleService}" row="%{#attr.rowInf.row}">
																	<h:evaluateTag tag="${column}" rowInfo="${rowInf}" lastColumn="${columnStatus.last}" />
																</ui:viewRule>
															</c:if>
														</td>
													</s:iterator>
												</tr>
											</s:push>
										</s:push>
									</s:push>
								</s:push>
								<s:if test="#attr.rowInf.rowInError">
						        	<tr class="standard-row rowErrorMessages">
						        		<td colspan="${tag.numberOfColumns}">
						        			<ul>
							        			<s:iterator value="#attr.rowInf.fieldErrors">
							        				<s:iterator value="value">
							        					<li>
							        						<s:property value="top"/>
							        						<s:if test="key in #attr.tag.hiddenFieldsInError">
							        							(<s:text name="core.hiddenFieldInError" />)
							        						</s:if>
							        					</li>
							        				</s:iterator>
							        			</s:iterator>
						        			</ul>
						        		</td>
						        	</tr>
						        </s:if>
				    		</c:forEach>
				    		</s:else>
				    		</tbody>
				    	</table>
			    	</div>
		    	</td>
	    	</tr>
		</tbody>
	</table>

</div>
