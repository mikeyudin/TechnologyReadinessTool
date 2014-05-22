package net.techreadiness.ui.util;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.AuditedBaseEntity;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.mapping.MappingService;

import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.stereotype.Component;

@Component
public class ServiceObjectTypeConverter extends StrutsTypeConverter {

	@Inject
	MappingService mappingService;

	@Override
	@SuppressWarnings({ "unchecked" })
	public Object convertFromString(Map context, String[] representations, Class objectType) {
		if (representations == null || representations.length != 1) {
			return null;
		}
		return convert(objectType, representations[0]);
	}

	private <T extends AuditedBaseEntity> Object convert(Class<BaseObject<T>> clas, String repr) {
		BaseObject<T> obj = mappingService.fromStringRepresentation(clas, repr);
		return obj;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public String convertToString(Map context, Object object) {
		BaseObject<? extends AuditedBaseEntity> baseObject = (BaseObject<? extends AuditedBaseEntity>) object;
		return mappingService.toStringRepresentation(baseObject);
	}

}
