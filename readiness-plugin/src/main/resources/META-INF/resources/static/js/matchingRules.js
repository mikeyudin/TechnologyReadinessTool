function JulKeys() {
	this.length = 0;
	this.items = new Array();

	this.addItem = function(in_key, in_value) {
		if (typeof (in_value) != 'undefined') {
			if (typeof (this.items[in_key]) == 'undefined') {
				this.length++;
				this.items[in_key] = new Array();
				this.items[in_key].push(in_value);
			} else {
				this.items[in_key].push(in_value);
			}
		}
	};

	this.getKeysToString = function() {
		var retval = '';
		var firstDone = false;
		for ( var i in this.items) {
			if (firstDone)
				retval += "," + i;
			else {
				firstDone = true;
				retval = i;
			}
		}
		return retval;
	};

	this.getKeyValueToString = function() {
		var retval = '';
		var firstDone = false;
		for ( var i in this.items) {
			var valuepair = "[" + i + "," + this.items[i] + "]";
			if (firstDone)
				retval += "," + valuepair;
			else {
				firstDone = true;
				retval = valuepair;
			}
		}
		return retval;
	};

	this.getKeysAtLevel = function(level) {
		var retArray = new Array();
		for ( var i in this.items) {
			if ((i.indexOf("_") == -1) && (level == 1))
				retArray.push(i);
			else {
				var keyArray = i.split("_");
				if (keyArray.length == level)
					retArray.push(i);
			}
		}
		return retArray;
	};

	this.getKeysAtLevelWith = function(level, subKey) {
		if (level == 1)
			return undefined;

		var retArray = new Array();
		for ( var i in this.items) {
			var keyArray = i.split("_");
			if ((keyArray.length == level) && (i.indexOf(subKey) != -1))
				retArray.push(i);
		}
		return retArray;
	};

	this.getValues = function(aKey) {
		return this.items[aKey];
	};

	this.getValuesToString = function(aKey) {
		var valuesArray = this.getValues(aKey);
		var retval = '';
		var firstDone = false;
		for ( var val in valuesArray) {
			if (firstDone)
				retval += "," + valuesArray[val];
			else {
				firstDone = true;
				retval = valuesArray[val];
			}
		}
		return retval;
	};

}// JulKeys

