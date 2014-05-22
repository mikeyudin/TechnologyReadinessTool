package net.techreadiness.persistence.dao;

import java.util.List;

/**
 * Base interface defining the common CRUD functions.
 *
 * @param <T>
 *            Entity type of the data access object
 */
public interface BaseDAO<T> {

	/**
	 * Creates a new database entry.
	 *
	 * @param t
	 *            the object to persist.
	 * @return the newly created object.
	 */
	T create(T t);

	/**
	 * Creates a new database entry.
	 *
	 * @param t
	 *            the object to persist.
	 */
	void persist(T t);

	/**
	 * Updates a database entry.
	 *
	 * @param t
	 *            the object to update with.
	 * @return the newly updated object.
	 */
	T update(T t);

	/**
	 * Deletes a database entry.
	 *
	 * @param t
	 *            the object to delete.
	 */
	void delete(T t);

	/**
	 * Finds a database object by its Id.
	 *
	 * @param id
	 *            the id to find
	 * @return the found object
	 */
	T getById(Long id);

	/**
	 * Finds all database entries (all rows).
	 *
	 * @return a <code>List</code> of the all the found objects.
	 */
	List<T> findAll();

}
