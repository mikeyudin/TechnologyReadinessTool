jQuery(function($) {
	//set up dates and numeric inputs
	$('input.DATE').datepicker({
		    changeMonth: true,  
		    changeYear: true
		  });
	$('input.DATETIME').datepicker({
	    changeMonth: true,  
	    changeYear: true
	  });
	$('input.NUMBER').numeric();
	
	//Detect changes to fieldset inputs and mark them dirty
	
	var dirty = [];
	var inputSelector = '.fieldset :input, .data-grid-object-alpha .inlineEdit :input';
	
	function markDirty(){
		if(!$(this).parents(".editAlertDisabled").length && !$(this).hasClass("editAlertDisabled")) {
			dirty.push($(this));
		}
	}
	
	$('body').delegate(inputSelector, 'change', markDirty);
	$('body').delegate(inputSelector, 'keypress', markDirty);
	
	//detect when we are leaving the page and alert if necessary
	
	// since beforeunload event gives no information about the source of the event
	// (whether it was the result of a link or back button or form submission)
	// we need to detect all button clicks and set a variable so we can allow forms
	// to be submitted when there is dirty data on the page. We will unset the variable
	// after a few ms in case the button click did not result in leaving the page and
	// we are now leaving the page for some other reason.
	
	var allowDirty = false;
	var reloadText = "Any unsaved data will be lost.";
	
	$('body').delegate(':button, a', 'click', function() {
		allowDirty = $(this).is(":button") || $(this).hasClass("allowDirty");
		setTimeout(function(){
			allowDirty = false;
		}, 50);
	});
	
	
	function isDirty() {
		for(var i = 0; i < dirty.length; i++) {
			if(dirty[i] && dirty[i].is(":visible")) {
				return true;
			}
		}
		return false;
	}
	
	/** pop up the dialog if we are dirty and the last thing clicked was not a button **/
	$(window).bind('beforeunload', function(){
		if(!allowDirty && isDirty()) {
			return reloadText;
		}
	});

	/** execute the callback unless the data is dirty and the user clicks no **/
	$(window).bind('dirtyReload', function(event, callback){
		if(!isDirty() || confirm(reloadText)) {
			callback && callback();
		}
	});

	$('body').bind('componentLoaded', function(event){
		$(event.target).find('.input-info').cluetip({
			showTitle: false,
			attribute: 'rel',
			local: true,
			cursor: 'pointer',
			hoverIntent: {
				sensitivity: 3,
				interval: 100,
				timeout: 100
			}
		});
	});
});

