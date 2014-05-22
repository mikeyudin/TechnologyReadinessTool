package net.techreadiness.persistence.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.techreadiness.service.ServiceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDAOImpl<T> implements BaseDAO<T> {
	private final Logger log = LoggerFactory.getLogger(BaseDAOImpl.class);
	private Class<T> domainClass;

	@Inject
	private ServiceContext serviceContext;

	@PersistenceContext
	protected EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public BaseDAOImpl() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		this.domainClass = (Class<T>) type.getActualTypeArguments()[0];
	}

	@Override
	public T create(T t) {
		return em.merge(t);
	}

	@Override
	public void persist(T t) {
		if (t == null) {
			return;
		}
		em.persist(t);
	}

	@Override
	public void delete(T t) {
		log.debug("Delete: {}; serviceContext={}", domainClass, serviceContext);
		if (t == null) {
			return;
		}

		// We need to provide the user name to the audit history.
		if (serviceContext != null && serviceContext.getUserName() != null) {
			Query q = em.createNativeQuery("select set_audit_delete_info(:username) from dual;");
			q.setParameter("username", serviceContext.getUserName());
		}

		em.remove(em.merge(t));
	}

	@Override
	public List<T> findAll() {
		List<T> entities = new ArrayList<>();
		try {
			String s = "select c from " + domainClass.getName() + " c";
			if (getOrderBy() != null) {
				s += " order by " + getOrderBy();
			}

			entities = em.createQuery(s, domainClass).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}

	@Override
	public T getById(Long id) {
		if (id == null) {
			return null;
		}
		return em.find(domainClass, id);
	}

	@Override
	public T update(T t) {
		return em.merge(t);
	}

	public String getOrderBy() {
		return null;
	}

	protected <X> X getSingleResult(TypedQuery<X> query) {
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	protected List<T> getResultList(TypedQuery<T> query) {
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	protected Object getSingleResult(Query query) {
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	protected TypedQuery<T> getNamedQuery(String queryName) {
		return em.createNamedQuery(queryName, domainClass);
	}
}
