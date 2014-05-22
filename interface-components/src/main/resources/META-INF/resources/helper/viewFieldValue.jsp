<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://techreadiness.net/helpers" prefix="h"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<s:push value="#attr.tag.field">

<c:choose>
	<c:when test="${type=='ViewText'}">
		<div style="margin-left:3px">${text}</div>
	</c:when>
	<c:otherwise>
		<s:set var="fieldRowVar" value="#attr.tag.rowVar + '.' + code"/>
		<s:if test="%{#attr.tag.evaluatedActionFieldName not in {null, ''}}">
			<s:set var="fullFieldName" value="#attr.tag.evaluatedActionFieldName + '.' + code" />
		</s:if>
		<s:else>
			<s:set var="fullFieldName" value="#fieldRowVar" /> 
		</s:else>
		<s:set var="readonly" value="#attr.tag.readOnly"/>
		<s:set var="fieldValue" value="#attr[#fieldRowVar]" />
		<s:set var="fieldLabel" value="name"/>
		<s:if test="%{labelPosition == 'hidden'}">
			<s:set var="fieldLabel" value=""/>
		</s:if>
	
		<s:set var="inputCss" value="(displayWidth==''?'':'width:' + displayWidth) + (inputStyle==''?'':';' + inputStyle)"/>
		<c:choose>
			<c:when test="${valueOnly}">
				<s:if test="field.options.isEmpty && !field.options.containsKey(#fieldValue)">
			        <s:property value="#fieldValue"/>
				</s:if>
				<s:else>
					<s:property value="field.options[#fieldValue]"/>
				</s:else>
			</c:when>
			<c:when test="${disabled}">
			</c:when>
		    <c:when test="${readonly}">
				<s:label value="%{fieldLabel}" required="%{required}" cssStyle="%{labelStyle}"/>
		        <div class="read-only-field">
					<s:if test="field.options.isEmpty">
				        <s:property value="#fieldValue"/>
					</s:if>
					<s:else>
						<s:property value="field.options[#fieldValue]"/>
					</s:else>
		        </div>
		    </c:when>
			<c:when test="${!empty options && inputType=='radio'}">
					<s:label value="%{fieldLabel}" required="%{required}" cssStyle="%{labelStyle}"/>
					<ul class="radio">
						<s:radio name="%{fullFieldName}" list="options"  required="%{required}" value="%{fieldValue}" cssClass="radio"/>	
					</ul>
					<c:if test="${!hideErrors and !empty(fieldErrors[fullFieldName]) }">
						<div style="clear:both"></div>
						<div class="field-errors">
							<ul>
								<c:forEach items="${fieldErrors[fullFieldName]}" var="error">
									<li class="inline-validation-error"><em><c:out value="${error}"/></em></li>
								</c:forEach>
							</ul>
						</div>
					</c:if>
			</c:when>
			<c:when test="${!empty options }">
				<s:select name="%{fullFieldName}" list="options"  
					cssStyle="%{inputCss}" required="%{required}" 
					emptyOption="true" value="%{fieldValue}"  tooltip="%{field.description}" label="%{fieldLabel}"/>
			</c:when>
			<c:when test="${dataType == 'BOOLEAN'}">
				<s:checkbox name="%{fullFieldName}" 
					cssStyle="%{inputCss}" required="%{required}" value="%{fieldValue}" label="%{fieldLabel}"/>
			</c:when>
			<c:when test="${dataType == 'DATE'}">
				<s:textfield name="%{fullFieldName}" 
				    id=""
					required="%{required}"
					cssClass="%{dataType}"
					cssStyle="%{inputCss}"
					size="%{maxLength > 0? (maxLength>14?14:maxLength) : ''}"
					maxlength="%{maxLength > 0? maxLength : ''}" value="%{#attr[#fieldRowVar]}" label="%{fieldLabel}"/>
			</c:when>
			<c:when test="${dataType == 'DATETIME'}">
				<s:textfield name="%{fullFieldName}" 
				    id=""
					required="%{required}"
					cssClass="%{dataType}"
					cssStyle="%{inputCss}"
					size="%{maxLength > 0? (maxLength>14?14:maxLength) : ''}"
					maxlength="%{maxLength > 0? maxLength : ''}" value="%{#attr[#fieldRowVar]}" label="%{fieldLabel}"/>
			</c:when>
			<c:otherwise>
                <s:property value="field.id"/>
				<s:textfield name="%{fullFieldName}" 
					label="%{fieldLabel}" 
					required="%{required}"
					cssClass="%{dataType}"
					cssStyle="%{inputCss}"
					size="%{maxLength > 0? (maxLength>14?14:maxLength) : ''}"
					maxlength="%{maxLength > 0? maxLength : ''}" value="%{fieldValue}" tooltip="%{field.description}"/>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>	
</s:push>