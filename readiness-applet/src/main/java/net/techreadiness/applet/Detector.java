package net.techreadiness.applet;

import java.applet.Applet;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

public class Detector extends Applet {
	private static final long serialVersionUID = 1L;
	private Sigar sigar = null;
	private Mem memory;
	private CpuInfo[] cpuInfo;
	private File library;

	@Override
	public void init() {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {

				public String run() throws Exception {
					System.setProperty("org.hyperic.sigar.path", "-");
					library = DLLHandler.loadDLLs(getOSName(), getOS64Bit());
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			e.getException().printStackTrace();
		}

		add(new Label("Technology Readiness Tool"));
	}

	@Override
	public void start() {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {

				public String run() throws Exception {

					if (sigar == null) {
						sigar = new Sigar();
						memory = sigar.getMem();
						cpuInfo = sigar.getCpuInfoList();
					}
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			e.getException().printStackTrace();
		}
	}

	@Override
	public void stop() {
		sigar = null;
		memory = null;
		cpuInfo = null;
	}

	@Override
	public void destroy() {
		library.delete();
	}

	public String getInformationsAboutCPU() {
		if (getOSName().toLowerCase().contains("mac")) {
			return getMacOsXEnvironment().get("Processor Name");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(cpuInfo[0].getVendor());
		sb.append(" ");
		sb.append(cpuInfo[0].getModel());
		return sb.toString();
	}

	private static Map<String, String> getMacOsXEnvironment() {
		return AccessController.doPrivileged(new PrivilegedAction<Map<String, String>>() {

			public Map<String, String> run() {
				try {
					Process process = Runtime.getRuntime().exec(
							new String[] { "/usr/sbin/system_profiler", "SPHardwareDataType" });
					return propertiesFileToMultimap(new InputStreamReader(process.getInputStream(), "UTF-8"));
				} catch (IOException e) {
					return Collections.emptyMap();
				}
			}
		});
	}

	private static Map<String, String> propertiesFileToMultimap(Reader reader) throws IOException {
		Map<String, String> result = new HashMap<String, String>();
		BufferedReader in = new BufferedReader(reader);

		String line;
		while ((line = in.readLine()) != null) {
			String[] parts = line.split("\\s*[\\:\\=]\\s*", 2);
			if (parts.length == 2) {
				result.put(parts[0].trim(), parts[1]);
			}
		}
		return result;
	}

	public long getInformationsAboutMemory() {
		return memory.getRam();
	}

	public String getMaHostAddress() {
		String hostip = AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				try {
					return InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					return null;
				}
			}
		});

		return hostip;

	}

	public String getMaHostName() {
		String hostname = AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				try {
					return InetAddress.getLocalHost().getHostName();
				} catch (UnknownHostException e) {
					return null;
				}
			}
		});

		return hostname;
	}

	public String getOSName() {
		return System.getProperty("os.name");
	}

	public String getOSVersion() {
		return System.getProperty("os.version");
	}

	public boolean getOS64Bit() {
		String bit = AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				try {
					return System.getProperty("sun.arch.data.model");
				} catch (Exception e) {
					return null;
				}
			}
		});
		return bit.indexOf("64") != -1;
	}

	public String getOSServicePack() {
		String servicepack = AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				return System.getProperty("sun.os.patch.level");
			}
		});
		return servicepack;
	}

}