
/**
 * Determines which OS we are on. If it is a linux
 * system, we use the navigator.userAgent to determine
 * which flavor of linux we are using (Java does not tell
 * us this). Sets the os value.
 * 
 * @param osname - The OS name returned by java applet
 * @return String current operating system
 */
function find_OS(osname){
	var os="";
	if(osname !=null){
		osname = osname.toLowerCase();
		if((osname.indexOf("win") != -1) && (osname.indexOf("server") != -1)){
            if(osname.indexOf("2003") != -1)
			    os = "winserver2003";
            else
            if(osname.indexOf("2008") != -1)
			    os = "winserver2008";
            else
            if(osname.indexOf("2012") != -1)
			    os = "winserver2012";            
		}
        else
		if(osname.indexOf("win") != -1){
			os = "win";
		}
		else if(osname.indexOf("mac") != -1)
		{
			os="mac";
		}
		else if(osname.indexOf("chrome") != -1)
		{
			os="chrome";
		}
		else if(osname.indexOf("lin") != -1)
		{
			var checker = navigator.userAgent;
			checker = checker.toLowerCase();
			if(checker.indexOf("ubuntu") != -1){
				os = "ub";
			}
			else if(checker.indexOf("suse") != -1){
				os = "suse";
			}
			else if(checker.indexOf("red") != -1){
				os = "red";
			}
			else if(checker.indexOf("fedora") != -1){
				os = "fedora";
			}

		}
	}
	return os;
}

/**
 * Check and returns the min flash version depending
 * on which OS we are on
 * @param osname
 * @return String min flash version
 */
function check_flash(osname){
	minflashver="0";
	if(osname=="win"){
		minflashver = win_flash_ver;
	}
	else if(osname=="mac"){
		minflashver = mac_flash_ver;
	}
	else if(osname=="ub"){
		minflashver = ubuntu_flash_ver;
	}
	else if(osname=="suse"){
		minflashver = sus_flash_ver;
	}
	else if(osname=="red"){
		minflashver = redhat_flash_ver;
	}
	return minflashver;
}

/** 
 * Returns the min java version depending
 * on which OS we are on
 * @param osname
 * @return String min java version
 */
function check_java(osname){
	minjavaver = "0";
	
	if(osname=="win"){
		minjavaver = win_jre_ver;
	}
	else if(osname=="mac"){
		minjavaver = mac_jre_ver;
	}
	else if(osname=="ub"){
		minjavaver = ubuntu_jre_ver;
	}
	else if(osname="suse"){
		minjavaver = sus_jre_ver;
	}
	else if(osname=="red"){
		minjavaver = redhat_jre_ver;
	}
	return minjavaver;
}

/** 
 * Returns the min browser version depending on
 * the OS we are on and its version.
 * @param browser_name
 * @param osname
 * @param osver
 * @return String        the browser name to be displayed, 
 * 						 if null the error string is displayed.
 */
function check_browser(browser_name,osname,osver){
	var min_browser = null; 
	if(osver!=null){
		if(osname=="win"){
			//check that it is greater then xp but less then vista
			if(parseFloat(osver)>5.0 && parseFloat(osver)<6.0){
				if(browser_name=="Firefox"){
						min_browser = browser_winxp_firefox_ver;
				}
				else if(browser_name=="Explorer"){
					min_browser = browser_winxp_ie_ver;
				}
				else if(browser_name=="Safari"){
					min_browser = browser_winxp_safari_ver;
				}
			}
			//check that it is vista or higher
			else if(parseFloat(osver)>5.9){
				if(browser_name=="Firefox"){
					min_browser = browser_win7_firefox_ver;
				}
				else if(browser_name=="Explorer"){
					min_browser = browser_win7_ie_ver;
				}
				else if(browser_name=="Safari"){
					min_browser = browser_win7_safari_ver;
				}
			}
		}
		else if(osname=="mac"){
			
			if(browser_name=="Firefox"){
				min_browser = mac_browser_firefox_ver;
			}
			else if(browser_name=="Safari"){
				min_browser = mac_browser_safari_ver;
			}
		}
		else if(osname=="ub"){
			if(browser_name=="Firefox"){
				min_browser = ubuntu_browser_firefox_ver;
			}
		}
		else if(osname="suse"){
			if(browser_name == "Firefox"){
				min_browser = sus_browser_firefox_ver;
			}
		}
		else if(osname="red"){
			if(browser_name == "Firefox"){
				min_browser = redhat_browser_firefox_ver;
			}
		}
	}
	return min_browser;
}

