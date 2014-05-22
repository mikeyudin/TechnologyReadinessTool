<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<ul class="unstyled">
	<s:if test="orgs.isEmpty">
      	<s:if test="promptIfEmpty">
      		<li>
	      		<span class="icons-16x16 inline-icon-left warning"></span>
	      		<s:text name="core.selectAValue" />
      		</li>
      	</s:if>
	</s:if>
	<s:iterator value="orgs" var="org">
		<li>
			<s:a action="remove"><span class="remove">&times;</span>
				<s:param name="orgId" value="orgId" />
				<s:param name="ajax" value="true" />
				<s:param name="multiple" value="%{multiple}" />
			</s:a>
			<s:property value="name"/> (<s:property value="code" />)
			<s:hidden name="orgId" />
		</li>
	</s:iterator>
</ul>