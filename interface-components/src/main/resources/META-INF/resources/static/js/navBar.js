jQuery(function($) {
	var subtabsHovered = false;
	var mainTabHovered = false;

	var hideSubtabs = function() {
		if (!subtabsHovered && !mainTabHovered) {
			$("#navigation-gamma div.navigation-gamma").addClass('ui-helper-accessible-hidden').hide();
			$('#navigation-beta-bkgd-beta li').removeClass('navigation-beta-selected');
			var $selectedMainTab = $('#navigation-beta-bkgd-beta li').filter(function() {
				return $(this).data('selected');
			});
			$selectedMainTab.addClass('navigation-beta-selected');
			
			var $wrappingDiv = $('#navigation-gamma li.navigation-gamma-selected').closest('div');
			if($wrappingDiv.length == 0) {
				$wrappingDiv = $('#navigation-gamma-' + $selectedMainTab.attr("id"));
			}
			$wrappingDiv.removeClass('ui-helper-accessible-hidden').show();
		}
	};
	
	var configMainTabs = {
		sensitivity: 10,
		interval: 250,
		timeout: 500,
		over: function() {
					var parentCode = $(this).attr('id');
					$('#navigation-beta-bkgd-beta li').removeClass('navigation-beta-selected');
					$(this).addClass('navigation-beta-selected');
					$("#navigation-gamma div.navigation-gamma").addClass('ui-helper-accessible-hidden').hide();
					$('#navigation-gamma-' + parentCode).removeClass('ui-helper-accessible-hidden').show();
					mainTabHovered = true;
		},
		out: function() {
			mainTabHovered = false;
			setTimeout(hideSubtabs, 1750);
		}
	};

	$('#navigation-beta-bkgd-beta li').hoverIntent(configMainTabs);
	
	var configSubTabs = {
		over: function() {
			subtabsHovered = true;
		}, timeout: 50,
		out: function() {
			subtabsHovered = false;
			setTimeout(hideSubtabs, 1750);
		}
	};
	$("#navigation-gamma").hoverIntent(configSubTabs);

});