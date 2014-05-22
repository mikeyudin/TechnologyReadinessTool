$.widget('core.coreautocomplete', $.ui.autocomplete, {
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

$(document).ready(function() {
	// TOOLTIPS / CONTEXTUAL HELP / ADDED INFO FLYOUT
	$('.added-info a').cluetip({
		positionBy: 'mouse',
		topOffset: 20,
		leftOffset: 20,
		splitTitle: '|',
		cluetipClass: 'flyout-alpha',
		waitImage: false,
		cursor: 'pointer',
		arrows: true,
		sticky: true,
		activation: 'click',
		closePosition: 'title',
		closeText:'X'
	});
	
	$('.inline-error a').cluetip({
		positionBy: 'mouse',
		topOffset: 20,
		leftOffset: 20,
		splitTitle: '|',
		showTitle: false,
		cluetipClass: 'flyout-beta',
		waitImage: false,
		cursor: 'pointer',
		arrows: true,
		sticky: false,
		activation: 'hover'
	});

	$('body').delegate('a.entity-name', 'click', function(event) {
		event.preventDefault();
		var anchor = $(event.currentTarget);
		$.ajax(anchor.attr('href'), {
			success: function(response, textStatus, jqXHR) {
				var dialog = $("<div />").attr("id", "detailDialog");
				dialog.addClass("detail-dialog");
				dialog.html(response);
				$("body").append(dialog);
				dialog.dialog({
					title: anchor.attr("title"),
					height: 600,
					width: 600,
					modal: true,
					close : function(event, ui){
						dialog.remove();
					},
					resizeStop: function(event, ui) {
						dialog.trigger('resizeToTarget');
					},
					open: function(event, ui) {
						dialog.trigger("resizeToTarget");
					}
				});

			}
		});
	});
	
	var isScopeSelecting = false;
	$('body').delegate('a.entity-name-hud', 'click', function(event) {
		event.preventDefault();
		if(isScopeSelecting){
			return;
		}
		isScopeSelecting = true;
		var anchor = $(event.target);
		$.ajax(anchor.attr('href'), {
			success: function(response, textStatus, jqXHR) {
				var dialog = $("<div />").attr("id", "detailDialog");
				dialog.addClass("detail-dialog");
				dialog.html(response);
				$("body").append(dialog);
				isScopeSelecting = false;
				dialog.dialog({
					title: anchor.attr("title"),
					height: 150,
					width: 500,
					modal: true,
					position: [25,25],
					close : function(event, ui){
						dialog.remove();
					},
					resizeStop: function(event, ui) {
						dialog.trigger('resizeToTarget');
					},
					open: function(event, ui) {
						dialog.trigger("resizeToTarget");
					}
				});

			}
		});
	});
	
	var isOrgSelecting = false;
	$('body').delegate('a.org-selection-popup', 'click', function(event) {
		event.preventDefault();
		if(isOrgSelecting){
			return;
		}
		isOrgSelecting = true;
		var anchor = $(event.target);
		$.ajax(anchor.attr('href'), {
			success: function(response, textStatus, jqXHR) {
				var dialog = $("<div />").attr("id", "detailDialog");
				dialog.addClass("detail-dialog");
				dialog.html(response);
				$("body").append(dialog);
				isOrgSelecting = false;
				dialog.dialog({
					title: anchor.attr("title"),
					height: 600,
					width: 600,
					modal: true,
					position: { my: 'left top', at: 'left bottom', of: event.target },
					close : function(event, ui){
						dialog.remove();
					},
					resizeStop: function(event, ui) {
						dialog.trigger('resizeToTarget');
					},
					open: function(event, ui) {
						dialog.trigger("resizeToTarget");
					}
				});
			}
		});
	});
	
	// these links can display (and be click-able) before they've been
	// initialized into jQuery dialogs.  so now, they are initially hidden
	// via css.  
	$('.entity-name').removeClass('initially_hidden');
	$('.entity-name-hud').removeClass('initially_hidden');
	$('.org-selection-popup').removeClass('initially_hidden');
});

// Window load event used just in case window height is dependent upon images
$(window).bind("load", function() { 
		
	
	// all ajax requests should not cache, IE issue workaround
	$.ajaxSetup({
		  cache: false
		});
	
	// IE6 FLICKER ON HOVER FIX
	try {
		document.execCommand('BackgroundImageCache', false, true);
	} catch(e) {}
	
	// IE pre-9 doesn't implement this
	if (typeof String.prototype.trim !== 'function') {
		String.prototype.trim = function() {
			return this.replace(/^\s+|\s+$/g, '');
		};
	}
	
	$(document).ajaxError(function(e, xhr, settings) {
		window.location.reload();
	});
	
	// LIGHTBOX
	$('a[id=saveb4leave]').click(function(e){
		e.preventDefault();
		var type = $(this).attr('lang');
		var message  = $(this).attr('href');
		var question = $(this).attr('rel');
		
		showLightBox(e,type,message,question);
	});
	
	function showLightBox(e,type,message,question) {
		var arrPageSizes = ___getPageSize();
		
		// Hide some elements to avoid conflict with overlay in IE. These elements appear above the overlay.
		$('embed, object, select').css({ 'visibility' : 'hidden' });
		
		if (type == 'warning') {
			$('body').append('<div id="message-overlay"></div><div id="message-lightbox"><div id="message-lightbox-container"><a href="#" id="message-lightbox-close">X</a><table><tr><td rowspan="3"><span class="icons-abnormal-sizes inline-icon-left warningBig"></span></td><td><h2>Unsaved Task</h2></span></td></tr><tr><td>'+message+'</td></tr><tr colspan="2"><td><em>'+question+'</em><button id="" type="submit" value="submit"><span style="color:#000; margin:0 5px; font-size:12px;">Save Changes</span></button><button id="" type="submit" value="submit" class="gray-button"><span style="color:#000; margin:0 5px; font-size:12px;">Discard Changes</span></button><button id="message-lightbox-cancel" type="submit" value="submit" class="gray-button"><span style="color:#000; margin:0 5px; font-size:12px;">Cancel</span></button></td></tr></table></div></div>');
		}
			
		$('#message-overlay').css({
			backgroundColor:	'#000',
			opacity:			0.7,
			width:				arrPageSizes[0],
			height:				arrPageSizes[1]
		}).fadeIn();
		
		var arrPageScroll = ___getPageScroll();
		// Calculate top and left offset for the jquery-lightbox div object and show it
		$('#message-lightbox').css({
			top:	arrPageScroll[1] + (arrPageSizes[3] / 10),
			left:	arrPageScroll[0]
		}).fadeIn();		
		
		// If window was resized, calculate the new overlay dimensions
		$(window).resize(function() {
			// Get page sizes
			var arrPageSizes = ___getPageSize();
			// Style overlay and show it
			$('#message-overlay').css({
				width:		arrPageSizes[0],
				height:		arrPageSizes[1]
			});
			// Get page scroll
			var arrPageScroll = ___getPageScroll();
			// Calculate top and left offset for the jquery-lightbox div object and show it
			$('#message-lightbox').css({
				top:	arrPageScroll[1] + (arrPageSizes[3] / 10),
				left:	arrPageScroll[0]
			});
		});
		
		$('#message-lightbox-close, #message-lightbox-cancel').click(function(e){ //'#message-overlay, #message-lightbox, #message-light-close', #message-cancel
			e.preventDefault();
			$('#message-lightbox').remove();
			$('#message-overlay').fadeOut(function() { $('#message-overlay').remove(); });
			// Show some elements to avoid conflict with overlay in IE. These elements appear above the overlay.
			$('embed, object, select').css({ 'visibility' : 'visible' });
		});	
	}
	
	/**
	 * getPageSize() by quirksmode.com
	 *
	 * @return Array Return an array with page width, height and window width, height
	 */
	function ___getPageSize() {
		var xScroll, yScroll;
		if (window.innerHeight && window.scrollMaxY) {	
			xScroll = window.innerWidth + window.scrollMaxX;
			yScroll = window.innerHeight + window.scrollMaxY;
		} else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
			xScroll = document.body.scrollWidth;
			yScroll = document.body.scrollHeight;
		} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
			xScroll = document.body.offsetWidth;
			yScroll = document.body.offsetHeight;
		}
		var windowWidth, windowHeight;
		if (self.innerHeight) {	// all except Explorer
			if(document.documentElement.clientWidth){
				windowWidth = document.documentElement.clientWidth; 
			} else {
				windowWidth = self.innerWidth;
			}
			windowHeight = self.innerHeight;
		} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
			windowWidth = document.documentElement.clientWidth;
			windowHeight = document.documentElement.clientHeight;
		} else if (document.body) { // other Explorers
			windowWidth = document.body.clientWidth;
			windowHeight = document.body.clientHeight;
		}	
		// for small pages with total height less then height of the view port
		if(yScroll < windowHeight){
			pageHeight = windowHeight;
		} else { 
			pageHeight = yScroll;
		}
		// for small pages with total width less then width of the view port
		if(xScroll < windowWidth){	
			pageWidth = xScroll;		

		} else {
			pageWidth = windowWidth;
		}
		arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight);
		return arrayPageSize;
	};
	/**
	 * getPageScroll() by quirksmode.com
	 *
	 * @return Array Return an array with x,y page scroll values.
	 */
	function ___getPageScroll() {
		var xScroll, yScroll;
		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
			xScroll = self.pageXOffset;
		} else if (document.documentElement && document.documentElement.scrollTop) {	 // Explorer 6 Strict
			yScroll = document.documentElement.scrollTop;
			xScroll = document.documentElement.scrollLeft;
		} else if (document.body) {// all other Explorers
			yScroll = document.body.scrollTop;
			xScroll = document.body.scrollLeft;	
		}
		arrayPageScroll = new Array(xScroll,yScroll);
		return arrayPageScroll;
	};

	var corePlugins = {
		autocomplete: {
			init: function(element, options) {
				$(element).coreautocomplete(options);
			},
			
			options: {
				select:function(event, selected) {
					var hidden = $(this).data("hidden");
					if(!hidden) {
						hidden = $('<input type="hidden"/>').attr("name", $(this).attr("name"));
						$(this).data("hidden", hidden);
						$(this).attr("name", "");
						$(this).after(hidden);
					}
					$(hidden).val(selected.item.value);
					$(this).val(selected.item.label);
					event.preventDefault();
				},
				
				create: function(event) {
					var autocomplete = $(this);
					var active = false;
					var dropdown = $('<a href="#"/>').addClass("caret autocomplete-dropdown");
					autocomplete.after(dropdown);
					autocomplete.width(autocomplete.width() - 15);
					dropdown.click(function(event){
						if(!active) {
							active = true;
						    autocomplete.coreautocomplete("search", " ");
						} else {
							active = false;
							autocomplete.coreautocomplete("close");
						}
						event.preventDefault();
						event.stopPropagation();
					});
				}
			}
		}
	};
	
	$('body').bind('componentLoaded', function(event){
		$.each(corePlugins, function(name, info){
			$(event.target).find('.core-'+name).each(function(i, element){
				var options = eval('(' +$(element).data(name+"-options") + ')');
				options = $.extend(info.options, options);
				info.init(element, options);
			});
		});
	});
		
	// This prepends the context onto images that require it (included by custom text HTML)
	// It is dependent on the 'contextPath' variable initialized in the sitemesh decorators.
	$('.addcontext-src-path').each(function () {
		$(this).attr("src",contextPath+$(this).attr("src"));
	});
	
	// This prepends the context onto links that require it (included by custom text HTML)
	// It is dependent on the 'contextPath' variable initialized in the sitemesh decorators.
	$('.addcontext-href-path').each(function () {
		$(this).attr("href",contextPath+$(this).attr("href"));
	});
	
	$('body').trigger('init');
	
});