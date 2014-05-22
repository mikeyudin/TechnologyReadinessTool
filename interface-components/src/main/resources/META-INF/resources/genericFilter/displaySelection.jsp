<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<ul>
	<s:if test="values.isEmpty">
      <s:if test="promptIfEmpty" >
      	<span class="icons-16x16 inline-icon-left warning"></span>
      	<ul style="list-style: none;">
        	<li class="error"><s:text name="core.selectAValue" /></li>
     	</ul>
      </s:if>
    </s:if>
	<s:iterator value="values" var="selectedItem">
		<li>
			<s:a action="remove" namespace="/controls" includeParams="get"><span class="remove">&times;</span>
				<s:param name="id" value="%{(#attr.valueKey)(#selectedItem)}" />
				<s:param name="ajax" value="true" />
				<s:param name="filterCode" value="filterCode" />
			</s:a>
			
			<s:property value="%{(#attr.nameKey)(#selectedItem)}"/>
		</li>
	</s:iterator>
</ul>
