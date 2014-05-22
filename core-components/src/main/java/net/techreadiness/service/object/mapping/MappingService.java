package net.techreadiness.service.object.mapping;

import java.util.List;
import java.util.Map;

import ma.glasnost.orika.MapperFacade;
import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.service.object.BaseObject;

public interface MappingService {

	// expose the MapperFacade itself. this will allow for mapping
	// of any two types of objects, rather than just the explicit, known
	// ones below.
	public MapperFacade getMapper();

	// generic mapping methods:
	public <T extends BaseEntity> T map(BaseObject<T> object);

	public <T extends BaseEntity, U extends BaseObject<T>> U map(T entity);

	public <T extends BaseEntity> List<T> mapToDOList(Iterable<BaseObject<T>> list);

	public <T extends BaseEntity, U extends BaseObject<T>> List<U> mapFromDOList(Iterable<T> list);

	public <T extends BaseEntity, U extends BaseObject<T>> List<Map<String, String>> mapFromDOListToMap(Iterable<T> list);

	public <T extends BaseEntity, U extends BaseObject<T>> U map(T entity, Class<U> serviceObjectType);

	public <T extends BaseEntity> String toStringRepresentation(BaseObject<T> object);

	public <T extends BaseEntity, U extends BaseObject<T>> U fromStringRepresentation(Class<U> serviceObjectType,
			String representation);
}
