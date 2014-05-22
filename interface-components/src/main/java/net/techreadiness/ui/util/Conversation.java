package net.techreadiness.ui.util;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

public class Conversation implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> conversation = Maps.newHashMap();

	public <T> T get(Class<T> clazz, String name) {
		T instance;
		if (conversation.containsKey(name)) {
			instance = (T) conversation.get(name);
		} else {
			try {
				instance = clazz.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			conversation.put(name, instance);
		}
		return instance;
	}

	public Map<String, Object> getAll() {
		return conversation;
	}

	public void put(String name, Object value) {
		conversation.put(name, value);
	}
}
