package net.techreadiness.util.observables;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * A Set wrapper which allows observation of added and removed elements via an ElementObserver
 * 
 */
public class ObservableSet<T> extends AbstractSet<T> implements ElementObserver<T> {
	private static final long serialVersionUID = 1L;
	private final Collection<T> delegate;
	private final ElementObserver<T> observer;

	public ObservableSet(Collection<T> delegate, ElementObserver<T> observer) {
		this.delegate = delegate;
		this.observer = observer;
	}

	@Override
	public Iterator<T> iterator() {
		return new ObservableIterator<>(delegate.iterator(), this);
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean add(T e) {
		observer.elementAdded(e);
		return delegate.add(e);
	}

	@Override
	public void elementAdded(T element) {
		observer.elementAdded(element);
	}

	@Override
	public void elementRemoved(T element) {
		observer.elementRemoved(element);
	}
}