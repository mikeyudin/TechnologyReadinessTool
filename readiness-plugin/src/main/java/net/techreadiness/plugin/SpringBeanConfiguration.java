package net.techreadiness.plugin;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SpringBeanConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate jdbcTemp = new JdbcTemplate();
		DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
		jdbcTemp.setDataSource(dataSource);
		return jdbcTemp;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
