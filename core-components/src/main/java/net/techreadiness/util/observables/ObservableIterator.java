package net.techreadiness.util.observables;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 
 * An Itertor wrapper which allows observation of added and removed elements via an ElementObserver
 * 
 */
public class ObservableIterator<T> implements Iterator<T>, Serializable {
	private static final long serialVersionUID = 1L;
	private final Iterator<T> delegate;
	private final ElementObserver<T> observer;
	private T curr;

	public ObservableIterator(Iterator<T> delegate, ElementObserver<T> observer) {
		this.delegate = delegate;
		this.observer = observer;
	}

	@Override
	public boolean hasNext() {
		return delegate.hasNext();
	}

	@Override
	public T next() {
		this.curr = delegate.next();
		return curr;
	}

	@Override
	public void remove() {
		observer.elementRemoved(curr);
		delegate.remove();
	}
}