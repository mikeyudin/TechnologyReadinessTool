package net.techreadiness.customer.action;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class AddPrefixToKey implements Function<Map<String, String>, Map<String, String>> {
	private String prefix;

	public AddPrefixToKey(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Map<String, String> apply(Map<String, String> input) {
		Map<String, String> newMap = Maps.newHashMap();
		for (Entry<String, String> entry : input.entrySet()) {
			newMap.put(prefix + entry.getKey(), entry.getValue());
		}
		return newMap;
	}

}