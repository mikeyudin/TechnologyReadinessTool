// NAVIGATION MENU INTERACTION CONTROL

$(window).bind("load", function() { 
	// On Hover Over
	function hoverOver(){
		$(this).find(".sub-outer").stop().show();
	}
	
	// On Hover Out
	function hoverOut(){
		$(this).find(".sub-outer").stop().hide();
	}
	
	//Navigation configuration
	var navConfig = {
		 sensitivity: 2, // number = sensitivity threshold (must be 1 or higher)
		 interval: 40, // number = milliseconds for onMouseOver polling interval
		 over: hoverOver, // function = onMouseOver callback (REQUIRED)
		 timeout: 80, // number = milliseconds delay before onMouseOut
		 out: hoverOut // function = onMouseOut callback (REQUIRED)
	};
	
	//$("#navigation-beta ul li .sub-outer").css({'opacity':'0'});			// Fade sub nav to 0 opacity on default
	$("#navigation-beta ul li .sub-outer").bgiframe({ opacity: false });	// IE6 absolute positioned element fix
	$("#navigation-beta ul li").hoverIntent(navConfig);				// Trigger Hover intent with custom configurations
	
	// On LIs, attach link if not selected LI
	$("#navigation-beta ul li .sub-outer li").click(function(e) {
		window.location = $(this).find("a").attr("href");
	});
});