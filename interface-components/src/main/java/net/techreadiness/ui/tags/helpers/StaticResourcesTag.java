package net.techreadiness.ui.tags.helpers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.TagSerializer;
import org.apache.struts2.views.java.XHTMLTagSerializer;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class StaticResourcesTag extends SimpleTagSupport {
	private String additionalCssFiles;
	private String additionalJsFiles;

	@Override
	public void doTag() throws JspException, IOException {
		TagSerializer s = new XHTMLTagSerializer();
		TemplateRenderingContext renderingContext = new TemplateRenderingContext(null, getJspContext().getOut(), null, null,
				null);
		s.setup(renderingContext);
		ServletContext context = ((PageContext) getJspContext()).getServletContext();
		String contextPath = context.getContextPath();

		List<String> cssFiles = Lists.newArrayList();
		List<String> jsFiles = Lists.newArrayList();
		List<String> lessFiles = Lists.newArrayList();

		Gson gson = new GsonBuilder().create();
		Type mapType = new TypeToken<Map<String, List<String>>>() {
			// Define a type for JSON parsing
		}.getType();

		Map<String, Collection<String>> resourcePack = gson.fromJson(
				new InputStreamReader(getClass().getResourceAsStream("/mainResourcePack.json")), mapType);
		cssFiles.addAll(getResources("css", resourcePack));
		jsFiles.addAll(getResources("js", resourcePack));
		lessFiles.addAll(getResources("less", resourcePack));

		Attributes cssAttrs = new Attributes();
		cssAttrs.add("rel", "stylesheet").add("type", "text/css");

		Attributes jsAttrs = new Attributes();
		jsAttrs.add("type", "text/javascript");

		if (StringUtils.isNotBlank(additionalCssFiles)) {
			String[] css = additionalCssFiles.split(" ");

			for (int i = 0; i < css.length; i++) {
				cssFiles.add(css[i]);
			}
		}

		if (StringUtils.isNotBlank(additionalJsFiles)) {
			String[] js = additionalJsFiles.split(" ");

			for (int i = 0; i < js.length; i++) {
				jsFiles.add(js[i]);
			}
		}

		s.start("script", jsAttrs);
		StringBuilder contextBuilder = new StringBuilder(13 + contextPath.length());
		contextBuilder.append("var ctx = '");
		contextBuilder.append(contextPath);
		contextBuilder.append("';");
		s.characters(contextBuilder.toString());
		s.end("script");

		// For development -- these files are heavily cached by the browser, append something (for now)
		// so they are retrieved each time.

		for (String file : cssFiles) {
			StringBuilder sb = new StringBuilder(contextPath.length() + file.length());
			sb.append(contextPath);
			sb.append(file);
			cssAttrs.add("href", sb.toString());
			s.start("link", cssAttrs);
		}

		for (String file : lessFiles) {
			String cssFile = file.replace(".less", ".css");
			StringBuilder sb = new StringBuilder(contextPath.length() + cssFile.length());
			sb.append(contextPath);
			sb.append(cssFile);
			cssAttrs.add("href", sb.toString());
			s.start("link", cssAttrs);
		}

		for (String file : jsFiles) {
			StringBuilder sb = new StringBuilder(contextPath.length() + file.length());
			sb.append(contextPath);
			sb.append(file);
			jsAttrs.add("src", sb.toString());
			s.start("script", jsAttrs);
			s.end("script");
		}

	}

	private static Collection<String> getResources(String type, Map<String, Collection<String>> map) {
		Collection<String> resources = map.get(type);
		if (resources == null) {
			return Collections.emptySet();
		}
		return resources;
	}

	public String getAdditionalJsFiles() {
		return additionalJsFiles;
	}

	public void setAdditionalJsFiles(String additionalJsFiles) {
		this.additionalJsFiles = additionalJsFiles;
	}

	public String getAdditionalCssFiles() {
		return additionalCssFiles;
	}

	public void setAdditionalCssFiles(String additionalCssFiles) {
		this.additionalCssFiles = additionalCssFiles;
	}
}
