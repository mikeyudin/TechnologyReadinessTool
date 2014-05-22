function geoChartSelectHandler() {
	var selection = geoChart.getSelection();
	if (selection.length > 0) {
		var href = $('#selectOrg').attr('href') + '&orgCode=' + geoData.getRowProperty(selection[0].row, 'stateCode');
		window.location = href;
	}
}

function selectOrg(orgCode) {
	var href = $('#selectOrg').attr('href') + '&orgCode=' + orgCode;
	window.location = href;
}

$(function(){
	$('input[type="radio"]').on('change', function(event){
		$(event.target).closest('form').submit();
	});
});