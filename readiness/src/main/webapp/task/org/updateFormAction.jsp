<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<s:select name="orgs['%{orgId}'].orgTypeId" list="orgTypes" listKey="key" listValue="value" required="true" emptyOption="false"
						value="orgs['%{orgId}'].orgTypeId" label="Organization Type"/>
