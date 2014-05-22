'use strict';
var iHaveRunAlready = false;
var comp_errors = new Array();
var isIE = (navigator.appVersion.indexOf("MSIE") != -1) ? true : false;
var isWin = (navigator.appVersion.toLowerCase().indexOf("win") != -1) ? true : false;
var isOpera = (navigator.userAgent.indexOf("Opera") != -1) ? true : false;
var isIpad = (navigator.userAgent.indexOf("iPad") != -1) ? true : false;
var isAndroid = (navigator.userAgent.indexOf("Android") != -1) ? true : false;
var browsername = navigator.appVersion;
var system_name;
var OS;
var OSVersion;
var OSName;
var IS_VERTICAL = 0;

function CCStartIcon_click() {
	if (iHaveRunAlready) {
		alert("Please use the reset button on the top right to reset auto-detect and run again.");
	} else {
		$("#CCStartIcon").attr('src', ctx + '/static/images/stop_icon.png');

		var ap_name = getHostName();
		var ap_location = getHostAddress();
		var deviceNameAuto = '';
		var deviceLocationAuto = '';

		if (ap_name != "not found") {
			$("#create_device_name").attr('value', ap_name);
			deviceNameAuto = '<span style="color:#00478f; float:right">' + ap_name
					+ '</span>';
		}

		if (ap_location != "not found") {
			$("#create_device_location").attr('value', ap_location);
			deviceLocationAuto = '<span style="color:#00478f; float:right">'
					+ ap_location + '</span>';
		}

		$("#create_device_name").after(deviceNameAuto);
		$("#create_device_location").after(deviceLocationAuto);
		$("#CCSectionStatusIcon").attr('src', ctx + '/static/images/sectionStatus_Icon_started.gif');
		$("#CCSectionStatusString").css('width', 430);
		$("#CCSectionStatusString").html("Gathering Results...");
		$("#CCSectionStatusString").css("color", "#000");

		OSName = getOSName();
		OS = find_OS(OSName);
		OSVersion = getOSVersion();

		detectBrowser();
		detectMemory();
		detectOperatingSystem();
		detectProcessor();
		detectScreenResolution();

		if (comp_errors.length > 0) {
			$("#CCSectionStatusIcon").attr('src', ctx + '/static/images/X_Large.gif');
			$("#CCSectionStatusString").css('width', 410);
			$("#CCSectionStatusString").html(comp_errors.length + " Checks Failed.");
			$("#CCSectionStatusString").css("color", "#721313");
		} else {
			$("#CCSectionStatusIcon").attr('src', ctx + '/static/images/Check_Large.gif');
			$("#CCSectionStatusString").css('width', 470);
			$("#CCSectionStatusString").html("Auto analysis completed.");
			$("#CCSectionStatusString").css("color", "#18401B");
			$("#create_device_count").val('1');
		}

		$("#CCStartIcon").attr('src', ctx + '/static/images/autoPop_done.png');
		iHaveRunAlready = true;
	}
}

