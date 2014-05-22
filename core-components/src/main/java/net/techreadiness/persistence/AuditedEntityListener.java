package net.techreadiness.persistence;

import javax.inject.Inject;

import net.techreadiness.service.ServiceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditedEntityListener implements PreUpdateEventListener, PreInsertEventListener {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceContext serviceContext;

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		if (event.getEntity() instanceof AuditedBaseEntity) {
			AuditedBaseEntity e = (AuditedBaseEntity) event.getEntity();

			e.setChangeUser(serviceContext.getUserName());

			setValue(event.getState(), event.getPersister().getEntityMetamodel().getPropertyNames(), "changeUser",
					e.getChangeUser());
		}
		return false;
	}

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		if (event.getEntity() instanceof AuditedBaseEntity) {
			AuditedBaseEntity e = (AuditedBaseEntity) event.getEntity();

			e.setChangeUser(serviceContext.getUserName());

			setValue(event.getState(), event.getPersister().getEntityMetamodel().getPropertyNames(), "changeUser",
					e.getChangeUser());
		}
		return false;
	}

	private static void setValue(Object[] currentState, String[] propertyNames, String propertyToSet, Object value) {
		int index = ArrayUtils.indexOf(propertyNames, propertyToSet);
		if (index >= 0) {
			currentState[index] = value;
		}
	}
}