function deviceProcessorValue2(selectBoxID, cpuLowerCase) {
	var julretval = '-1';

	var $chipSelect = $(selectBoxID);
	if ($chipSelect.length > 0) {
		var $chipOptions = $('option', $chipSelect);
		var optionKeys = new JulKeys();

		$chipOptions.each(function() {
			var opttext = $(this).text().toLowerCase();
			var splitText = opttext.split(" ");
			var subSplit_lvl2;
			var subSplit_lvl3;
			var subSplit_lvl4;
			var rollingKey = '';
			var sp = 0;
			var sp3 = 0;
			var sp4 = 0;
			if (splitText[0].length > 0) {
				optionKeys.addItem(splitText[0], $(this).val());
				if (splitText.length >= 2) {
					if (splitText[1].indexOf("/") != -1) {
						subSplit_lvl2 = splitText[1].split("/");
						for (sp = 0; sp < subSplit_lvl2.length; sp++) {
							rollingKey = splitText[0] + "_" + subSplit_lvl2[sp];
							optionKeys.addItem(rollingKey, $(this).val());
						}
					} else {
						rollingKey = splitText[0] + "_" + splitText[1];
						optionKeys.addItem(rollingKey, $(this).val());
					}
				}
				if (splitText.length >= 3) {
					if ((subSplit_lvl2 == undefined) && (splitText[2].indexOf("/") == -1)) {
						rollingKey = splitText[0] + "_" + splitText[1] + "_" + splitText[2];
						optionKeys.addItem(rollingKey, $(this).val());
					} else if ((subSplit_lvl2 != undefined) && (splitText[2].indexOf("/") != -1)) {
						for (sp = 0; sp < subSplit_lvl2.length; sp++) {
							subSplit_lvl3 = splitText[2].split("/");
							for (sp3 = 0; sp3 < subSplit_lvl3.length; sp3++) {
								rollingKey = splitText[0] + "_" + subSplit_lvl2[sp] + "_" + subSplit_lvl3[sp3];
								optionKeys.addItem(rollingKey, $(this).val());
							}
						}
					} else if (subSplit_lvl2 == undefined) {
						subSplit_lvl3 = splitText[2].split("/");
						for (sp = 0; sp < subSplit_lvl3.length; sp++) {
							rollingKey = splitText[0] + "_" + splitText[1] + "_" + subSplit_lvl3[sp];
							optionKeys.addItem(rollingKey, $(this).val());
						}
					} else {
						for (sp = 0; sp < subSplit_lvl2.length; sp++) {
							rollingKey = splitText[0] + "_" + subSplit_lvl2[sp] + "_" + splitText[2];
							optionKeys.addItem(rollingKey, $(this).val());
						}
					}

				}
				if (splitText.length >= 4) {
					if ((subSplit_lvl2 == undefined) && (subSplit_lvl3 == undefined)
							&& (splitText[3].indexOf("/") == -1)) {
						rollingKey = splitText[0] + "_" + splitText[1] + "_" + splitText[2] + "_" + splitText[3];
						optionKeys.addItem(rollingKey, $(this).val());
					} else if ((subSplit_lvl2 == undefined) && (subSplit_lvl3 == undefined)
							&& (splitText[3].indexOf("/") != -1)) {
						subSplit_lvl4 = splitText[3].split("/");
						for (sp = 0; sp < subSplit_lvl4.length; sp++) {
							rollingKey = splitText[0] + "_" + splitText[1] + "_" + splitText[2] + "_"
									+ subSplit_lvl4[sp];
							optionKeys.addItem(rollingKey, $(this).val());
						}
					} else if ((subSplit_lvl2 == undefined) && (subSplit_lvl3 != undefined)
							&& (splitText[3].indexOf("/") != -1)) {
						subSplit_lvl4 = splitText[3].split("/");
						for (sp3 = 0; sp3 < subSplit_lvl3.length; sp3++) {
							for (sp4 = 0; sp4 < subSplit_lvl4.length; sp4++) {
								rollingKey = splitText[0] + "_" + splitText[1] + "_" + subSplit_lvl3[sp3] + "_"
										+ subSplit_lvl4[sp4];
								optionKeys.addItem(rollingKey, $(this).val());
							}
						}
					} else if ((subSplit_lvl2 != undefined) && (subSplit_lvl3 != undefined)
							&& (splitText[3].indexOf("/") != -1)) {
						subSplit_lvl4 = splitText[3].split("/");
						for (sp = 0; sp < subSplit_lvl2.length; sp++) {
							for (sp3 = 0; sp3 < subSplit_lvl3.length; sp3++) {
								for (sp4 = 0; sp4 < subSplit_lvl4.length; sp4++) {
									rollingKey = splitText[0] + "_" + subSplit_lvl2[sp] + "_" + subSplit_lvl3[sp3]
											+ "_" + subSplit_lvl4[sp4];
									optionKeys.addItem(rollingKey, $(this).val());
								}
							}
						}
					} else if ((subSplit_lvl2 != undefined) && (subSplit_lvl3 != undefined)
							&& (splitText[3].indexOf("/") == -1)) {
						for (sp = 0; sp < subSplit_lvl2.length; sp++) {
							for (sp3 = 0; sp3 < subSplit_lvl3.length; sp3++) {
								rollingKey = splitText[0] + "_" + subSplit_lvl2[sp] + "_" + subSplit_lvl3[sp3] + "_"
										+ splitText[3];
								optionKeys.addItem(rollingKey, $(this).val());
							}
						}
					} else if ((subSplit_lvl2 != undefined) && (subSplit_lvl3 == undefined)
							&& (splitText[3].indexOf("/") == -1)) {
						for (sp = 0; sp < subSplit_lvl2.length; sp++) {
							rollingKey = splitText[0] + "_" + subSplit_lvl2[sp] + "_" + splitText[2] + "_"
									+ splitText[3];
							optionKeys.addItem(rollingKey, $(this).val());
						}
					} else if ((subSplit_lvl2 == undefined) && (subSplit_lvl3 != undefined)
							&& (splitText[3].indexOf("/") == -1)) {
						for (sp3 = 0; sp3 < subSplit_lvl3.length; sp3++) {
							rollingKey = splitText[0] + "_" + splitText[1] + "_" + subSplit_lvl3[sp3] + "_"
									+ splitText[3];
							optionKeys.addItem(rollingKey, $(this).val());
						}
					} else if ((subSplit_lvl2 != undefined) && (subSplit_lvl3 == undefined)
							&& (splitText[3].indexOf("/") != -1)) {
						subSplit_lvl4 = splitText[3].split("/");
						for (sp = 0; sp < subSplit_lvl2.length; sp++) {
							for (sp4 = 0; sp4 < subSplit_lvl4.length; sp4++) {
								rollingKey = splitText[0] + "_" + subSplit_lvl2[sp] + "_" + splitText[2] + "_"
										+ subSplit_lvl4[sp4];
								optionKeys.addItem(rollingKey, $(this).val());
							}
						}
					}
				}
			}
		});

		var aMatchedKey = getProcessor_exactMatch(optionKeys, cpuLowerCase, 0, "other");
		var valuesForKey = optionKeys.getValues(aMatchedKey);
		valuesForKey.sort();
		julretval = valuesForKey[0];
	}
	return parseInt(julretval);
}