function detectBrowser() {
	var browser = "";
	var browser_ver_num = 0;
	if (isIpad) {
		browser = getMobileBrowserInfo();
	} else {
		browser_ver_num = BrowserDetect.version;
		browser = BrowserDetect.browser + " " + browser_ver_num;
	}

	var deviceBrowserAuto = '<span style="color:#00478f; float:right">' + browser
			+ '</span>';
	$("#create_device_browser").after(deviceBrowserAuto);

	var $browserSelect = $("#create_device_browser");
	if ($browserSelect.length > 0) {
		var $browserOptions = $('option', $browserSelect);
		var browserVals = [];
		$browserOptions.each(function() {
			browserVals.push({
				val : $(this).val(),
				text : $(this).text()
			});
		});
		var browserFound = "null";
		for (var browi = 0; browi < browserVals.length; browi++) {
			var browserVal_Lower = browserVals[browi].text.toLowerCase();
			if (browser.toLowerCase().indexOf("explorer") != -1 && browserVal_Lower.indexOf("explorer") != -1) {
				if (browserVal_Lower.indexOf(browser_ver_num) != -1) {
					browserFound = browserVals[browi].val;
					break;
				}
			} else if (browser.toLowerCase().indexOf("firefox") != -1 && browserVal_Lower.indexOf("firefox") != -1) {
				if (browserVal_Lower.indexOf(browser_ver_num) != -1 || browserVal_Lower.indexOf("3") != -1
						&& parseFloat(browser_ver_num) < 3.0 || browserVal_Lower.indexOf("4") != -1
						&& parseFloat(browser_ver_num) > 4.0) {
					browserFound = browserVals[browi].val;
					break;
				}
			} else if (browser.toLowerCase().indexOf("safari") != -1 && browserVal_Lower.indexOf("safari") != -1) {
				browserFound = browserVals[browi].val;
				break;
			} else if (browser.toLowerCase().indexOf("chrome") != -1 && browserVal_Lower.indexOf("chrome") != -1) {
				browserFound = browserVals[browi].val;
				break;
			}

		}
		if (browserFound != "null") {
			$("#create_device_browser").val(browserFound);
		}
	}
}

function detectMemory() {
	var ramInfo = getRAMInfo();
	var deviceMemoryAuto = "";
	if (ramInfo != null) {
		deviceMemoryAuto = '<span style="color:#00478f; float:right">' + ramInfo
				+ '</span>';
		$("#create_device_memory").after(deviceMemoryAuto);
		var $ramSelect = $("#create_device_memory");
		var notAvailableValue = '';
		if ($ramSelect.length > 0) {
			var $ramOptions = $('option', $ramSelect);
			var ramVals = [];
			$ramOptions.each(function() {
				var optTxt = "";
				optTxt = $(this).text().toLowerCase();
				if (optTxt.length > 0) {
					var optMult = optTxt.indexOf("gb") != -1 ? 1000 : 1;
					var stIndx = 0;
					var optIncrement = 0;
					if (optTxt.indexOf("<") != -1 || optTxt.indexOf(">") != -1) {
						stIndx = 1;
						optIncrement = optTxt.indexOf("<") != -1 ? -1 : 1;
					}
					var intOptRam = parseInt(optTxt.substring(stIndx, optTxt.indexOf(" ", stIndx))) + optIncrement;
					intOptRam *= optMult;
					ramVals.push({
						val : intOptRam,
						optval : $(this).val()
					});
				} else {
					notAvailableValue = $(this).val();
				}
			});

				ramVals.sort(function(a, b) {
					return b.val - a.val;
				});
				var ramOPTfound = false;
				for (var rami = 0; rami < ramVals.length; rami++) {
					if (ramInfo >= ramVals[rami].val) {
						$("#create_device_memory").val(ramVals[rami].optval);
						ramOPTfound = true;
						break;
					}
				}
				if (!ramOPTfound) {
					$("#create_device_memory").val(ramVals[(ramVals.length - 1)].optval);
				}
		}
	} else {
		var ramErrMsg = "RAM not found.";
		comp_errors.push(ramErrMsg);
		deviceMemoryAuto = '<span class="text-error" style="float:right">' + ramErrMsg + '</span>';
		$("#create_device_memory").after(deviceMemoryAuto);
	}
}

