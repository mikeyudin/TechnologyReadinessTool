package net.techreadiness.util;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

public class MapUtils {

	public static <T> Map<String, Map<String, T>> makeComputingMap() {
		return new MapMaker().makeComputingMap(new Function<String, Map<String, T>>() {
			@Override
			public Map<String, T> apply(String input) {
				return Maps.newHashMap();
			}
		});
	}
}
