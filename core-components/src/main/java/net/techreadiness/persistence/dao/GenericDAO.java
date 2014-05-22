package net.techreadiness.persistence.dao;

public interface GenericDAO {
	<T> T find(Class<T> entityClass, Object primaryKey);
}
