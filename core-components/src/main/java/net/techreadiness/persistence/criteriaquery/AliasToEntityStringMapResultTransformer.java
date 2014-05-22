package net.techreadiness.persistence.criteriaquery;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.transform.BasicTransformerAdapter;

public class AliasToEntityStringMapResultTransformer extends BasicTransformerAdapter {
	private static final long serialVersionUID = 1L;
	public static final AliasToEntityStringMapResultTransformer INSTANCE = new AliasToEntityStringMapResultTransformer();

	private AliasToEntityStringMapResultTransformer() {
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map<String, String> result = new HashMap<>(tuple.length);
		for (int i = 0; i < tuple.length; i++) {
			String alias = aliases[i];
			if (alias != null) {
				result.put(alias, tuple[i] == null ? null : tuple[i].toString());
			}
		}
		return result;
	}

}
