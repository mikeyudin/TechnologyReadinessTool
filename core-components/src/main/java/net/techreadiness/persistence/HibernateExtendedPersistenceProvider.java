package net.techreadiness.persistence;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEventListener;

/**
 * Allows us to wire up spring beans inside of the event listeners. We use this mechanism for auditing purposes. eg. setting
 * the username of the person updating an entity.
 * 
 */

public class HibernateExtendedPersistenceProvider extends HibernatePersistence {

	private PreInsertEventListener[] preInsertEventListeners;
	private PreUpdateEventListener[] preUpdateEventListeners;

	@Override
	public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
		Ejb3Configuration cfg = new Ejb3Configuration();
		setupConfiguration(cfg);
		Ejb3Configuration configured = cfg.configure(persistenceUnitName, properties);
		return configured != null ? configured.buildEntityManagerFactory() : null;
	}

	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
		Ejb3Configuration cfg = new Ejb3Configuration();
		setupConfiguration(cfg);
		Ejb3Configuration configured = cfg.configure(info, properties);
		return configured != null ? configured.buildEntityManagerFactory() : null;
	}

	private void setupConfiguration(Ejb3Configuration cfg) {
		cfg.getEventListeners().setPreUpdateEventListeners(preUpdateEventListeners);
		cfg.getEventListeners().setPreInsertEventListeners(preInsertEventListeners);
	}

	public void setPreUpdateEventListeners(PreUpdateEventListener[] preUpdateEventListeners) {
		this.preUpdateEventListeners = preUpdateEventListeners;
	}

	public void setPreInsertEventListeners(PreInsertEventListener[] preInsertEventListeners) {
		this.preInsertEventListeners = preInsertEventListeners;
	}
}
