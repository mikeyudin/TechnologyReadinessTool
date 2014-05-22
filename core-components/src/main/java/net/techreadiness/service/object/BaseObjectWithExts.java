package net.techreadiness.service.object;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.techreadiness.annotation.CoreExtFields;
import net.techreadiness.persistence.BaseEntity;

import com.google.common.collect.Maps;

public abstract class BaseObjectWithExts<T extends BaseEntity> extends BaseObject<T> {
	private static final long serialVersionUID = 1L;

	@CoreExtFields
	Map<String, String> extendedAttributes;

	public void setExtendedAttributes(Map<String, String> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}

	@XmlJavaTypeAdapter(AttributeXMLAdapter.class)
	public Map<String, String> getExtendedAttributes() {
		if (extendedAttributes == null) {
			extendedAttributes = Maps.newHashMap();
		}
		return extendedAttributes;
	}
}