function getProcessor_exactMatch(jul_keys, cpu, level, subKey) {
	var nextLevel = level;
	if (level != 1) {
		if (level == 0) {
			var primaryKeys = jul_keys.getKeysAtLevel(1);
			var primaryVal = "none";
			for ( var pk in primaryKeys) {
				if (cpu.indexOf(primaryKeys[pk]) != -1) {
					primaryVal = primaryKeys[pk];
					break;
				}
			}
			if (primaryVal != "none") {
				return getProcessor_exactMatch(jul_keys, cpu, 4, primaryVal);
			}
		} else if (level > 1) {
			var secondaryKeys = jul_keys.getKeysAtLevelWith(level, subKey);
			if (secondaryKeys.length > 0) {
				secondaryKeys.sort();
				var secondaryVal = "none";
				for ( var sk in secondaryKeys) {
					var skArray = secondaryKeys[sk].split("_");
					var itsaMatch = true;
					for ( var ska in skArray) {
						var formattedKey = (skArray[ska].length > 1) ? skArray[ska] : " " + skArray[ska] + " ";
						if (cpu.indexOf(formattedKey) == -1) {
							itsaMatch = false;
							break;
						}
					}
					if (itsaMatch) {
						secondaryVal = secondaryKeys[sk];
						break;
					}
				}
				if (secondaryVal == "none") {
					nextLevel -= 1;
					return getProcessor_exactMatch(jul_keys, cpu, nextLevel, subKey);
				}
				return secondaryVal;
			}
			nextLevel -= 1;
			return getProcessor_exactMatch(jul_keys, cpu, nextLevel, subKey);
		}
	}
	return subKey;
}

function getProcessor_approximateMatch(jul_keys, cpu, level, subKey) {
	var nextLevel = level;
	if (level == 1) {
		var primaryKeys = jul_keys.getKeysAtLevel(1);
		var primaryVal = "none";
		for ( var pk in primaryKeys) {
			if (cpu.indexOf(primaryKeys[pk]) != -1) {
				primaryVal = primaryKeys[pk];
				break;
			}
		}
		if (primaryVal != "none") {
			nextLevel += 1;
			return getProcessorValue(jul_keys, cpu, nextLevel, primaryVal);
		}
	} else if (level < 5) {
		var secondaryKeys = jul_keys.getKeysAtLevelWith(level, subKey);
		if (secondaryKeys.length > 0) {
			secondaryKeys.sort();
			var secondaryVal = "none";
			for ( var sk in secondaryKeys) {
				var skArray = secondaryKeys[sk].split("_");
				var itsaMatch = true;
				for ( var ska in skArray) {
					var formattedKey = (skArray[ska].length > 1) ? skArray[ska] : " " + skArray[ska] + " ";
					if (cpu.indexOf(formattedKey) == -1) {
						itsaMatch = false;
						break;
					}
				}
				if (itsaMatch) {
					secondaryVal = secondaryKeys[sk];
					break;
				}
			}
			if (secondaryVal != "none") {
				nextLevel += 1;
				return getProcessorValue(jul_keys, cpu, nextLevel, secondaryVal);
			}
		}
	}
	return subKey;
}
