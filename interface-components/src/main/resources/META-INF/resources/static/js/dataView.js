jQuery(function($){

    $("body").delegate("a.updateShoppingCart", "click", function(event) {
		event.preventDefault();
		var link = $(event.currentTarget);
		$('table.data-grid-object-alpha').trigger("update-state", {replaceSearch:true, refreshControls:true, url:link.attr('href')});

	});
    
    $.fn.exists = function () {
        return this.length !== 0;
    };
    
	$('body').delegate('.dataViewControl .dataViewControlHeader a', 'click', function(event) {
		event.preventDefault();
		var link = $(event.target);
		if (link.next(".dataViewControlConfig").exists()) {
			//the control is already showing, exit
			return;
		}
		var header = link.parents(".dataViewControlHeader:first");
		$.ajax($(event.target).attr('href'), {
			success: function(response, textStatus, jqXHR) {
				if (link.hasClass("configFullPopup") || header.hasClass("configFullPopup")) {

				} else if(link.hasClass("configPopup") || header.hasClass("configPopup")) {
					var overlay = $('<div/>').attr('id', 'dataViewControlOverlay');
					var container = $('<div/>').attr('class', 'dataViewControlConfig').attr('style', 'min-width: 130px;');
					
					overlay.click(function(event){
						if ($(event.target).closest('.dataViewControlConfig').length == 0) {
							container.remove();
							overlay.remove();
						}
					});
					
					container.offset({top: header.outerHeight(), left: link.offset().left - link.outerWidth()});
					$(event.target).after(container);
					container.html(response);
					
					$('body').append(overlay);
				} else if(link.hasClass("configInline")){
					header.next(".dataViewControlBody").html(response);
					$('table.data-grid-object-alpha').trigger("update-state", {refreshControls:true});
				} else if(header.hasClass("shoppingCartHeader")) {
					$('table.data-grid-object-alpha').trigger("update-state");
				} 
			}
		});
	});
	
	$("#filterDisplayControl").click(function(event) {
		event.preventDefault();
		var div = $('.dataGrid-overflow');
		if (!div.data("orig-width")) {
			div.data("orig-width", div.parent().width());
		}
		
		$("div.dataViewContainer div.data-grid-container").toggleClass("full-page");
		$("#taskViewfilterControls").toggle();
		
		$('.dataGrid-overflow').each(function(i,div) {
			$(div).data("resize", false);
		});
		
		if ($(this).html() == "hide filters") {
			$(this).html("show filters");
			$('div.data-grid-container').trigger('componentLoaded');
			
		} else {
			$(this).html("hide filters");
			div.width(div.data("orig-width"));
		}
	});
	
	function updatePage(container, href) {
		container.load(href, function() {
		   $('table.data-grid-object-alpha').trigger("update-state", {replaceSearch:true, refreshControls:true});
		   $("div.task-detail-content").trigger("update-state");
		   $(container).trigger('componentLoaded');
		});
		$('#dataViewControlOverlay').trigger('click');
		$('#dataViewControlOverlay').remove();
	}

	$('.dataViewControl').delegate('.dataViewControlConfig a', 'click', function(event) {
		event.preventDefault();
		var a = $(event.target);
		var body = a.parents('.dataViewControl:first').find('.dataViewControlBody');
		updatePage(body, a.attr("href"));
		$('.dataViewControl').trigger('dataViewControlUpdated');
	});
	
	$('body').delegate("form[remote='true'] :button", 'click', function(event) {
		var button = $(event.currentTarget);
		var hidden = $('<input type="hidden"/>');
		hidden.attr('name', button.attr("name"));
		hidden.attr('value', button.val());
		button.after(hidden);
	});

	$('body').delegate("form[remote='true'] :checkbox", 'change', function(event) {
		var button = $(event.target).nextUntil("button").next();
		button.click();
	});
	
	$('body').delegate("form[remote='true']", 'submit', function(event) {
		event.preventDefault();
		var form = $(event.target);

		// ie 8 fix
		if (!form.is("form")) {
			form = form.parents("form:first");
		}

		var data = form.serializeArray();
		$.ajax(form.attr("action"), {
			method : form.attr("method"),
			data : data,
			type : 'POST',
			success : function(response) {
				form.html(response);
				$(form).trigger('componentLoaded');
				$('table.data-grid-object-alpha').trigger(
						"update-state", {
							replaceSearch : true,
							refreshControls : true
						});
				$("div.task-detail-content").trigger("update-state");
				
			}
		});
	});
	
	$('.dataViewControl').delegate('.dataViewControlConfig form[data-filters="manage"]', 'submit', function (event) {
		event.preventDefault();
		var form = $(event.target);

		// ie 8 fix
		if (!form.is("form")) {
			form = form.parents("form:first");
		}
		
		var data = form.serializeArray();
		$.ajax(form.attr("action"), {
			method : form.attr("method"),
			data : data,
			type : 'POST',
			success : function(response) {
				$('table.data-grid-object-alpha').trigger(
						"update-state", {
							replaceSearch : true,
							refreshControls : true
						});
			}
		});
		$('#dataViewControlOverlay').trigger('click');
	});

	$('body').delegate(".dataViewControlBody a:not(.task-link)", 'click', function(event) {
		event.preventDefault();
		var link = $(event.target);
		var controlBody = link.parents("div.dataViewControlBody:first");
		$('table.data-grid-object-alpha').trigger("resize");
		$.get($(this).attr('href'), function(response) {
			if (!controlBody.hasClass("shoppingCartBody")) {
				controlBody.html(response);
				$(controlBody).trigger('componentLoaded');
			}
			$('table.data-grid-object-alpha').trigger("update-state", {
				replaceSearch : true,
				refreshControls : true
			});
			$("div.task-detail-content").trigger("update-state");
		});
	});
	
	$('body').delegate(".dataViewControl a.refresh", 'click', function(event){
		event.preventDefault();
		var link = $(event.target);
		var controlBody = link.parent().find("div.dataViewControlBody:first");
		$.get(link.attr('href'), function(response){
			controlBody.html(response);
			$(controlBody).trigger('componentLoaded');
		});
	});
	
	$(".dataViewControl").bind("componentLoaded", function () {
		$('table.data-grid-object-alpha').removeData("resize").trigger("resize");
	});
	
	$('body').bind("refreshControls", function(){
		$('.dataViewControl a.refresh').trigger('click');
	});
	
	$('body').delegate('a.show-selectable-filters', 'click', function(event){
		event.preventDefault();
		var dropdown = $(this).next(".filter-select-dropdown");
		dropdown.toggle().offset({left: 10});
	});
	
	var taskCheckboxes = $("#task-checkboxes input[type='checkbox']"),
    startButton = $("#start-tasks-button");
    taskCheckboxes.click(function() {
    	if (taskCheckboxes.is(':checked')) {
    	  startButton.removeAttr('disabled');
    	} else {
    	  startButton.attr('disabled', 'true');
    	}
    });
    
    $(document).ready(function() {
    	if (!taskCheckboxes.is(':checked')) {
    		startButton.attr('disabled', 'true');
      	}	
    });
    
	$('body').delegate(".filterList input[type='text']", "keydown", function(event){
		if (event.keyCode == 13) {
			$(this).next(':button').trigger('click');
			return false;	
		}
	});
});