/**
 * Determine what OS the user is on and determin
 * whether it is supported.
 * @param osname
 * @param osver
 * @return String     the OS name to be displayed,
 * 					  if null, the error string is displayed
 */
function check_OS(osname,osver){
	var sys_name=null;
	if(osver!=null){
		//alert("Version:"+osver);
		if(osname.indexOf("winserver") != -1){
            sys_name = "Windows Server ";
            if(osname.indexOf("2003") != -1){
                sys_name = sys_name+"2003";
            }
            else
            if(osname.indexOf("2008") != -1){
                sys_name = sys_name+"2008";
            }
            else
            if(osname.indexOf("2012") != -1){
                sys_name = sys_name+"2012";
            }
        }
        else
		if(osname == "win"){
			if(parseFloat(osver)>=windows_ver){
				sys_name = "Windows ";

				if(osver== "5.0"){
                    sys_name = sys_name+"2000";
                }
				else if(osver== "5.1"){
					sys_name = sys_name+"XP";
					var sp = document.getElementById('detectPluginApplet').getOSServicePack();
					var sp_num = sp.split(" ");
					//alert(parseFloat(sp_num[2]));
					if(parseFloat(sp_num[2])>=parseFloat(windows_xp_sp)){
						sys_name = sys_name+ " "+sp;
					}
					else{
						sys_name = null;
					}
					
				}
				else if(osver == "6.0"){
					sys_name = sys_name+"Vista";
				}
				else if(osver == "6.1"){
					sys_name = sys_name+"7";
				}
			}
		}
		else if(osname=="mac"){
			
//			if(parseFloat(osver)>=parseFloat(mac_ver)){
//				sys_name = "Mac OS X";
//			}
            sys_name = "Mac OS X " + osver;
		}
		else if(osname=="chrome"){
            sys_name = "Chrome " + osver;
		}
		else if(osname=="ub"){
			//use checker to get version and name of ubuntu
			var checker = navigator.userAgent;
			checker = checker.toLowerCase();
			var splitter = checker.split("ubuntu/");
			var splitter2 = splitter[1].split(" ");
            sys_name = "Ubuntu "+splitter2[0];
		}
		else if(osname=="fedora"){
			//use checker to get version and name of fedora
			var checker_f = navigator.userAgent;
			checker_f = checker_f.toLowerCase();
			var splitter_f = checker_f.split("fedora/");
			var splitter2_f = splitter_f[1].split(" ");
            var splitter3_f = splitter2_f[0].split(".");
            sys_name = "Fedora "+splitter3_f[0];
		}
		else if(osname=="suse"){
			var sys_ver = osver.split(".");
			var check_ver = suse_ker.split(".");
			//only way to find out version is to use kernel version which is returned
			//by java applet
			if(parseInt(sys_ver[0])>=parseInt(check_ver[0]) && parseInt(sys_ver[1])>=
			parseInt(check_ver[1])&& parseInt(sys_ver[2]) >= parseInt(check_ver[2])){
				sys_name = "OpenSuse";
			}

		}
		else if(osname=="red"){
			var sys_ver = osver.split(".");
			var check_ver = redhat_ker.split(".");
			if(parseInt(sys_ver[0])>=parseInt(check_ver[0]) && parseInt(sys_ver[1])>=
				parseInt(check_ver[1])&& parseInt(sys_ver[2]) >= parseInt(check_ver[2])){
				sys_name = "RedHat";
			}
		}
        else
            sys_name = osname;
	}
	return sys_name;
}