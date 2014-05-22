package net.techreadiness.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.techreadiness.persistence.AuditedBaseEntityWithExt;

import com.google.common.collect.Maps;

public class Entities {

	public static List<Map<String, String>> asMaps(List<? extends AuditedBaseEntityWithExt> entities) {
		List<Map<String, String>> maps = new ArrayList<>();
		for (AuditedBaseEntityWithExt entity : entities) {
			maps.add(entity.getExtAttributes());
		}
		return maps;
	}

	public static Map<String, String> asFullMaps(AuditedBaseEntityWithExt entity) {
		Map<String, String> map = Maps.newHashMap();
		map.putAll(entity.getAsMap());
		map.putAll(entity.getExtAttributes());
		return map;
	}
}
