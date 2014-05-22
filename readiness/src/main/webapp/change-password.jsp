<!DOCTYPE html>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title><s:text name="loginCustom.pageTitle" /></title>

	
	<h:staticResources />
	<style type="text/css">
		.change {
			border: #DADADA 1px groove;
			font-size: 95%;
			width: 100%;
			margin-bottom: .4em;
		}
	</style>
</head>
<body>
	<div style="min-height: 150px;"></div>
	<div id="background-layer-alpha">
		<div id="background-layer-beta" style="width: 450px;">
			<div id="background-layer-gamma">
				<div id="background-layer-delta">
					<div id="page-header-layer-alpha" class="ui-helper-clearfix">
						<div id="page-header-layer-beta">
							<div id="page-header-layer-gamma">
								<div id="page-header-layer-delta">
									<div id="header-beta" class="ui-helper-clearfix" style="text-align:center">
									  <img style="margin-bottom: 10px; margin-top: 10px;" src="${pageContext.request.contextPath}/static/images/program/readiness-logo.png" /> 
									</div>
								</div>
							</div>
						</div>
					</div>

					<div id="page-header-layer-alpha" class="ui-helper-clearfix">
						<div id="page-header-layer-beta">
							<div id="page-header-layer-gamma">
								<div id="page-header-layer-delta">
									<div id="background-layer-delta-sub-container-one">
										<div id="background-layer-delta-sub-one">
											<div id="background-layer-delta-sub-two"></div>
										</div>
										<div id="background-layer-delta-sub-three"></div>
									</div>

									<!-- START CONTENT -->
									<div id="page-content-layer-alpha" class="ui-helper-clearfix">
										<div id="page-content-layer-beta">

											<div id="page-content-layer-gamma" style="min-height: 250px;">
												<div id="page-content-layer-delta">

													<div id="content-column-alpha" class="content-column">
														<div style="margin-left: 3em; margin-right: 3em; margin-top: 1.4em;">
															<h3 style="color: #004890;"><s:text name="changeResetPassword.boxHeader" /></h3>
															<s:form action="change-password-accept" autocomplete="off">								
																<ul>
																    <s:actionerror/>
																	<c:if test="${not empty param.reset_error}">
																		<li class="error"><s:text name="changeResetPassword.invalid" /></li>
																	</c:if>	
																	<li><label style="color: #6B6B6B"><s:text name="changeResetPassword.username" /></label></li>
																	<li><s:textfield name="username" value="%{#parameters.username}" tabindex="1" cssClass="change" /></li>																																	
																	<li><label style="color: #6B6B6B"><s:text name="changeResetPassword.token" /></label></li>
																	<li><s:textfield name="token" value="%{#parameters.token}" tabindex="2"	cssClass="change" /></li>
																	<li><label style="color: #6B6B6B"><s:text name="changeResetPassword.password" /></label></li>
																	<li><s:password name="passwordField" tabindex="3" cssClass="change" /></li>
																	<li><label style="color: #6B6B6B"><s:text name="changeResetPassword.confirmPassword" /></label></li>
																	<li><s:password name="confirmPasswordField" tabindex="4" cssClass="change" /></li>
																</ul>
						
																<s:submit type="button" cssClass="btn btn-default btn-xs">
																	<s:text name="changeResetPassword.setPassword" />
																</s:submit>
															</s:form>
														</div>
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
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>