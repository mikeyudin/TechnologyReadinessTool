package net.techreadiness.text;

import java.util.Collection;
import java.util.Map;

import javax.inject.Named;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedList;

@Named
public class MessageSourceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Map<String, ResourceBundleNameProvider> beans = beanFactory.getBeansOfType(ResourceBundleNameProvider.class);
		if (beans != null) {
			BeanDefinition messageSourceDef = beanFactory.getBeanDefinition("messageSource");

			ManagedList<TypedStringValue> bundleNames = new ManagedList<>();
			bundleNames.setMergeEnabled(true);

			Collection<ResourceBundleNameProvider> nameProviders = beans.values();
			for (ResourceBundleNameProvider resourceBundleNameProvider : nameProviders) {
				for (String bundleName : resourceBundleNameProvider.getBundleNames()) {
					bundleNames.add(new TypedStringValue(bundleName));
				}
			}

			messageSourceDef.getPropertyValues().add("basenames", bundleNames);
		}
	}

}
