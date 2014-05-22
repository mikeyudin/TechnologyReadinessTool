jQuery(function($) {
	$("body").delegate('.detailset-section-title', "click", function(event) {
		var title = $(this);
		event.preventDefault();
		var section = $(".detail-set-section").filter(function(index) {
			return $(this).data("id") == title.data("id");
		}).toggle();
		
		if (section.is(":hidden")) {
			title.find("span").html("[+]");
		} else {
			title.find("span").html("[-]");
		}
	});

	$("body").delegate(".detail-set-container a.show-all", "click", function(event) {
		event.preventDefault();
		var section = $(this).closest(".detail-set-section");
		$.get($(event.currentTarget).attr('href'), function(response) {
			$.get($("#detail-set-update").attr('href'), function(response) {
				var newContainer = $(response).find(".detail-set-section").filter(function(index) {
					return $(this).data("id") == section.data("id");
				});
				newContainer.find('.dataGrid-overflow').each(function(i,div){
					$(div).width("100%");
				});
				section.replaceWith(newContainer);
				
			});
		});
	});
});