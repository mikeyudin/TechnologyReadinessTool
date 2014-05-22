<%@ taglib prefix="h" uri="http://techreadiness.net/helpers"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://techreadiness.net/components"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:set var="taskViewTag" scope="page" value="#attr.tag" />
<script type="text/javascript">
$(function() {
	$("#${name}_autoCompleteControlBody").load($('#${name}ajaxShowUrl').attr('href') + "&key=" + "${instanceId}" );
});
</script>		
<div class="autoCompleteControl">
	<div class="control-group autoCompleteControlHeader">
		<label for="${name}" class="control-label"><s:text name="%{labelKey}"/><c:if test="${required}"><span class="required">*</span></c:if></label>
		<div class="controls">
		<s:textfield id="%{name}" type="text" data-type="%{originalName}" name="%{name}" instanceId="%{instanceId}" cssClass="autocompleteInputField" />
		<span id="${name}DownArrow" class="ui-icon ui-icon-triangle-1-s" style="display: inline-block;"></span>
		<span id="${name}addItemUrl" style="display: none;"> <s:url escapeAmp="false" action="add"
				namespace="%{namespace}">
				<s:param name="ajax" value="true" />
				<s:param name="multiple" value="%{multiple}" />
			</s:url>
		</span>
		<s:a id="%{name}ajaxLoadUrl" action="%{loadAction}"
			namespace="%{namespace}" cssStyle="display:none" />
		<s:a id="%{name}ajaxShowUrl" action="%{showAction}"
			namespace="%{namespace}" cssStyle="display:none" ><s:param name="ajax" value="true" /></s:a>	
		<div class="autoCompleteControlConfig">
			<ul>
			</ul>
		</div>
		<div id="${name}_autoCompleteControlBody" class="autoCompleteControlBody">
			<ul>
			</ul>
		</div>
		<s:textfield cssStyle="display:none;" name="%{name}Validation"/>
		</div>
	</div>
</div>