<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://techreadiness.net/components" prefix="ui"%>

<script type="text/javascript">
$.widget('ready.orgautocomplete', $.ui.autocomplete, {
	_renderItem: function(ul, item) {
		var href = $("#addItemUrl").attr("href");
		var itemText = item.name + " (" + item.code + ")";
		var anchor = "<a href='" + href + "&orgId=" + item.orgId + "'>" + itemText + "</a>";
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
		var input = $("#org");
		input.orgautocomplete({
			minLength : 0,
			source : function(request, response) {
				$.getJSON($("#ajaxOrgLoadUrl").attr("href"), {
					ajax : "true",
					term : request.term
				}, response);
			},
			focus : function(event, ui) {
				$("#org").val(ui.item.name + '(' + ui.item.code + ')');
				return false;
			},
			select: function( event, ui ) {
				var container = $(event.target).parents('.dataViewControl:first').find('.dataViewControlBody');
                container.load($("#addItemUrl").attr("href") + "&orgId=" + ui.item.orgId, function(){
				   $('table.data-grid-object-alpha').trigger("update-state");
				   $('body').trigger('refreshControls');
				});
				$('#dataViewControlOverlay').trigger('click');
				$('#dataViewControlOverlay').remove();
				event.preventDefault();
			},
			appendTo: "div.dataViewControlConfig"
		});
		
		$("#orgDownArrow").click(function() {
			// close if already visible
			if ( input.orgautocomplete( "widget" ).is( ":visible" ) ) {
				input.orgautocomplete( "close" );
				return;
			}

			// work around a bug (likely same cause as #5265)
			$( this ).blur();

			// pass empty string as value to search for, displaying all results
			input.orgautocomplete( "search", input.val() );
			input.focus();
		});
		input.focus();
	});
</script>
	<label for="org" ><s:text name="core.searchForOrganizations" /></label>
	<input id="org" type="text" style="width: 206px;">
	<span id="orgDownArrow" class="ui-icon ui-icon-triangle-1-s" style="display: inline-block;"></span>

	<s:a action="addOrganization" id="addItemUrl" cssStyle="display: none">
		<s:param name="multiple" value="multiple" />
		<s:param name="ajax" value="true" />
        <s:text name="core.addItem" />
	</s:a>
    <s:a id="ajaxOrgLoadUrl" action="ajaxOrgLoad" cssStyle="display:none" />