function detectOperatingSystem() {
	if (isIpad || isAndroid) {
		system_name = getMobileOSInfo();
	} else {
		system_name = check_OS(OS, OSVersion);
	}
	var deviceOSAuto = "";
	if (system_name != null) {
		if (getOS64Bit() == 1) {
			system_name = system_name + " 64 bit";
		}
		deviceOSAuto = '<span style="color:#00478f; float:right">' + system_name
				+ '</span>';
		$("#create_device_operatingSystem").after(deviceOSAuto);
		var sysNameLower = system_name.toLowerCase();

		var $opsysSelect = $("#create_device_operatingSystem");
		if ($opsysSelect.length > 0) {
			var $opsysOptions = $('option', $opsysSelect);
			var opsysVals = [];
			$opsysOptions.each(function() {
				opsysVals.push({
					val : $(this).val(),
					text : $(this).text()
				});
			});

			var otherVal = new Object();
			var opSysValue = "null";
			for (var opsysi = 0; opsysi < opsysVals.length; opsysi++) {
				var opsysVal_Lower = opsysVals[opsysi].text.toLowerCase();
				if (opsysVal_Lower.indexOf("android") != -1 && opsysVal_Lower.indexOf("other") != -1) {
					otherVal["android"] = opsysVals[opsysi].val;
				} else if (opsysVal_Lower.indexOf("ios") != -1 && opsysVal_Lower.indexOf("other") != -1) {
					otherVal["ios"] = opsysVals[opsysi].val;
				} else if (opsysVal_Lower.indexOf("linux") != -1 && opsysVal_Lower.indexOf("other") != -1) {
					otherVal["linux"] = opsysVals[opsysi].val;
				} else if (opsysVal_Lower.indexOf("mac") != -1 && opsysVal_Lower.indexOf("other") != -1) {
					otherVal["mac"] = opsysVals[opsysi].val;
				} else if (opsysVal_Lower.indexOf("windows") != -1 && opsysVal_Lower.indexOf("other") != -1) {
					otherVal["windows"] = opsysVals[opsysi].val;
				} else if (opsysVal_Lower.indexOf("other os") != -1) {
					otherVal["otheros"] = opsysVals[opsysi].val;
				} else if (sysNameLower.indexOf("android") != -1 && opsysVal_Lower.indexOf("android") != -1) {
					if (sysNameLower.indexOf("3.") != -1 && opsysVal_Lower.indexOf("3.") != -1
							|| sysNameLower.indexOf("4.") != -1 && opsysVal_Lower.indexOf("4.") != -1) {
						opSysValue = opsysVals[opsysi].val;
					}
				} else if (sysNameLower.indexOf("ios") != -1 && opsysVal_Lower.indexOf("ios") != -1) {
					if (sysNameLower.indexOf("4.") != -1 && opsysVal_Lower.indexOf("4.") != -1
							|| sysNameLower.indexOf("5.") != -1 && opsysVal_Lower.indexOf("5.") != -1
							|| sysNameLower.indexOf("6.") != -1 && opsysVal_Lower.indexOf("6.") != -1) {
						opSysValue = opsysVals[opsysi].val;
					}
				} else if (opsysVal_Lower.indexOf("linux") != -1 && opsysVal_Lower.indexOf("ubuntu") != -1) {
					if (sysNameLower.indexOf("ubuntu") != -1) {
						var ubuntuStartIndex = sysNameLower.indexOf(" ");
						var ubuntuVer = 0.0;
						ubuntuVer = parseFloat(sysNameLower.substring(ubuntuStartIndex));
						if (opsysVal_Lower.indexOf("v4") != -1 && ubuntuVer < 9 || opsysVal_Lower.indexOf("v9") != -1
								&& ubuntuVer < 13 || opsysVal_Lower.indexOf("v13") != -1 && ubuntuVer > 12.99) {
							opSysValue = opsysVals[opsysi].val;
						}
					}
				} else if (opsysVal_Lower.indexOf("linux") != -1 && opsysVal_Lower.indexOf("fedora") != -1) {
					if (sysNameLower.indexOf("fedora") != -1) {
						var fedoraStartIndex = sysNameLower.indexOf(" ");
						var fedoraVer = 0.0;
						fedoraVer = parseFloat(sysNameLower.substring(fedoraStartIndex));
						if (opsysVal_Lower.indexOf("v1") != -1 && fedoraVer < 6 || opsysVal_Lower.indexOf("v6") != -1
								&& fedoraVer < 16 || opsysVal_Lower.indexOf("v16") != -1 && fedoraVer > 15.99) {
							opSysValue = opsysVals[opsysi].val;
						}
					}
				} else if (opsysVal_Lower.indexOf("linux") != -1) {
					if (sysNameLower.indexOf("suse") != -1 && opsysVal_Lower.indexOf("suse") != -1) {
						opSysValue = opsysVals[opsysi].val;
					}
				} else if (sysNameLower.indexOf("windows server") != -1
						&& opsysVal_Lower.indexOf("windows server") != -1) {
					if (sysNameLower.indexOf(" 2003") != -1 && opsysVal_Lower.indexOf("2003") != -1
							|| sysNameLower.indexOf(" 2008") != -1 && opsysVal_Lower.indexOf("2008") != -1
							|| sysNameLower.indexOf(" 2012") != -1 && opsysVal_Lower.indexOf("2012") != -1) {
						opSysValue = opsysVals[opsysi].val;
					}
				} else if (sysNameLower.indexOf("windows") != -1 && opsysVal_Lower.indexOf("windows") != -1
						&& sysNameLower.indexOf(" xp") != -1 && opsysVal_Lower.indexOf("xp") != -1) {
					if (sysNameLower.indexOf(" 2") != -1 && opsysVal_Lower.indexOf("sp2") != -1
							|| sysNameLower.indexOf(" 3") != -1 && opsysVal_Lower.indexOf("sp3") != -1) {
						opSysValue = opsysVals[opsysi].val;
					}
				} else if (sysNameLower.indexOf("windows") != -1 && opsysVal_Lower.indexOf("windows") != -1) {
					if (sysNameLower.indexOf(" 7") != -1 && opsysVal_Lower.indexOf("7") != -1
							|| sysNameLower.indexOf(" 8") != -1 && opsysVal_Lower.indexOf("8") != -1
							|| sysNameLower.indexOf(" rt") != -1 && opsysVal_Lower.indexOf("rt") != -1
							|| sysNameLower.indexOf(" vista") != -1 && opsysVal_Lower.indexOf("vista") != -1) {
						opSysValue = opsysVals[opsysi].val;
					}
				} else if (sysNameLower.indexOf("mac") != -1 && opsysVal_Lower.indexOf("mac") != -1) {
					var osxStartIndex = sysNameLower.indexOf("10.");
					var osxVer = 0.0;
					osxVer = parseFloat(sysNameLower.substring(osxStartIndex, (osxStartIndex + 4)));
					if (opsysVal_Lower.indexOf("10.1") != -1 && osxVer < 10.4 || opsysVal_Lower.indexOf("10.4.4") != -1
							&& osxVer < 10.5 || opsysVal_Lower.indexOf("10.5") != -1 && osxVer < 10.6
							|| opsysVal_Lower.indexOf("10.6") != -1 && osxVer < 10.7
							|| opsysVal_Lower.indexOf("10.7") != -1 && osxVer < 10.8
							|| opsysVal_Lower.indexOf("10.8") != -1 && osxVer < 10.9
							|| opsysVal_Lower.indexOf("10.9") != -1 && osxVer < 10.10) {
						opSysValue = opsysVals[opsysi].val;
					}
				} else if (sysNameLower.indexOf("chrome") != -1 && opsysVal_Lower.indexOf("chrome") != -1) {
					var chromeStartIndex = sysNameLower.indexOf(" ") + 1;
					var chromeVer = 0.0;
					chromeVer = parseFloat(sysNameLower.substring(chromeStartIndex, (chromeStartIndex + 4)));
					if (opsysVal_Lower.indexOf("v18") != -1 && chromeVer < 19 || opsysVal_Lower.indexOf("v19") != -1
							&& chromeVer > 18.99) {
						opSysValue = opsysVals[opsysi].val;
					}
				}
				if (opSysValue != "null") {
					break;
				}
			}
			if (opSysValue != "null") {
				$("#create_device_operatingSystem").val(opSysValue);
			} else {
				if (sysNameLower.indexOf("android") != -1) {
					opSysValue = otherVal["android"];
				} else if (sysNameLower.indexOf("ios") != -1) {
					opSysValue = otherVal["ios"];
				} else if (sysNameLower.indexOf("linux") != -1) {
					opSysValue = otherVal["linux"];
				} else if (sysNameLower.indexOf("mac") != -1) {
					opSysValue = otherVal["mac"];
				} else if (sysNameLower.indexOf("windows") != -1) {
					opSysValue = otherVal["windows"];
				} else {
					opSysValue = otherVal["otheros"];
				}
				$("#create_device_operatingSystem").val(opSysValue);
			}
		}
	} else {
		var OSErrMsg = "OS not supported.";
		comp_errors.push(OSErrMsg);
		deviceOSAuto = '<span class="text-error" style="float:right">' + OSErrMsg + '</span>';
		$("#create_device_operatingSystem").after(deviceOSAuto);
	}
}

