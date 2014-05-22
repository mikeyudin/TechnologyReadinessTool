
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:iterator value="resourcePack.css">
	<link rel="stylesheet" type="text/css" href="${baseDir}${location}?v=${version}" media="${media}"/>
</s:iterator>

<s:iterator value="resourcePack.js">
	<script type="text/javascript" src="${baseDir}${location}?v=${version}"></script>
</s:iterator>