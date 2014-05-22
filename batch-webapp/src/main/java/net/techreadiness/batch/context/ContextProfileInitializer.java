package net.techreadiness.batch.context;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class ContextProfileInitializer extends PropertiesLoaderSupport implements
		ApplicationContextInitializer<ConfigurableWebApplicationContext> {
	private final static Logger logger = LoggerFactory.getLogger(ContextProfileInitializer.class);

	private final static String TERRACOTTA_URL_KEY = "terracotta.url";

	@Override
	public void initialize(ConfigurableWebApplicationContext ctx) {
		try {
			this.setLocations(ctx.getResources("/WEB-INF/application-batch-dev.properties"));

			Properties props = this.mergeProperties();
			if (props.containsKey(TERRACOTTA_URL_KEY)) {
				System.setProperty(TERRACOTTA_URL_KEY, props.getProperty(TERRACOTTA_URL_KEY));
			}

			ctx.getEnvironment().getPropertySources().addLast(new PropertiesPropertySource("default_properties", props));

			this.setLocations(ctx.getResources("classpath:application-batch.properties"));

			props = this.mergeProperties();
			if (props.containsKey(TERRACOTTA_URL_KEY)) {
				System.setProperty(TERRACOTTA_URL_KEY, props.getProperty(TERRACOTTA_URL_KEY));
			}

			ctx.getEnvironment().getPropertySources()
					.addFirst(new PropertiesPropertySource("environment_properties", props));
		} catch (IOException e) {
			logger.info("Unable to load properties file.", e);
		}
	}
}