function detectProcessor() {
	var cpuInfo = getCPUInfo();
	var deviceCPUAuto = "";

	if (cpuInfo != null) {
		deviceCPUAuto = '<span style="color:#00478f; float:right">'
				+ trimProcessorName(cpuInfo) + '</span>';
		$("#create_device_processor").after(deviceCPUAuto);
		var cpuLower = cpuInfo.toLowerCase();

		var chip_value_found = deviceProcessorValue2("#create_device_processor", cpuLower);
		if (chip_value_found != -1) {
			$("#create_device_processor").val(chip_value_found);
		}
	} else {
		var cpuErrMsg = "Unable to detect CPU info.";
		comp_errors.push(cpuErrMsg);
		deviceCPUAuto = '<span class="text-error" style="float:right">' + cpuErrMsg + '</span>';
		$("#create_device_processor").after(deviceCPUAuto);
	}
}

function detectScreenResolution() {
	var width = 0;
	width = getScreenWidth();
	var height = 0;
	height = getScreenHeight();
	var deviceDimensionsAuto = "";
	if (height != 0 && width != 0) {
		var widthHeightOut = width + "x" + height;
		deviceDimensionsAuto = '<span style="color:#00478f; float:right">'
				+ widthHeightOut + '</span>';
		$("#create_device_screenResolution").after(deviceDimensionsAuto);

		var $scrnResSelect = $("#create_device_screenResolution");
		if ($scrnResSelect.length > 0) {
			var $scrnResOptions = $('option', $scrnResSelect);
			var scrnResVals = [];
			var minScrnWidth = 0;
			var maxScrnWidth = 0;
			var minScrnVAL = "";
			var maxScrnVAL = "";
			var widthHeightFound = false;
			$scrnResOptions.each(function() {
				var optScrnText = $(this).text().toLowerCase();
				if (optScrnText.length > 0) {
					if (optScrnText.indexOf("<") != -1) {
						minScrnWidth = parseInt(optScrnText.substring(1, optScrnText.indexOf(" ", 1)));
						minScrnVAL = $(this).val();
					} else if (optScrnText.indexOf(">") != -1) {
						maxScrnWidth = parseInt(optScrnText.substring(1, optScrnText.indexOf(" ", 1)));
						maxScrnVAL = $(this).val();
					} else {
						var optScrnWidth = optScrnText.substring(0, optScrnText.indexOf(" "));
						var optScrnHeight = optScrnText.substring((optScrnText.indexOf("x ") + 2));
						scrnResVals.push({
							val : $(this).val(),
							optwidth : optScrnWidth,
							optheight : optScrnHeight
						});
					}
				}
			});

			var strWidth = width.toString();
			var strHeight = height.toString();
			for (var scrnResi = 0; scrnResi < scrnResVals.length; scrnResi++) {
				if (scrnResVals[scrnResi].optwidth.indexOf(strWidth) > -1
						&& scrnResVals[scrnResi].optheight.indexOf(strHeight) > -1) {
					$("#create_device_screenResolution").val(scrnResVals[scrnResi].val);
					widthHeightFound = true;
					break;
				}
			}
			if (!widthHeightFound) {
				if (width <= minScrnWidth) {
					$("#create_device_screenResolution").val(minScrnVAL);
				} else if (width >= maxScrnWidth) {
					$("#create_device_screenResolution").val(maxScrnVAL);
				}
			}
		}
	} else {
		var dimensionsError = "Unable to detect Screen Resolution.";
		comp_errors.push(dimensionsError);
		deviceDimensionsAuto = '<span class="text-error" style="float:right">' + dimensionsError + '</span>';
		$("#create_device_screenResolution").after(deviceDimensionsAuto);
	}
}

