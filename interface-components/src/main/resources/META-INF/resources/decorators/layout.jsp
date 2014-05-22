<!DOCTYPE html>
<%@taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title><s:text name="core.technologyReadinessTool" />: <decorator:title />
</title>
	<h:staticResources />
	<decorator:head />
	<script type="text/javascript">contextPath='${pageContext.request.contextPath}';</script>
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/program/readiness.ico">
	<style type="text/css">
		#orgSelectModal {
		    display: none;
		}
		#orgSelectModal > div {
		    padding: 5px;
		    margin-top: 10px;
		}
	</style>
</head>
<body>
	<a class="ui-helper-accessible-hidden" accesskey="c"
		href="#content-column-alpha">skip to content</a>


	<div id="background-layer-alpha">
		<div id="background-layer-beta">
			<div id="background-layer-gamma">
				<div id="background-layer-delta">


					<div id="page-header-layer-alpha" class="ui-helper-clearfix">
						<div id="page-header-layer-beta">
							<div id="page-header-layer-gamma">
								<div id="page-header-layer-delta">

									<div id="header-alpha" class="ui-helper-clearfix">
									<c:if test="${not empty serviceContext.userId}">
										<div class="navigation-object-alpha header-links" style="float: left;">
											<ul class="unstyled">
												<li>
													<s:if test="hasPermission(coreChangeGlobalScopePermission.toArray())">
														<s:a namespace="/" action="change-scope" cssClass="entity-name-hud initially_hidden">
															<c:out value="${serviceContext.scopeName}" />
															<s:param name="returnUrl" value="request.servletPath" />
															<s:param name="ajax" value="true" />
														</s:a>
													</s:if>
													<s:else>
														<c:out value="${serviceContext.scopeName}" />
													</s:else>
												</li>
												<li>
													<a href="#" id="clickme">
														<c:out value="${serviceContext.orgName}" />
													</a>
												</li>
		                                    </ul>
	                                    </div>
										<div id="navigation-alpha" class="navigation-object-alpha header-links">
											<ul class="unstyled">
												<li>
													<s:text name="header.welcomeUser">
														<s:param value="serviceContext.user.firstName" />
														<s:param value="serviceContext.user.lastName" />
													</s:text>
												</li>
												<li>
													<s:url namespace="/" action="help" var="helpUrl">
														<s:param name="ajax" value="true"/>
														<s:param name="helpUrl" value="request.requestURI"/>
													</s:url>
													<a target="help_window" href="${helpUrl}" >
														<img src="${pageContext.request.contextPath}/static/images/icons/help.png" alt="Help" />
													</a>
												</li>
												<li>
													<s:text name="ready.link.answercenter.url" var="answerCenterUrl"/>
													<a href="${answerCenterUrl}" target="answer_center">
														<s:text name="ready.link.answercenter.text" />
													</a>
												</li>
												<li>
													<s:a namespace="/user" action="account-details" cssClass="entity-name initially_hidden">
														<s:text name="yourAccount" />
														<s:param name="showTaskLinks" value="false" />
														<s:param name="ajax" value="true" />
													</s:a>
												</li>
												<li>
													<s:a value="/j_spring_security_logout" title="%{getText('core.signout')}">
														<s:text name="core.signout" />
													</s:a>
												</li>
											</ul>
										</div>
										</c:if>
									</div>
									<div id="header-beta" class="ui-helper-clearfix">
										
										<div id="header-column-alpha">
											<s:a href="%{request.contextPath}/info.action" id="logo-link-beta" title="Go to Home Page" accesskey="1">
												<img src="${pageContext.request.contextPath}/static/images/program/readiness-logo.png" alt="Go to Home Page" />
											</s:a>
											<div id="container-content-alpha">
												<div id="header-beta-cc-alpha"></div>
												<div id="header-beta-cc-beta"></div>
												<div id="header-beta-cc-gamma"></div>
											</div>
											
										</div>
										
										<div id="header-column-beta">
											<div id="container-content-alpha">
												
											</div>
											<div id="container-content-beta">
												
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>


					<ui:navigationBar />


					<div id="page-content-layer-alpha" class="ui-helper-clearfix">
						<div id="page-content-layer-beta">
							<div id="page-content-layer-gamma">
								<div id="page-content-layer-delta">

									<div id="content-column-alpha"
										class="content-column">




										<a class="ui-helper-accessible-hidden" accesskey="k"
											href="#column-title-beta">skip content navigation and go
											to content</a>

										<div id="navigation-delta" class="ui-helper-clearfix">

										</div>

										
										<decorator:body />
									</div>


									<div id="content-column-beta" class="content-column">

									</div>


									<div id="content-column-gamma" class="content-column">

									</div>

								</div>
							</div>
						</div>
					</div>

					<div id="background-layer-delta-sub-container-two">
						<div id="background-layer-delta-sub-four">
							<div id="background-layer-delta-sub-five"></div>
						</div>
						<div id="background-layer-delta-sub-six"></div>
					</div>
					<div id="footer-alpha" class="footer ui-helper-clearfix">
						<div id="container-content-beta">
							<a href="http://www.k12.wa.us/smarter" target="_blank" class="logo logo-sbac"></a>
							<a href="http://www.parcconline.org" target="_blank" class="logo logo-parcc"></a>
							<a href="http://www.setda.org" target="_blank" class="logo logo-setda"></a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="orgSelectModal" title="Choose an organization">
	<form action="${pageContext.request.contextPath}/update-org.action" method="post" class="form-no-margin">
		<input type="hidden" name="returnUrl"
			value="${pageContext.request.servletPath}${not empty pageContext.request.queryString ? '?' : ''}${not empty pageContext.request.queryString ? pageContext.request.queryString : ''}">
			<div>
				<input type="text" name="organization">
				<input type="hidden" name="selectedOrgId">
			</div>
			<div style="margin-top: 10px;">
				<button type="submit" class="btn btn-xs btn-primary">
					<s:text name="core.change" />
				</button>
				<button type="button" class="btn btn-xs btn-default" onclick="$( '#orgSelectModal' ).dialog( 'close' );">
					<s:text name="core.cancel" />
				</button>
			</div>
	</form>
		</div>
 
	<script type="text/javascript">
	$(function(){
		var orgSelector = $('input[name="organization"]');
		
		$('#clickme').click(function(event) {
			$( "#orgSelectModal" ).dialog( "open" );
			orgSelector.focus();
			event.preventDefault();
		});		
		
		orgSelector.typeahead([{
			name: 'orgs',
			remote: {
				url: ctx + '/services/rest/users/' + ${serviceContext.userId} + '/authorized-orgs?q=%QUERY',
				filter: function(response) {
					var datums = [];
					var i = 0;
					for(; i < response.org.length; i++) {
						var datum = {
							orgId: response.org[i].orgId,
							value: response.org[i].name,
							orgCode: response.org[i].code,
							tokens: [response.org[i].name, '' + response.org[i].localCode + '', response.org[i].orgTypeName]
						};
						datums.push(datum);
					}
					return datums;
				}
			},
			limit: 10,
			template: function (datum) {
				return '<p>' + datum.value + ' (' + datum.orgCode + ')</p>';
			}
		}]);
		
		handleSelection = function (event, datum) {
			$('input[name="selectedOrgId"]').val(datum.orgId);
		}
		orgSelector.on('typeahead:selected', handleSelection);
		orgSelector.on('typeahead:autocompleted', handleSelection);
	});

	$( "#orgSelectModal" ).dialog({
	      autoOpen: false,
		title: "Select an organization",
		height: 620,
	      width: 480,
    		modal: true,
			position: [100,25],
	      show: {
	      },
	      hide: {
	      }
	    });
	 
	    $( "#opener" ).click(function() {
	      $( "#dialog" ).dialog( "open" );
	    });
	</script>
	
</body>
</html>