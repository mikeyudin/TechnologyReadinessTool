$(function() {
	setupVerticalTabs();
	if($("ul.task-detail-options li.selected").size() != 1){
		selectEntity($("ul.task-detail-options li:first"));
	}	

	function selectEntity(element) {
		$("ul.task-detail-options li").removeClass("selected");
		element.addClass("selected");
		var index = element.data("index");
		$("td.task-detail div.entity-detail").hide();
		$("td.task-detail div.entity-detail").filter(function() {
			return $(this).data("index") == index;
		}).show();
	}

	function setupVerticalTabs() {
		$("ul.task-detail-options").delegate("li a", "click", function(event) {
			// only prevent this when showing the details view.
			if ($(".task-detail-content").length != 0) {
				event.preventDefault();
				var selected = $(this).closest("li");
				selectEntity(selected);
			}
		});
	}

});