function trimProcessorName(pname) {
	return pname.indexOf("@") > 0 ? pname.substring(0, pname.indexOf("@")) : pname;
}

function fixString(s) {
	var st = "";
	var splitter = s.split("&nbsp");
	for (var i = 0; i < splitter.length; i++) {
		st = st + splitter[i] + " ";
	}
	return st;

}

function getHostAddress() {
	try {
		if (isIpad || isAndroid) {
			return $('input[name="remoteAddr"]').val();
		}
		return document.getElementById('detectPluginApplet').getMaHostAddress();
	} catch (e) {
		return $('input[name="remoteAddr"]').val();
	}
}

function getHostName() {
	try {
		if (isIpad) {
			return "IPad";
		} else if (isAndroid) {
			var userA = "" + navigator.userAgent;
			if (userA.indexOf("Xoom") != -1) {
				return "Xoom";
			}
			return "Android tablet";
		} else {
			return document.getElementById('detectPluginApplet').getMaHostName();
		}
	} catch (e) {
		return "not found";
	}
}

function getMobileBrowserInfo() {
	try {
		var userA = "" + navigator.userAgent;
		var verBegin = userA.indexOf("Version/") + 8;
		var verEnd = userA.indexOf(" ", verBegin);
		var verValue = userA.substring(verBegin, verEnd);
		return "Safari " + verValue;
	} catch (e) {
		return "not found";
	}
}

