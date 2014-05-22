package net.techreadiness.service.object.mapping;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.persistence.ServiceObjectMapped;
import net.techreadiness.persistence.dao.GenericDAO;
import net.techreadiness.persistence.domain.ContactDO;
import net.techreadiness.persistence.domain.DeviceDO;
import net.techreadiness.persistence.domain.FileDO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserOrgDO;
import net.techreadiness.persistence.domain.UserRoleDO;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.Contact;
import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.File;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;
import net.techreadiness.service.object.UserOrg;
import net.techreadiness.service.object.UserRole;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Service
public class MappingServiceImpl implements MappingService {

	@Inject
	GenericDAO genericDao;
	private MapperFactory factory = null;

	public MappingServiceImpl() {
		buildFactory();
	}

	private MapperFactory getFactory() {
		if (factory == null) {
			buildFactory();
		}

		return factory;
	}

	/**
	 * By using the ClassMapBuilder.byDefault(), we are relying on the automatic mapping of identically named fields (much as
	 * you would get with BeanUtils.copyProperties()), as well as any nested classes. The service objects have core fields
	 * that are the same as the DO object they represent as a base.
	 * 
	 * We then customize the class map to work with extended fields that don't fit the simple model. Because of this, mapper
	 * classes are not needed for all of our objects. Some are fulfilled by the base mapping provided by orika. Orika handles
	 * any two classes thrown at it, mapping what it can.
	 */
	private void buildFactory() {

		// Use for troubleshooting. This will make Orika write the generated mapping
		// file out to the filesystem.
		// System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES,"true");
		// System.setProperty(OrikaSystemProperties.WRITE_CLASS_FILES,"true");

		factory = new DefaultMapperFactory.Builder().build();

		factory.classMap(ContactDO.class, Contact.class).byDefault().customize(new ContactDOandContactMapper()).register();
		factory.classMap(OrgDO.class, Org.class).byDefault().customize(new OrgDOandOrgMapper()).register();
		factory.classMap(ScopeDO.class, Scope.class).byDefault().customize(new ScopeDOandScopeMapper()).register();
		factory.classMap(UserDO.class, User.class).byDefault().customize(new UserDOandUserMapper()).register();
		factory.classMap(DeviceDO.class, Device.class).byDefault().customize(new DeviceDOandDeviceMapper()).register();
		factory.classMap(FileDO.class, File.class).byDefault().customize(new FileDOandFileMapper()).register();
		factory.classMap(UserRoleDO.class, UserRole.class).byDefault().customize(new UserRoleDOtoUserRoleMapper())
				.register();
		factory.classMap(UserOrgDO.class, UserOrg.class).byDefault().customize(new UserOrgDOToUserOrgMapper()).register();
	}

	@Override
	public <T extends BaseEntity> T map(BaseObject<T> object) {
		if (object == null) {
			return null;
		}
		return getFactory().getMapperFacade().map(object, object.getBaseEntityType());
	}

	@Override
	public <T extends BaseEntity, U extends BaseObject<T>> U map(T entity, Class<U> serviceObjectType) {
		if (entity == null) {
			return null;
		}
		U map = getFactory().getMapperFacade().map(entity, serviceObjectType);
		return map;
	}

	@Override
	public <T extends BaseEntity, U extends BaseObject<T>> U map(T entity) {
		if (entity == null) {
			return null;
		}

		U u = (U) getFactory().getMapperFacade().map(entity, getServiceObjectClass(entity));

		return u;
	}

	@Override
	public <T extends BaseEntity> List<T> mapToDOList(Iterable<BaseObject<T>> list) {
		if (list == null) {
			return null;
		}

		Iterator<BaseObject<T>> it = list.iterator();

		BaseObject<T> o = null;
		if (it.hasNext()) {
			o = it.next();
		}

		if (o != null) {
			List<T> t = getFactory().getMapperFacade().mapAsList(list, o.getBaseEntityType());

			return t;
		}

		return null;
	}

	@Override
	public <T extends BaseEntity, U extends BaseObject<T>> List<U> mapFromDOList(Iterable<T> domainObjects) {
		if (domainObjects == null) {
			return null;
		} else if (Iterables.isEmpty(domainObjects)) {
			return Collections.emptyList();
		}

		BaseEntity entity = Iterables.getFirst(domainObjects, null);
		return (List<U>) getFactory().getMapperFacade().mapAsList(domainObjects, getServiceObjectClass(entity));
	}

	@Override
	public <T extends BaseEntity, U extends BaseObject<T>> List<Map<String, String>> mapFromDOListToMap(
			Iterable<T> domainObjects) {
		List<U> l = mapFromDOList(domainObjects);

		List<Map<String, String>> list = Lists.newArrayList();

		if (l != null) {
			for (U u : l) {
				list.add(u.getAsMap());
			}
		}

		return list;
	}

	private static Class<?> getServiceObjectClass(Object o) {

		if (o instanceof ServiceObjectMapped) {
			return ((ServiceObjectMapped) o).getServiceObjectType();
		}

		throw new IllegalArgumentException("Trying to map a class not implementing ServiceObjectMapped.");
	}

	@Override
	public <T extends BaseEntity> String toStringRepresentation(BaseObject<T> object) {
		Long id = object.getId();
		if (id == null) {
			return "NEW";
		}
		return id.toString();
	}

	@Override
	@Transactional(readOnly = true)
	public <T extends BaseEntity, U extends BaseObject<T>> U fromStringRepresentation(Class<U> serviceObjectType,
			String representation) {
		Class<T> domainObjectClass;
		try {
			domainObjectClass = serviceObjectType.newInstance().getBaseEntityType();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		T domainObject;
		if ("NEW".equals(representation)) {
			try {
				domainObject = domainObjectClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			domainObject = genericDao.find(domainObjectClass, Long.valueOf(representation));
		}

		return map(domainObject, serviceObjectType);
	}

	@Override
	public MapperFacade getMapper() {
		return getFactory().getMapperFacade();
	}
}
