var autocomplete_loaded = "no";
jQuery.fn.setupCoreAutocompleteFields = function(index) {
    	
		var input = $(this);
		var inputName = input.attr("id");
		var inputType = input.data("type");
		var inputKey = input.attr("instanceId");
		var latest_date;
		
		$.widget('core.fieldAutocomplete', $.core.coreautocomplete, {
			_renderItem: function(ul, item) {
				var href = $("#"+inputName+"addItemUrl").html().trim();
				var itemText = item.name + " (" + item.code + ")";
				var anchor = "<a href='" + href + "&multiple=true&" + inputType + "Id=" + item.id + "&key=" + inputKey + "'>" + itemText + "</a>";
				return $("<li>").append(anchor).appendTo(ul);
			}
		});
		
		input.fieldAutocomplete({
			minLength : 0,
			source : function(request, response) {
				var instance_date = new Date();
				latest_date = instance_date;
				$.getJSON($("#"+ inputName + "ajaxLoadUrl").attr("href"), {
					ajax : "true",
					term : request.term,
					key  : inputKey
				}, function(data, textStatus, jqXHR) {
					if(instance_date == latest_date) {
					  response(data,textStatus,jqXHR);
					}
				});
			},
			focus : function(event, ui) {
				input.val(ui.item.name + '(' + ui.item.code + ')');
				return false;
			},
			select : function(event, ui) {
				var container = $(event.target).parents(
						'.autoCompleteControl:first').find(
						'.autoCompleteControlBody');
				container
						.load($("#" + inputName +"addItemUrl").text().trim()
								+ "&" + inputType + "Id=" + ui.item.id + "&key=" + inputKey,
								function() {
									$('body').trigger('refreshControls');
								});
				
				event.preventDefault();
			},
			appendTo : input.parent()
		});

		$("#"+inputName+"DownArrow").click(function() {
			// close if already visible
			if (input.fieldAutocomplete("widget").is(":visible")) {
				input.fieldAutocomplete("close");
				return;
			}
			// work around a bug (likely same cause as #5265)
			$(this).blur();

			// pass empty string as value to search for, displaying all results
			input.fieldAutocomplete("search", input.val());
			input.focus();
			return false;
		});
		$(document).delegate('.autoCompleteControlBody a', 'click', function(event) {
		    event.preventDefault();
		    var container = $(event.target).parents(
			'.autoCompleteControl:first').find(
			'.autoCompleteControlBody');
		    container.load($(this).attr('href'), function() {
				$('body').trigger('refreshControls'); });
		    event.preventDefault();
		});
	    };
	    
jQuery(function() {
	if(autocomplete_loaded == "no") {
	    $('input.autocompleteInputField').each(jQuery.fn.setupCoreAutocompleteFields);
	    autocomplete_loaded = "yes";
	}
	});