function getMobileOSInfo() {
	try {
		var userA = "";
		var osBegin = 0;
		var osEnd = 0;
		var osValue = "";
		if (isIpad) {
			userA = navigator.userAgent;
			osBegin = userA.indexOf("CPU ") + 4;
			osEnd = userA.indexOf(" like", osBegin);
			osValue = userA.substring(osBegin, osEnd);
			var verNum = osValue.substring((osValue.indexOf("OS ") + 3));
			return "iOS " + verNum.replace(/_/g, ".");
		} else if (isAndroid) {
			userA = navigator.userAgent;
			osBegin = userA.indexOf("Android");
			osEnd = userA.indexOf(";", osBegin);
			osValue = userA.substring(osBegin, osEnd);
		}

		return osValue;
	} catch (e) {
		return "not found";
	}
}

function getScreenHeight() {
	var size = 0;
	try {
		if (isIpad || isAndroid) {
			if (window.orientation == IS_VERTICAL) {
				size = screen.height;
			} else {
				size = screen.width;
			}
		} else {
			size = window.screen.height;
		}
	} catch (e) {
		size = 0;
	}
	return size;
}

function getScreenWidth() {
	var size = 0;
	try {
		if (isIpad || isAndroid) {
			if (window.orientation == IS_VERTICAL) {
				size = screen.width;
			} else {
				size = screen.height;
			}
		} else {
			size = window.screen.width;
		}
	} catch (e) {
		size = 0;
	}
	return size;
}

function getCPUInfo() {
	var retval = null;
	try {
		var userA = "" + navigator.userAgent;
		if (isIpad) {
			var cpuidx = userA.indexOf("CPU ") + 4;
			var cpuBegin = userA.indexOf("like ", cpuidx) + 5;
			var cpuEnd = userA.indexOf(")", cpuBegin);
			retval = "like " + userA.substring(cpuBegin, cpuEnd);
		} else if (isAndroid) {
			if (userA.indexOf("arm") != -1) {
				retval = userA.substring(userA.indexOf("arm"), userA.indexOf(";", userA.indexOf("arm")));
			} else if (userA.indexOf("amd") != -1) {
				retval = userA.substring(userA.indexOf("amd"), userA.indexOf(";", userA.indexOf("amd")));
			} else if (userA.indexOf("intel") != -1) {
				retval = userA.substring(userA.indexOf("intel"), userA.indexOf(";", userA.indexOf("intel")));
			} else if (userA.indexOf("snapdragon") != -1) {
				retval = userA.substring(userA.indexOf("snapdragon"), userA.indexOf(";", userA.indexOf("snapdragon")));
			} else {
				retval = "unknown";
			}
		} else {
			retval = document.getElementById('detectPluginApplet').getInformationsAboutCPU();
		}
	} catch (e) {
		console.error(e);
		retval = null;
	}
	return retval;
}

