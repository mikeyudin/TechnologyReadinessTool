package net.techreadiness.ui.util;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.dao.GenericDAO;
import net.techreadiness.util.EntityUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BaseEntityTypeConverter extends StrutsTypeConverter {
	private final Logger log = LoggerFactory.getLogger(BaseEntityTypeConverter.class);

	@Inject
	GenericDAO genericDao;

	@SuppressWarnings("unchecked")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {

		if (values.length == 1 && StringUtils.isNotBlank(values[0])) {
			String value = values[0];
			if (value.equals("NEW")) {
				try {
					return toClass.newInstance();
				} catch (Exception e) {
					log.warn("Could not convert class: {}", toClass.getName(), e);
					return null;
				}
			}
			return genericDao.find(toClass, Long.valueOf(values[0]));
		}
		return null;

	}

	@Override
	public String convertToString(Map context, Object o) {
		Object id = EntityUtils.getId(o);
		if (id == null) {
			return "NEW";
		}
		return id.toString();
	}

}
