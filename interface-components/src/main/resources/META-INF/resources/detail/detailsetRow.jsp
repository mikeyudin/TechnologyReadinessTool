<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<c:choose>
	<c:when test="${empty tag.field}">
		<h:evaluateTagBody tag="${tag}"/>
	</c:when>
	<c:otherwise>
		<s:push value="#attr.tag.field">
		
			<s:set var="fieldRowVar" value="#attr.tag.fieldName + '.' + code" />
			<s:set var="fieldValue" value="#attr[#fieldRowVar]" />

			<s:label value="%{#attr.tag.field.name}" />
               :
               <s:if test="field.options.isEmpty">
                   <s:property value="#fieldValue"/>
               </s:if>
               <s:else>
                   <s:property value="field.options[#fieldValue]"/>
               </s:else>
		</s:push>
	</c:otherwise>
</c:choose>
