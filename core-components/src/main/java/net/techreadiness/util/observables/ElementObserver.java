package net.techreadiness.util.observables;

import java.io.Serializable;

/**
 * 
 * Implementations of ElementObserver will be able to detect changes made to any Observable collections.
 * @param <T> Type of object that is in the collection.
 * 
 */
public interface ElementObserver<T> extends Serializable {
	void elementAdded(T element);

	void elementRemoved(T element);
}