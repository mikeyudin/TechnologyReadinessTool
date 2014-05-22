(function ($) {
	$.fn.geoChart = function (source, cfg) {
		var items = this;
		var dataTable;
		if (items.length > 0) {
			$.get(source, function(data){
				dataTable = new google.visualization.DataTable(data);
	
				var options = {};
				options['colorAxis'] = { minValue : 0, maxValue : 4, colors : ['#B81F4B','#F6851F','#FCB913','#00A15E', '#9A9A9A']};
				options['backgroundColor'] = '#FFFFFF';
				options['datalessRegionColor'] = '#E5E5E5';
				options['legend'] = 'none';
				options['region'] = 'US';
				options['resolution'] = 'provinces';
				options['datalessRegionColor'] = '#E5E5E5';
	
				$.extend(options, cfg);
				items.each(function(index) {
					var currentElement = $(this).empty();
					var chart = new google.visualization.GeoChart(this);
					var hideTooltip = currentElement.data("hide-tooltip");
					if (hideTooltip && (hideTooltip == true || hideTooltip == 'true')) {
						var view = new google.visualization.DataView(dataTable);
						view.setColumns([0, 1, {type: 'string', role: 'tooltip', calc: function(table, row){return '';}}]);
						chart.draw(view, options);
					} else {
						chart.draw(dataTable, options);
					}

					google.visualization.events.addListener(chart, 'select', function(){
						var selection = chart.getSelection();
						currentElement.trigger("regionSelected", [dataTable.getRowProperty(selection[0].row, 'orgId')]);
					});
				});
	
				
			});
		}
		return this;
	};
})(jQuery);