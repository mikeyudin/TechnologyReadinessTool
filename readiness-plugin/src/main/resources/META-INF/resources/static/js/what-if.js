$(function() {
	'use strict';
	var whatIfDialog = $('#what-if-dialog');
	var requirements = $('#whatIfRequirements');
	whatIfDialog.dialog({
		autoOpen : false,
		modal : true,
		width : 900
	});

	var WhatIf = function(org, type) {
		this.type = type;
		this.devicePassingCount = org.devicePassingCount;
		this.testingWindowLength = org.testingWindowLength;
		this.sessionsPerDay = org.sessionsPerDay;
		this.internetSpeed = org.internetSpeed;
		if (this.internetSpeed == null || this.internetSpeed === "") {
			this.internetSpeed = 0;
		}
		this.internetUtilization = org.internetUtilization;
		if (this.internetUtilization == null || this.internetUtilization === "") {
			this.internetUtilization = 100;
		}
		this.networkSpeed = org.networkSpeed;
		if (this.networkSpeed == null || this.networkSpeed === "") {
			this.networkSpeed = 0;
		}
		this.networkUtilization = org.networkUtilization;
		if (this.networkUtilization == null || this.networkUtilization === "") {
			this.networkUtilization = 100;
		}
		this.testStarts = org.testingTestStartCount;
		this.deviceRatio = 0;
		this.networkRatio = 0;
		this.networkConstraint = null;
	};

	WhatIf.prototype = {
		calcDeviceToTestTaker : function() {
			var testingWindow = this.getTestingWindow();
			this.deviceRatio = (this.devicePassingCount * testingWindow * this.sessionsPerDay) / this.testStarts * 100;
			if (isNaN(this.deviceRatio) || !isFinite(this.deviceRatio)) {
				this.deviceRatio = 0;
			} else {
				this.deviceRatio = parseInt(this.deviceRatio.toFixed(0));
			}

			var percentElement = whatIfDialog.find('[data-percent-ready="testTakerPercentStudentsTestable"]');
			var bar = percentElement.find('.progress[data-percent="' + this.type + '"] > .bar');
			resetBar(bar);
			updateBar(bar, this.deviceRatio);

			return this.deviceRatio;
		},
		calcNetworkReadiness : function() {
			var testingWindow = this.getTestingWindow();
			var throughput;
			if (requirements.val() == 'MINIMUM') {
				throughput = whatIfDialog.find('input[name="minThroughput"]').val();
			} else {
				throughput = whatIfDialog.find('input[name="recThroughput"]').val();
			}

			this.networkRatio = (((this.getMinBandwidth() / throughput) * testingWindow) / this.testStarts) * 100;
			if (isNaN(this.networkRatio) || !isFinite(this.networkRatio)) {
				this.networkRatio = 0;
			} else {
				this.networkRatio = parseInt(this.networkRatio.toFixed(0));
			}

			var percentElement = whatIfDialog.find('[data-percent-ready="networkPercentStudentsTestable"]');
			var bar = percentElement.find('.progress[data-percent="' + this.type + '"] > .bar');
			resetBar(bar);
			updateBar(bar, this.networkRatio);
			return this.networkRatio;
		},
		determineMostConstrained : function() {
			var mostConstrained = whatIfDialog.find('[data-most-constrained] [data-constrained="' + this.type + '"]');
			if (this.deviceRatio == this.networkRatio) {
				mostConstrained.text('Both Equally');
			} else if (parseInt(this.deviceRatio) >= 100 && parseInt(this.networkRatio) >= 100) {
				mostConstrained.text('Both Equally');
			} else if (parseInt(this.deviceRatio) < parseInt(this.networkRatio)) {
				mostConstrained.text('Compliant Devices');
			} else if (parseInt(this.deviceRatio) > parseInt(this.networkRatio)) {
				if (this.networkConstraint == 'both') {
					mostConstrained.text('Internet and Internal Network Bandwidth');
				} else if (this.networkConstraint == 'network') {
					mostConstrained.text('Internal Network Bandwidth');
				} else {
					mostConstrained.text('Internet Bandwidth');
				}
			}
		},
		getMinBandwidth : function() {
			var network = this.networkSpeed * ((100 - this.networkUtilization) / 100) * 1000;
			var internet = this.internetSpeed * ((100 - this.internetUtilization) / 100) * 1000;

			if (internet == network) {
				this.networkConstraint = 'both';
				return network;
			} else if (internet > network) {
				this.networkConstraint = 'network';
				return network;
			} else {
				this.networkConstraint = 'internet';
				return internet;
			}
		},
		getTestingWindow : function() {
			var scopeWindow;
			if (requirements.val() == 'MINIMUM') {
				scopeWindow = parseInt(whatIfDialog.find('input[name="minTestingWindow"]').val());
			} else {
				scopeWindow = parseInt(whatIfDialog.find('input[name="recTestingWindow"]').val());
			}

			if (this.testingWindowLength < scopeWindow) {
				return this.testingWindowLength;
			}
			return scopeWindow;
		}
	}

	whatIfDialog.on('change', '.what-if input', function(event) {
		var $element = $(event.currentTarget);
		if ($element.prop('defaultValue') != $element.val()) {
			$element.addClass('input-modified');
		} else {
			$element.removeClass('input-modified');
		}
	});

	whatIfDialog.on('change', '.what-if select', function(event) {
		var $element = $(event.currentTarget);
		var defaultValue = $element.children().filter(function(index) {
			var $option = $(this);
			return $option.prop('defaultSelected');
		});
		if (defaultValue.attr('value') != $element.val()) {
			$element.addClass('input-modified');
		} else {
			$element.removeClass('input-modified');
		}
	});

	whatIfDialog.find('.what-if-info').tooltip();

	$('.data-grid-object-alpha').on('click', 'a[data-what-if="open"]', function(event) {
		event.preventDefault();
		var anchor = $(event.currentTarget);
		var orgId = anchor.data('org');
		var snapshotId = anchor.data('snapshot');
		whatIfDialog.find('input[name="orgId"]').attr('value', orgId);
		whatIfDialog.find('input[name="snapshotId"]').attr('value', snapshotId);

		requirements.closest('form').trigger('reset');
		var xhr = updateDialog(snapshotId, orgId);
		xhr.done(function() {
			whatIfDialog.dialog('open');
		});
	});

	function updateDialog(snapshotId, orgId) {
		reset();
		var options = {
			url : ctx + '/services/rest/snapshots/' + snapshotId + '/' + orgId,
			data : requirements.serializeArray()
		}
		var exportLink = whatIfDialog.find('a[data-link="export"]');
		exportLink.attr('href', exportLink.data('href') + snapshotId + '/' + orgId + '/non-compliant-devices?flag='
				+ requirements.serializeArray()[0].value);

		$('input,select').removeClass('input-modified');

		var snapshotDataXhr = $.ajax(options);
		snapshotDataXhr.done(function(response) {
			var org = response.snapshotOrg;
			whatIfDialog.find('.what-if-title').text(org.orgName);

			var deviceLink = whatIfDialog.find('a[data-link="device"]');
			deviceLink.attr('href', deviceLink.data('href') + 'orgCode=' + org.orgCode + '&requirements='
					+ capitaliseFirstLetter(requirements.serializeArray()[0].value));

			var errorsElement = whatIfDialog.find('.what-if-errors');
			errorsElement.empty();
			if (org['testingTestStartCount'] == '(missing)' || org['testingTestStartCount'] == '(Not Applicable)') {
				$('<p>').addClass('text-error').text('You must provide enrollment counts in order to use this tool.')
						.appendTo(errorsElement);
			} else {
				$('input[name="testingTestStartCount"]').val(toNumber(org['testingTestStartCount']));
			}

			whatIfDialog.find('input').each(function(index, element) {
				handleInput(element, org);
			});

			whatIfDialog.find('select').each(function(index, element) {
				handleSelect(element, org);
			});

			var currentData = {};
			currentData.testingTestStartCount = $('input[name="testingTestStartCount"]').val();
			$('select[data-snapshot="current"],input[data-snapshot="current"]').each(function(index, element) {
				var $element = $(element);
				currentData[$element.attr('name')] = toNumber($element.val());
			});
			var current = new WhatIf(currentData, 'current');

			var deviceCalc = current.calcDeviceToTestTaker();
			var networkCalc = current.calcNetworkReadiness();
			current.determineMostConstrained();
		});

		return snapshotDataXhr;
	}

	whatIfDialog.on('change', '#whatIfRequirements', function(event) {
		var orgId = whatIfDialog.find('input[name="orgId"]').val();
		var snapshotId = whatIfDialog.find('input[name="snapshotId"]').val();
		updateDialog(snapshotId, orgId);
	});

	whatIfDialog.on('click', 'button[name="calculate"]', function(event) {
		var whatIfData = {};
		whatIfData.testingTestStartCount = $('input[name="testingTestStartCount"]').val();
		$('select[data-snapshot="what-if"],input[data-snapshot="what-if"]').each(function(index, element) {
			var $element = $(element);
			whatIfData[$element.attr('name')] = toNumber($element.val());
		});
		var whatIf = new WhatIf(whatIfData, 'what-if');
		whatIfDialog.find('[data-most-constrained] [data-constrained="what-if"]').empty();

		var deviceCalc = whatIf.calcDeviceToTestTaker();
		var networkCalc = whatIf.calcNetworkReadiness();
		whatIf.determineMostConstrained();
	});

	whatIfDialog.on('click', 'input[type="reset"]', function(event) {
		reset();
	});

	function reset() {
		whatIfDialog.find(
				'[data-percent-ready="testTakerPercentStudentsTestable"] .progress[data-percent="what-if"] > .bar')
				.css('width', '0').empty();
		whatIfDialog.find(
				'[data-percent-ready="networkPercentStudentsTestable"] .progress[data-percent="what-if"] > .bar').css(
				'width', '0').empty();
		whatIfDialog.find('[data-most-constrained] [data-constrained="what-if"]').empty();
		whatIfDialog.removeData('most-constrained');
		whatIfDialog.find('input,select').removeClass('input-modified');
	}

	function resetBar($bar) {
		$bar.removeClass('report-level1').removeClass('report-level2');
		$bar.removeClass('report-level3').removeClass('report-level4');
		$bar.css('width', 0).empty();
	}

	function updateBar($bar, percent) {
		var thisPercent = percent;
		if (isNaN(thisPercent)) {
			thisPercent = 0;
		}
		if (thisPercent < 26) {
			$bar.addClass('report-level1');
		} else if (thisPercent < 51) {
			$bar.addClass('report-level2');
		} else if (thisPercent < 76) {
			$bar.addClass('report-level3');
		} else {
			$bar.addClass('report-level4');
		}

		$bar.css('width', thisPercent > 100 ? '100%' : thisPercent + '%').text(formatRatio(thisPercent));
	}

	function getWhatIfValue(name) {
		return whatIfDialog.find('input[name="' + name + '"]:not(:disabled)').val();
	}

	function getWhatIfSelectValue(name) {
		return whatIfDialog.find('select[name="' + name + '"]:not(:disabled)').val();
	}

	function getSnapshotValue(name) {
		return whatIfDialog.find('input[name="' + name + '"]:disabled').val();
	}

	function getSnapshotSelectValue(name) {
		return whatIfDialog.find('select[name="' + name + '"]:disabled').val();
	}

	function formatRatio(number) {
		if (number > 100) {
			return '>100%';
		}
		return number + '%';
	}

	function isMissing(someString) {
		if (typeof someString != 'string') {
			return false;
		}
		return someString.indexOf('missing') != -1;
	}

	function isTBD(someString) {
		if (typeof someString != 'string') {
			return false;
		}
		return someString.indexOf('TBD') != -1;
	}

	function isNotApplicable(someString) {
		if (typeof someString != 'string') {
			return false;
		}
		return someString.indexOf('Not Applicable') != -1;
	}

	function toNumber(aVar) {
		if (typeof aVar == 'string') {
			var parsed = parseInt(aVar);
			if (isNaN(parsed)) {
				return null;
			}
			return parsed;
		}
		return aVar;
	}

	function handleInput(element, response) {
		var $element = $(element);
		if (!($element.attr('type') == 'submit' || $element.attr('type') == 'reset' || $element.attr('type') == 'hidden')) {
			var val = response[$element.attr('name')];
			if (typeof val == 'string') {
				val = val.replace('%', '');
			}

			if ($element.attr('type') == 'number' && (isMissing(val) || isTBD(val))) {
				val = "";
			}
			$element.attr('value', val);
			$element.prop('defaultValue', val);
		}
	}

	function handleSelect(element, response) {
		var $element = $(element);
		if ($element.attr('id') == 'whatIfRequirements') {
			return;
		}
		$element.find('option').removeAttr('selected');
		var val = response[$element.attr('name')];
		$element.children().each(function(index, element) {
			var $option = $(element);
			if ($option.text() == val) {
				$option.attr('selected', 'selected');
				$option.prop('defaultSelected', true);
			}
		});

		if ($element.find('option[selected]').length == 0) {
			$element.children().first().attr('selected', 'selected');
		}
	}

	function capitaliseFirstLetter(string) {
		return string.charAt(0).toUpperCase() + string.toLowerCase().slice(1);
	}
});