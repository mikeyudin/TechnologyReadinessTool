package net.techreadiness.customer.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Ordering;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@Results({ @Result(name = Action.SUCCESS, location = "/WEB-INF/version.jsp") })
public class VersionAction extends ActionSupport {
	private final Logger log = LoggerFactory.getLogger(VersionAction.class);
	@Inject
	private ServletContext context;

	class PropertiesComparator implements Comparator<Properties> {
		@Override
		public int compare(Properties p1, Properties p2) {
			Collection<String> properties = Arrays.asList("groupId", "artifactId", "version");
			for (String property : properties) {
				int diff = Ordering.natural().compare(p1.getProperty(property), p2.getProperty(property));
				if (diff != 0) {
					return diff;
				}
			}
			return 0;
		}
	}

	private static final long serialVersionUID = 1L;
	private Collection<URL> resources;
	private Collection<URI> pomResources;
	private Collection<Properties> versions;
	private Collection<Properties> trtVersions;
	private Properties application;
	private Date buildDate;

	@Override
	public String execute() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			resources = Collections.list(classLoader.getResources("META-INF/maven"));
		} catch (IOException e2) {
			String msg = "Failed to list Maven package resources";
			log.error(msg);
			addActionError(msg);
		}
		pomResources = new HashSet<>();

		for (URL url : resources) {
			String path;
			try {
				path = URLDecoder.decode(url.getPath(), "UTF-8");
				if (path.startsWith("file:")) {
					path = path.substring(5);
				}

				if (path.indexOf('!') > 0) {
					path = path.substring(0, path.indexOf('!'));
				}

				File file = new File(path);
				if (file.isDirectory()) {
					IOFileFilter fileFilter = new NameFileFilter("pom.properties");
					Collection<File> files = FileUtils.listFiles(file, fileFilter, TrueFileFilter.INSTANCE);

					for (File child : files) {
						pomResources.add(child.toURI());
					}
				} else {
					JarEntry entry;
					try (JarInputStream jarStream = new JarInputStream(new FileInputStream(file))) {

						while ((entry = jarStream.getNextJarEntry()) != null) {
							String name = entry.getName();
							if (!entry.isDirectory() && name.startsWith("META-INF/maven") && name.endsWith("pom.properties")) {
								pomResources.add(classLoader.getResource(name).toURI());
							}
						}
					} catch (IOException | URISyntaxException e) {
						String msg = "Failed to extract Maven property resource from jar";
						log.error(msg);
						addActionError(msg);
					}
				}
			} catch (UnsupportedEncodingException e) {
				String msg = "Failed to decode path to Maven resource location";
				log.error(msg);
				addActionError(msg);
			}
		}
		versions = new TreeSet<>(new PropertiesComparator());
		trtVersions = new TreeSet<>(new PropertiesComparator());
		for (URI properties : pomResources) {
			Properties p = new Properties();
			try (InputStream stream = properties.toURL().openStream()) {
				p.load(stream);
				versions.add(p);
				if (p.getProperty("groupId").startsWith("net.techreadiness")) {
					trtVersions.add(p);
				}
			} catch (IOException e) {
				String msg = "Failed to read Maven property information";
				log.error(msg);
				addActionError(msg);
			}
		}

		String basePath = context.getRealPath("/");

		try (FileInputStream manifestInputStream = new FileInputStream(new File(basePath, "META-INF/MANIFEST.MF"))) {
			Attributes manifest = new Manifest(manifestInputStream).getMainAttributes();
			application = new Properties();
			application.setProperty("groupId", manifest.getValue("Implementation-Vendor-Id"));
			application.setProperty("artifactId", manifest.getValue("Implementation-Title"));
			application.setProperty("version", manifest.getValue("Implementation-Version"));
			application.setProperty("buildNumber", manifest.getValue("Build-Number"));
			String buildTime = manifest.getValue("Build-Id");
			if (StringUtils.isNotBlank(buildTime)) {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				format.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
				buildDate = format.parse(buildTime);
			}

		} catch (IOException | ParseException e) {
			String msg = "Failed to read application manifest";
			log.error(msg);
			addActionError(msg);
		}
		return SUCCESS;
	}

	public Collection<Properties> getVersions() {
		return versions;
	}

	public Collection<Properties> getTrtVersions() {
		return trtVersions;
	}

	public Properties getApplication() {
		return application;
	}

	public Date getBuildDate() {
		return buildDate;
	}
}
