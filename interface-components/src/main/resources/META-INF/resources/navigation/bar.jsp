<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="h" uri="http://techreadiness.net/helpers"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="page-navigation-layer-alpha" class="ui-helper-clearfix">
	<div id="page-navigation-layer-beta">
		<div id="page-navigation-layer-gamma">
			<div id="page-navigation-layer-delta">
				<div id="navigation-beta" class="navigation-object-beta ui-helper-clearfix">
					<div id="navigation-beta-bkgd-alpha" class="navigation-beta-bkgd-column ui-helper-clearfix">
						<div id="navigation-beta-bkgd-beta" class="navigation-beta-bkgd-column ui-helper-clearfix">
							<ul class="primary-navigation unstyled">
								<c:forEach var="tab" items="${tag.mainTabs}" varStatus="tabStatus">
									<li data-selected="${tab.code eq tag.selectedMainTab.code ? 'true' : 'false'}" id="${tab.code}"
										class="nav-tab ${tab.code eq tag.selectedMainTab.code ? 'navigation-beta-selected' : ''} ${tab.isEmpty() ? 'navigation-beta-sub-empty' : ''}">
										<c:choose>
											<c:when test="${empty tab.namespace && empty tab.defaultAction}">
												<a style="cursor: default;">
													<s:text name="%{#attr.tab.label}" /><i class="caret"></i>
												</a>
											</c:when>
											<c:otherwise>
												<s:a namespace="%{#attr.tab.namespace eq null || #attr.tab.namespace eq '' ? '/' : #attr.tab.namespace}" action="%{#attr.tab.defaultAction eq null || #attr.tab.defaultAction eq '' ? 'index' : #attr.tab.defaultAction}">
													<s:text name="%{#attr.tab.label}" />
												</s:a>
											</c:otherwise>
										</c:choose>
										<c:if test="${!tab.isEmpty()}">
											<div class="sub-outer">
												<!-- Subtitle here -->
												<c:if test="${not empty tab.subtitle}">
													<div class="menu-group-subtitle">
														<s:text name="%{#attr.tab.subtitle}" />
													</div>
												</c:if>
												<ul class="sub-menu unstyled">
													<c:forEach var="group" items="${tab.groups}" varStatus="groupStatus">
														<c:if test="${!group.children.isEmpty()}">
															<c:if test="${!empty group.name}">
																<li class="menu-group-heading">
																	<s:text name="%{#attr.group.name}" />
																</li>
															</c:if>
															<c:forEach var="subTab" items="${group.children}">
																<li class="${subTab.code eq tag.selectedSubTab.code ? 'navigation-beta-sub-selected' : ''}">
																	<s:a namespace="%{#attr.subTab.namespace}" action="%{#attr.subTab.defaultAction eq null ? '' : #attr.subTab.defaultAction}">
																		<s:text name="%{#attr.subTab.label}" />
																	</s:a>
																	<div>
																		<s:text name="%{#attr.subTab.descriptionText}" />
																	</div>
																</li>
															</c:forEach>
														</c:if>
													</c:forEach>
												</ul>
											</div>
										</c:if>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>

					<div id="navigation-beta-bkgd-gamma" class="navigation-beta-bkgd-column"></div>

				</div>

				<div id="background-layer-delta-sub-container-one">
					<div id="background-layer-delta-sub-one">
						<div id="background-layer-delta-sub-two"></div>
					</div>
					<div id="background-layer-delta-sub-three"></div>
				</div>

				<div id="navigation-gamma" class="ui-helper-clearfix">
					<div id="navigation-gamma-alpha">
						<div id="navigation-gamma-beta">
							<div id="navigation-gamma-gamma">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>