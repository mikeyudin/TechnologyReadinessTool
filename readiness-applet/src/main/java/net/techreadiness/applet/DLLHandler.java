package net.techreadiness.applet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DLLHandler {
	final static int KILOBYTE = 1024;
	final static int MEGABYTE = KILOBYTE * KILOBYTE;
	final static int BUFFER_SIZE = 2 * MEGABYTE;
	final static private String[] windowsDLLlist = { "sigar-x86-winnt.dll", "sigar-amd64-winnt.dll" };
	final static private String[] macDYLIBlist = { "libsigar-universal-macosx.dylib", "libsigar-universal64-macosx.dylib" };
	final static private String[] linuxDYLIBlist = { "libsigar-x86-linux.so", "libsigar-amd64-linux.so" };

	public static File loadDLLs(String osname, boolean is64bit) throws IOException {
		String libName = getDLLlist(osname)[is64bit ? 1 : 0];
		InputStream in = DLLHandler.class.getResourceAsStream(libName);
		File fileOut = File.createTempFile(libName, null);
		OutputStream out = null;

		try {
			out = new FileOutputStream(fileOut);
			byte[] buffer = new byte[BUFFER_SIZE];
			BufferedInputStream bis = new BufferedInputStream(in, BUFFER_SIZE);
			BufferedOutputStream bos = new BufferedOutputStream(out, BUFFER_SIZE);
			int n;
			while ((n = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
				bos.write(buffer, 0, n);
			}
			bos.flush();
			bos.close();
			bis.close();
			buffer = null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// Nothing to do
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// Nothing to do
			}
		}
		
		System.load(fileOut.toString());
		return fileOut;
	}

	private static String[] getDLLlist(String osname) {
		String[] retval = null;
		if (osname != null) {
			osname = osname.toLowerCase();
			if (osname.indexOf("win") != -1) {
				retval = windowsDLLlist;
			} else if (osname.indexOf("mac") != -1) {
				retval = macDYLIBlist;
			} else if (osname.indexOf("linux") != -1) {
				retval = linuxDYLIBlist;
			}
		}
		return retval;
	}

}
