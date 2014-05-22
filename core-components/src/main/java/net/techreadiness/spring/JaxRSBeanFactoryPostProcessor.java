package net.techreadiness.spring;

import java.util.Collection;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.Path;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

@Named
public class JaxRSBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// Nothing to do
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		if (registry.containsBeanDefinition("restServer")) {
			BeanDefinition beanDefinition = registry.getBeanDefinition("restServer");
			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
			scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class));
			BeanNameGenerator nameGenerator = new AnnotationBeanNameGenerator();
			List<RuntimeBeanReference> beanNames = new ManagedList<>();
			for (BeanDefinition definition : scanner.findCandidateComponents("net.techreadiness")) {
				String beanName = nameGenerator.generateBeanName(definition, registry);
				beanNames.add(new RuntimeBeanReference(beanName));
			}
			beanNames.addAll((Collection<? extends RuntimeBeanReference>) beanDefinition.getPropertyValues()
					.getPropertyValue("serviceBeans").getValue());
			beanDefinition.getPropertyValues().add("serviceBeans", beanNames);
		}
	}

}
