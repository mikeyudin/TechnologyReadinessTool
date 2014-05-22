jQuery.fn.fadeReplaceWith = function(replacement, callback) {
	$orig = $(this);
	$orig.fadeOut("fast", function(){
		$orig.replaceWith($(replacement).hide().fadeIn("fast", callback));
	});
};

jQuery.fn.replaceWithAndCallback = function(replacement, callback) {
	$(this).replaceWith(replacement);
	callback();
};

jQuery(function($) {
    var body = $('body');
    $('.column-select-dropdown input[type="checkbox"]').attr('disabled', 'true');

	var $grids = $('table.data-grid-object-alpha');

	body.delegate('table.data-grid-object-alpha a.dataGrid-update-state', 'click', function(event) {
		event.preventDefault();
		$(this).trigger('update-state', {data: jQuery.parseQuery(this.search), fade:true});
	});
	
	body.delegate('table.data-grid-object-alpha .dataGrid-update-state:input:not(:checkbox)', 'change', function(event) {
		$(this).trigger("update-state", {data: $(this).serializeArray()});
	});

	body.on('change', 'input[type="checkbox"][data-grid="select-all"]', function(event) {
	     var data = {};
	     var $target = $(event.target);
	     if ($target.is(':checked')) {
            $target.closest('table').find('input[type="checkbox"][data-grid="selection"]:not(:disabled)').each(function (index, element) {
            	$(this).prop("checked", true);
            	data[$(this).attr("name")] = $(this).prop("checked");
            });
	     } else {
	    	 $target.closest('table').find('input[type="checkbox"][data-grid="selection"]:not(:disabled)').each(function (index, element) { 
        	   $(this).prop("checked", false);
        	   data[$(this).attr("name")] = $(this).prop("checked");
             });
	     }      
	     data["refreshGrid"] = false;
	     $(this).trigger("update-state", {data: data});
	});

	body.delegate('table.data-grid-object-alpha .dataGrid-update-state:checkbox', 'change', function(event) {
		$(this).parents("tr:first").toggleClass("rowSelected");
		var data = {};
		data[$(this).attr("name")] = $(this).prop("checked");
		data["refreshGrid"] = false;
		$(this).trigger("update-state", {data: data});
	});
	
	// Search Handling outside of data view
	body.delegate('#datagrid-search-button', 'click', function(event) {
		event.preventDefault();
		$grids.trigger('update-state', {data: $('#datagrid-search-field').serializeArray(), fade:false});
	});
	
	body.delegate('#datagrid-search-field', 'keyup', function(event) {
		if(event.keyCode == 13) {
			event.preventDefault();
			$grids.trigger('update-state', {data: $('#datagrid-search-field').serializeArray(), fade:false});
		}
	});

    body.delegate('table.data-grid-object-alpha tr.standard-row td', 'dblclick', function() {
		$(this).trigger('inline-edit');
	});
	
	body.delegate('table.data-grid-object-alpha td.inline-indicator', 'click', function(){
		$(this).siblings("td.inlineEditable:first").trigger('inline-edit');
	});
	
	body.delegate('table.data-grid-object-alpha td.inlineEditable', 'inline-edit', function(event){
		var data = eval("({"+$(this).data("ajax-params")+"})");
		var grid = $(this).parents("table.data-grid-object-alpha:first");
		
		//we need to pass in the original widths so that we can restore them once the page is reloaded
		var origWidths = $(this).parent().children("td").map(function(i, td){return $(td).width();});
		grid.data("origWidths", origWidths);
		
		//we also need to pass in the index of the clicked cell so that we can focus it later
		var index = $(this).index();
		grid.data('clickedCell', index);
		
		$(this).trigger("update-state", {data:data, positionInline:true});
	});
	
	body.delegate('table.data-grid-object-alpha .inlineSave', 'click', function(event, postLoad){
	 	event.preventDefault();
	 	var $link = $(event.target);
			updateOptions = {
				data: $(this).parents("tr:first").children().not(".rowselect").find(":input").serializeArray(), 
				url: $link.attr('href'),
				//type: $form.attr("method"),
				positionInline:true,
				postLoad:postLoad,
				allowDirty:true
			};
		
		$(this).trigger("update-state", updateOptions);
	});

	body.delegate('table.data-grid-object-alpha .inlineCancel', 'click', function(event){
	 	event.preventDefault();
		var $link = $(event.target);
		$(this).trigger("update-state", {
			data: $(this).parents("tr:first").find(":input").serializeArray(), 
			url: $link.attr('href')
		});
	});

	var TAB_KEY = 9,
		ENTER_KEY = 13,
		ESC_KEY = 27;
	
	//trap the tab key on the cancel button, redirect it to the first inline input
	body.delegate('table.data-grid-object-alpha .inlineCancel', 'keydown', function(event){
		if(event.which == TAB_KEY && !event.ctrlKey && !event.shiftKey && !event.altKey && !event.metaKey) {
			event.preventDefault();
			$(event.target).parents("tr:first").find("td.inlineEdit :input:visible:first").focus();
		}
	});
	
	//trap shift-tab on the first inline input, redirect it to the cancel button
	body.delegate('table.data-grid-object-alpha td.inlineEdit :input:visible:first', 'keydown', function(event){
		if(event.which == TAB_KEY && event.shiftKey && !event.ctrlKey && !event.altKey && !event.metaKey) {
			event.preventDefault();
			$(event.target).parents("tr:first").find(".inlineCancel").focus();
		}
	});
	
	//enter key behavior
	body.delegate('table.data-grid-object-alpha td.inlineEdit :input', 'keyup', function(event){
		if(event.which == ENTER_KEY && !event.ctrlKey && !event.shiftKey && !event.altKey && !event.metaKey) {
			event.preventDefault();
			var row = $(this).parents("tr:first"),
				rowIndex = row.index(),
				colIndex = $(this).parents("td:first").index();
			row.find(".inlineSave").trigger('click', [function(grid){
				var next = grid.find("tr.standard-row").eq(rowIndex).find("td").eq(colIndex);
				if(next.hasClass("inlineEditable")) {
					next.click();
				}
			}]);
		}
	});

	body.delegate('table.data-grid-object-alpha a.show-selectable-columns', 'click', function(event){
		event.preventDefault();
		event.stopPropagation();
		$('.column-select-dropdown input[type="checkbox"]').removeAttr('disabled');
		var dropdown = $(this).next(".column-select-dropdown");
		dropdown.toggle().offset({left: $(this).offset().left + $(this).outerWidth() - dropdown.outerWidth()});
	});
	
	body.delegate('table.data-grid-object-alpha a.select-columns', 'click', function(event){
		event.preventDefault();
        var data = $(this).parents(".column-select-dropdown:first").find(":checkbox").serializeArray();
        $(this).trigger('update-state', {data: data});
	});
	
	body.delegate('table.data-grid-object-alpha .column-select-dropdown', 'click', function(event){
		event.stopPropagation();
	});
	
	$(document).bind('click', function(){
		$('.column-select-dropdown input[type="checkbox"]').attr('disabled', 'true');
		$('.column-select-dropdown').hide();
	});
	
	
	//escape key should cancel the edit
	$(document).keyup(function(event) {
		if(event.which == ESC_KEY) {
			$('.inlineCancel').click();
		}
	});

	function trim(s)
	{
		var l=0; var r=s.length -1;
		while(l < s.length && s[l] == ' ')
		{	l++; }
		while(r > l && s[r] == ' ')
		{	r-=1;	}
		return s.substring(l, r+1);
	}
	
	var queryTimes = {};
	
	function positionInlineEditElements($grid) {
		var saveCancel = $grid.find('div.inlineSaveCancel');
		var container = saveCancel.parents("div.dataGrid-overflow:first");
		var widths = $grid.data("origWidths");
		if(container.offset()) {
			saveCancel.offset({left: container.offset().left + container.width(),
							   top: saveCancel.parents("td:first").offset().top + 6});
		}
		$grid.find("td.inlineEdit input[type='text'], td.inlineEdit select").each(function(i, input){
			var $td = $(input).parents("td:first");
			var width = widths[$td.index()] / $td.find(":input").length;
			width = Math.max(width, 40);
			$td.css("paddingLeft", 0);
			$(input).width(width);
		});
		
		//focus the first element in error
		var firstErrorElement = $grid.find(":input.input-error:first");
		
		if(firstErrorElement.length) {
			firstErrorElement.focus();
		} else {
			//otherwise focus the first element in the clicked cell
			var clickedCellIndex = $grid.data('clickedCell') + 1;
			$grid.find("tr.inlineEdit").children("td:nth-child("+clickedCellIndex +")")
				 .nextAll()
				 .andSelf()
				 .find(":input:visible")
				 .eq(0)
				 .focus();
		}
	};
	
	function replaceShoppingCart(origCart, newCart) {
		var origItems = origCart.find("li"),
			newItems = newCart.find("li");
		origCart.replaceWith(newCart);
		if(origItems.length != newItems.length) {
			$("#shoppingCartTitle").effect("highlight", {}, 2000);
		}
		for(var i=0; i< newItems.length; i++) {
			var newItem = $(newItems.eq(i));
			if(!origCart.find("#" + newItem.attr("id")).length) {
				newItem.effect("highlight", {}, 2000);
			}
		}
        $("#shoppingCartSize").text(newItems.length);
		
	}
    body.delegate('table.data-grid-object-alpha', 'update-state', function(event, options) {
		event.stopPropagation();
		var timeoutHandle;
		options = options || {fade: true};
		var $grid = $(this),
			id = $grid.attr('id'),
			bodyReplaceFunction = options.fade? "fadeReplaceWith" : "replaceWithAndCallback",
			queryTime = new Date().getTime(),
			
			//default ajax behavior which can be overriden by callers
			defaultAjax = {
				url: $grid.data("update-link"),
				
				success: function(response){
					clearTimeout(timeoutHandle);
					$.unblockUI();
					if(queryTimes[id] == queryTime) {
						var $response = $(response);
                        var $newTable = $response.find('table#'+id);
						if($newTable.length) {
							$grid.find("thead.results tr:not(.data-grid-task-bar)").replaceWith($newTable.find("thead.results tr:not(.data-grid-task-bar)"));
							
							$grid.find("thead.headers").replaceWith($newTable.find("thead.headers"));
							
							$grid.find("tfoot").replaceWith($newTable.find("tfoot"));
							$grid.find("tbody tbody")[bodyReplaceFunction]($newTable.find("tbody tbody"), function(){
								options.positionInline && positionInlineEditElements($grid);
								options.postLoad && options.postLoad($grid);
								//refresh page height
								$('body').height(0);
								$grid.trigger('componentLoaded');
							});
						}

                        if($response.find("#data-grid-selection").length) {
                            replaceShoppingCart($('#data-grid-selection'), $response.find("#data-grid-selection"));
                        }
                        if(options.replaceSearch) {
                            $('div.searchContainer').replaceWith($response.find("div.searchContainer"));
                        }
                        if(options.refreshControls) {
                            $('body').trigger('refreshControls');
                        }
						
					}
				},
				
				error: function(response) {
					$.unblockUI();
					$('body').html(response.responseText);
				}
			}, 
			reloadGrid = function() {
				queryTimes[id] = queryTime;
				$(this.$table).block({message: '<h4>Loading...</h4>', fadeIn: 100, fadeOut: 100, timeout: 500}); 
			      timeoutHandle = setTimeout(function() { 
			    	  $.blockUI({message: '<h4>Please wait... <div class="grid-loading"></div></h4>', timeout: 120000}); 
			      }, 500); 
			    $.ajax($.extend(defaultAjax, options));
			};
		if(options.allowDirty) {
			reloadGrid();
		} else {
			$(window).trigger('dirtyReload', [reloadGrid]);
		}
		
	});

	$grids.delegate(":input", "focusin", function(e){
		$(e.target).parents("tr:first").addClass("focus");
	});
	
	$grids.delegate(":input", "focusout", function(e){
		$(e.target).parents("tr:first").removeClass("focus");
	});

	$(".confirm").click(function(event, skipPopup){
		if(skipPopup) {
			return true;
		}
		var $clickedLink = $(this);
		var text = $clickedLink.attr("confirm");
		var $dialog = $('<div></div>');
		$dialog.dialog({
			resizable: false,
			autoOpen:false,
			height:140,
			modal: true,
			position:'center',
			buttons: {
				Yes: function() {
					$( this ).dialog( "close" );
					$clickedLink.trigger("click", true);
					$dialog.remove();
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			}
		});
		
		$dialog.html(text);
		$dialog.dialog('open');
		
		return false;
	});
	
	function setGridWidth(target) {
		$(target).find('.dataGrid-overflow:not(:hidden)').each(
				function(i, div) {
					// only resize the datagrid once
					if (!$(div).data("resize")) {
						$(div).data("resize", true);

						$(div).width($(div).parent().width());
					}
					var id = $(div).parents(".data-grid-object-alpha:first").attr("id");
					var expandLink = $("#filter-expand-" + id);
					if (expandLink.length) {
						var controlHeight = expandLink.closest(".dataViewControlBody").height();
						var scopeTest = jQuery('<div style="display: none; font-size: 1em; margin: 0; padding:0; height: auto; line-height: 1; border:0;">&nbsp;</div>').appendTo(expandLink.closest(".dataViewControlBody"));
						var scopeVal = scopeTest.height();
						scopeTest.remove();
						
						var width;
						if (expandLink.is(".expanded") && (controlHeight / scopeVal).toFixed(8) > 8) {
							width = $('.dataViewContainer').innerWidth() - $('.header-control').width() - 10;
						} else {
							width = $('.dataViewContainer').innerWidth();
						}
						$('.dataGrid-overflow').width(width);
					}
				});
	}
	
	$('body').bind('resize', function(event) {
		setGridWidth(event.target);
	});

	$('body').bind('subComponentLoaded', function(event) {
		setGridWidth(event.target);
	});
	
	$('body').bind('componentLoaded', function(event) {
		setGridWidth(event.target);
	});
	
	$("body").bind("resizeToTarget", function(event) {
		$(event.target).find('.dataGrid-overflow').each(function(i,div){
			$(div).width("100%");
		});
	});
	
	$('body').bind('init', function(){
		$('body').trigger('componentLoaded');
	});
}); 
 
