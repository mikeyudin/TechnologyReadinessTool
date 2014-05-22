<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components"%>
<head>
<title><s:text name="task.filebatch.ie" /></title>
<script type="text/javascript">
	jQuery(function($) {

		function disableImportFields() {
			$('#process_serviceFile_mode').attr('disabled', 'true');
			$('#process_upload').attr('disabled', 'true');
		}
		function enableImportFields() {
			$('#process_serviceFile_mode').removeAttr('disabled');
			$('#process_upload').removeAttr('disabled');
		}

		$("select#action").change(function(event) {
			if ($(this).children('option:selected').html() == "Export") {
				disableImportFields();
			} else {
				enableImportFields();
			}
		});

	});
</script>
<style type="text/css">
#fieldError li span{
    font-family: sans-serif;
    font-size: 12px;
    color: #d00000;
}
#fieldError li, #successMessage li{
    margin-top: 3px;    
}
#fieldError ul{
    margin: 0em 0em 0em 14em;
    background-color: #FDF2F2;
    list-style:none;
    height: auto;
}
</style>
	
</head>
<s:form action="process" enctype="multipart/form-data" method="POST">
	<ui:taskView taskFlow="${taskFlowData}" detailMode="true">
		<ui:taskNavigation saveButton="task.filebatch.process" />
		
		  <s:if test="requestSuccess">
			<ui:entityField code="success">
				<div class="message success-message">
					<span class="icons-16x16 inline-icon-left check"></span>
						<s:text name="task.filebatch.request.success" />
				</div>
			</ui:entityField>
		</s:if>
	
		<ui:entityField code="message">
			
			<c:if test="${(taskFlowData.taskFlowState.currentTask.taskName == 'task.filebatch.devices') || (taskFlowData.taskFlowState.currentTask.taskName == 'task.filebatch.orgInfo')}">  
				<div class="message success-message-warning">
		 		 	<s:text name="task.filebatch.device.upload.warning" />
		 		</div>
			</c:if>
			<s:text name="task.filebatch.email.complete" />
		</ui:entityField>

		<ui:entityField code="action">
			<s:select id="action" label="Action" list="types" name="serviceFile.fileTypeCode" value="%{types.{^ #this.import}}" listValue="%{#this.import ? getText('core.import') : getText('core.export')}" required="true" />
		</ui:entityField>

		<c:if test="${!empty modes}">
			<ui:entityField code="method">
				<s:select label="Method" list="modes" name="serviceFile.mode" listValue="%{getText('core.batchMode.' + #this.toString())}" required="true" />
			</ui:entityField>
		</c:if>
		
		<ui:entityField code="file">
			<s:label id="process_upload_label" for="process_upload" value="File Name and Location" required="true" />
			<s:file name="upload" />
			<div id="fieldError">
    			<s:fielderror />
			</div>
		</ui:entityField>
		<ui:entityField code="description">
			<s:textfield name="serviceFile.description" label="Description" size="47" maxlength="500" />
		</ui:entityField>
	</ui:taskView>
</s:form>