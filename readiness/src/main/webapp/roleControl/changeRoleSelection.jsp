<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<script type="text/javascript">
$.widget('core.roleautocomplete', $.ui.autocomplete, {
	_renderItem: function(ul, item) {
		var href = $("#addItemUrl").attr("href");
		var itemText = item.name + " (" + item.code + ")";
		var anchor = "<a href='" + href + "&roleId=" + item.roleId + "'>" + itemText + "</a>";
		return $("<li>").append(anchor).appendTo(ul);
	},
	_renderMenu: function (ul, items) {
		var that = this;
		if(items.length == 50) {
			ul.append("<li>50 first matches shown</li>");
		}
		$.each(items, function(index, item){
			that._renderItemData(ul, item);
		});
	}
});
	$(function() {
		var input = $("#role");
		input.bind('click', function(){
			event.stopPropagation();
		});
		input.roleautocomplete({
			minLength : 0,
			source : function(request, response) {
				$.getJSON($("#ajaxRoleLoadUrl").attr("href"), {
					ajax : "true",
					term : request.term
				}, response);
			},
			focus : function(event, ui) {
				$("#role").val(ui.item.name + '(' + ui.item.code + ')');
				return false;
			},
			select: function( event, ui ) {
				var container = $(event.target).parents('.dataViewControl:first').find('.dataViewControlBody');
				container.load($("#addItemUrl").html().trim() + "&roleId=" + ui.item.roleId, function(){
				   $('table.data-grid-object-alpha').trigger("update-state");
				   $('body').trigger('refreshControls');
				});
				$('#dataViewControlOverlay').trigger('click');
				$('#dataViewControlOverlay').remove();
				event.preventDefault();
			},
			appendTo: "div.dataViewControlConfig"
		});
		
		$("#roleDownArrow").click(function() {
			// close if already visible
			if ( input.roleautocomplete( "widget" ).is( ":visible" ) ) {
				input.roleautocomplete( "close" );
				return;
			}

			// work around a bug (likely same cause as #5265)
			$( this ).blur();

			// pass empty string as value to search for, displaying all results
			input.roleautocomplete( "search", input.val() );
			input.focus();
		});
		input.focus();
	});
</script>
	<label for="role"><s:text name="core.searchForRoles" /></label>
	<input id="role" type="text">
	<span id="roleDownArrow" class="ui-icon ui-icon-triangle-1-s" style="display: inline-block;"></span>
	
	<span id="addItemUrl" style="display: none;">
	<s:url action="add">
		<s:param name="multiple" value="multiple" />
		<s:param name="ajax" value="true" />
	</s:url>
	</span>
	<s:a id="ajaxRoleLoadUrl" action="ajaxRoleLoad" cssStyle="display:none" />
