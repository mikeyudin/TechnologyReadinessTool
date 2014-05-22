<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="h" uri="http://techreadiness.net/helpers"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form namespace="%{taskFlow.namespace}" action="%{taskFlow.startAction}">
	<s:hidden name="dataGridId"/>
	<div style="clear:both"></div>
	<ul class="task-list">
	    <s:iterator value="availableTasks" var="task" status="taskStatus">
	        <li><s:checkbox name="selectedTasks[%{#taskStatus.index}]" label="%{#task.taskName}" fieldValue="%{#task.taskName}" /></li>
	    </s:iterator>
	</ul>
	<div class="start-tasks-button-container">
	   	<s:submit cssClass="start-tasks-button btn btn-mini btn-success" type="button">Foo Start Tasks</s:submit>
	</div>
</s:form>
