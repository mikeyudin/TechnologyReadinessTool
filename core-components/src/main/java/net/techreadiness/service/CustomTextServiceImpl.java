package net.techreadiness.service;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import net.techreadiness.persistence.dao.CustomTextDAO;
import net.techreadiness.persistence.domain.CustomTextDO;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service
public class CustomTextServiceImpl implements CustomTextService {

	@Inject
	private CustomTextDAO customTextDAO;
	@Inject
	private ApplicationContext applicationContext;
	private ResourceBundle combinedBundle;

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> findAllCustomText(ServiceContext context) {
		List<CustomTextDO> allCustomText = customTextDAO.findAllCustomTextForScope(context.getScopeId());
		HashMap<String, String> customTextMap = Maps.newHashMap();
		for (CustomTextDO text : allCustomText) {
			customTextMap.put(text.getCode(), text.getText());
		}
		return customTextMap;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "customText", key = "#context.scopeId + #code")
	public String getCustomTextByCode(ServiceContext context, String code) {
		CustomTextDO text = customTextDAO.getCustomTextByScopeAndCode(context.getScopeId(), code);
		if (text == null) {
			return null;
		}
		return text.getText();
	}

	@Override
	public ResourceBundle getCombinedResourceBundle() {
		if (combinedBundle == null) {
			combinedBundle = new CompositeResourceBundle(getResourceBundles());
		}
		return combinedBundle;
	}

	private Collection<String> getBasenames() {
		ConfigurableApplicationContext config = (ConfigurableApplicationContext) applicationContext;
		BeanDefinition def = config.getBeanFactory().getBeanDefinition("messageSource");
		PropertyValue basenameProperty = def.getPropertyValues().getPropertyValue("basenames");
		Object value = basenameProperty.getValue();
		@SuppressWarnings("unchecked")
		Collection<TypedStringValue> values = (Collection<TypedStringValue>) value;
		Collection<String> basenames = Sets.newHashSet();
		for (TypedStringValue stringValue : values) {
			basenames.add(stringValue.getValue());
		}
		return basenames;
	}

	private Collection<ResourceBundle> getResourceBundles() {
		Collection<String> bundleNames = getBasenames();
		Collection<ResourceBundle> bundles = Sets.newHashSet();

		for (String bundleName : bundleNames) {
			try {
				ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
				bundles.add(bundle);
			} catch (Exception e) {
				// Ignore a missing resource bundle
			}
		}
		return bundles;
	}

	class CompositeResourceBundle extends ResourceBundle {
		private Map<String, String> bundle;

		public CompositeResourceBundle(Collection<ResourceBundle> bundles) {
			bundle = Maps.newHashMap();
			for (ResourceBundle b : bundles) {
				Iterator<String> i = Iterators.forEnumeration(b.getKeys());
				while (i.hasNext()) {
					String key = i.next();
					String value = b.getString(key);
					bundle.put(key, value);
				}
			}
		}

		@Override
		protected Object handleGetObject(String key) {
			return bundle.get(key);
		}

		@Override
		public Enumeration<String> getKeys() {
			return Iterators.asEnumeration(bundle.keySet().iterator());
		}

	}
}
