package net.techreadiness.service.object;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import net.techreadiness.service.object.AttributeXMLAdapter.MapElement;

public class AttributeXMLAdapter extends XmlAdapter<MapElement[], Map<String, String>> {

	public static class MapElement {
		@XmlElement
		public String key;
		@XmlElement
		public String value;

		public MapElement() {
		} // Required by JAXB

		public MapElement(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	@Override
	public MapElement[] marshal(Map<String, String> map) throws Exception {
		MapElement[] mapElements = new MapElement[map.size()];

		int i = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			mapElements[i++] = new MapElement(entry.getKey(), entry.getValue());
		}

		return mapElements;
	}

	@Override
	public Map<String, String> unmarshal(MapElement[] elementsList) throws Exception {
		Map<String, String> map = new HashMap<>();
		for (MapElement mapelement : elementsList) {
			map.put(mapelement.key, mapelement.value);
		}
		return map;
	}
}