function getRAMInfo() {
	try {
		if (isIpad || isAndroid) {
			return null;
		}
		return document.getElementById('detectPluginApplet').getInformationsAboutMemory();
	} catch (e) {
		console.error(e);
		return null;
	}
}

function getOSName() {
	try {
		return document.getElementById('detectPluginApplet').getOSName();
	} catch (e) {
		console.error(e);
		return null;
	}
}

function getOS64Bit() {
	try {
		return document.getElementById('detectPluginApplet').getOS64Bit();
	} catch (e) {
		console.error(e);
		return null;
	}
}

function getOSVersion() {
	try {
		return document.getElementById('detectPluginApplet').getOSVersion();
	} catch (e) {
		console.error(e);
		return null;
	}
}

var BrowserDetect = {
	init : function() {
		this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
		this.version = this.searchVersion(navigator.userAgent) || this.searchVersion(navigator.appVersion)
				|| "an unknown version";
		this.OS = this.searchString(this.dataOS) || "an unknown OS";
	},
	searchString : function(data) {
		for (var i = 0; i < data.length; i++) {
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			this.versionSearchString = data[i].versionSearch || data[i].identity;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1) {
					return data[i].identity;
				}
			} else if (dataProp) {
				return data[i].identity;
			}
		}
	},
	searchVersion : function(dataString) {
		var index = dataString.indexOf(this.versionSearchString);
		if (index == -1) {
			return;
		}
		return parseFloat(dataString.substring(index + this.versionSearchString.length + 1));
	},
	dataBrowser : [ {
		string : navigator.userAgent,
		subString : "Chrome",
		identity : "Chrome"
	}, {
		string : navigator.userAgent,
		subString : "OmniWeb",
		versionSearch : "OmniWeb/",
		identity : "OmniWeb"
	}, {
		string : navigator.vendor,
		subString : "Apple",
		identity : "Safari",
		versionSearch : "Version"
	}, {
		prop : window.opera,
		identity : "Opera"
	}, {
		string : navigator.vendor,
		subString : "iCab",
		identity : "iCab"
	}, {
		string : navigator.vendor,
		subString : "KDE",
		identity : "Konqueror"
	}, {
		string : navigator.userAgent,
		subString : "Firefox",
		identity : "Firefox"
	}, {
		string : navigator.vendor,
		subString : "Camino",
		identity : "Camino"
	}, { // for newer Netscapes (6+)
		string : navigator.userAgent,
		subString : "Netscape",
		identity : "Netscape"
	}, {
		string : navigator.userAgent,
		subString : "MSIE",
		identity : "Explorer",
		versionSearch : "MSIE"
	}, {
		string : navigator.userAgent,
		subString : "Gecko",
		identity : "Mozilla",
		versionSearch : "rv"
	}, { // for older Netscapes (4-)
		string : navigator.userAgent,
		subString : "Mozilla",
		identity : "Netscape",
		versionSearch : "Mozilla"
	} ],
	dataOS : [ {
		string : navigator.platform,
		subString : "Win",
		identity : "Windows"
	}, {
		string : navigator.platform,
		subString : "Mac",
		identity : "Mac"
	}, {
		string : navigator.userAgent,
		subString : "iPhone",
		identity : "iPhone/iPod"
	}, {
		string : navigator.platform,
		subString : "Linux",
		identity : "Linux"
	} ]

};
BrowserDetect.init();
