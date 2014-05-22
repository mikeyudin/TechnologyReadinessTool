<!DOCTYPE html>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<html>
<head>
<title>Current Info</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reporting.css" />
</head>

<body>
<s:form>
	<div class="current-info-subtitle">${currentOrg.name}</div>     
	<ui:detailset>
		<ui:detailsetRow title="ready.current.info.device.heading">
		<div class="label5">Total Devices:                                 ${orgSummary.deviceCount} </div>
		<div class="label5"># Devices Meeting Minimum Requirements:        ${ orgSummary.devicePassingCount }</div>	
		<div class="label5"># Devices Meeting Recommended Requirements:    ${orgSummary.recDevicePassingCount} </div>
		<div class="label5">% Devices Meeting Minimum Requirements:        ${orgSummary.devicePassingPercent} </div>		
		<div class="label5">% Devices Meeting Recommended Requirements:    ${orgSummary.recDevicePassingPercent} </div>		
		</ui:detailsetRow>
		<ui:detailsetRow title="ready.current.info.school.survey.heading">
	    <div class="label4">School And Network Information </div>
		<div class="label5">Total Enrollment Count:  ${orgSummary.totalEnrollmentCount} </div>	
		<div class="label5">Enrollment Count for the Grades Tested:  ${orgSummary.testingStudentCount} </div>	
		<div class="label5">Est Internet Bandwidth:                  ${orgSummary.internetSpeed} </div>
		<div class="label5">Est Internet Bandwidth Utilization:      ${orgSummary.internetUtilization}%</div>		
		<div class="label5">Est Internal Network Bandwidth:           ${orgSummary.networkSpeed} </div>	
		<div class="label5">Est Internal Network Bandwidth Utilization:  ${orgSummary.networkUtilization}% </div>		
		<div class="label5">Total # Test Starts Per School:           ${orgSummary.testingTestStartCount} </div>	
		<div class="label5">Testing Window (#School Days):            ${orgSummary.testingWindowLength} </div>		
		<div class="label5">Sessions Per Day:                         ${orgSummary.sessionsPerDay} </div>	
		<div class="label5">Maximum # Possible Test Starts in Window: ${orgSummary.networkPossibleTestCount} </div>		
		<br>	
		<div class="label4">Staff and Personnel Issues </div>
		<div class="label5">Level of Concern for having sufficient number of test administrators: ${currentOrg.surveyAdminCount} </div>	
		<div class="label5">Level of Concern for test administrators having sufficient technical understanding: ${currentOrg.surveyAdminUnderstanding} </div>	
		<div class="label5">Level of Concern for Providing all appropriate training needed for test administrators: ${currentOrg.surveyAdminTraining} </div>	
		<div class="label5">Level of Concern for having sufficient number of technology support staff to support online testing: ${currentOrg.surveyTechstaffCount} </div>	
		<div class="label5">Level of Concern for technology support staff having sufficient technical understanding to support online testing: ${currentOrg.surveyTechstaffUnderstanding} </div>	
		<div class="label5">Level of Concern for Providing all appropriate training needed for technology support staff:  ${currentOrg.surveyTechstaffTraining} </div>	
		</ui:detailsetRow>
		<ui:detailsetRow title="ready.current.info.data.complete.heading">
		<div class="label5">Data Entry Complete: ${orgSummary.dataEntryComplete} </div>		
		</ui:detailsetRow>
	</ui:detailset>
</s:form>
</body>
</html>